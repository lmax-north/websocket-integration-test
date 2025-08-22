package dnt.websockets.server;

import dnt.websockets.communications.*;

import java.util.HashMap;

/**
 * Class to handle requests
 */
public class RequestProcessor implements RequestVisitor
{
    private final ExecutionLayer executionLayer;

    private final HashMap<String, String> properties = new HashMap<>();

    public RequestProcessor(ExecutionLayer executionLayer)
    {
        this.executionLayer = executionLayer;
    }

    @Override
    public void visit(GetPropertyRequest request)
    {
        String value = properties.get(request.key);
        GetPropertyResponse response = new GetPropertyResponse(request.correlationId, value);
        executionLayer.respond(response);
    }

    @Override
    public void visit(SetPropertyRequest request)
    {
        if("do_not_send_response".equals(request.key) && "true".equals(request.value))
        {
            return;
        }

        properties.put(request.key, request.value);
        System.out.println(properties);
        SetPropertyResponse response = new SetPropertyResponse(request.correlationId);
        executionLayer.respond(response);
    }
}
