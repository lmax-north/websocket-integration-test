package dnt.websockets.client;

import dnt.websockets.communications.*;

// TODO Is there a point to this class. The future is the response handler
public class ResponseProcessor implements ResponseVisitor
{
    private final ExecutionLayer executionLayer;

    public ResponseProcessor(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    @Override
    public void visit(OptionsResponse optionsResponse)
    {
        executionLayer.respond(optionsResponse);
    }
}
