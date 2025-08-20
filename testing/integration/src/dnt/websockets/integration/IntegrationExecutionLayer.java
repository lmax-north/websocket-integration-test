package dnt.websockets.integration;

import dnt.websockets.client.ResponseProcessor;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.RequestProcessor;
import education.common.result.Result;
import io.vertx.core.Future;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private final IntegrationPublisher publisher;
    private final RequestProcessor requestProcessor;

    public IntegrationExecutionLayer()
    {
        this.publisher = new IntegrationPublisher();
        this.requestProcessor = new RequestProcessor(this);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> send(AbstractRequest request)
    {
        return Future.succeededFuture()
                .map(unused ->
                {
                    sendAndProcessImmediately(request);
                    return Result.success((T) publisher.getLastMessage());
                })
                .map(r -> r.mapError(String::valueOf));
    }

    @Override
    public void notifyResponseReceived(AbstractResponse response)
    {
        publisher.send(response);
    }

    private void sendAndProcessImmediately(AbstractMessage message)
    {
        if (message instanceof OptionsRequest)
        {
            requestProcessor.visit((OptionsRequest) message);
        }
    }

    @Override
    public void broadcast(AbstractMessage message)
    {
        publisher.send(message);
    }

    @Override
    public void unicast(String source, AbstractMessage message)
    {
        publisher.send(source, message);
    }

    public AbstractMessage getLastMessage()
    {
        return publisher.getLastMessage();
    }
}
