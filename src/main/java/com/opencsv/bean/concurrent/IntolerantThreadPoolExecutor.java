/*
 * Copyright 2017 Andrew Rucker Jones.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.opencsv.bean.concurrent;

import com.opencsv.exceptions.CsvException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This ThreadPoolExecutor automatically shuts down on any failed thread.
 * <p>This is the historically established precedent for dealing with input errors
 * in opencsv. This implementation expects all uncaught exceptions from its
 * threads to be wrapped in a {@link java.lang.RuntimeException}. The number of
 * threads in the pool is fixed.</p>
 * <p>It is not intended for this executor to be instantiated and receive jobs
 * directly. There are function-specific derived classes for that purpose.</p>
 * <p>This executor adds significant logic to the basic idea of an
 * {@link java.util.concurrent.Executor}, and thus must be used differently
 * from other executors. Usage follows this pattern:
 * <ol><li>{@link #prepare()}</li>
 * <li>Submit tasks. This is not intended to be done directly to this class, but
 * rather to one of the submission methods of the derived classes.</li>
 * <li>{@link #complete()}</li>
 * <li>{@link #resultStream()}</li>
 * <li>Possibly {@link #getCapturedExceptions()}</li></ol></p>
 * <p>The execution structure of this class is:
 * <ol><li>The main thread (outside of this executor) parses input and passes
 * it on to</li>
 * <li>This executor, which performs a number of conversions in parallel and
 * passes these results and any resultant errors to</li>
 * <li>The accumulator, which creates an ordered list of the results.</li></ol></p>
 * <p>The threads in the executor queue their results in a thread-safe
 * queue, which should be O(1), minimizing wait time due to synchronization.
 * The accumulator then removes items from the queue and inserts them into a
 * sorted data structure, which is O(log n) on average and O(n) in the worst
 * case. If the user has told us she doesn't need sorted data, the
 * accumulator is not necessary, and thus is not started.</p>
 *
 * @param <T> The type of the object being created by the threads run
 * @author Andrew Rucker Jones
 * @since 4.0
 */
class IntolerantThreadPoolExecutor<T> extends ThreadPoolExecutor {

    /** A queue of the beans created. */
    protected final BlockingQueue<OrderedObject<T>> resultQueue = new LinkedBlockingQueue<>();

    /** A queue of exceptions thrown by threads during processing. */
    protected final BlockingQueue<OrderedObject<CsvException>> thrownExceptionsQueue = new LinkedBlockingQueue<>();

    /** A sorted, concurrent map for the beans created. */
    private ConcurrentNavigableMap<Long, T> resultantBeansMap = null;

    /** A sorted, concurrent map for any exceptions captured. */
    private ConcurrentNavigableMap<Long, CsvException> thrownExceptionsMap = null;

    /** A separate thread that accumulates and orders results. */
    protected AccumulateCsvResults<T> accumulateThread = null;

    /**
     * Determines whether resulting data sets have to be in the same order as
     * the input.
     */
    private final boolean orderedResults;

    /** The exception that caused this Executor to stop executing. */
    private Throwable terminalException;

    /**
     * Constructor for a thread pool executor that stops by itself as soon as
     * any thread throws an exception.
     * Threads never time out and the queue for inbound work is unbounded.
     * @param orderedResults Whether order should be preserved in the results
     */
    IntolerantThreadPoolExecutor(boolean orderedResults) {
        super(Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors(), Long.MAX_VALUE,
                TimeUnit.NANOSECONDS, new LinkedBlockingQueue<>());
        this.orderedResults = orderedResults;
    }

    /**
     * Prepares this Executor to receive jobs.
     */
    public void prepare() {
        prestartAllCoreThreads();

        // The ordered maps and accumulator are only necessary if ordering is
        // stipulated. After this, the presence or absence of the accumulator is
        // used to indicate ordering or not so as to guard against the unlikely
        // problem that someone sets orderedResults right in the middle of
        // processing.
        if(orderedResults) {
            resultantBeansMap = new ConcurrentSkipListMap<>();
            thrownExceptionsMap = new ConcurrentSkipListMap<>();

            // Start the process for accumulating results and cleaning up
            accumulateThread = new AccumulateCsvResults<>(
                    resultQueue, thrownExceptionsQueue, resultantBeansMap,
                    thrownExceptionsMap);
            accumulateThread.start();
        }
    }

    /**
     * Sends a signal to the Executor that it should shut down once all threads
     * have completed.
     *
     * @throws InterruptedException If the current thread is interrupted while
     * waiting. Shouldn't be thrown, since the Executor
     * waits indefinitely for all threads to end.
     * @throws RejectedExecutionException If an exception during processing
     * forced this Executor to shut down.
     */
    public void complete() throws InterruptedException {
        // Normal termination
        super.shutdown();
        awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS); // Wait indefinitely
        if(accumulateThread != null) {
            accumulateThread.setMustStop(true);
            accumulateThread.join();
        }

        // There's one more possibility: The very last bean caused a problem.
        if(terminalException != null) {
            // Trigger a catch in the calling method
            throw new RejectedExecutionException();
        }
    }

    /**
     * Returns the results of conversion as a {@link java.util.stream.Stream}.
     * @return A {@link java.util.stream.Stream} of results
     */
    public Stream<T> resultStream() {
        // Prepare results. Checking for this map to be != null makes the
        // compiler feel better than checking that the accumulator is not null.
        // This is to differentiate between the ordered and unordered cases.
        return resultantBeansMap != null ?
                resultantBeansMap.values().stream() :
                resultQueue.stream()
                        .filter(Objects::nonNull)
                        .map(OrderedObject::getElement);
    }

    /**
     * Returns exceptions captured during the conversion process if
     * the conversion process was set not to propagate these errors
     * up the call stack.
     * The call is nondestructive.
     *
     * @return All exceptions captured
     */
    public List<CsvException> getCapturedExceptions() {
        return thrownExceptionsMap == null ?
                thrownExceptionsQueue.stream()
                        .filter(Objects::nonNull)
                        .map(OrderedObject::getElement)
                        .collect(Collectors.toList()) :
                new ArrayList<>(thrownExceptionsMap.values());
    }

    @Override
    public List<Runnable> shutdownNow() {
        if(accumulateThread != null) {
            accumulateThread.setMustStop(true);
        }
        return super.shutdownNow();
    }
    
    /**
     * Shuts the Executor down if the thread ended in an exception.
     * @param r {@inheritDoc}
     * @param t {@inheritDoc} 
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        super.afterExecute(r, t);
        if(t != null) {
            if(t.getCause() != null) {
                // Normally, everything that gets to this point should be
                // wrapped in a RuntimeException to get past the lack of checked
                // exceptions in Runnable.run().
                terminalException = t.getCause();
            }
            else {
                terminalException = t;
            }
            shutdownNow();
        }
    }
    
    /**
     * If an unrecoverable exception was thrown during processing, it can be
     * retrieved here.
     * @return The exception that halted one of the threads, which caused the
     *   executor to shut itself down
     */
    public Throwable getTerminalException() {
        return terminalException;
    }
}
