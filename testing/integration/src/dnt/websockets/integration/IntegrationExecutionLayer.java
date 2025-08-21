package dnt.websockets.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.ServerTextMessageHandler;
import education.common.result.Result;
import io.vertx.core.Future;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private final IntegrationPublisher publisher;

    private final ServerTextMessageHandler serverTextMessageHandler;
    private final ClientTextMessageHandler clientTextMessageHandler;
    private final PushMessageCollector collector;

    public IntegrationExecutionLayer(PushMessageCollector collector)
    {
        this.publisher = new IntegrationPublisher(collector);
        this.collector = collector;

        this.serverTextMessageHandler = new ServerTextMessageHandler(this);
        this.clientTextMessageHandler = new ClientTextMessageHandler(this, this.collector);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        return Future.succeededFuture()
                .map(unused ->
                {
                    try
                    {
                        serverTextMessageHandler.handle(ServerTextMessageHandler.OBJECT_MAPPER.writeValueAsString(request));
                        return Result.success((T) collector.getLastMessage());
                    }
                    catch (JsonProcessingException e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .map(r -> r.mapError(String::valueOf));
    }

    @Override
    public void respond(AbstractResponse response)
    {
        publisher.send(response);
    }

    @Override
    public void send(AbstractMessage message)
    {
        try
        {
            clientTextMessageHandler.handle(ClientTextMessageHandler.OBJECT_MAPPER.writeValueAsString(message)); // Prove our serde works.
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }
}
