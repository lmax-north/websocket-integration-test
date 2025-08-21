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
    public void serverShouldUnicastMessage()
    {
        server.unicastMessage("source2");

        client("source1").verifyMessage("NoMessage");
        client("source2").verifyMessage("PushMessage");
    }
}
