package main.java.tlv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Handles the parsing and formatting of response data
 */
public class TLVParser {

    // Splits the incoming string on the 3 expected TLV blob keys (E110, DA7A, 0B1E)
    final static String INPUT_REGEX_SPLIT = TypeEnum.HELLO.getHexKey() + "|" + TypeEnum.DATA.getHexKey() + "|" + TypeEnum.GOODBYE.getHexKey();

    public static List<TLVResponse> parseTLV(final String input, final String ip, final String port) {
        if (!TLVDataValidator.validate(input)) {
            // Throws error if in-blob does not contain at least 1 entry of Hello, Data & Goodbye Hex values
            final String invalidBlobErr = "Invalid TLV Blob. Must contain at least one entry of " + TypeEnum.HELLO.getTextValue()
                    + TypeEnum.DATA.getTextValue() + TypeEnum.GOODBYE.getTextValue();
            throw new TLVApplicationException(invalidBlobErr);
        }
        List<String> blobs = Arrays.asList(input.split(INPUT_REGEX_SPLIT));
        int index = 0;
        if ("".equals(blobs.get(0))) {
            index++;
        }
        String e110 = TypeEnum.HELLO.getHexKey() + blobs.get(index++);
        String da7a = TypeEnum.DATA.getHexKey() + blobs.get(index++);
        String b1e = TypeEnum.GOODBYE.getHexKey() + blobs.get(index);

        return Arrays.asList(buildTLVResponse(TypeEnum.HELLO, e110, ip, port),
                buildTLVResponse(TypeEnum.DATA, da7a, ip, port),
                buildTLVResponse(TypeEnum.GOODBYE, b1e, ip, port));
    }

    private static TLVResponse buildTLVResponse(final TypeEnum typeEnum, final String data, final String ip, final String port) {
        TLVResponse tlvResponse;
        if (TypeEnum.HELLO.equals(typeEnum)) {
             tlvResponse = parseHello(data);
        } else if (TypeEnum.DATA.equals(typeEnum)) {
            tlvResponse = parseData(data);
        } else {
            tlvResponse = parseGoodbye(data);
        }
        tlvResponse.setIp(ip);
        tlvResponse.setPort(port);
        return tlvResponse;
    }

    private static TLVResponse parseHello(final String data) {
        final String type = TypeEnum.HELLO.getTextValue();
        final int length = getLength(TypeEnum.HELLO, data);
        final List<String> values = getValuesList(TypeEnum.HELLO, data, length);
        final int lenToDisplay = values.isEmpty() ? 0 : length;
        return new TLVResponse(type, lenToDisplay, values);
    }

    private static TLVResponse parseData(final String data) {
        final String type = TypeEnum.DATA.getTextValue();
        final int length = getLength(TypeEnum.DATA, data);
        final List<String> values = getValuesList(TypeEnum.DATA, data, length);
        final int lenToDisplay = values.isEmpty() ? 0 : length;
        return new TLVResponse(type, lenToDisplay, values);
    }

    private static TLVResponse parseGoodbye(final String data) {
        final String type = TypeEnum.GOODBYE.getTextValue();
        final int length = getLength(TypeEnum.GOODBYE, data);
        final List<String> values = getValuesList(TypeEnum.GOODBYE, data, length);
        final int lenToDisplay = values.isEmpty() ? 0 : length;
        return new TLVResponse(type, lenToDisplay, values);
    }

    /**
     * Gets the length value in Decimal form from Hex
     */
    private static int getLength(final TypeEnum typeEnum, final String data) {
        int indexStart = data.indexOf(typeEnum.getHexKey());
        String lengthValue = data.substring(indexStart + 2, indexStart + 6);
        int num = Integer.parseInt(lengthValue, 16);
        return String.valueOf(num).length();
    }

    private static List<String> getValuesList(final TypeEnum typeEnum, final String data, final int length) {
        final int START_INDEX = 6;
        if (data.length() <= 6) {
            throw new TLVApplicationException("TLV Blob has no value. Blob is too short");
        }
        final String value = data.substring(START_INDEX, data.length());
        if (value.matches("^0+$")) {
            // enter here if value contains all zeros
            return Collections.emptyList();
        } else {
            // build array
            List<String> values = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                final String displayValue = value.charAt(i) + "x" + buildTwoDigitIndex(i);
                values.add(displayValue);
            }
            return values;
        }
    }

    private static String buildTwoDigitIndex(final int addr) {
        return String.format("%02d", addr);
    }

    public static String formatResponse(final List<TLVResponse> tlvResponses) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final TLVResponse tlvResponse : tlvResponses) {
            stringBuilder.append(tlvResponse.toString().replace("[[", "[")
                    .replace("]]", "]"));
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}
