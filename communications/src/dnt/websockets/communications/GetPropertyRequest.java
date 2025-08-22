package dnt.websockets.communications;

public class GetPropertyRequest extends AbstractRequest
{
    public String key;

    public GetPropertyRequest()
    {
        super();
    }
    public GetPropertyRequest(String key)
    {
        this();
        this.key = key;
    }

    @Override
    public void visit(RequestVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "GetPropertyRequest{" +
                "key='" + key + '\'' +
                "} " + super.toString();
    }
}
