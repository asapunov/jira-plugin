package com.plugins.customfield;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This is the singular object for this custom field type.
 * It has a Double field for amounts and a String field
 * for comments about the the amount.
 */
public class Carrier {

    private final Double fullAmount, advance, willingness, rate ;
    private final int  daysAdvance, daysWillingness;
    public static final int NUMBER_OF_VALUES = 6;
    public static final int NUMBER_OF_INT_VALUES = 2;
    public static final int NUMBER_OF_DOUBLE_VALUES = 4;
    public Carrier( Double fullAmount, Double rate, Double advance,  Double willingness, int daysAdvance, int daysWillingness) {
        this.fullAmount = fullAmount;
        this.advance = advance;
        this.willingness = willingness;
        this.daysAdvance = daysAdvance;
        this.daysWillingness = daysWillingness;
        this.rate = rate;
    }

    public String toString() {
        //sb.append("Сумма кредита: ");
        String sb = String.valueOf(fullAmount) +
                //sb.append(" Доля аванса: ");
                (rate) +
                //sb.append(" Доля готовности: ");
                (advance) +
                //sb.append(" Дней аванса: ");
                (willingness) +
                //sb.append(" Дней готовности: ");
                daysAdvance +
                //sb.append(" Ставка: ");
                daysWillingness;
        return sb;
    }


    public Double getFullAmount() {
        return fullAmount;
    }

    public Double getAdvance() {
        return advance;
    }

    public Double getWillingness() {
        return willingness;
    }

    public int getDaysAdvance() {
        return daysAdvance;
    }

    public int getDaysWillingness() {
        return daysWillingness;
    }

    public Double getRate() {
        return rate;
    }

    public String getAnswer()
    {
        DecimalFormat dF = new DecimalFormat("#.##");
        Double ans= advance*rate/365*daysAdvance+willingness*rate/365*daysWillingness;
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
