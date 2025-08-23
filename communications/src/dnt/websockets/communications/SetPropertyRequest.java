package dnt.websockets.communications;

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

    @Override
    public String toString()
    {
        return "SetPropertyRequest{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                "} " + super.toString();
    }
}
