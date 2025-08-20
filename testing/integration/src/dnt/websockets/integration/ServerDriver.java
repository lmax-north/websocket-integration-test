package dnt.websockets.integration;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;

public class ServerDriver
{
    private final ExecutionLayer executionLayer;

    public ServerDriver(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    public void broadcastMessage()
    {
        executionLayer.broadcast(new PushMessage());
    }

    public void unicastMessage(String source)
    {
        executionLayer.unicast(source, new PushMessage());
    }
}
