package dnt.websockets.integration;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import education.common.result.Result;
import io.vertx.core.Future;

import java.util.LinkedList;

public class IntegrationExecutionLayer implements ExecutionLayer
{
    private final IntegrationPublisher publisher;
    private final RequestProcessor requestProcessor;
    private final LinkedList<AbstractMessage> serverPushMessages = new LinkedList<>();

    private final ServerTextMessageHandler serverTextMessageHandler;
    private final ClientTextMessageHandler clientTextMessageHandler;

    public IntegrationExecutionLayer()
    {
        this.publisher = new IntegrationPublisher(serverPushMessages::add);

        this.serverTextMessageHandler = new ServerTextMessageHandler(this);
        this.clientTextMessageHandler = new ClientTextMessageHandler(this, serverPushMessages::add);

        this.requestProcessor = new RequestProcessor(this);
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request)
    {
        return Future.succeededFuture()
                .map(unused ->
                {
                    serverTextMessageHandler.handle(request);
                    return Result.success((T) serverPushMessages.getLast());
                })
                .map(r -> r.mapError(String::valueOf));
    }

    @Override
    public void respond(AbstractResponse response)
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
    public void send(AbstractMessage message)
    {
        clientTextMessageHandler.handle(message);
        publisher.send(message);
    }

    public <T extends AbstractMessage> T getLastMessage()
    {
        if(serverPushMessages.isEmpty())
        {
            return null;
        }
        return (T) serverPushMessages.remove();
    }
}
