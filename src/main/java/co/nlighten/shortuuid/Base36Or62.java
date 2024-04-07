package co.nlighten.shortuuid;

import java.math.BigInteger;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * Base36Or62 is a utility class that provides methods to encode and decode numbers using Base36 or Base62 encoding.
 */
public class Base36Or62 {

    private static final BigInteger BASE_62 = BigInteger.valueOf(62);
    private static final String DIGITS_62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS_62_REGEX = "[" + DIGITS_62 + "]*";
    private static final BigInteger BASE_36 = BigInteger.valueOf(36);
    private static final String DIGITS_36 = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS_36_REGEX = "[" + DIGITS_36 + "]*";

    /**
     * Encodes a number using Base36 encoding.
     *
     * @param number a positive integer
     * @param base62 whether to use Base62 or Base36
     * @return a Base36 string
     *
     * @throws IllegalArgumentException if <code>number</code> is a negative integer
     */
    static String encode(BigInteger number, boolean base62) {
        if (number.compareTo(BigInteger.ZERO) < 0) {
            throw new IllegalArgumentException("number must not be negative");
        }
        var base = base62 ? BASE_62 : BASE_36;
        var digits = base62 ? DIGITS_62 : DIGITS_36;

        StringBuilder result = new StringBuilder();
        while (number.compareTo(BigInteger.ZERO) > 0) {
            var divMod = number.divideAndRemainder(base);
            number = divMod[0];
            result.insert(0, digits.charAt(divMod[1].intValue()));
        }
        return (result.length() == 0) ? digits.substring(0, 1) : result.toString();
    }

    /**
     * Decodes a string using Base36 encoding.
     *
     * @param string a Base36 string
     * @param base62 whether to use Base62 or Base36
     * @param bitLimit amount of bits the number is allowed to have
     * @return a positive integer
     *
     * @throws IllegalArgumentException if <code>string</code> is empty
     */
    static BigInteger decode(final String string, boolean base62, int bitLimit) {
        requireNonNull(string, "Decoded string must not be null");
        if (string.length() == 0) {
            throw new IllegalArgumentException(String.format("String '%s' must not be empty", string));
        }

        var base = base62 ? BASE_62 : BASE_36;
        var digits = base62 ? DIGITS_62 : DIGITS_36;
        var regexp = base62 ? DIGITS_62_REGEX : DIGITS_36_REGEX;

        if (!Pattern.matches(regexp, string)) {
            throw new IllegalArgumentException(String.format("String '%s' contains illegal characters, only '%s' are allowed", string, digits));
        }

        return IntStream.range(0, string.length())
            .mapToObj(index -> BigInteger
                    .valueOf(digits.indexOf(string.charAt(string.length() - index - 1)))
                    .multiply(base.pow(index))
            )
            .reduce(BigInteger.ZERO, (acc, value) -> {
                var sum = acc.add(value);
                if (bitLimit > 0 && sum.bitLength() > bitLimit) {
                    throw new IllegalArgumentException(String.format("String '%s' contains more than %d bit information", string, bitLimit));
                }
                return sum;
            });

    }
}