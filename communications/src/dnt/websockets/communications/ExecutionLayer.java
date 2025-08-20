package dnt.websockets.communications;

import education.common.result.Result;
import io.vertx.core.Future;

public interface ExecutionLayer
{
    <T extends AbstractResponse> Future<Result<T, String>> send(AbstractRequest request);

    void broadcast(AbstractMessage message);

    void unicast(String source, AbstractMessage message);

    void notifyResponseReceived(AbstractResponse response);
}
