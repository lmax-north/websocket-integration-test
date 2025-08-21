package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.server.ServerTextMessageHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ServerIntegration
{
    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();

    public ServerIntegration(final ExecutionLayer executionLayer)
    {
        this.textMessageHandlers.add(new ServerTextMessageHandler(executionLayer));
    }

    public void push(AbstractMessage message)
    {
        Iterator<ServerTextMessageHandler> iterator = textMessageHandlers.iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
            }
        }
    }
}
