package dnt.websockets.integration.dsl;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.communications.PushMessageVisitor;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.IntegrationPushMessageVisitor;
import dnt.websockets.server.Source;

import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

public abstract class AbstractIntegrationTest
{
    private final IntegrationPushMessageVisitor pushMessageVisitor = new IntegrationPushMessageVisitor();
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(pushMessageVisitor);

    protected final ServerDsl server = new ServerDsl(executionLayer);
    protected final ClientDsl client = new ClientDsl(executionLayer, pushMessageVisitor);
    protected final ClientDsl client2 = new ClientDsl(executionLayer, pushMessageVisitor);

    protected final Map<Source, ClientDsl> clients = Map.of(
            Source.SOURCE1, client,
            Source.SOURCE2, client2);

    protected ClientDsl client(String source)
    {
        return clients.get(Source.valueOf(source.toUpperCase(Locale.ROOT)));
    }
}
