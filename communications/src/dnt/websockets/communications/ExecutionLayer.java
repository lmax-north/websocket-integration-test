package dnt.websockets.communications;

import education.common.result.Result;
import io.vertx.core.Future;

/*

- Integration Execution Layer
  - Real life, client has its own, server has its own
  - Created on connection, along with text message handlers
  - Integration combines the 2 execution layers together.
  - Requests use vertx future syntax, complete the future immediately
  - Opportunity to intercept messages.

- Client/Server Text Message Handlers
  - Handle the serde
  - Pass off message to request/processors
  - Same ones used for real and integration

- Request Processors
  - Follow the visitor design pattern to get from AbstractMessage to desired functionality. (see RequestProcessor)
  - Same ones used for real and integration
  - Created in the text message handlers
  - Debugging

 */
public interface ExecutionLayer
{
    <T extends AbstractResponse> Future<Result<T, String>> request(AbstractRequest request);

    void respond(AbstractResponse response);

    void send(AbstractMessage message);
}
