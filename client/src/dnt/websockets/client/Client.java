package dnt.websockets.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.http.WebSocketConnectOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.function.Consumer;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class Client implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    private final ObjectMapper mapper;
    private final ObjectReader messageReader;
    private final Consumer<AbstractMessage> messageConsumer;

    private Vertx vertx;
    private ClientExecutionLayer executorLayer;
    private URI uri;

    public Client(Consumer<AbstractMessage> messageConsumer)
    {
        this("SOURCE1", messageConsumer);
    }
    public Client(String source, Consumer<AbstractMessage> messageConsumer)
    {
        this.messageConsumer = messageConsumer;
        mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(OptionsResponse.class, OptionsResponse.class.getSimpleName()));
        mapper.registerSubtypes(new NamedType(PushMessage.class, PushMessage.class.getSimpleName()));
        messageReader = mapper.readerFor(AbstractMessage.class);
        uri = URI.create("/v1/websocket/").resolve(source);
    }

    public Future<WebSocket> run()
    {
        vertx = newVertx();
        HttpClient httpClient = vertx.createHttpClient();

        WebSocketConnectOptions options = new WebSocketConnectOptions()
                .setURI(uri.toString())
                .setHost("localhost")
                .setPort(7777);
        options.setTimeout(3000);
        return httpClient.webSocket(options)
                .onSuccess(this::handle)
                .onFailure(t -> LOGGER.error("Failed to start client.", t));
    }

    private void handle(WebSocket webSocket)
    {
        MessagePublisher messagePublisher = new MessagePublisher(webSocket, mapper);
        executorLayer = new ClientExecutionLayer(vertx, messagePublisher);

        ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(messageReader, executorLayer, messageConsumer::accept);
        webSocket.textMessageHandler(messageHandler);
    }

    @Override
    public Future<Result<OptionsResponse, String>> requestOptions()
    {
        return executorLayer.send(new OptionsRequest());
    }
}
