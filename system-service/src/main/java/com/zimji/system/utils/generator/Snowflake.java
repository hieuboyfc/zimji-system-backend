package com.zimji.system.utils.generator;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.ObjectUtils;

import java.net.NetworkInterface;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Enumeration;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Snowflake {

    static final int UNUSED_BITS = 1; // Sign bit, Unused (always set to 0)
    static final int EPOCH_BITS = 41;
    static final int NODE_ID_BITS = 6;
    static final int SEQUENCE_BITS = 10;

    static final long MAX_NODE_ID = (1L << NODE_ID_BITS) - 1;
    static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    static final long DEFAULT_CUSTOM_EPOCH = 1622505600000L;

    static final Snowflake instance = new Snowflake();

    final long nodeId;
    final long customEpoch;

    volatile long lastTimestamp = -1L;
    volatile long sequence = 0L;

    public static Snowflake getInstance() {
        return instance;
    }

    public static void main(String[] args) {
        long maxNode = 63L;
        System.out.println(maxNode + "  " + getInstance().nextId());
    }

    private Snowflake(long nodeId, long customEpoch) {
        validateNodeId(nodeId);
        this.nodeId = nodeId;
        this.customEpoch = customEpoch;
    }

    public Snowflake(long nodeId) {
        this(nodeId, DEFAULT_CUSTOM_EPOCH);
    }

    public Snowflake() {
        this.nodeId = createNodeId();
        this.customEpoch = DEFAULT_CUSTOM_EPOCH;
    }

    public synchronized long nextId() {
        long currentTimestamp = timestamp();
        validateTimestamp(currentTimestamp);

        if (currentTimestamp != lastTimestamp) {
            sequence = 0; // Nếu timestamp thay đổi, reset sequence về 0
        }

        lastTimestamp = currentTimestamp;
        long id = (currentTimestamp << (NODE_ID_BITS + SEQUENCE_BITS)) | (nodeId << SEQUENCE_BITS) | sequence;

        sequence = (sequence + 1) & MAX_SEQUENCE;
        if (sequence == 0) {
            id = waitNextMillis(currentTimestamp);
        }

        return id;
    }

    private long timestamp() {
        return Instant.now().toEpochMilli() - customEpoch;
    }

    private long waitNextMillis(long currentTimestamp) {
        while (currentTimestamp == lastTimestamp) {
            currentTimestamp = timestamp();
        }
        return currentTimestamp;
    }

    /*private long createNodeId() {
        long nodeId;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (mac != null) {
                    for (byte macPort : mac) {
                        sb.append(String.format("%02X", macPort));
                    }
                }
            }
            nodeId = sb.toString().hashCode();
        } catch (Exception ex) {
            nodeId = new SecureRandom().nextInt();
        }
        nodeId &= MAX_NODE_ID;
        return nodeId;
    }*/

    private long createNodeId() {
        try {
            // Lấy danh sách các card mạng
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            StringBuilder sb = new StringBuilder();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                byte[] mac = networkInterface.getHardwareAddress();
                if (ObjectUtils.isNotEmpty(mac)) {
                    // Chuyển địa chỉ MAC thành chuỗi hex
                    for (byte b : mac) {
                        sb.append(String.format("%02X", b));
                    }
                }
            }
            // Tạo một mã băm từ chuỗi hex
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(sb.toString().getBytes());
            // Lấy giá trị của 6 byte cuối cùng của mã băm để làm Node ID
            long nodeId = 0;
            for (int i = 0; i < Math.min(hash.length, 6); i++) {
                nodeId |= ((long) (hash[hash.length - i - 1] & 0xFF)) << (i * 8);
            }
            return nodeId & MAX_NODE_ID;
        } catch (Exception ex) {
            // Trong trường hợp xảy ra lỗi, sử dụng số ngẫu nhiên
            return new SecureRandom().nextInt() & MAX_NODE_ID;
        }
    }

    private void validateNodeId(long nodeId) {
        if (nodeId < 0 || nodeId > MAX_NODE_ID) {
            throw new IllegalArgumentException(String.format("NodeId must be between %d and %d", 0, MAX_NODE_ID));
        }
    }

    private void validateTimestamp(long timestamp) {
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException("Invalid System Clock!");
        }
    }

    public long[] parse(long id) {
        long timestamp = (id >> (NODE_ID_BITS + SEQUENCE_BITS)) + customEpoch;
        long nodeId = (id >> SEQUENCE_BITS) & ((1L << NODE_ID_BITS) - 1);
        long sequence = id & ((1L << SEQUENCE_BITS) - 1);

        return new long[]{timestamp, nodeId, sequence};
    }

    @Override
    public String toString() {
        return "Snowflake Settings [EPOCH_BITS=" + EPOCH_BITS + ", NODE_ID_BITS=" + NODE_ID_BITS
                + ", SEQUENCE_BITS=" + SEQUENCE_BITS + ", CUSTOM_EPOCH=" + customEpoch
                + ", NodeId=" + nodeId + "]";
    }

}