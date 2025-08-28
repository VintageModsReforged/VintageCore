package mods.vintage.core.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class NumberFormatter {

    private NumberFormatter() {}

    public static String formatNumber(double number, int digits) {
        return formatNumber(number, digits, false);
    }

    public static String formatNumber(double number, int digits, boolean fixedLength) {
        String suffix = "";
        boolean allowSuffix = (number >= 1.0E9 ? String.valueOf((long) number) : String.valueOf(number)).length() > digits;
        double outputNumber = number;

        for (int i = 0; i < "kmbt".length() && outputNumber >= 1000.0 && allowSuffix; i++) {
            outputNumber /= 1000.0;
            suffix = Character.toString("kmbt".charAt(i));
        }

        int actualDigits = digits - suffix.length();
        if (outputNumber % 1.0 == 1.0) actualDigits++;

        int naturalLength = Format.INTEGER_COMMA.format((int) outputNumber).length();
        int decimalLength = Format.FRACTIONAL.format(outputNumber - (int) outputNumber).length();

        StringBuilder patternBuilder = new StringBuilder();
        for (int j = 1; actualDigits > 1 && naturalLength > 1; j++) {
            patternBuilder.insert(0, "#");
            actualDigits--;
            naturalLength--;

            if (j % 2 == 0 && actualDigits > 1 && naturalLength > 1) {
                if (actualDigits == 2 || naturalLength == 2) break;
                patternBuilder.insert(0, ",");
                actualDigits--;
                naturalLength--;
            }
        }

        patternBuilder.append("0");
        if (actualDigits > 1 && decimalLength > 0) {
            patternBuilder.append(".");
            actualDigits--;
            while (actualDigits > 0 && decimalLength > 0) {
                patternBuilder.append("#");
                actualDigits--;
                decimalLength--;
            }
        }

        String output = new DecimalFormat(patternBuilder.toString() + suffix, new DecimalFormatSymbols(Locale.ROOT))
                .format(outputNumber);

        if (fixedLength && output.length() < digits) {
            StringBuilder fill = new StringBuilder();
            for (int j = 0; j < digits - output.length(); j++) fill.append(" ");
            output = fill + output;
        }

        return output;
    }

    public static String formatInt(int number, int digits) {
        return formatInt(number, digits, false);
    }

    public static String formatInt(int number, int digits, boolean fixedLength) {
        return formatNumber(number, digits, fixedLength);
    }

    public static String formatLong(long number, int digits) {
        return formatLong(number, digits, false);
    }

    public static String formatLong(long number, int digits, boolean fixedLength) {
        return formatNumber(number, digits, fixedLength);
    }

    public enum Format {
        TWO_DECIMAL("#0.00"),       // generic two decimal format
        TWO_DECIMAL_PAD("#00.00"),  // two decimals, always at least 2 digits before decimal
        INTEGER_COMMA("###,##0"),   // whole numbers with commas
        FRACTIONAL(".#########"),   // decimal fraction, up to 9 digits
        INTEGER("###,###"),         // integer, general use
        FLOAT("###,###.##"),        // float, general use
        SMALL_DECIMAL("0.####"),    // small fractional values
        CUSTOM(null);               // pattern supplied dynamically

        private final String defaultPattern;

        Format(String pattern) {
            this.defaultPattern = pattern;
        }

        public String format(double value) {
            return format(value, defaultPattern);
        }

        public String format(int value) {
            return format((double) value, defaultPattern);
        }

        /** Format a value with a custom pattern (used for CUSTOM or ad-hoc formatting) */
        public static String format(double value, String pattern) {
            if (pattern == null) {
                throw new IllegalArgumentException("Pattern must be provided for CUSTOM format");
            }
            DecimalFormat df = new DecimalFormat(pattern, new DecimalFormatSymbols(Locale.ROOT));
            return df.format(value);
        }

        public static String format(int value, String pattern) {
            return format((double) value, pattern);
        }
    }
}
