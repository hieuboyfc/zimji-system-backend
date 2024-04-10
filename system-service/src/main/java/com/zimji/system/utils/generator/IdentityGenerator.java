package com.zimji.system.utils.generator;

import com.zimji.system.utils.string.StringGeneratorUtils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class IdentityGenerator {

    public IdentityGenerator() {
    }

    public static String ofUUID() {
        return StringGeneratorUtils.getUUID();
    }

    public static Long ofSnowFlake() {
        return Snowflake.getInstance().nextId();
    }

    public static String ofShortUUID() {
        return Long.toString(ByteBuffer.wrap(UUID.randomUUID().toString().getBytes()).getLong(), 36);
    }

    public static UUID asUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    public static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}