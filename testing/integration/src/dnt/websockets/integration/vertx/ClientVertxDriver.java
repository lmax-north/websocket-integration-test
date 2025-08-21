package dnt.websockets.integration.vertx;

import dnt.websockets.client.vertx.VertxClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;

import java.util.LinkedList;
import java.util.Queue;

public class ClientVertxDriver
{
    private final VertxClient client;
    private final PushMessageCollector collector = new PushMessageCollector();

    public ClientVertxDriver(String source)
    {
        client = new VertxClient(source, collector);
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
        return collector.getLastMessage();
    }
}
