package com.opencsv.bean.validators;

import com.opencsv.bean.BeanField;
import com.opencsv.exceptions.CsvValidationException;

import java.util.ResourceBundle;

/**
 * <p>This is a validator that, due to the addition of the parameter, allows the validation of multiple different types
 * of input.  The paramString must be a valid regular expression.  The MustMatchRegularExpression validator will
 * the {@link String#matches(String)} method on the string to be converted and the regular expression string and if
 * the two do not match then a {@link CsvValidationException} will be thrown.</p>
 * <p>Because this is validating the string <em>before</em> it is parsed/converted, the capture settings
 * of the string must be taken into account.</p>
 * <p>Examples:</p>
 * <pre>
 *     // The String that becomes id must be a number with three to six digits.
 *     &#64;PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[0-9]{3,6}$")
 *     &#64;CsvBindByName(column = "id")
 *     private int beanId;
 *
 *     // The String that becomes bigNumber must be a number with seven to ten digits.
 *     // The String for this field is after the word "value: " in the field.
 *     &#64;PreAssignmentValidator(validator = MustMatchRegexExpression.class, paramString = "^[A-Za-z ]*value: [0-9]{7,10}$")
 *     &#64;CsvBindByName(column = "big number", capture = "^[A-Za-z ]*value: (.*)$", format = "value: %s")
 *     private long bigNumber;
 * </pre>
 */
public class MustMatchRegexExpression implements StringValidator {
    private String regex = "";

    /**
     * Default constructor.
     */
    public MustMatchRegexExpression() {
        this.regex = "";
    }

    @Override
    public boolean isValid(String value) {
        if (regex.isEmpty()) {
            return true;
        }
        return value.matches(regex);
    }

    @Override
    public void validate(String value, BeanField field) throws CsvValidationException {
        if (!isValid(value)) {
            throw new CsvValidationException(String.format(ResourceBundle.getBundle("mustMatchRegex", field.getErrorLocale())
                    .getString("validator.regex.mismatch"), field.getField().getName(), value, regex));
        }
    }

    @Override
    public void setParameterString(String value) {
        if (value != null && !value.isEmpty()) {
            regex = value;
        }
    }
}
