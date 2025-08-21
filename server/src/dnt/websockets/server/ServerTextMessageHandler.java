package dnt.websockets.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerTextMessageHandler implements TextMessageHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerTextMessageHandler.class);
    private static final ObjectReader MESSAGE_READER = getServerMessageReader();

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
            handle(request);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    @Override
    public void handle(AbstractRequest request)
    {
        request.visit(processor);
    }

    public void send(AbstractMessage message)
    {
        executionLayer.send(message);
    }

    private static ObjectReader getServerMessageReader()
    {
        ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.registerSubtypes(new NamedType(OptionsRequest.class, OptionsRequest.class.getSimpleName()));
        return objectMapper.readerFor(AbstractMessage.class);
    }
}
