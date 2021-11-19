package com.plugins.accountCF;
import com.atlassian.jira.issue.customfields.impl.DateCFType;
import edu.umd.cs.findbugs.annotations.NonNull;

import javax.validation.constraints.NotNull;
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
    private Double fullAmount; /**Полная сумма счета */
    private Double amount; /**Сумма каждой части счета */
    private Date date; /**Дата предположительной оплаты */
    private Double amountPost; /**Сумма релаьной оплаты */
    private Date datePost; /**Дата релаьной оплаты */
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
    public Carrier(Date date, Double amount, Date datePost, Double amountPost) {
        this.amount = amount;
        this.date = date;
        this.datePost = datePost;
        this.amountPost = amountPost;
    }
    public Carrier (Double fullAmount){
        this.fullAmount = fullAmount;
    }

    public Double getFullAmount(){
        return fullAmount;
    }

    public String getStringFullAmount() {
        return MessageFormat.format(PATTERN_FOR_DOUBLE, fullAmount);
    }

    public Double getAmount() {
        return amount;
    }

    public String getStringAmount() {
        return MessageFormat.format(PATTERN_FOR_DOUBLE, amount);
    }

    /** Высчитывает проценты от суммы */
    public @NotNull String getStringPercent() {
        Double percent =  amount / fullAmount * 100;
        return MessageFormat.format(PATTERN_FOR_DOUBLE, percent) + "%";
    }

    public Double getAmountPost() {
        return amountPost;
    }

    public String getStringAmountPost() {
        return MessageFormat.format(PATTERN_FOR_DOUBLE, amountPost);
    }

    public String getStringDate() {
        return format.format(date);
    }

    public String getStringDatePost() {
        return format.format(datePost);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        //sb.append("Carrier object: ");
        sb.append((date));
       // sb.append(" and ");
        sb.append((amount));
        sb.append(amountPost);
        sb.append(datePost);
        return sb.toString();
    }

}
