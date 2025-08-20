package dnt.websockets.integration.vertx;

import dnt.websockets.integration.vertx.dsl.AbstractIntegrationVertxTest;
import org.junit.Ignore;
import org.junit.Test;

public class IntegrationVertxTest extends AbstractIntegrationVertxTest
{
    @Test
    public void shouldSendAndReceive()
    {
        client.fetchOptions();
        client.fetchOptions();
    }

    @Test
    public void serverShouldBroadcast()
    {
        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }

    @Test
    public void serverShouldUnicast()
    {
        server.unicastMessage("SOURCE2");

        client("source1").verifyNoMessage();
        client("source2").verifyMessage("PushMessage");
    }
}
