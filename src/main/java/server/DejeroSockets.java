package main.java.server;

import main.java.tlv.TLVApplicationException;

import java.io.IOException;

public class DejeroSockets
{
    /**
     *  This function starts the application and binds to a port passed in as a CMD-line arg
     * @param args the incoming port value for the Socket to listen on
     * @throws IOException if program args is not equal to 1 (for port value)
     */
    public static void main(String[] args) throws IOException
    {
        try {
            if (args.length != 1) {
                throw new TLVApplicationException("Program accepts 1 command line arg (port value)");
            }
            int port = Integer.parseInt(args[0]);
            if (port < 1 || port > 65535) {
                throw new TLVApplicationException("Invalid port value");
            } else {
                new Server(port).start();
            }
        } catch (IOException e) {
            throw new TLVApplicationException("Application start exception: " + e.getMessage());
        }
    }
}