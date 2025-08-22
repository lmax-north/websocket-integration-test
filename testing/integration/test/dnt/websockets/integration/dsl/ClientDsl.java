package dnt.websockets.integration.dsl;

import dnt.websockets.communications.*;
import dnt.websockets.integration.ClientDriver;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class ClientDsl
{
    private final ClientDriver clientDriver;
    private final PushMessageCollector collector;

    public ClientDsl(ExecutionLayer executionLayer, PushMessageCollector collector)
    {
        this.clientDriver = new ClientDriver(executionLayer);
        this.collector = collector;
    }

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(clientDriver.fetchOptions());
        assertTrue(result.isSuccess());
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }

    public void verifyMessage(String className)
    {
        AbstractMessage lastMessage = collector.getLastMessage();
        assertThat(lastMessage.getClass().getSimpleName()).isEqualTo(className);
    }

    public void verifyFailedToSend()
    {
        Result<AbstractResponse, String> result = join(clientDriver.sendRequestExpectingNoResponse());
        assertTrue(result.hasFailed());
        assertThat(result.error()).isEqualTo("Provoking failure.");
    }
}
