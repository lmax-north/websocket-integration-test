package dnt.websockets.server;

import dnt.websockets.communications.*;

import java.util.HashMap;

/**
 * Class to handle requests
 */
public class RequestProcessor implements RequestVisitor
{
    private final HashMap<String, String> properties = new HashMap<>();

    @Override
    public void visit(ExecutionLayer executionLayer, GetPropertyRequest request)
    {
        String value = properties.get(request.key);
        if(value == null)
        {
            final ErrorResponse response = new ErrorResponse(request.correlationId, 404, "Value not found");
            executionLayer.respond(response);
        }
        else
        {
            GetPropertyResponse response = new GetPropertyResponse(request.correlationId, value);
            executionLayer.respond(response);
        }
    }

    @Override
    public void visit(ExecutionLayer executionLayer, SetPropertyRequest request)
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
