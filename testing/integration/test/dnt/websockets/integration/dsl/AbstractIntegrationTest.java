package dnt.websockets.integration.dsl;

import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.IntegrationPushMessageVisitor;

public abstract class AbstractIntegrationTest
{
    private final IntegrationPushMessageVisitor pushMessageVisitor = new IntegrationPushMessageVisitor();
    private final IntegrationExecutionLayer executionLayer = new IntegrationExecutionLayer(pushMessageVisitor);

    protected final ServerDsl server = new ServerDsl(executionLayer);
    protected final ClientDsl client = new ClientDsl(executionLayer, pushMessageVisitor);
}
