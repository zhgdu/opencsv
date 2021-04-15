package com.opencsv.bean;

import com.opencsv.ICSVParser;
import org.apache.commons.collections4.MapIterator;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * A {@link BeanDissector} that takes configuration information from
 * annotations.
 *
 * @param <T> The type of the bean to be managed
 */
public class AnnotationDissector<T> implements BeanDissector<T> {

    private final Class<? extends T> type;
    private final Locale errorLocale;
    private final String profile;

    /** Storage for all manually excluded class/field pairs. */
    private MultiValuedMap<Class<?>, Field> ignoredFields = new ArrayListValuedHashMap<>();

    @Override
    public List<Field> getAllFields() {
        return filterIgnoredFields(FieldUtils.getAllFields(type));
    }

    /**
     * Constructor for a {@link BeanDissector} that takes configuration
     * information from annotations.
     *
     * @param type The type of the bean to be managed
     * @param errorLocale The locale for error messages
     * @param profile The profile for bindings
     */
    public AnnotationDissector(Class<? extends T> type, Locale errorLocale, String profile) {
        this.type = type;
        this.errorLocale = ObjectUtils.defaultIfNull(errorLocale, Locale.getDefault());
        this.profile = StringUtils.defaultString(profile);
    }

    @Override
    public void ignoreFields(MultiValuedMap<Class<?>, Field> fields) throws IllegalArgumentException {

        // Check input for consistency
        if(fields == null) {
            ignoredFields.clear();
        }
        else {
            ignoredFields = fields;
            MapIterator<Class<?>, Field> it = ignoredFields.mapIterator();
            it.forEachRemaining(t -> {
                final Field f = it.getValue();
                if (t == null || f == null
                        || !f.getDeclaringClass().isAssignableFrom(t)) {
                    throw new IllegalArgumentException(ResourceBundle.getBundle(
                            ICSVParser.DEFAULT_BUNDLE_NAME, errorLocale)
                            .getString("ignore.field.inconsistent"));
                }
            });
        }

    }

    @Override
    public T newInstance() throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        return type.getDeclaredConstructor().newInstance();
    }

    @Override
    public Class<? extends T> getType() {
        return type;
    }

    @Override
    public List<Field> getFieldsWithRecursiveBinding() {
        return filterIgnoredFields(FieldUtils.getFieldsWithAnnotation(type, CsvRecurse.class));
    }

    @Override
    public BeanDissector<?> newDissector(Class<?> type) {
        return new AnnotationDissector<>(type, errorLocale, profile);
    }

    /**
     * Filters all fields that opencsv has been instructed to ignore and
     * returns a list of the rest.
     * @param fields The fields to be filtered
     * @return A list of fields that exist for opencsv
     */
    private List<Field> filterIgnoredFields(Field[] fields) {
        final List<Field> filteredFields = new LinkedList<>();
        for(Field f : fields) {
            CsvIgnore ignoreAnnotation = f.getAnnotation(CsvIgnore.class);
            Set<String> ignoredProfiles = ignoreAnnotation == null ?
                    SetUtils.<String>emptySet() :
                    new HashSet<String>(Arrays.asList(ignoreAnnotation.profiles())); // This is easier in Java 9 with Set.of()
            if(!ignoredFields.containsMapping(type, f) &&
                    !ignoredProfiles.contains(profile) &&
                    !ignoredProfiles.contains(StringUtils.EMPTY)) {
                filteredFields.add(f);
            }
        }
        return filteredFields;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        return obj instanceof AnnotationDissector
                && Objects.equals(((AnnotationDissector<T>)obj).type, type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
