package dnt.websockets.integration.maybecool;

import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.maybecool.TcpServer;

public class UdpServerDriver
{
    private final TcpServer server;

    public UdpServerDriver()
    {
        server = new TcpServer();
        server.start();
    }

    public void broadcastMessage()
    {
        server.broadcast(new PushMessage());
    }

    public void close()
    {
        server.shutdown();
    }
}
