package co.nlighten.shortuuid;

import java.math.BigInteger;
import java.util.UUID;

/**
 * ShortUuid is a class that can be used to encode and decode UUIDs to a shorter string representation.
 */
public class ShortUuid {

    private final static int UUID_BITS = 128;

    /**
     * Create a ShortUuid
     *
     * @param caseSensitive whether the id can be case-sensitive (in this case use a larger base to encode the id)
     * @return ShortUuid encoded UUID
     */
    public static String random(boolean caseSensitive) {
        return toShortUuid(UUID.randomUUID(), caseSensitive);
    }

    /**
     * Encode UUID to ShortUuid
     *
     * @param uuid UUID to be encoded
     * @param caseSensitive whether the id can be case-sensitive (in this case use a larger base to encode the id)
     * @return ShortUuid encoded UUID
     */
    public static String toShortUuid(UUID uuid, boolean caseSensitive) {
        BigInteger pair = UuidConverter.toBigInteger(uuid);
        return Base36Or62.encode(pair, caseSensitive);
    }

    /**
     * Decode ShortUuid to UUID
     *
     * @param shortId encoded UUID
     * @param caseSensitive whether the id can be case-sensitive (in this case use a larger base to encode the id)
     * @return decoded UUID
     */
    public static UUID toUuid(String shortId, boolean caseSensitive) {
        BigInteger decoded = Base36Or62.decode(shortId, caseSensitive, UUID_BITS);
        return UuidConverter.fromBigInteger(decoded);
    }

    /**
     * Try to shorten an identifier, if it is a UUID it will be converted to Base36.
     * Otherwise, all non-alphanumeric characters will be removed
     *
     * @param id An identifier to short
     * @param caseSensitive whether the id can be case-sensitive (in this case use a larger base to encode the id)
     * @return The shortened string
     */
    public static String tryShorteningId(String id, boolean caseSensitive) {
        var uuid = UuidConverter.tryParseUUID(id);
        if (uuid != null) {
            return ShortUuid.toShortUuid(uuid, caseSensitive);
        } else {
            return id.replaceAll("[^A-Za-z0-9]+", "");
        }
    }
}
