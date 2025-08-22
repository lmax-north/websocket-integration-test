package dnt.websockets.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.*;
import io.vertx.core.Handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTextMessageHandler implements Handler<String>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTextMessageHandler.class);

    public static final ObjectMapper OBJECT_MAPPER = getServerObjectMapper();
    private static final ObjectReader MESSAGE_READER = getServerMessageReader(OBJECT_MAPPER);

    private final RequestVisitor processor;

    private final ExecutionLayer executionLayer;

    public ServerTextMessageHandler(ExecutionLayer executionLayer)
    {
        this.processor = new RequestProcessor(executionLayer);
        this.executionLayer = executionLayer;
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Receiving {}", maybeJson);
        try
        {
            AbstractRequest request = MESSAGE_READER.readValue(maybeJson);
            request.visit(processor);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    public void send(AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        executionLayer.send(message);
    }

    private static ObjectMapper getServerObjectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.registerSubtypes(new NamedType(GetPropertyRequest.class, GetPropertyRequest.class.getSimpleName()));
        objectMapper.registerSubtypes(new NamedType(SetPropertyRequest.class, SetPropertyRequest.class.getSimpleName()));
        return objectMapper;
    }

    private static ObjectReader getServerMessageReader(ObjectMapper objectMapper)
    {
        return objectMapper.readerFor(AbstractMessage.class);
    }
}
