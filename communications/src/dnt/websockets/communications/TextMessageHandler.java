package dnt.websockets.communications;

import io.vertx.core.Handler;

public interface TextMessageHandler extends Handler<String>
{
    default void handle(AbstractMessage message) {}
    default void handle(AbstractRequest request) {}
    default void handle(AbstractResponse response) {}
}
