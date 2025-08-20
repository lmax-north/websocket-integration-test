package dnt.websockets.communications;

public interface Publisher
{
    void send(AbstractMessage message);

    void send(String source, AbstractMessage message);
}
