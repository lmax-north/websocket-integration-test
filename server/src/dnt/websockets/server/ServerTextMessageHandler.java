package dnt.websockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectReader;
import dnt.websockets.communications.*;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ServerTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTextMessageHandler.class);

    private final ObjectReader messageReader;
    private final RequestVisitor processor;
    private final ExecutionLayer executionLayer;

    ServerTextMessageHandler(ObjectReader messageReader, ExecutionLayer executionLayer)
    {
        this.messageReader = messageReader;
        this.processor = new RequestProcessor(executionLayer);
        this.executionLayer = executionLayer;
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Receiving {}", maybeJson);
        try
        {
            AbstractRequest request = messageReader.readValue(maybeJson);
            request.visit(processor);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    public void broadcast(AbstractMessage message)
    {
        executionLayer.broadcast(message);
    }
}
