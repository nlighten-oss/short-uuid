package co.nlighten.shortuuid;

import co.nlighten.shortuuid.UuidConverter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class UuidConverterTests {
    @Test
    void namedV3() {
        var uuid = UuidConverter.namedByVersion(3,  "widget/1234567890");
        // compare to java's impl
        var expected = UUID.nameUUIDFromBytes("widget/1234567890".getBytes(StandardCharsets.UTF_8));
        Assertions.assertEquals(expected, uuid);
    }

    @Test
    void namedV3WithNamespace() {
        // taken from https://uuid.ramsey.dev/en/stable/ examples
        var ns = UUID.fromString("4bdbe8ec-5cb5-11ea-bc55-0242ac130003");
        var uuid = UuidConverter.namedByVersion(3, ns, "widget/1234567890");
        var expected = UUID.fromString("53564aa3-4154-3ca5-ac90-dba59dc7d3cb");
        Assertions.assertEquals(expected, uuid);
    }

    @Test
    void namedV5() {
        var uuid = UuidConverter.namedByVersion(5, "widget/1234567890");
        var expected = UUID.fromString("f8703385-6883-5762-a0ae-31e939be2482");
        Assertions.assertEquals(expected, uuid);
    }

    @Test
    void namedV5WithNamespace() {
        var ns = UUID.fromString("4bdbe8ec-5cb5-11ea-bc55-0242ac130003");
        var uuid = UuidConverter.namedByVersion(5, ns, "widget/1234567890");
        var expected = UUID.fromString("a35477ae-bfb1-5f2e-b5a4-4711594d855f");
        Assertions.assertEquals(expected, uuid);
    }
}
