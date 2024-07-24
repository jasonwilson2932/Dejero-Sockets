package main.java.tlv;

/**
 * Enum for valid Type values
 */
public enum TypeEnum {
    HELLO("E110", "Hello"),
    DATA("DA7A", "Data"),
    GOODBYE("0B1E","Goodbye");

    private final String hexKey;
    private final String textValue;

    TypeEnum(String hexKey, String textValue) {

        this.hexKey = hexKey;
        this.textValue = textValue;
    }

    public String getHexKey() {
        return hexKey;
    }

    public String getTextValue() {
        return textValue;
    }
}
