package dnt.websockets.integration.maybecool;

import dnt.websockets.client.maybecool.TcpClient;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;

public class TcpClientDriver
{
    private final TcpClient client;
    private final PushMessageCollector collector = new PushMessageCollector();

    public TcpClientDriver()
    {
        client = new TcpClient(collector);
        new Thread(client)
                .start();

        waitForClientToConnect();
    }

    private static void waitForClientToConnect() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Future<Result<OptionsResponse, String>> requestOptions()
    {
        return client.fetchOptions()
                .onFailure(t -> System.out.println("ERROR:" + t.getMessage()));
    }

    public AbstractMessage popLastMessage()
    {
        return collector.getLastMessage();
    }
}
