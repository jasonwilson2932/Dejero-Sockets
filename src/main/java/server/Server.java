package main.java.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class Server {

    private final String STATIC_IP = "127.0.0.1";

    private ServerSocket sSocket;
    private boolean run;
    private int port;

    public Server(int port) throws IOException {
        this.port = port;
        this.sSocket = new ServerSocket(this.port);
    }

    /**
     * This creates 1 listener that stops program execution to listen on the socket, then resumes processing of that
     * socket data and creates a new thread to listen on while data processing of the original thread completes. This
     * cycle continues for the duration of the program runtime
     */
    public void start() {

        this.run = true;
        Logger.getLogger(getClass().getName()).info("main.java.server.Server is listening on port: " + port);

        try {
            int execCounter = 0;
            while (run) {
                Socket cs = sSocket.accept();  // New thread stops here to listen
                Logger.getLogger(getClass().getName())
                        .info("New main.java.server.Client Connected! " + cs.getPort());
                new Thread(new Client(cs, STATIC_IP, port, execCounter++)).start(); // Put to a new thread.
            }
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
        }
    }

    /**
     * Implement to listen to sig-int and stop program
     */
    public void stop() {
        this.run = false;
    }
}
