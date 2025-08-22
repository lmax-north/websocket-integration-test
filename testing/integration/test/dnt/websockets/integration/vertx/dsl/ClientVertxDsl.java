package dnt.websockets.integration.vertx.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.GetPropertyResponse;
import dnt.websockets.communications.SetPropertyResponse;
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

    public void setProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("value"),
                new OptionalArg("expectSuccess").setDefault("true"));
        boolean expectSuccess = params.valueAsBoolean("expectSuccess");

        String key = params.value("key");
        String value = params.value("value");

        Result<SetPropertyResponse, String> result = join(clientDriver.setProperty(key, value));
        assertThat(result.isSuccess()).isEqualTo(expectSuccess);
    }

    public void getProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new OptionalArg("expectedValue"),
                new OptionalArg("expectSuccess").setDefault("true"));
        boolean expectSuccess = params.valueAsBoolean("expectSuccess");

        String key = params.value("key");
        Result<GetPropertyResponse, String> result = join(clientDriver.getProperty(key));
        assertThat(result.isSuccess()).isEqualTo(expectSuccess);
        params.valueAsOptional("expectedValue").ifPresent(expectedValue ->
                assertThat(result.success().value).isEqualTo(expectedValue));
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
