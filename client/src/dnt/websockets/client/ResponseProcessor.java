package dnt.websockets.client;

import dnt.websockets.communications.*;

public class ResponseProcessor implements ResponseVisitor
{
    private final ExecutionLayer execution;

    public ResponseProcessor(ExecutionLayer execution)
    {
        this.execution = execution;
    }

    @Override
    public void visit(OptionsResponse optionsResponse)
    {
        execution.notifyResponseReceived(optionsResponse);
    }
}
