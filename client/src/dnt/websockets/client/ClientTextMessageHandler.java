package dnt.websockets.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientTextMessageHandler implements TextMessageHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientTextMessageHandler.class);

    private static final ObjectReader MESSAGE_READER = getClientMessageReader();

    private final ResponseVisitor processor;
    private final PushMessageVisitor pushMessageProcessor;

    public ClientTextMessageHandler(ExecutionLayer publisher, PushMessageVisitor pushMessageProcessor)
    {
        this.pushMessageProcessor = pushMessageProcessor;
        this.processor = new ResponseProcessor(publisher);
    }

    @Override
    public void handle(String maybeJson)
    {
        LOGGER.debug("Raw input {}", maybeJson);
        try
        {
            AbstractMessage message = MESSAGE_READER.readValue(maybeJson);
            if(message instanceof AbstractResponse)
            {
                handle((AbstractResponse)message);
                return;
            }

            handle(message);
        }
        catch (JsonProcessingException e)
        {
            LOGGER.warn("Failed to decode json. Error: {}, '{}'", e.getMessage(), maybeJson);
        }
    }

    @Override
    public void handle(AbstractResponse response)
    {
        response.visit(processor);
    }

    @Override
    public void handle(AbstractMessage message)
    {
        message.visit(pushMessageProcessor);
    }

    private static ObjectReader getClientMessageReader()
    {
        ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(OptionsResponse.class, OptionsResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(PushMessage.class, PushMessage.class.getSimpleName()));
        return mapper.readerFor(AbstractMessage.class);
    }
}
