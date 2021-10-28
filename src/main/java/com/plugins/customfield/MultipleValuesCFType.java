package com.plugins.customfield;

import com.atlassian.plugin.spring.scanner.annotation.imports.JiraImport;
import org.apache.commons.lang.StringUtils;
import com.atlassian.jira.issue.customfields.persistence.PersistenceFieldType;
import com.atlassian.jira.util.ErrorCollection;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.AbstractSingleFieldType;
import com.atlassian.jira.issue.customfields.manager.GenericConfigManager;
import com.atlassian.jira.issue.customfields.persistence.CustomFieldValuePersister;
import com.atlassian.jira.issue.customfields.view.CustomFieldParams;
import com.atlassian.jira.issue.fields.CustomField;
import com.atlassian.jira.issue.fields.config.FieldConfig;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collection;
import org.apache.log4j.Logger;
import com.atlassian.jira.issue.customfields.CustomFieldType;

import javax.annotation.Nonnull;

/**
 * All the other Multi* classes refer to Users or Options. This class,
 * like VersionCFType, uses a different transport object, a Collection
 * of Carrier objects.
 *
 * The changes for JIRA 5.0 mean that the transport and singular types
 * have to be given as a parameter to AbstractCustomFieldType. Also
 *
 * More information can be found at
 * "https://developer.atlassian.com/display/JIRADEV/Java+API+Changes+in+JIRA+5.0#JavaAPIChangesinJIRA50-CustomFieldTypes
 */
public class MultipleValuesCFType extends AbstractSingleFieldType<Carrier> {

    public static final Logger log = Logger.getLogger(MultipleValuesCFType.class);
    private final CustomFieldValuePersister persister;
    private final GenericConfigManager genericConfigManager;
    // The type of data in the database, one entry per value in this field
    private static final PersistenceFieldType DB_TYPE = PersistenceFieldType.TYPE_UNLIMITED_TEXT;

    /**
     * Used in the database representation of a singular value.
     * Treated as a regex when checking text input.
     */
    public static final String DB_SEP = "###";
    protected MultipleValuesCFType(@JiraImport CustomFieldValuePersister customFieldValuePersister,
                                   @JiraImport GenericConfigManager genericConfigManager) {
        super(customFieldValuePersister, genericConfigManager);
        this.persister = customFieldValuePersister;
        this.genericConfigManager = genericConfigManager;
    }

    @Nonnull
    @Override
    protected PersistenceFieldType getDatabaseType() {
        return DB_TYPE;
    }

    @Override
    protected Object getDbValueFromObject(Carrier carrier) {
        log.debug("getDbValueFromObject: " + carrier);
        if (carrier == null) {
            return "";
        }
        //for (int i = 0; i < Carrier.NUMBER_OF_VALUES; i++)
       // {
        //}
        return carrier.getFullAmount().toString() +
                DB_SEP +
                carrier.getRate().toString() +
                DB_SEP +
                carrier.getAdvance().toString() +
                DB_SEP +
                carrier.getWillingness().toString()+
                DB_SEP +
                carrier.getDaysAdvance() +
                DB_SEP +
                carrier.getDaysWillingness();
    }

    @Override
    protected Carrier getObjectFromDbValue(@Nonnull Object databaseValue) throws FieldValidationException {
         return getSingularObjectFromString((String) databaseValue);
    }


    @Override
    public String getStringFromSingularObject(Carrier singularObject) {
        return singularObject.toString();
    }

    @Override
    public Carrier getSingularObjectFromString(String dbValue) throws FieldValidationException {
        log.debug("getSingularObjectFromString: " + dbValue);
        if (StringUtils.isEmpty(dbValue)) {
            return null;
        }
        String[] parts = dbValue.split(DB_SEP);
        if (parts.length == 0 || parts.length > Carrier.NUMBER_OF_VALUES) {
            log.warn("Invalid database value for MultipleValuesCFType ignored: " + dbValue);
            // If this should not be allowed, then throw a
            // FieldValidationException instead
            return null;
        }

        List<Double> valuesD = new ArrayList<>();
        for (int i = 0; i < Carrier.NUMBER_OF_DOUBLE_VALUES; i++)
            valuesD.add(Double.parseDouble(parts[i]));
        List<Integer> valuesI = new ArrayList<>();
        for (int i = Carrier.NUMBER_OF_DOUBLE_VALUES; i < (Carrier.NUMBER_OF_DOUBLE_VALUES + Carrier.NUMBER_OF_INT_VALUES); i++)
            valuesI.add(Integer.parseInt((parts[i])));
        return new Carrier(valuesD.get(0), valuesD.get(1), valuesD.get(2), valuesD.get(3), valuesI.get(0), valuesI.get(1));

    }
    @Override
    public Object getStringValueFromCustomFieldParams(CustomFieldParams parameters) {
        log.debug("getStringValueFromCustomFieldParams: " + parameters.getKeysAndValues());
        return parameters.getAllValues();
    }

    @Override
    public Carrier getValueFromIssue(CustomField field,
                                     Issue issue) {
        // This is also called to display a default value in view.vm
        // in which case the issue is a dummy one with no key
        if (issue == null || issue.getKey() == null) {
            log.debug("getValueFromIssue was called with a dummy issue for default");
            return null;
        }

        // These are the database representation of the singular objects
        final List<Object> value = persister.getValues(field, issue.getId(), DB_TYPE);
        log.debug("getValueFromIssue entered with " + value);
        if ((value != null) && !value.isEmpty()) {
            return getSingularObjectFromString((String)value.get(0));
        } else {
            return null;
        }
    }

    public Carrier getValueFromCustomFieldParams(CustomFieldParams parameters)
            throws FieldValidationException{
        log.debug("getValueFromCustomFieldParams: " + parameters.getKeysAndValues());
        // Strings in the order they appeared in the web page
        final Collection values = parameters.getValuesForNullKey();
        if ((values != null) && !values.isEmpty()) {
            Iterator it = values.iterator();
            List<String> dStr = new ArrayList<>();
            for (int i = 0;  i < Carrier.NUMBER_OF_VALUES; i++) {
                dStr.add((String) it.next());
                String temp = dStr.get(i).replaceAll(",", ".");
                dStr.set(i, temp);
            }
            String temp = dStr.get(3);//
            dStr.set(3, dStr.get(4) );// костыль
            dStr.set(4, temp);//
            List<Double> dDbl = new ArrayList<>();
            for(int i = 0;  i < Carrier.NUMBER_OF_DOUBLE_VALUES; i++) {
                try {
                    dDbl.add(Double.parseDouble(dStr.get(i)));
                } catch (NumberFormatException nfe) {
                    throw new FieldValidationException(dStr.get(i) + " должно быть числом");
                }
            }
            if (dDbl.get(1) > 1 || dDbl.get(1) < 0)
                throw new FieldValidationException(" Ставка не может быть больше 1");
            List<Integer> dInt = new ArrayList<>();
            for(int i = Carrier.NUMBER_OF_DOUBLE_VALUES;  i < (Carrier.NUMBER_OF_INT_VALUES+Carrier.NUMBER_OF_DOUBLE_VALUES); i++) {
                try {
                    dInt.add(Integer.parseInt(dStr.get(i)));
                } catch (NumberFormatException nfe) {
                    throw new FieldValidationException(dStr.get(i) + " Должно быть целочисленным числом");
                }
            }

            return new Carrier(dDbl.get(0), dDbl.get(1), dDbl.get(2), dDbl.get(3), dInt.get(0), dInt.get(1));
        } else
            {
            return null;
        }
    }

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

    public Carrier getDefaultValue(FieldConfig fieldConfig) {
        final Object o = genericConfigManager.retrieve(CustomFieldType.DEFAULT_VALUE_TYPE, fieldConfig.getId().toString());
        log.debug("getDefaultValue with database value " + o);

        Carrier carrier = null;
        if (o instanceof Carrier) {
            carrier = (Carrier) o;
        }

        return carrier; // No default value exists
    }
    public void setDefaultValue(FieldConfig fieldConfig, Carrier value) {
        log.debug("setDefaultValue with object " + value);
        Object strings = getDbValueFromObject(value);
            genericConfigManager.update(CustomFieldType.DEFAULT_VALUE_TYPE, fieldConfig.getId().toString(), strings);
    }

   /* public void createValue(CustomField field, Issue issue, Carrier value) {
        List<Carrier> collection = new ArrayList<Carrier>();
        collection.add(value);
        if (value instanceof Collection)
        {
            persister.createValues(field, issue.getId(), DB_TYPE, collection);
        }
        else
        {
            // With JIRA 5.0 we should no longer need to test for this case
            persister.createValues(field, issue.getId(), DB_TYPE, collection);
        }
    }

    public void updateValue(CustomField field, Issue issue, Carrier value) {
        List<Carrier> collection = new ArrayList<Carrier>();
        collection.add(value);
        persister.updateValues(field, issue.getId(), DB_TYPE, collection);
    }

   /* public Collection getDbValueFromCarrier(Carrier carrier) {
        log.debug("getDbValueFromObject: " + carrier);
        if (carrier == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        //for (int i = 0; i < Carrier.NUMBER_OF_VALUES; i++)
        // {
        sb.append(carrier.getFullAmount().toString());
        sb.append(DB_SEP);
        sb.append(carrier.getAdvance().toString());
        sb.append(DB_SEP);
        sb.append(carrier.getWillingness().toString());
        sb.append(DB_SEP);
        sb.append(carrier.getDaysAdvance().toString());
        sb.append(DB_SEP);
        sb.append(carrier.getDaysWillingness().toString());
        sb.append(DB_SEP);
        sb.append(carrier.getRate().toString());
        //}
        return sb.toString();
    }*/
}