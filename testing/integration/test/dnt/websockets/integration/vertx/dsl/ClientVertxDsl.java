package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.OptionsResponse;
import dnt.websockets.integration.vertx.VertxClientDriver;
import education.common.result.Result;
import io.vertx.core.Future;
import org.awaitility.Awaitility;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.assertj.core.api.Assertions.assertThat;

public class ClientVertxDsl
{
    private final VertxClientDriver clientDriver;

    public ClientVertxDsl(VertxClientDriver clientDriver)
    {
        this.clientDriver = clientDriver;
    }

    public void fetchOptions()
    {
        Result<OptionsResponse, String> result = join(clientDriver.requestOptions());
        System.out.println(result);
        assertThat(result.isSuccess()).isTrue();
    }

    private <R> R join(Future<R> future)
    {
        return future.toCompletionStage().toCompletableFuture().join();
    }

    public void verifyMessage(String className)
    {
        Awaitility
                .await()
                .pollInterval(ofMillis(100))
                .atMost(ofSeconds(2))
                .untilAsserted(() ->
                {
                    AbstractMessage message = clientDriver.popLastMessage();
                    assertThat(message).isNotNull();
                    assertThat(message.getClass().getSimpleName()).isEqualToIgnoringCase(className);
                });
    }

    public void verifyNoMoreMessages()
    {
        Awaitility
                .await()
                .pollInterval(ofMillis(100))
                .during(ofSeconds(2))
                .atMost(ofSeconds(3))
                .until(() -> {
                    AbstractMessage abstractMessage = clientDriver.popLastMessage();
                    return abstractMessage == null;
                });
    }
}
