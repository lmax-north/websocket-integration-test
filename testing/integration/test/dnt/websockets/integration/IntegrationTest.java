package dnt.websockets.integration;

import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import org.junit.Test;

public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void clientShouldRequestAndResponse()
    {
        client.setProperty("key: name", "value: sam");
        client.getProperty("key: name", "expectedValue: sam");
    }

    @Test
    public void serverShouldBroadcastMessage()
    {
        client.verifyNoMoreMessages();

        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }

    @Test
    public void serverShouldFailNextMessage()
    {
        client.getProperty("key: name", "expectSuccess: true");

        integration.failNextMessage("Not available for this user.");

        client.getProperty("key: name", "expectSuccess: false");
    }


    @Test
    public void serverShouldFutureFailNextMessage()
    {
        client.getProperty("key: name", "expectException: false");

        integration.throwOnNextMessage();

        client.getProperty("key: name", "expectException: true");
    }
}
