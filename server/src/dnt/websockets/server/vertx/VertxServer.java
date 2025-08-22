package dnt.websockets.server.vertx;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.Publisher;
import dnt.websockets.server.ServerTextMessageHandler;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class VertxServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxServer.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();

    public Future<HttpServer> start()
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

        Publisher publisher = new VertxPublisher(serverWebSocket);
        ExecutionLayer executionLayer = new VertxServerExecutionLayer(publisher);
        ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer);
        serverWebSocket.textMessageHandler(textMessageHandler);
        textMessageHandlers.add(textMessageHandler);
    }

    public void broadcast(AbstractMessage message)
    {
        Iterator<ServerTextMessageHandler> iterator = textMessageHandlers.iterator();
        while (iterator.hasNext())
        {
            ServerTextMessageHandler next;
            try
            {
                next = iterator.next();
                next.send(message);
            }
            catch (Exception e)
            {
                iterator.remove();
                LOGGER.error("Error writing message", e);
            }
        }
    }
}
