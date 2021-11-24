package com.plugins.accountCF;
import com.atlassian.jira.util.DateFieldFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the singular object for this custom field type.
 * It has a Double field for amounts and a String field
 * for comments about the the amount.
 */
public class Carrier {

    private Double fullAmount; /**Полная сумма счета */
    private Double amount; /**Сумма каждой части счета */
    private Date date; /**Дата предположительной оплаты */
    private Double amountPost; /**Сумма релаьной оплаты */
    private Date datePost; /**Дата релаьной оплаты */
    private final SimpleDateFormat format = new SimpleDateFormat("d/MMM/yy");
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

    public Double getAmount() {
        return amount;
    }

    public Double getAmountPost() {
        if (amountPost != null)
            return amountPost;
        else
            return 0.0;
    }
    /**
     Возвращает дату планирующейся оплаты в формате, который принят в Jira по умолчанию (d/MMM/yy)
     */
    public String getStringDate() {
        return format.format(date);
    }
    /**
     Возвращает дату рельаной оплаты в формате, который принят в Jira по умолчанию (d/MMM/yy)
     */
    public String getStringDatePost() {
        if (datePost != null)
            return format.format(datePost);
        else
            return "0";
    }
    public Date getDate() {
        return date;
    }

    public Date getDatePost() {
        return date;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append((date));
        sb.append((amount));
        sb.append(amountPost);
        sb.append(datePost);
        return sb.toString();
    }

}
