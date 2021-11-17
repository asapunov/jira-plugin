package com.plugins.accountCF;
import com.atlassian.jira.issue.customfields.impl.DateCFType;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the singular object for this custom field type.
 * It has a Double field for amounts and a String field
 * for comments about the the amount.
 */
public class Carrier {

    private static final String PATTERN_FOR_DOUBLE = "{0,choice,-1<{0,number,'#.##'}|0#|0<{0,number,'###,###,###.##'}}";
    private static final String PATTERN_FOR_INT = "{0,choice,-1<{0,integer,'#.##'}|0#|0<{0,number,integer}}";
    private Double amount; /**Сумма */
    private Double percent; /**Прцоенты */
    private Date date; /**Дата оплаты */
    private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
    public Carrier(  Date date, Double percent, Double amount) {
        this.amount = amount;
        this.percent = percent;
        this.date = date;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getPercent() {
        return percent;
    }

    public String getStringAmount() {
        return MessageFormat.format(PATTERN_FOR_DOUBLE, amount);
    }

    public String getStringPercent() {
        return MessageFormat.format(PATTERN_FOR_DOUBLE, percent) + "%";
    }

    public String getStringDate() {
        return format.format(date);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        //sb.append("Carrier object: ");
        sb.append((date));
       // sb.append(" and ");
        sb.append((percent));
        sb.append((amount));
        return sb.toString();
    }

}
