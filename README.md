# short-uuid

[![Maven Central](https://res.cloudinary.com/practicaldev/image/fetch/s--A2crg8Rq--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://img.shields.io/maven-central/v/co.nlighten/short-uuid.svg%3Flabel%3DMaven%2520Central%26logo%3Dapachemaven)](https://central.sonatype.com/artifact/co.nlighten/short-uuid)
[![Release](https://res.cloudinary.com/practicaldev/image/fetch/s--OZF8A8Pg--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://img.shields.io/github/v/release/nlighten-oss/short-uuid%3Flogo%3DGitHub)](https://github.com/nlighten-oss/short-uuid/releases)
[![License](https://res.cloudinary.com/practicaldev/image/fetch/s--_6Z6Z6Z_--/c_limit%2Cf_auto%2Cfl_progressive%2Cq_auto%2Cw_880/https://img.shields.io/github/license/nlighten-oss/short-uuid.svg%3Flogo%3DOpen%20Source%20Initiative)](./LICENSE)

Short UUID conversion and generation library for Java.
- Can make a UUID shorter in string format (down to 22 characters)

## Installation

Maven
```xml
<dependency>
    <groupId>co.nlighten</groupId>
    <artifactId>short-uuid</artifactId>
    <version>0.1.1</version>
</dependency>
```

Gradle
```groovy
implementation 'co.nlighten:short-uuid:0.1.1'
```

## Usage Example

```java
import co.nlighten.shortuuid.ShortUuid;

public class Main {
    public static void main(String[] args) {
        String a = ShortUuid.random(false /* Use Base36 */);
        System.out.println(a);
        // e.g. "bazl2kp9io74mfdaid40j6xia"
        
        String b = ShortUuid.random(true /* Use Base62 */);
        System.out.println(b);
        // e.g. "2RGPvdeVim1yS4rHlLpN9f"
        
        // You can also convert a UUID to Base64 (URL)
        String c = UuidConverter.toBase64(UUID.randomUUID());
        System.out.println(c);
        
        // Shorten an existing UUID
        String d = UuidConverter.toShortUuid(UUID.randomUUID(), true /* Use Base62 */);
        System.out.println(d);
    }
}
```

See tests for more examples.

## License

[Apache License 2.0](./LICENSE)