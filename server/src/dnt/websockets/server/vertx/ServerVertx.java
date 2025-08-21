package dnt.websockets.server.vertx;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.VertxPublisher;
import dnt.websockets.server.ServerIntegrationExecutionLayer;
import dnt.websockets.server.ServerTextMessageHandler;
import dnt.websockets.server.Source;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static dnt.websockets.server.Source.getSource;
import static dnt.websockets.vertx.VertxFactory.newVertx;

public class ServerVertx
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerVertx.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private final Map<Source, ServerTextMessageHandler> sourceToTextMessageHandlers = new HashMap<>();

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

        VertxPublisher publisher = new VertxPublisher(serverWebSocket);
        ServerIntegrationExecutionLayer executionLayer = new ServerIntegrationExecutionLayer(publisher);
        ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer);
        serverWebSocket.textMessageHandler(textMessageHandler);
        sourceToTextMessageHandlers.put(getSource(uri), textMessageHandler);
    }

    public void broadcast(AbstractMessage message)
    {
        Iterator<Map.Entry<Source, ServerTextMessageHandler>> iterator = sourceToTextMessageHandlers.entrySet().iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next().getValue();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
    }

    public void unicast(String source, AbstractMessage message)
    {
        sourceToTextMessageHandlers.get(Source.valueOf(source.toUpperCase(Locale.ROOT))).send(message);
    }
}
