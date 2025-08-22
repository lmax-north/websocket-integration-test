package dnt.websockets.integration;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;

public class ServerDriver
{
    private final IntegrationServer server;

    public ServerDriver(final ExecutionLayer executionLayer)
    {
        server = new IntegrationServer(executionLayer);
    }

    public void broadcastMessage(final PushMessage message)
    {
        server.push(message);
    }
}
