package dnt.websockets.server.maybecool;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;

public class TcpPublisher implements Publisher
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpPublisher.class);

    private final ObjectMapper objectMapper;
    private final PrintWriter writer;

    public TcpPublisher(Socket socket) throws IOException
    {
        this.writer = new PrintWriter(socket.getOutputStream(), true);
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void send(AbstractMessage message)
    {
        LOGGER.error("Sending {}", message);
        try
        {
            writer.println(objectMapper.writeValueAsString(message));
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to serialize message", e);
        }
    }
}
