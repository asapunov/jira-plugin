package com.enviogroup.plugins.accountCF;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This is the singular object for this custom field type.
 */
public class Carrier {

    /**
     * Дата релаьной оплаты
     */
    private final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
    private Double fullAmount;
    /**
     * Полная сумма счета
     */
    private Double amount;
    /**
     * Сумма каждой части счета
     */
    private Date date;
    /**
     * Дата предположительной оплаты
     */
    private Double amountPost;
    /**
     * Сумма релаьной оплаты
     */
    private Date datePost;

    public Carrier(Date date, Double amount, Date datePost, Double amountPost) {
        this.amount = amount;
        this.date = date;
        this.datePost = datePost;
        this.amountPost = amountPost;
    }

    public Carrier(Double fullAmount) {
        this.fullAmount = fullAmount;
    }

    public Double getFullAmount() {
        return fullAmount;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getAmountPost() {
        if (amountPost != null)
            return amountPost;
        else
            return null;
    }

    /**
     * Возвращает дату планирующейся оплаты в формате dd.MM.yyyy
     */
    public String getStringDate() {
        return format.format(date);
    }

    /**
     * Возвращает дату рельаной оплаты в формате dd.MM.yyyy
     */
    public String getStringDatePost() {
        if (datePost != null)
            return format.format(datePost);
        else
            return null;
    }

    public Date getDate() {
        return date;
    }

    public Date getDatePost() {
        return datePost;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append((date));
        sb.append((amount));
        sb.append(amountPost);
        sb.append(datePost);
        return sb.toString();
    }

}
