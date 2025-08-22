package dnt.websockets.communications;

public interface RequestVisitor
{
    default void visit(GetPropertyRequest request) {}
    default void visit(SetPropertyRequest request) {}
}
