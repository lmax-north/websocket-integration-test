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
    public void visit(RequestVisitor visitor)
    {
        visitor.visit(this);
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
