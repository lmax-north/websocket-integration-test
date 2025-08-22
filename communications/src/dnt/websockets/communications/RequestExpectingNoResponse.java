package dnt.websockets.communications;

public class RequestExpectingNoResponse extends AbstractRequest
{
    public RequestExpectingNoResponse() {}

    @Override
    public void visit(RequestVisitor visitor)
    {
        visitor.visit(this);
    }
}
