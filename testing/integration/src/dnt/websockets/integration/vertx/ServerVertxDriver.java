package dnt.websockets.integration.vertx;

import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.vertx.ServerVertx;

public class ServerVertxDriver
{
    private final ServerVertx server;

    public ServerVertxDriver()
    {
        server = new ServerVertx();
        server.run()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
    }

    public void broadcastMessage()
    {
        server.broadcast(new PushMessage());
    }

    public void unicast(String source)
    {
        server.unicast(source, new PushMessage());
    }
}
