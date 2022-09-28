package com.enviogroup.plugins.accountCF;

import com.atlassian.jira.datetime.DateTimeFormatterFactory;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.CustomFieldType;
import com.atlassian.jira.issue.customfields.converters.DatePickerConverter;
import com.atlassian.jira.issue.customfields.impl.AbstractCustomFieldType;
import com.atlassian.jira.issue.customfields.impl.DateCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;
import com.atlassian.jira.issue.customfields.view.CustomFieldParams;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import com.atlassian.jira.issue.fields.layout.field.FieldLayoutItem;
import com.atlassian.jira.util.DateFieldFormat;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.jira.util.velocity.NumberTool;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NotNullPredicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.DateTool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * All the other Multi* classes refer to Users or Options. This class,
 * like VersionCFType, uses a different transport object, a Collection
 * of Carrier objects.
 * <p>
 * The changes for JIRA 5.0 mean that the transport and singular types
 * have to be given as a parameter to AbstractCustomFieldType. Also
 * <p>
 * More information can be found at
 * "https://developer.atlassian.com/display/JIRADEV/Java+API+Changes+in+JIRA+5.0#JavaAPIChangesinJIRA50-CustomFieldTypes
 */
public class MultipleValuesCFType extends AbstractCustomFieldType<Collection<Carrier>, Carrier> {

    public static final Logger log = Logger.getLogger(MultipleValuesCFType.class);
    /**
     * Used in the database representation of a singular value.
     * Treated as a regex when checking text input.
     */
    public static final String DB_SEP = "###";
    // The type of data in the database, one entry per value in this field
    private static final PersistenceFieldType DB_TYPE = PersistenceFieldType.TYPE_UNLIMITED_TEXT;

    private final CustomFieldValuePersister persister;
    private final GenericConfigManager genericConfigManager;
    private final DatePickerConverter datePickerConverter;
    private final DateTimeFormatterFactory dateTimeFormatterFactory;
    private final DateFieldFormat dateFieldFormat;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    public MultipleValuesCFType(@ComponentImport CustomFieldValuePersister customFieldValuePersister,
                                @ComponentImport GenericConfigManager genericConfigManager,
                                @ComponentImport DatePickerConverter datePickerConverter,
                                @ComponentImport DateTimeFormatterFactory dateTimeFormatterFactory,
                                @ComponentImport DateFieldFormat dateFieldFormat) {
        this.persister = customFieldValuePersister;
        this.genericConfigManager = genericConfigManager;
        this.datePickerConverter = datePickerConverter;
        this.dateTimeFormatterFactory = dateTimeFormatterFactory;
        this.dateFieldFormat = dateFieldFormat;
    }

    /**
     * Convert a database representation of a Carrier object into
     * a Carrier object.
     */
    public Carrier getSingularObjectFromString(String dbValue)
            throws FieldValidationException {
        log.debug("getSingularObjectFromString: " + dbValue);
        if (StringUtils.isEmpty(dbValue)) {
            return null;
        }
        String[] parts = dbValue.split(DB_SEP);
        if (parts.length == 1) {
            log.warn("Invalid database value for MultipleValuesCFType ignored: " + dbValue);
            Double fA = Double.parseDouble(parts[0]);
            // If this should not be allowed, then throw a
            // FieldValidationException instead
            return new Carrier(fA);
        }

        Date d = null;
        Date dP = null;
        try {
            d = sdf.parse(parts[0]);
            dP = sdf.parse(parts[2]);
        } catch (ParseException e) {
            log.warn(e.getMessage());
        }
        Double p = Double.parseDouble(parts[1]);
        Double a = null;
        if (!parts[3].equals("null"))
            a = Double.parseDouble(parts[3]);
        return new Carrier(d, p, dP, a);
    }

    public Collection<Carrier> getValueFromIssue(CustomField field,
                                                 Issue issue) {
        // This is also called to display a default value in view.vm
        // in which case the issue is a dummy one with no key
        if (issue == null || issue.getKey() == null) {
            log.debug("getValueFromIssue was called with a dummy issue for default");
            return null;
        }

        // These are the database representation of the singular objects
        final List<Object> values = persister.getValues(field, issue.getId(), DB_TYPE);
        log.debug("getValueFromIssue entered with " + values);
        if ((values != null) && !values.isEmpty()) {
            List<Carrier> result = new ArrayList<Carrier>();
            for (Iterator it = values.iterator(); it.hasNext(); ) {
                String dbValue = (String) it.next();
                Carrier carrier = getSingularObjectFromString(dbValue);
                if (carrier == null) {
                    continue;
                }
                result.add(carrier);
            }
            return result;
        } else {
            return null;
        }
    }

    public void createValue(CustomField field, Issue issue, Collection<Carrier> value) {
        if (value instanceof Collection) {
            persister.createValues(field, issue.getId(), DB_TYPE, getDbValueFromCollection(value));
        } else {
            // With JIRA 5.0 we should no longer need to test for this case
            persister.createValues(field, issue.getId(), DB_TYPE, getDbValueFromCollection(Lists.newArrayList(value)));
        }
    }

    public void updateValue(CustomField field, Issue issue, Collection<Carrier> value) {
        persister.updateValues(field, issue.getId(), DB_TYPE, getDbValueFromCollection(value));
    }

    /**
     * For removing the field, not for removing one value
     */
    public Set<Long> remove(CustomField field) {
        return persister.removeAllValues(field.getId());
    }

    /**
     * Convert a transport object (a Collection of Carrier objects) to
     * its database representation and store it in the database.
     */
    public void setDefaultValue(FieldConfig fieldConfig, Collection<Carrier> value) {
        log.debug("setDefaultValue with object " + value);
        Collection carrierStrings = getDbValueFromCollection(value);
        if (carrierStrings != null) {
            carrierStrings = new ArrayList(carrierStrings);
            genericConfigManager.update(CustomFieldType.DEFAULT_VALUE_TYPE, fieldConfig.getId().toString(), carrierStrings);
        }
    }

    /**
     * Retrieve the stored default value (if any) from the database
     * and convert it to a transport object (a Collection of Carrier
     * objects).
     */
    public Collection<Carrier> getDefaultValue(FieldConfig fieldConfig) {
        final Object o = genericConfigManager.retrieve(CustomFieldType.DEFAULT_VALUE_TYPE, fieldConfig.getId().toString());
        log.debug("getDefaultValue with database value " + o);

        Collection<Carrier> collectionOfCarriers = null;
        if (o instanceof Collection) {
            collectionOfCarriers = (Collection) o;
        } else if (o instanceof Carrier) {
            log.warn("Backwards compatible default value, should not occur");
            collectionOfCarriers = Lists.newArrayList((Collection) o);
        }

        if (collectionOfCarriers == null) {
            return null; // No default value exists
        }

        final Collection collection = CollectionUtils.collect(collectionOfCarriers, new Transformer() {
            // Convert a database value (String) to a singular Object (Carrier)
            public Object transform(final Object input) {
                if (input == null) {
                    return null;
                }
                String dbValue = (String) input;
                return getSingularObjectFromString(dbValue);
            }
        });
        CollectionUtils.filter(collection, NotNullPredicate.getInstance());
        log.debug("getDefaultValue returning " + collection);
        return collection;
    }

    /**
     * Validate the input from the web pages, a Collection of Strings.
     * Exceptions raised later on after this has passed appear as an
     * ugly page.
     */
    public void validateFromParams(CustomFieldParams relevantParams,
                                   ErrorCollection errorCollectionToAddTo,
                                   FieldConfig config) {
        log.debug("validateFromParams: " + relevantParams.getKeysAndValues());
        try {
            getValueFromCustomFieldParams(relevantParams);
        } catch (FieldValidationException fve) {
            errorCollectionToAddTo.addError(config.getCustomField().getId(), fve.getMessage());
        }
    }

    /**
     * Extract a transport object from the string parameters,
     * Clearing an amount removes the row.
     */
    public Collection<Carrier> getValueFromCustomFieldParams(CustomFieldParams parameters)
            throws FieldValidationException {
        log.debug("getValueFromCustomFieldParams: " + parameters.getKeysAndValues());
        // Strings in the order they appeared in the web page
        final Collection values = parameters.getValuesForNullKey();
        if ((values != null) && !values.isEmpty()) {
            Collection<Carrier> value = new ArrayList();
            Iterator it = values.iterator();
            String fStr = ((String) it.next()).replaceAll("[\\s|\\u00A0]+", "");
            fStr = fStr.replaceAll(",", ".");
            try {
                Double f = Double.parseDouble(fStr);
                value.add(new Carrier(f));
            } catch (NumberFormatException nfe) {
                // A value was provided but it was an invalid value
                throw new FieldValidationException("Полная сумма счета должна быть числом");
            }
            while (it.hasNext()) {
                String dStr = (String) it.next();
                // This won't be true if only one parameter is passed in a query
                String aStr = ((String) it.next()).replaceAll("[\\s|\\u00A0]+", "");
                aStr = aStr.replaceAll(",", ".");
                // Allow empty text but not empty amounts
                String dpStr = (String) it.next();
                String apStr = ((String) it.next()).replaceAll("[\\s|\\u00A0]+", "");
                apStr = apStr.replaceAll(",", ".");
                if (dStr == null || dStr.equals("")) {
                    log.debug("Ignoring text " + aStr + " because the amount is empty");
                    // This is used to delete a row so do not throw a
                    // FieldValidationException
                    continue;
                }
                Date d = null;
                Date dP = null;
                if (!(StringUtils.isEmpty(dStr)) && (StringUtils.isEmpty(dpStr) || dpStr.equals("null"))) {
                    try {
                        d = sdf.parse(dStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new FieldValidationException("Вы не ввели верную дату. Пожалуйста, введите дату в формате \"dd.MM.yyyy\", " +
                                "например. \"24.11.2021\"");
                    }
                } else {
                    try {
                        d = sdf.parse(dStr);
                        dP = sdf.parse(dpStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        throw new FieldValidationException("Вы не ввели верную дату. Пожалуйста, введите дату в формате \"dd.MM.yyyy\", " +
                                "например. \"24.11.2021\"");
                    }
                }
                Double a = null;
                Double aP = null;
                if (!(StringUtils.isEmpty(aStr)) && (StringUtils.isEmpty(apStr)) || apStr == null)
                    try {
                        a = Double.parseDouble(aStr);
                    } catch (NumberFormatException nfe) {
                        // A value was provided but it was an invalid value
                        throw new FieldValidationException("Поля со значениями суммы должны быть числовыми");
                    }
                else {
                    try {
                        a = Double.parseDouble(aStr);
                        aP = Double.parseDouble(apStr);
                    } catch (NumberFormatException nfe) {
                        // A value was provided but it was an invalid value
                        throw new FieldValidationException("Поля со значениями суммы должны быть числовыми");
                    }
                }
                value.add(new Carrier(d, a, dP, aP));
            }
            if (value.size() < 2) {
                throw new FieldValidationException("Необходимо заполнить хоть одну строчку с датой выплат");
            }
            return value;
        } else {
            return null;
        }
    }

    /**
     * This method is used to create the $value object in Velocity templates.
     */
    public Object getStringValueFromCustomFieldParams(CustomFieldParams parameters) {
        log.debug("getStringValueFromCustomFieldParams: " + parameters.getKeysAndValues());
        return parameters.getAllValues();
    }

    public String getStringFromSingularObject(Carrier singularObject) {
        return singularObject.toString();
    }

    public String getChangelogValue(CustomField field, Collection<Carrier> value) {
        if (value == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        Collection<Carrier> carriers = value;
        for (Carrier carrier : carriers) {
            sb.append(carrier.toString());
            // Newlines are text not HTML here
            sb.append(", ");

        }
        return sb.toString();
    }

    // Helper Methods

    /**
     * Convert the Transport object to a collection of the
     * representation used in the database.
     */
    private Collection getDbValueFromCollection(final Collection<Carrier> value) {
        log.debug("getDbValueFromCollection: " + value);
        if (value == null) {
            return Collections.EMPTY_LIST;
        }
        Collection<Carrier> carriers = value;
        List<String> result = new ArrayList<String>();
        int i = 0;
        for (Carrier carrier : carriers) {
            if (carrier == null) {
                continue;
            }
            StringBuffer sb = new StringBuffer();
            if (i == 0) {
                sb.append(carrier.getFullAmount());
                result.add(sb.toString());
            } else {
                sb.append(carrier.getStringDate());
                sb.append(DB_SEP);
                sb.append(carrier.getAmount());
                sb.append(DB_SEP);
                sb.append(carrier.getStringDatePost());
                sb.append(DB_SEP);
                sb.append(carrier.getAmountPost());
                result.add(sb.toString());
            }
            i++;
        }
        return result;
    }

    @Override
    public Map<String, Object> getVelocityParameters(final Issue issue,
                                                     final CustomField field,
                                                     final FieldLayoutItem fieldLayoutItem) {


        //NumberTool numberTool = new NumberTool();
        //map.put("number", numberTool);
        //$dateTimeFormat $dateFormat $timeFormat $dateTimePicker $currentMillis $currentCalendar
        DateCFType dateCFType = new DateCFType(persister, datePickerConverter, genericConfigManager, null, dateFieldFormat,
                dateTimeFormatterFactory, null);
        final Map<String, Object> map = dateCFType.getVelocityParameters(issue, field, fieldLayoutItem);
        NumberTool numberTool = new NumberTool(new Locale("ru", "RU"));
        map.put("number", numberTool);
        DateTool dateTool = new DateTool();
        map.put("date", dateTool);
        return map;
    }

}