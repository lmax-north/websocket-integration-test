package dnt.websockets.integration.dsl;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.ServerDriver;

public class ServerDsl
{
    private final ServerDriver serverDriver;

    public ServerDsl(final ExecutionLayer executionLayer)
    {
        serverDriver = new ServerDriver(executionLayer);
    }

    public void broadcastMessage()
    {
        serverDriver.broadcastMessage(new PushMessage());
    }
}
