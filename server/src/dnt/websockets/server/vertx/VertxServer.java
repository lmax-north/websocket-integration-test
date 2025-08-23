package dnt.websockets.server.vertx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dnt.websockets.communications.*;
import dnt.websockets.server.RequestProcessor;
import dnt.websockets.server.ServerTextMessageHandler;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.ServerWebSocket;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VertxServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(VertxServer.class);
    private static final short WEBSOCKET_CODE_FAILED_TO_CONNECT = 100;

    private final List<ServerTextMessageHandler> textMessageHandlers = new ArrayList<>();
    private final Vertx vertx;
    private final RequestProcessor requestProcessor = new RequestProcessor();

    public VertxServer(Vertx vertx)
    {
        this.vertx = vertx;
    }

    public Future<HttpServer> start()
    {
        Router router = Router.router(vertx);
        router.get("/property").handler(this::restGetProperty);
        router.post("/property").handler(this::restSetProperty);
        return vertx.createHttpServer()
                .requestHandler(router)
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
        ServerTextMessageHandler textMessageHandler = new ServerTextMessageHandler(executionLayer, requestProcessor);
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

    private static class LazyPublisher implements Publisher
    {
        Publisher publisher;

        @Override
        public void send(AbstractMessage message)
        {
            publisher.send(message);
        }
    }


    private void restGetProperty(RoutingContext ctx)
    {
        try
        {
            final LazyPublisher restPublisher = new LazyPublisher();
            final VertxServerExecutionLayer restExecutionLayer = new VertxServerExecutionLayer(restPublisher);
            restPublisher.publisher = new VertxRestPublisher(ctx);
            final ServerTextMessageHandler restServerTextMessageHandler = new ServerTextMessageHandler(restExecutionLayer, requestProcessor);

            String key = ctx.queryParams().get("key");
            GetPropertyRequest request = new GetPropertyRequest(key);
            String maybeJson = new ObjectMapper().writeValueAsString(request);
            restServerTextMessageHandler.handle(maybeJson);
        }
        catch (JsonProcessingException e)
        {
            ctx.response().setStatusCode(500).end();
        }
    }
    private void restSetProperty(RoutingContext ctx)
    {
        try
        {
            final LazyPublisher restPublisher = new LazyPublisher();
            final VertxServerExecutionLayer restExecutionLayer = new VertxServerExecutionLayer(restPublisher);
            restPublisher.publisher = new VertxRestPublisher(ctx);
            final ServerTextMessageHandler restServerTextMessageHandler = new ServerTextMessageHandler(restExecutionLayer, requestProcessor);

            String key = ctx.queryParams().get("key");
            String value = ctx.queryParams().get("value");
            SetPropertyRequest request = new SetPropertyRequest(key, value);
            String maybeJson = new ObjectMapper().writeValueAsString(request);
            restServerTextMessageHandler.handle(maybeJson);
        }
        catch (JsonProcessingException e)
        {
            ctx.response().setStatusCode(500).end();
        }
    }
}
