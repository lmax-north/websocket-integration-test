package dnt.websockets.integration.dsl;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.PushMessageCollector;

public abstract class AbstractIntegrationTest
{
    private final PushMessageCollector collector = new PushMessageCollector();
    private final ExecutionLayer executionLayer = new IntegrationExecutionLayer(collector);

    protected final ServerDsl server = new ServerDsl(executionLayer);
    protected final ClientDsl client = new ClientDsl(executionLayer, collector);
}
