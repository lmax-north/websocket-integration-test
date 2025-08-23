package dnt.websockets.integration.dsl;

import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.PushMessageCollector;
import dnt.websockets.server.RequestProcessor;

public abstract class AbstractIntegrationTest
{
    private final PushMessageCollector collector = new PushMessageCollector();
    private final RequestProcessor requestProcessor = new RequestProcessor();
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(requestProcessor, collector);

    protected final ServerDsl server = new ServerDsl(executionLayer, requestProcessor);
    protected final ClientDsl client = new ClientDsl(executionLayer, collector);
    protected final IntegrationDsl integration = new IntegrationDsl(executionLayer);
    }
