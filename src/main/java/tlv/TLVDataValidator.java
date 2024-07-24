package main.java.tlv;

import java.util.Arrays;
import java.util.List;

public abstract class TLVDataValidator {

    final private static List<String> harmfulStrings = Arrays.asList("SELECT", "INSERT", "CREATE", "DELETE", "SQL", "<html>");

    // Template validation class for incoming TLV blob
    public static boolean validate(final String incomingData) {
        final int helloMatches = countMatches(TypeEnum.HELLO.getHexKey(), incomingData);
        final int dataMatches = countMatches(TypeEnum.DATA.getHexKey(), incomingData);
        final int goodbyeMatches = countMatches(TypeEnum.GOODBYE.getHexKey(), incomingData);
        return !TLVDataValidator.containsHarmfulData(incomingData) && helloMatches + dataMatches + goodbyeMatches == 3;
    }

    private static int countMatches(final String strToCount, final String incomingData) {
        int lastIndex = 0;
        int count = 0;
        while(lastIndex != -1){
            lastIndex = incomingData.indexOf(strToCount,lastIndex);
            if(lastIndex != -1){
                count ++;
                lastIndex += strToCount.length();
            }
        }
        return count;
    }

    /**
     * We can modify this include a java library that detects harmful string specifically for TLV data
     * @param incomingData TLB blob
     * @return True if the string contains any of the harmful sub-strings
     */
    private static boolean containsHarmfulData(final String incomingData) {
        for (String harmfulString : harmfulStrings) {
            if (incomingData.contains(harmfulString)) {
                return true;
            }
        }
        return false;
    }
}
