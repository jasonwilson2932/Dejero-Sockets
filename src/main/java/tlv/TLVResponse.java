package main.java.tlv;

import java.util.List;

/**
 * Represents a single line in console output
 */
public class TLVResponse {

    private String ip;
    private String port;
    private final String type;
    private final int length;
    private final List<String> valuesList;

    public TLVResponse(String type, int length, List<String> valuesList) {
        this.type = type;
        this.length = length;
        this.valuesList = valuesList;
    }

    public void setIp(final String ip) {
        this.ip = ip;
    }

    public void setPort(final String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "[" + ip + ":" + port + "] [" + type + "] [" + length + "] [" + valuesList + "]";
    }
}
