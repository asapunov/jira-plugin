package com.plugins.customfield;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This is the singular object for this custom field type.
 * It has a Double field for amounts and a String field
 * for comments about the the amount.
 */
public class Carrier {

    private final Double advance;
    private final Double willingness;
    private final Double rate;
    private final Double prepayment;
    private final Double postpaid;
    private final int  daysAdvance;
    private final int  daysWillingness;
    private final int  daysPrepayment;
    private final int  daysPostpaid;
    public static final int NUMBER_OF_VALUES = 9;
    public static final int NUMBER_OF_INT_VALUES = 4;
    public static final int NUMBER_OF_DOUBLE_VALUES = 5;
    public Carrier( Double rate, Double prepayment, Double advance,  Double willingness,
                    Double postpaid, int daysPrepayment, int daysAdvance, int daysWillingness, int daysPostpaid) {
        this.advance = advance;
        this.willingness = willingness;
        this.daysAdvance = daysAdvance;
        this.daysWillingness = daysWillingness;
        this.rate = rate;
        this.prepayment = prepayment;
        this.postpaid = postpaid;
        this.daysPrepayment = daysPrepayment;
        this.daysPostpaid = daysPostpaid;
    }

    public String toString() {
        //sb.append("Сумма кредита: ");
        String sb = String.valueOf(rate) +
                prepayment +
                (advance) +
                (willingness) +
                postpaid +
                daysPrepayment +
                daysAdvance +
                daysWillingness +
                daysPostpaid;
        return sb;
    }


    //public Double getFullAmount() {
    //return fullAmount;
    // }
    public Double getPrepayment() { return prepayment;}

    public Double getAdvance() {
        return advance;
    }

    public Double getWillingness() {
        return willingness;
    }

    public Double getPostpaid() { return postpaid;}

    public int getDaysPrepayment() {
        return daysPrepayment;
    }

    public int getDaysAdvance() {
        return daysAdvance;
    }

    public int getDaysWillingness() {
        return daysWillingness;
    }

    public int getDaysPostpaid() {
        return daysPostpaid;
    }

    public Double getRate() {
        return rate;
    }

    public String getAnswer()
    {
        DecimalFormat dF = new DecimalFormat("#.##");
        Double ans = prepayment * rate / 36500 * daysPrepayment +
                advance * rate / 36500 * daysAdvance +
                willingness * rate / 36500 * daysWillingness +
                postpaid * rate / 36500 * daysPostpaid;
        return dF.format(ans);
    }

   /* public ArrayList<Double> getCarrierValues(){
        ArrayList<Double> allValues = new ArrayList<Double>();
        allValues.add(fullAmount);
        allValues.add(advance);
        allValues.add(daysAdvance);
        allValues.add(daysWillingness);
        allValues.add(rate);
        return allValues;
    }*/
}
