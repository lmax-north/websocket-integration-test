package dnt.websockets.integration.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import dnt.websockets.communications.*;
import dnt.websockets.integration.ClientDriver;
import dnt.websockets.integration.PushMessageCollector;
import education.common.result.Result;
import io.vertx.core.Future;
import org.assertj.core.api.Assertions;

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

    public void fetchOptions(String... args)
    {
        final DslParams dslParams = DslParams.create(args,
                new OptionalArg("expectSuccess").setDefault("true"),
                new OptionalArg("expectException").setDefault("false"));
        boolean expectSuccess = dslParams.valueAsBoolean("expectSuccess");

        if(dslParams.valueAsBoolean("expectException"))
        {
            Assertions.assertThatException()
                    .isThrownBy(() -> join(clientDriver.fetchOptions()))
                    .withCauseInstanceOf(RuntimeException.class);
            return;
        }

        Result<OptionsResponse, String> result = join(clientDriver.fetchOptions());
        assertThat(result.isSuccess()).isEqualTo(expectSuccess);
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
}
