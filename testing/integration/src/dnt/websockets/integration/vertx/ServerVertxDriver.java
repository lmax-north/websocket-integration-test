package dnt.websockets.integration.vertx;

import dnt.websockets.communications.PushMessage;
import dnt.websockets.server.Server;

public class ServerVertxDriver
{
    private final Server server;

    public ServerVertxDriver()
    {
        server = new Server();
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
