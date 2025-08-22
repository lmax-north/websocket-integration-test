package dnt.websockets.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.ServerTextMessageHandler;
import education.common.result.Result;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegrationExecutionLayer.class);

    private final IntegrationPublisher publisher;

    private final ServerTextMessageHandler serverTextMessageHandler;
    private final ClientTextMessageHandler clientTextMessageHandler;
    private final PushMessageCollector collector;

    private Optional<String> maybeFailNextMessage = Optional.empty();
    private boolean throwOnNextMessage = false;

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
                        T lastMessage = collector.getLastMessage();
                        if(lastMessage == null)
                        {
                            LOGGER.error("No response received.");
                            return Result.<T, Object>failure("No response received");
                        }
                        return intercept(request, lastMessage);
                    }
                    catch (JsonProcessingException e)
                    {
                        throw new RuntimeException(e);
                    }
                })
                .map(r -> r.mapError(String::valueOf));
    }

    private <T extends AbstractResponse> Result<T, Object> intercept(AbstractRequest request, T lastMessage)
    {
        if(throwOnNextMessage)
        {
            throwOnNextMessage = false;
            throw new RuntimeException("Throw on next message");
        }
        if(maybeFailNextMessage.isPresent())
        {
            Result<T, Object> failure = Result.failure(maybeFailNextMessage.get());
            maybeFailNextMessage = Optional.empty();
            return failure;
        }
        return Result.success(lastMessage);
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

    public void failNextMessage(String failureMessage)
    {
        maybeFailNextMessage = Optional.of(failureMessage);
    }

    public void throwOnNextMessage()
    {
        throwOnNextMessage = true;
    }
}
