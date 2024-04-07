package co.nlighten.shortuuid;

import co.nlighten.shortuuid.ShortUuid;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class ShortUuidTests {

    private final static UUID PAIR_1_UUID = UUID.fromString("a8e41dc6-74c9-42c5-bb03-3bfd623044c5");
    private final static String PAIR_1_BASE36 = "9zye6dau0hvwo54msqyyjyzt1";
    private final static String PAIR_1_BASE62 = "58gxXh69c8X7f2Id3D84W5";

    @Test
    void testEncodeAndDecode() {
        var uuid = UUID.randomUUID();

        // insensitive
        var b36 = ShortUuid.toShortUuid(uuid, false);
        var uuid_from_b36 = ShortUuid.toUuid(b36, false);

        Assertions.assertEquals(uuid, uuid_from_b36);

        // sensitive
        var b62 = ShortUuid.toShortUuid(uuid, true);
        var uuid_from_b62 = ShortUuid.toUuid(b62, true);

        Assertions.assertEquals(uuid, uuid_from_b62);
    }

    @Test
    void testDecode() {
        // insensitive
        var uuid = ShortUuid.toUuid(PAIR_1_BASE36, false);
        Assertions.assertEquals(PAIR_1_UUID, uuid);

        // sensitive
        uuid = ShortUuid.toUuid(PAIR_1_BASE62, true);
        Assertions.assertEquals(PAIR_1_UUID, uuid);
    }

    @Test
    void testEncode() {
        // insensitive
        var b36 = ShortUuid.toShortUuid(PAIR_1_UUID, false);
        Assertions.assertEquals(PAIR_1_BASE36, b36);

        // sensitive
        var b62 = ShortUuid.toShortUuid(PAIR_1_UUID, true);
        Assertions.assertEquals(PAIR_1_BASE62, b62);
    }

    @Test
    void testTryShorteningIdUUID() {
        // insensitive
        var shortened = ShortUuid.tryShorteningId(PAIR_1_UUID.toString(), false);
        Assertions.assertEquals(PAIR_1_BASE36, shortened);

        // sensitive
        shortened = ShortUuid.tryShorteningId(PAIR_1_UUID.toString(), true);
        Assertions.assertEquals(PAIR_1_BASE62, shortened);
    }
    @Test
    void testTryShorteningIdNonUUID() {
        // insensitive
        var rand = ShortUuid.random(false);
        var shortened = ShortUuid.tryShorteningId("/*<$%{" + rand + "}!@>*/", false);
        Assertions.assertEquals(rand, shortened);

        // sensitive
        rand = ShortUuid.random(true);
        shortened = ShortUuid.tryShorteningId("/*<$%{" + rand + "}!@>*/", true);
        Assertions.assertEquals(rand, shortened);
    }

    @Test
    void testBadDecode() {
        // insensitive
        Assertions.assertThrows(NullPointerException.class, () ->
            ShortUuid.toUuid(null, false)
        );
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            ShortUuid.toUuid("", false)
        );
        var rand_ins = ShortUuid.random(false);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            ShortUuid.toUuid("/*<$%{" + rand_ins + "}!@>*/", false)
        );
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            ShortUuid.toUuid(rand_ins + rand_ins, false)
        );

        // sensitive
        Assertions.assertThrows(NullPointerException.class, () ->
            ShortUuid.toUuid(null, true)
        );
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            ShortUuid.toUuid("", true)
        );
        var rand_sens = ShortUuid.random(true);
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            ShortUuid.toUuid("/*<$%{" + rand_sens + "}!@>*/", true)
        );
        Assertions.assertThrows(IllegalArgumentException.class, () ->
            ShortUuid.toUuid(rand_sens + rand_sens, true)
        );
    }
}

