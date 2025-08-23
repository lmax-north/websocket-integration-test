package dnt.websockets.integration.vertx;

import dnt.websockets.client.vertx.VertxClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;

public class VertxRestDriver
{
    private final Vertx vertx;

    public VertxRestDriver(Vertx vertx)
    {
        this.vertx = vertx;
    }
}
