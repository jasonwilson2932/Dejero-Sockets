package main.java.tlv;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TLVOutputWriter {

    /**
     * This function writes the formatted response to a TXT file in the same directory as the executable JAR
     * @param data data to write
     * @param execCount tracks the file num to append to the txt
     */
    public static void writeFileOut(final String data, final int execCount) {
        BufferedWriter writer;
        try {
            // Will overwrite existing file if exists
            writer = new BufferedWriter(new FileWriter("dejero-sockets-out-" + execCount + ".txt"));
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            throw new TLVApplicationException(e.getMessage());
        }
    }
}
