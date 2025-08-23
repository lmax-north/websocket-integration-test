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
    public void serverShouldFutureFailNextMessage()
    {
        client.getProperty("key: name", "expectException: false");

        integration.throwOnNextMessage();

        client.getProperty("key: name", "expectException: true");
    }

    @Test
    public void shouldFailIfNoResponse()
    {
        client.setProperty("key: do_not_send_response", "value: true", "expectSuccess: false");
    }

    @Test
    public void serverShouldFailNextMessage()
    {
        client.setProperty("key: name", "value: sam", "expectSuccess: true");

        integration.failNextMessage("Not available for this user.");

        client.setProperty("key: name", "value: sam", "expectSuccess: false");
    }

    @Test
    public void shouldPauseProcessing()
    {
        client.setProperty("key: name", "value: sam", "complete: true");

        integration.pauseProcessing();
        client.setProperty("key: name", "value: terri", "complete: false");

        server.verifyProperty("key: name", "expectedValue: sam");
        integration.resumeProcessing();
        server.verifyProperty("key: name", "expectedValue: terri");
    }
}
