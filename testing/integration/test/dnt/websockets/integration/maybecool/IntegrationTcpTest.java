package dnt.websockets.integration.maybecool;

import dnt.websockets.integration.maybecool.dsl.AbstractIntegrationTcpTest;
import org.junit.Test;

public class IntegrationTcpTest extends AbstractIntegrationTcpTest
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
}
