package com.plugins.customfield;

import java.util.ArrayList;

/**
 * This is the singular object for this custom field type.
 * It has a Double field for amounts and a String field
 * for comments about the the amount.
 */
public class Carrier {

    private final Double fullAmount, advance, willingness,  daysAdvance, daysWillingness, rate ;
    public static final int NUMBER_OF_VALUES = 6;

    public Carrier( Double fullAmount, Double rate, Double advance, Double daysAdvance, Double willingness, Double daysWillingness) {
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
                (daysAdvance) +
                //sb.append(" Дней готовности: ");
                willingness +
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

    public Double getDaysAdvance() {
        return daysAdvance;
    }

    public Double getDaysWillingness() {
        return daysWillingness;
    }

    public Double getRate() {
        return rate;
    }

    public Double getAnswer()
    {
        Double ans= advance*rate/365*daysAdvance+willingness*rate/365*daysWillingness;
        return ans;
    }

    public ArrayList<Double> getCarrierValues(){
        ArrayList<Double> allValues = new ArrayList<Double>();
        allValues.add(fullAmount);
        allValues.add(advance);
        allValues.add(daysAdvance);
        allValues.add(daysWillingness);
        allValues.add(rate);
        return allValues;
    }
}
