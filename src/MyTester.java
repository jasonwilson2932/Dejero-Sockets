import java.io.IOException;

/**
 * Created by teocci.
 *
 * @author teocci@yandex.com on 2018-Apr-24
 */
public class MyTester
{
    private final static String CLIENT_CHANNEL = "clientChannel";
    private final static String SERVER_CHANNEL = "serverChannel";
    private final static String CHANNEL_TYPE = "channelType";

    private final static String HOSTNAME = "localhost";
    private final static int PORT = 6666;

    /**
     * ServerSocketChannel represents a channel for sockets that listen to
     * incoming connections.
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        try {
            new Server(6666).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}