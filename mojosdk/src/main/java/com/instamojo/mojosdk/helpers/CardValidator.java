package com.instamojo.mojosdk.helpers;


import com.instamojo.mojosdk.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Authored by Vedhavyas on 12/07/15.
 */
public class CardValidator {
    /**
     * Luhn Alogrithm
     *
     * @param card card value
     * @return valid value 0 or 1
     */
    public static int isValid(String card, boolean skipLengthCheck) {

        if (card.length() < 4) {
            return 0;
        }

        if (!skipLengthCheck) {
            if (validateCardTypeWithoutLengthForLimit(card) != card.length()) {
                return 0;
            }
        }

        long number = Long.valueOf(card);
        int total = sumOfDoubleEvenPlace(number) + sumOfOddPlace(number);

        if ((total % 10 == 0) && (prefixMatched(number, 1) != 0)) {
            return prefixMatched(number, 1);
        } else {
            return 0;
        }
    }

    public static int getDigit(int number) {

        if (number <= 9) {
            return number;
        } else {
            int firstDigit = number % 10;
            int secondDigit = number / 10;

            return firstDigit + secondDigit;
        }
    }

    public static int sumOfOddPlace(long number) {
        int result = 0;

        while (number > 0) {
            result += (int) (number % 10);
            number = number / 100;
        }

        return result;
    }

    public static int sumOfDoubleEvenPlace(long number) {

        int result = 0;
        long temp;

        while (number > 0) {
            temp = number % 100;
            result += getDigit((int) (temp / 10) * 2);
            number = number / 100;
        }

        return result;
    }

    public static int prefixMatched(long number, int d) {

        if ((getPrefix(number, d) == 3)
                || (getPrefix(number, d) == 4)
                || (getPrefix(number, d) == 5)
                || (getPrefix(number, d) == 6)) {

            if (getPrefix(number, d) == 3) {
                return 3;
            } else if (getPrefix(number, d) == 4) {
                return 4;
            } else if (getPrefix(number, d) == 5) {
                return 5;
            } else if (getPrefix(number, d) == 6) {
                return 6;
            }

            return 1;
        } else {
            return 0;

        }
    }

    public static int getSize(long d) {

        int count = 0;

        while (d > 0) {
            d = d / 10;

            count++;
        }

        return count;

    }

    /**
     * Return the first k number of digits from number. If the number of digits
     * in number is less than k, return number.
     */
    public static long getPrefix(long number, int k) {

        if (getSize(number) < k) {
            return number;
        } else {

            int size = getSize(number);

            for (int i = 0; i < (size - k); i++) {
                number = number / 10;
            }

            return number;

        }

    }

    public static boolean visaCard(String card) {
        String PREFIX = "4";
        return (
                card.substring(0, 1).equals(PREFIX)
                        && (card.length() == 13 || card.length() == 16));
    }

    public static boolean discoverCard(String card) {
        String PREFIX = "6011";
        return (card.substring(0, 4).equals(PREFIX) && (card.length() == 16));
    }

    public static boolean dinnersClubInternationalCard(String card) {
        String PREFIX = "36";
        return (card.substring(0, 2).equals(PREFIX) && (card.length() == 14));
    }

    public static boolean amexCard(String card) {
        String PREFIX = "34,37,";
        String prefix2 = card.substring(0, 2) + ",";
        return ((PREFIX.contains(prefix2)) && (card.length() == 15));
    }

    public static boolean masterCard(String card) {
        String PREFIX = "51,52,53,54,55,";
        String prefix2 = card.substring(0, 2) + ",";
        return ((PREFIX.contains(prefix2)) && (card.length() == 16));
    }

    public static boolean visaCardWithoutLength(String card) {
        String PREFIX = "4";
        return (card.substring(0, 1).equals(PREFIX));
    }

    public static boolean discoverCardWithoutLength(String card) {
        String PREFIX = "6011";
        return (card.substring(0, 4).equals(PREFIX));
    }

    public static boolean dinnersClubIntWithoutLength(String card) {
        String PREFIX = "36";
        return (card.substring(0, 2).equals(PREFIX));
    }

    public static boolean amexCardWithoutLength(String card) {
        String PREFIX = "34,37,";
        String prefix2 = card.substring(0, 2) + ",";
        return ((PREFIX.contains(prefix2)));
    }

    public static boolean masterCardWithoutLength(String card) {
        String PREFIX = "51,52,53,54,55,";
        String prefix2 = card.substring(0, 2) + ",";
        return ((PREFIX.contains(prefix2)));
    }

    public static boolean maestroCard(String card) {
        String PREFIX = "5018,5044,5020,5038,5893,6304,6759,6761,6762,6763,6220,";
        String prefix2 = card.substring(0, 4) + ",";
        return ((PREFIX.contains(prefix2)));
    }


    public static int getCardDrawable(String card) {

        if (visaCardWithoutLength(card)) {
            return R.drawable.ic_visa_card;
        }
        if (discoverCardWithoutLength(card)) {
            return R.drawable.ic_discover_card;
        }
        if (dinnersClubIntWithoutLength(card)) {
            return R.drawable.ic_dinners_club_int_card;
        }
        if (amexCardWithoutLength(card)) {
            return R.drawable.ic_amex_card;
        }
        if (masterCardWithoutLength(card)) {
            return R.drawable.ic_master_card;
        }
        if (maestroCard(card)) {
            return R.drawable.ic_maestro_card;
        }
        return R.drawable.ic_unknown_card;
    }

    public static String getCardType(String card) {

        if (visaCardWithoutLength(card)) {
            return "Visa";
        }
        if (discoverCardWithoutLength(card)) {
            return "Discover";
        }
        if (dinnersClubIntWithoutLength(card)) {
            return "Dinners club Int";
        }
        if (amexCardWithoutLength(card)) {
            return "Amex";
        }
        if (masterCardWithoutLength(card)) {
            return "Master";
        }
        if (maestroCard(card)) {
            return "Maestro";
        }
        return "Unknown";
    }

    public static int validateCardTypeWithoutLengthForLimit(String card) {
        if (card.contains(" ")) {
            card = card.replace(" ", "");
        }
        card = card.trim();
        if (visaCardWithoutLength(card)) {
            return 16;
        }
        if (discoverCard(card)) {
            return 16;
        }
        if (dinnersClubIntWithoutLength(card)) {
            return 14;
        }
        if (amexCardWithoutLength(card)) {
            return 15;
        }
        if (masterCardWithoutLength(card)) {
            return 16;
        }
        if (maestroCard(card)) {
            return 19;
        }
        return 19;
    }

    public static boolean isDateExpired(String expiry) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        dateFormat.setLenient(false);
        try {
            Date expiryDate = dateFormat.parse(expiry);
            return expiryDate.before(new Date());
        } catch (ParseException e) {
            return true;
        }
    }
}
