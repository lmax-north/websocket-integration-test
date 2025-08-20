package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import dnt.websockets.server.Source;

import java.util.*;

class IntegrationPublisher implements Publisher
{
    LinkedList<SourceAndMessage> messages = new LinkedList<>();

    @Override
    public void send(AbstractMessage message)
    {
        messages.add(new SourceAndMessage(null, message));
    }

    @Override
    public void send(String source, AbstractMessage message)
    {
        messages.add(new SourceAndMessage(source, message));
    }

    public <T extends AbstractMessage> T getLastMessage(String source)
    {
        Iterator<SourceAndMessage> iterator = messages.descendingIterator();
        while(iterator.hasNext())
        {
            SourceAndMessage item = iterator.next();
            if(source.equals(item.source))
            {
                return (T) messages.remove().message;
            }
        }
        return null;
    }

    public <T extends AbstractMessage> T getLastMessage()
    {
        return (T) messages.remove().message;
    }

    private static class SourceAndMessage
    {
        final String source;
        final AbstractMessage message;

        private SourceAndMessage(String source, AbstractMessage message)
        {
            this.source = source;
            this.message = message;
        }
    }
}
