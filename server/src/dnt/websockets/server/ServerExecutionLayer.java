package dnt.websockets.server;

import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServerExecutionLayer implements ExecutionLayer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerExecutionLayer.class);

    private final MessagePublisher messagePublisher;

    public ServerExecutionLayer(MessagePublisher messagePublisher)
    {
        this.messagePublisher = messagePublisher;
    }

    @Override
    public <T extends AbstractResponse> Future<Result<T, String>> send(AbstractRequest request)
    {
        throw new UnsupportedOperationException("Not implemented on server.");
    }

    @Override
    public void unicast(String source, AbstractMessage message)
    {
        throw new UnsupportedOperationException("Not implemented on server.");
    }

    @Override
    public void notifyResponseReceived(AbstractResponse response)
    {
        messagePublisher.send(response);
    }

    @Override
    public void broadcast(AbstractMessage message)
    {
        messagePublisher.send(message);
    }
}
