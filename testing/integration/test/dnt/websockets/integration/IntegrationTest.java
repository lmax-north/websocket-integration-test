package dnt.websockets.integration;

import dnt.websockets.integration.dsl.AbstractIntegrationTest;
import org.junit.Ignore;
import org.junit.Test;

public class IntegrationTest extends AbstractIntegrationTest
{
    @Test
    public void shouldRequestAndResponse()
    {
        client.fetchOptions();
    }

    @Test
    public void serverShouldBroadcastMessage()
    {
        server.broadcastMessage();

        client.verifyMessage("PushMessage");
    }

    @Test
    public void verifyFailNextMessage()
    {
        client.fetchOptions("expectSuccess: true");

        integration.failNextMessage("Not available for this user.");

        client.fetchOptions("expectSuccess: false");
    }


    @Test
    public void verifyFutureFailNextMessage()
    {
        client.fetchOptions("expectException: false");

        integration.throwOnNextMessage();

        client.fetchOptions("expectException: true");
    }
}
