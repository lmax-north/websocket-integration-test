package dnt.websockets.integration;

import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import org.junit.Test;

public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void clientShouldRequestAndResponse()
    {
        client.fetchOptions();
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
        client.fetchOptions("expectSuccess: true");

        integration.failNextMessage("Not available for this user.");

        client.fetchOptions("expectSuccess: false");
    }


    @Test
    public void serverShouldFutureFailNextMessage()
    {
        client.fetchOptions("expectException: false");

        integration.throwOnNextMessage();

        client.fetchOptions("expectException: true");
    }
}
