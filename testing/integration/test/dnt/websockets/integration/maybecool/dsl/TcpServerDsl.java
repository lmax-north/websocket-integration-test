package dnt.websockets.integration.maybecool.dsl;

import dnt.websockets.integration.maybecool.UdpServerDriver;

public class TcpServerDsl
{
    private final UdpServerDriver serverDriver;

    public TcpServerDsl(UdpServerDriver serverDriver)
    {
        this.serverDriver = serverDriver;
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }
}
