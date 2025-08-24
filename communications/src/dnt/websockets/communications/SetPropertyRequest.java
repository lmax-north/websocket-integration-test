package dnt.websockets.communications;

import java.util.Optional;

public class SetPropertyRequest extends AbstractRequest
{
    public String key;
    public String value;

    public SetPropertyRequest()
    {
        super();
    }
    public SetPropertyRequest(String key, String value)
    {
        this();
        this.key = key;
        this.value = value;
    }

    @Override
    public void visit(ExecutionLayer executionLayer, RequestVisitor visitor)
    {
        visitor.visit(executionLayer, this);
    }

    public static Optional<ErrorResponse> validate(SetPropertyRequest request)
    {
        if(request.key == null || request.key.isEmpty())
            return Optional.of(new ErrorResponse(request.correlationId, 400, "Key cannot be empty."));
        if(request.value == null || request.value.isEmpty())
            return Optional.of(new ErrorResponse(request.correlationId, 400, "Value cannot be empty."));
        return Optional.empty();
    }

    @Override
    public String toString()
    {
        return "SetPropertyRequest{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                "} " + super.toString();
    }
}
