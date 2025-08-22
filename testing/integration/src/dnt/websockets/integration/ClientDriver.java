package dnt.websockets.integration;

import dnt.websockets.client.Requests;
import dnt.websockets.communications.*;
import education.common.result.Result;
import io.vertx.core.Future;

public class ClientDriver implements Requests
{
    private final ExecutionLayer executionLayer;

    public ClientDriver(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    @Override
    public Future<Result<OptionsResponse, String>> fetchOptions()
    {
        return executionLayer.request(new OptionsRequest());
    }

    @Override
    public Future<Result<AbstractResponse, String>> sendRequestExpectingNoResponse()
    {
        return executionLayer.request(new RequestExpectingNoResponse());
    }
}
