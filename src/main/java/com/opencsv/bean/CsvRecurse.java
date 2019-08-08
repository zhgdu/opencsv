package com.opencsv.bean;

import java.lang.annotation.*;

/**
 * Instructs a mapping strategy to look inside a member variable for further
 * mapping annotations.
 *
 * @since 5.0
 * @author Andrew Rucker Jones
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvRecurse {
}
