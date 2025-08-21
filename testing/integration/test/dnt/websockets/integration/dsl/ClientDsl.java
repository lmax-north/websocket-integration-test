package dnt.websockets.integration.dsl;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.communications.PushMessageVisitor;
import dnt.websockets.integration.ClientDriver;
import dnt.websockets.integration.IntegrationExecutionLayer;
import dnt.websockets.integration.IntegrationPushMessageVisitor;
import education.common.result.Result;
import io.vertx.core.Future;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientDsl
{
    private final ClientDriver clientDriver;
    private final IntegrationPushMessageVisitor pushMessageVisitor;

    public ClientDsl(IntegrationExecutionLayer executionLayer, IntegrationPushMessageVisitor pushMessageVisitor)
    {
        this.clientDriver = new ClientDriver(executionLayer);
        this.pushMessageVisitor = pushMessageVisitor;
    }

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(clientDriver.fetchOptions());
        assertThat(result.isSuccess()).isTrue();
        System.out.println(result);
    }

    private static <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }

    public void verifyMessage(String className)
    {
        AbstractMessage lastMessage = pushMessageVisitor.getLastMessage();
        assertThat(lastMessage.getClass().getSimpleName()).isEqualTo(className);
    }
}
