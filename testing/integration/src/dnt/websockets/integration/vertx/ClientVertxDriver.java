package dnt.websockets.integration.vertx;

import dnt.websockets.client.Client;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import education.common.result.Result;
import io.vertx.core.Future;

import java.util.LinkedList;
import java.util.Queue;

public class ClientVertxDriver
{
    private final Client client;
    private final Queue<AbstractMessage> broadcastMessages = new LinkedList<>();

    public ClientVertxDriver(String source)
    {
        client = new Client(source, broadcastMessages::add);
        client.run()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()))
                .toCompletionStage().toCompletableFuture().join();
    }

    public Future<Result<OptionsResponse, String>> requestOptions()
    {
        return client.requestOptions()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public AbstractMessage popLastMessage()
    {
        if(broadcastMessages.isEmpty())
        {
            return null;
        }
        return broadcastMessages.remove();
    }
}
