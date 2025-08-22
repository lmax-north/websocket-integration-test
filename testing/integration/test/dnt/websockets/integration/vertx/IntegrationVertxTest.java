package dnt.websockets.integration.vertx;

import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.Test;

public class IntegrationVertxTest extends AbstractIntegrationVertxTest
{
    @Test
    public void shouldSendAndReceive()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    public void serverShouldBroadcast()
    {
        client.verifyNoMoreMessages();

        server.broadcastMessage();

        client.verifyMessage("PushMessage");
        client.verifyNoMoreMessages();
    }
}
