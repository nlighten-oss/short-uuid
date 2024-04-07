package co.nlighten.shortuuid;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

/**
 * UuidConverter is a class that can be used to convert UUIDs to and from different representations.
 * Including generation of named UUIDs (v3 and v5).
 */
public class UuidConverter {
    private static final Base64.Encoder BASE64_URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    /**
     * Try to parse a string as a UUID.
     * @param suspectedUUID String to be parsed
     * @return a UUID or null
     */
    public static UUID tryParseUUID(String suspectedUUID) {
        try{
            return UUID.fromString(suspectedUUID);
        } catch (IllegalArgumentException exception){
            return null;
        }
    }

    /**
     * Convert a UUID to a BigInteger.
     * @param uuid (required)
     * @return a BigInteger
     */
    public static BigInteger toBigInteger(UUID uuid) {
        return new BigInteger(uuid.toString().replace("-", ""), 16);
    }

    /**
     * Convert a UUID to a Base64 string.
     * @param uuid (required)
     * @return a Base64 string
     */
    public static String toBase64(UUID uuid) {
        var bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return BASE64_URL_ENCODER.encodeToString(bb.array());
    }

    /**
     * Convert a UUID to a BigInteger.
     * @param uuidAsBigInt (required)
     * @return a UUID
     */
    public static UUID fromBigInteger(BigInteger uuidAsBigInt) {
        var paddedBigIntAsHex = String
                .format("%1$" + 32 + "s", uuidAsBigInt.toString(16))
                .replace(' ', '0');
        return UUID.fromString(paddedBigIntAsHex.replaceAll(
                "^(.{8})(.{4})(.{4})(.{4})(.{12})$",
                "$1-$2-$3-$4-$5"
        ));
    }

    /**
     * Convert a UUID to a Base64 string.
     * @param base64 (required)
     * @return a UUID
     */
    public static UUID fromBase64(String base64) {
        var decoded = Base64.getUrlDecoder().decode(base64);
        var bb = ByteBuffer.wrap(decoded);
        var mostSigBits = bb.getLong();
        var leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    /**
     * Generate a name-based UUID (v3 or v5) using the specified version and name.
     * @param version 3 or 5
     * @param name (required)
     * @return a name-based UUID
     */
    public static UUID namedByVersion(int version, String name) {
        return namedByVersion(version, null, name);
    }

    /**
     * Generate a name-based UUID (v3 or v5) using the specified version, namespace, and name.
     * @param version 3 or 5
     * @param namespace (optional)
     * @param name (required)
     * @return a name-based UUID
     */
    public static UUID namedByVersion(int version, UUID namespace, String name) {
        MessageDigest hasher;
        try {
            if (version == 3) {
                hasher = MessageDigest.getInstance("MD5");
            } else if (version == 5) {
                hasher = MessageDigest.getInstance("SHA-1");
            } else {
                throw new NoSuchAlgorithmException();
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        if (namespace != null) {
            var ns = ByteBuffer.allocate(16);
            ns.putLong(namespace.getMostSignificantBits());
            ns.putLong(namespace.getLeastSignificantBits());
            hasher.update(ns.array());
        }

        hasher.update(name.getBytes(StandardCharsets.UTF_8));
        var hash = ByteBuffer.wrap(hasher.digest());

        final long msb = (hash.getLong() & 0xffffffffffff0fffL) | (version & 0x0f) << 12;
        final long lsb = (hash.getLong() & 0x3fffffffffffffffL) | 0x8000000000000000L;

        return new UUID(msb, lsb);
    }
}