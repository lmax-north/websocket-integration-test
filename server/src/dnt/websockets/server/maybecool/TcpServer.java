package dnt.websockets.server.maybecool;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.ExecutionLayer;
import dnt.websockets.communications.Publisher;
import dnt.websockets.server.vertx.VertxServerExecutionLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpServer
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TcpServer.class);

    private boolean acceptConnections = true;
    private ServerSocket serverSocket;
    private final List<ExecutionLayer> executionLayers = new ArrayList<>();

    public void start() {
        new Thread(() ->
        {
            try
            {
                serverSocket = new ServerSocket(7778);
                LOGGER.info("Server started");

                while (acceptConnections)
                {
                    final Socket client = serverSocket.accept();
                    handle(client);
                }
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }).start();

        waitForServerToStart();
    }

    private static void waitForServerToStart() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handle(Socket socket)
    {
        try
        {
            Publisher messagePublisher = new TcpPublisher(socket);
            ExecutionLayer executorLayer = new VertxServerExecutionLayer(messagePublisher); // Use vertx for now.
            executionLayers.add(executorLayer);
            LOGGER.info("Client connected. {}", socket.getRemoteSocketAddress());
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(AbstractMessage message)
    {
        executionLayers.forEach(executionLayer -> executionLayer.send(message));
    }

    public void shutdown()
    {
        if (serverSocket != null)
        {
            try
            {
                acceptConnections = false;
                serverSocket.close();
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
