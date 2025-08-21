package dnt.websockets.integration;

import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.PushMessage;

public class ServerDriver
{
    // Vertx Test -> DSL -> Driver -> ServerVertx                    -> (MANY) Execution Layer
    //                             -> SourceToTextMessageHandlers
    //       Test -> DSL -> Driver -> Execution Layer ->

    private final ExecutionLayer executionLayer;

    public ServerDriver(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    public void broadcastMessage()
    {
        executionLayer.send(new PushMessage());
    }

    public void unicastMessage(String source)
    {
//        executionLayer.send(source, new PushMessage());
    }
}
