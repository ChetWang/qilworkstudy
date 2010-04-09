package com.nci.domino.utils;

import java.math.BigDecimal;
import java.util.Date;

public class BeanParamUtils {

    public static BigDecimal parseBigDecimal(String value) {
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return null;
        }
    }

    public static BigDecimal parseBigDecimal(long value) {
        return parseBigDecimal(String.valueOf(value));
    }

    public static BigDecimal parseBigDecimal(int value) {
        return parseBigDecimal(String.valueOf(value));
    }

    public static BigDecimal parseBigDecimal(float value) {
        return parseBigDecimal(String.valueOf(value));
    }

    public static BigDecimal parseBigDecimal(double value) {
        return parseBigDecimal(String.valueOf(value));
    }

    public static int getIntValue(BigDecimal number) {
        if (number == null) {
            return 0;
        } else {
            return number.intValue();
        }
    }

    public static double getDoubleValue(BigDecimal number) {
        if (number == null) {
            return 0.0;
        } else {
            return number.doubleValue();
        }
    }

    public static Date getDateValue(Date date) {
        if (date == null) {
            return null;
        } else {
            return new Date(date.getTime());
        }
    }
}
