package dnt.websockets.communications;

import education.common.result.Result;
import io.vertx.core.Future;

public interface ExecutionLayer
{
    <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request);

    void respond(AbstractResponse response);

    void send(AbstractMessage message);
}
