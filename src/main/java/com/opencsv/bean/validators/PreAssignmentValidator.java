package com.opencsv.bean.validators;

import java.lang.annotation.*;

/**
 * Specifies the binding of a validator to a field in a bean.  This validator will run
 * against the string that will be converted and assigned to the field and will be run
 * prior to the conversion.
 *
 * @author Scott Conway
 * @since 5.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PreAssignmentValidator {

    /**
     * Returns the validator that will validate the string.
     *
     * @return The class of the validator that will validate the bean field
     * string value
     */
    Class<? extends StringValidator> validator();

    /**
     * This is used to store additional information needed by the
     * {@link StringValidator}.
     * This could, for example, contain a regular expression that will be
     * applied to the data.
     *
     * @return Parameter string required by the {@link StringValidator}
     */
    String paramString() default "";
}
