package dnt.websockets.integration.vertx;

import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.vertx.VertxServer;

public class VertxServerDriver
{
    private final VertxServer server;

    public VertxServerDriver()
    {
        server = new VertxServer();
        server.start()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
    }

    public void broadcastMessage()
    {
        server.broadcast(new PushMessage());
    }
}
