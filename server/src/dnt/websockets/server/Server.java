package dnt.websockets.server;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.AbstractRequest;
import dnt.websockets.communications.MessagePublisher;
import dnt.websockets.communications.OptionsRequest;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.*;

import static dnt.websockets.server.Source.getSource;
import static dnt.websockets.vertx.VertxFactory.newVertx;

public class Server
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private final ObjectMapper objectMapper;
    private final ObjectReader messageReader;
    private final Map<String, WebsocketTextMessageHandler> sourceToTextMessageHandlers = new HashMap<>();

    public Server()
    {
        objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.registerSubtypes(new NamedType(OptionsRequest.class, OptionsRequest.class.getSimpleName()));
        messageReader = objectMapper.readerFor(AbstractRequest.class);
    }

    public Future<HttpServer> run()
    {
        Vertx vertx = newVertx();
        return vertx.createHttpServer()
                .webSocketHandler(this::handle)
                .listen(7777)
                .onSuccess(httpServer -> {
                    LOGGER.info("Server started on port {}", httpServer.actualPort());
                })
                .onFailure(t -> LOGGER.error("Failed to start server", t));
    }

    private void handle(ServerWebSocket serverWebSocket)
    {
        URI uri = URI.create(serverWebSocket.path());
        if (!uri.toString().startsWith("/v1/websocket"))
        {
            LOGGER.warn("Failed to connect websocket");
            serverWebSocket.close(WEBSOCKET_CODE_FAILED_TO_CONNECT);
            return;
        }
        LOGGER.debug("Websocket connected {}", uri);

        MessagePublisher messagePublisher = new MessagePublisher(serverWebSocket, objectMapper);
        WebsocketTextMessageHandler textMessageHandler = new WebsocketTextMessageHandler(messageReader, messagePublisher);
        serverWebSocket.textMessageHandler(textMessageHandler);
        sourceToTextMessageHandlers.put(getSource(uri).name(), textMessageHandler);
    }

    public void broadcastMessage(AbstractMessage message)
    {
        Iterator<Map.Entry<String, WebsocketTextMessageHandler>> iterator = sourceToTextMessageHandlers.entrySet().iterator();
        while (iterator.hasNext())
        {
            WebsocketTextMessageHandler next;
            try
            {
                next = iterator.next().getValue();
                next.write(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
    }
}
