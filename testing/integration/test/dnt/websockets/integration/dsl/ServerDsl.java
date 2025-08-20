package dnt.websockets.integration.dsl;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.integration.ServerDriver;

public class ServerDsl
{
    private final ServerDriver serverDriver;

    public ServerDsl(ExecutionLayer executionLayer)
    {
        serverDriver = new ServerDriver(executionLayer);
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage();
    }

    public void unicastMessage(String source)
    {
        serverDriver.unicastMessage(source);
    }
}
