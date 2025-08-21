package dnt.websockets.integration;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.Source;

public class ServerDriver
{
    // Vertx Test -> DSL -> Driver -> ServerVertx                    -> (MANY) Execution Layer
    //                             -> SourceToTextMessageHandlers
    //       Test -> DSL -> Driver -> Execution Layer ->

    private final ServerIntegration server;

    public ServerDriver(final ExecutionLayer executionLayer)
    {
        server = new ServerIntegration(executionLayer);
        server.wireUp(Source.SOURCE1);
        server.wireUp(Source.SOURCE2);
    }

    public void broadcastMessage(final PushMessage message)
    {
        server.push(message);
    }

    public void unicastMessage(String source, final PushMessage message)
    {
        server.unicast(source, message);
    }
}
