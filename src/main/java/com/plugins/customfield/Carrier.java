package com.plugins.customfield;

import java.text.DecimalFormat;
import java.text.MessageFormat;

/**
 * This is the singular object for this custom field type.
 * It has a Double field for amounts and a String field
 * for comments about the the amount.
 */
public class Carrier {
    DecimalFormat dF = new DecimalFormat("###,###,###.##"); //формат вывода
    private final Double advance; /** Аванс */
    private final Double willingness; /** Уведомление по готовности */
    private final Double rate; /** Ставка */
    private final Double prepayment; /** Предоплата */
    private final Double postpaid; /** Постоплата */
    private final int  daysAdvance; /** Количество календарных дней выплаты аванса */
    private final int  daysWillingness; /** Количество календарных дней выплаты по готовности */
    private final int  daysPrepayment; /** Количество календарных дней выплаты предоплаты */
    private final int  daysPostpaid; /** количество календарных дней выплаты постоплаты */
    private static final String PATTERN_FOR_DOUBLE = "{0,choice,-1<{0,number,'#.##'}|0#|0<{0,number,'###,###,###.##'}}";
    private static final String PATTERN_FOR_INT = "{0,choice,-1<{0,integer,'#.##'}|0#|0<{0,number,integer}}";
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
        StringBuffer sb = new StringBuffer();
        sb.append(rate);
        sb.append(prepayment);
        sb.append(advance);
        sb.append(willingness);
        sb.append(postpaid);
        sb.append(daysPrepayment);
        sb.append(daysAdvance);
        sb.append(daysWillingness);
        sb.append(daysPostpaid);
        return sb.toString();
    }


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

    /**
     * Метод для расчета итогового значения
     */
    public String getAnswer()
    {
        Double rateF = rate / 36500;
        Double ans = prepayment * rateF * daysPrepayment +
                advance * rateF * daysAdvance +
                willingness * rateF * daysWillingness +
                postpaid * rateF * daysPostpaid;
        return dF.format(ans);
    }

    /**
     * Методы для форматированного вывода
     */
    public String getFormatPrepayment() { return MessageFormat.format(PATTERN_FOR_DOUBLE, prepayment); }

    public String getFormatAdvance() { return MessageFormat.format(PATTERN_FOR_DOUBLE, advance); }

    public String getFormatWillingness() { return MessageFormat.format(PATTERN_FOR_DOUBLE, willingness); }

    public String getFormatPostpaid() { return MessageFormat.format(PATTERN_FOR_DOUBLE, postpaid);}

    public String getFormatRate() { return MessageFormat.format(PATTERN_FOR_DOUBLE, rate); }

    public String getFormatDaysPrepayment() {
        return MessageFormat.format(PATTERN_FOR_INT, daysPrepayment);
    }

    public String getFormatDaysAdvance() {
        return MessageFormat.format(PATTERN_FOR_INT, daysAdvance);
    }

    public String getFormatDaysWillingness() {
        return MessageFormat.format(PATTERN_FOR_INT, daysWillingness);
    }

    public String getFormatDaysPostpaid() {
        return MessageFormat.format(PATTERN_FOR_INT, daysPostpaid);
    }

}
