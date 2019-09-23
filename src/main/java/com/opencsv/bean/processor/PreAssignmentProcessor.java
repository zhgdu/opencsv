package com.opencsv.bean.processor;

import java.lang.annotation.*;

/**
 * Specifies the binding of a processor to a field in a bean.  This processor will run
 * against the string that will be converted and assigned to the field and will be run
 * prior to the validation and conversion.
 *
 * @author Scott Conway
 * @since 5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PreAssignmentProcessor {
    /**
     * Returns the processor that will process the string.
     *
     * @return The class of the processor that will process the bean field
     * string value
     */
    Class<? extends StringProcessor> processor();

    /**
     * This is used to store additional information needed by the
     * {@link StringProcessor}.
     * This could, for example, be a default value so the same processor
     * could be used by different fields.
     *
     * @return Parameter string required by the {@link StringProcessor}
     */
    String paramString() default "";
}
