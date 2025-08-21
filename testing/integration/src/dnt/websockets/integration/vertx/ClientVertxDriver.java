package dnt.websockets.integration.vertx;

import dnt.websockets.client.vertx.VertxClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;

import java.util.LinkedList;
import java.util.Queue;

public class ClientVertxDriver
{
    private final VertxClient client;
    private final Queue<AbstractMessage> serverPushMessages = new LinkedList<>();

    public ClientVertxDriver(String source)
    {
        client = new VertxClient(source, serverPushMessages::add);
        client.run()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
    }

    public Future<Result<OptionsResponse, String>> requestOptions()
    {
        return client.fetchOptions()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public AbstractMessage popLastMessage()
    {
        if(serverPushMessages.isEmpty())
        {
            return null;
        }
        return serverPushMessages.remove();
    }
}
