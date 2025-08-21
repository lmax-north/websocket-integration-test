package dnt.websockets.integration.dsl;

import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.Source;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractIntegrationTest
{
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer();

    protected final ServerDsl server = new ServerDsl(executionLayer);
    protected final ClientDsl client = new ClientDsl(executionLayer);
    protected final ClientDsl client2 = new ClientDsl(executionLayer);

    protected final Map<Source, ClientDsl> clients = Map.of(
            Source.SOURCE1, client,
            Source.SOURCE2, client2);

    protected ClientDsl client(String source)
    {
        return clients.get(Source.valueOf(source.toUpperCase(Locale.ROOT)));
    }
}
