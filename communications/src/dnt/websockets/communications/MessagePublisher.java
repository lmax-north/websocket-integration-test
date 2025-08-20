package dnt.websockets.communications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.WebSocketBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE = 102;

    private final WebSocketBase serverWebSocket;
    private final ObjectMapper objectMapper;

    public MessagePublisher(WebSocketBase serverWebSocket, ObjectMapper objectMapper)
    {
        this.serverWebSocket = serverWebSocket;
        this.objectMapper = objectMapper;
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        try
        {
            String s = objectMapper.writeValueAsString(message);
            System.out.println(s);
            serverWebSocket.writeTextMessage(s);
        }
        catch (JsonProcessingException e)
        {
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE);
            throw new RuntimeException("Failed to serialize message", e);
        }
    }

    @Override
    public void send(String source, AbstractMessage message)
    {
        LOGGER.debug("Sending {}", message);
        try
        {
            String s = objectMapper.writeValueAsString(message);
            System.out.println(s);
            serverWebSocket.writeTextMessage(s);
        }
        catch (JsonProcessingException e)
        {
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_SEND_RESPONSE);
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
