package com.opencsv.bean;

import org.apache.commons.collections4.MultiValuedMap;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Uses configuration information to handle the management of beans.
 * TODO: When this becomes clearer, it needs better documentation.
 * @param <T> The type of the bean being managed
 */
public interface BeanDissector<T> {

    /**
     * Returns a new {@link BeanDissector} for the bean type passed in.
     * This ensures all {@link BeanDissector}s are of the same type.
     *
     * @param type The type of the bean to manage
     * @return A new {@link BeanDissector} wrapped around {code type}
     */
    BeanDissector<?> newDissector(Class<?> type);

    /**
     * Returns the type of the bean wrapped by this dissector.
     * @return The bean type
     */
    Class<? extends T> getType();

    /**
     * Creates a new instance of the bean managed.
     * @return A new instance of the bean managed
     * @throws ReflectiveOperationException If the bean cannot be instantiated
     *   with the nullary constructor
     */
    T newInstance() throws ReflectiveOperationException;

    /**
     * Returns a list of all fields in the bean.
     * This list does not include fields that are specifically ignored
     * by configuration.
     *
     * @return A list of relevant fields from the managed bean
     */
    List<Field> getAllFields();

    /**
     * Returns a list of all fields that must be recursed into.
     * This list does not include fields that are specifically ignored
     * by configuration.
     *
     * @return A list of fields from the managed bean that are subject
     *   to recursion
     */
    List<Field> getFieldsWithRecursiveBinding();

    /**
     * Sets which fields must be ignored.
     *
     * @param fields The fields to ignore
     * @throws IllegalArgumentException If any field in the map is {@code null}
     *   or the field does not belong to the associated class
     */
    void ignoreFields(MultiValuedMap<Class<?>, Field> fields) throws IllegalArgumentException;
}
