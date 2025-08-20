package dnt.websockets.server;

import dnt.websockets.communications.*;

public class RequestProcessor implements RequestVisitor
{
    private final ExecutionLayer executionLayer;

    public RequestProcessor(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    @Override
    public void visit(OptionsRequest optionsRequest)
    {
        executionLayer.notifyResponseReceived(new OptionsResponse(optionsRequest.correlationId));
    }
}
