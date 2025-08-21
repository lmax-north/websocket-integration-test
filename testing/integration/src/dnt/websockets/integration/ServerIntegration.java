package dnt.websockets.integration;

import dnt.websockets.communications.*;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.server.Source;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class ServerIntegration
{
    private final Map<Source, ServerTextMessageHandler> sourceToTextMessageHandlers = new HashMap<>();

    private void wireUp(Source source)
    {
//        Publisher publisher = new IntegrationPublisher(null);
        ExecutionLayer executionLayer = new IntegrationExecutionLayer();

        ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer);
        sourceToTextMessageHandlers.put(source, textMessageHandler);
    }

    public void broadcast(AbstractMessage message)
    {
        Iterator<Map.Entry<Source, ServerTextMessageHandler>> iterator = sourceToTextMessageHandlers.entrySet().iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next().getValue();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
            }
        }
    }

    public void unicast(String source, AbstractMessage message)
    {
        sourceToTextMessageHandlers.get(Source.valueOf(source.toUpperCase(Locale.ROOT))).send(message);
    }
}
