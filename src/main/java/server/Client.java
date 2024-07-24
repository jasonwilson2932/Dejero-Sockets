package main.java.server;

import main.java.tlv.TLVParser;
import main.java.tlv.TLVResponse;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Logger;

public class Client implements Runnable {

    private Socket clientSocket;

    private DataOutputStream out; // write for the client
    private BufferedReader in; // read from the client

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        // Do client process
        final String inMessage = inFromClient();
        final List<TLVResponse> tlvResponses = TLVParser.parseTLV(inMessage, "127.0.0.1", "8080");
        final String formattedResponse = TLVParser.formatResponse(tlvResponses);
        outToClient(formattedResponse);
        closeConnection();
    }

    /**
     * Handles processing data from value to String
     */
    private String inFromClient() {

        String messageFromClient = "";
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            messageFromClient = in.readLine();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).severe("In error from client: " + e.getMessage());
        }

        return messageFromClient.trim().equals("") ? "Empty string from client" : messageFromClient;
    }

    private void outToClient(String message) {
        try {
            out = new DataOutputStream(clientSocket.getOutputStream());
            out.writeBytes(message);
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).severe("Error writing to client: " + e.getMessage());
        }
    }

    private void closeConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (NullPointerException | IOException e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
        }
    }
}