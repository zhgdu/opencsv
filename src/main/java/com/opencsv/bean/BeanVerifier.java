package com.opencsv.bean;

import com.opencsv.exceptions.CsvConstraintViolationException;

/**
 * Classes implementing this interface may be used to verify and filter beans
 * after creation, but before being passed back to the calling application.
 * This is fully intended as a replacement for {@link CsvToBeanFilter}.
 * <p>Implementations of this interface <em>must</em> be thread-safe.</p>
 *
 * @param <T> The type of bean being verified
 * @since 4.4
 */
public interface BeanVerifier<T> {

    /**
     * Verifies and optionally filters the bean that has been created.
     * This method throws {@link CsvConstraintViolationException} if the bean
     * created is in some way inconsistent and thus unacceptable. If, however,
     * the bean is essentially correct, but for some logical reason should be
     * filtered silently out, the method should return {@code false}.
     *
     * @param bean The bean to be verified
     * @return {@code true} if the bean should be passed on to further
     * processing, {@code false} if it should be silently filtered
     * @throws CsvConstraintViolationException If the bean that has been
     * created is in some way logically inconsistent or impossible. This
     * exception will be propagated up the call stack and, depending on how
     * opencsv is being used, may simply be reported, or may halt execution.
     */
    boolean verifyBean(T bean) throws CsvConstraintViolationException;
}
