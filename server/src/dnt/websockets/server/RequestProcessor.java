package dnt.websockets.server;

import dnt.websockets.communications.*;

/**
 * Class to handle requests
 */
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
        executionLayer.respond(new OptionsResponse(optionsRequest.correlationId));
    }
}
