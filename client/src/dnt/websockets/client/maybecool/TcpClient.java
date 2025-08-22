package dnt.websockets.client.maybecool;

import dnt.websockets.client.ClientTextMessageHandler;
import dnt.websockets.client.Requests;
import dnt.websockets.client.vertx.VertxClientExecutionLayer;
import dnt.websockets.communications.*;
import dnt.websockets.server.maybecool.TcpPublisher;
import education.common.result.Result;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import static dnt.websockets.vertx.VertxFactory.newVertx;

public class TcpClient implements Requests
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpClient.class);
    private static final Vertx VERTX = newVertx();

    private final PushMessageVisitor pushMessageVisitor;

    private ExecutionLayer executorLayer;

    public TcpClient(PushMessageVisitor pushMessageVisitor)
    {
        this.pushMessageVisitor = pushMessageVisitor;
    }

    @Override
    public Future<Result<OptionsResponse, String>> fetchOptions()
    {
        return executorLayer.request(new OptionsRequest());
    }

    public void connect()
    {
        try
        {
            final Socket socket = new Socket("localhost", 7778);
            LOGGER.info("Connected to server");

            final Publisher publisher = new TcpPublisher(socket);
            executorLayer = new VertxClientExecutionLayer(VERTX, publisher); // Use vertx futures for now.

            new Thread(() ->
            {
                ClientTextMessageHandler messageHandler = new ClientTextMessageHandler(executorLayer, pushMessageVisitor);
                try(final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
                {
                    while(true)
                    {
                        String maybeJson = in.readLine();
                        if(maybeJson == null) break;

                        messageHandler.handle(maybeJson);
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException(e);
                }
            }).start();

            waitForClientToStart();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void waitForClientToStart() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
