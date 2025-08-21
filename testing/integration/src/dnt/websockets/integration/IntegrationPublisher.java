package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import dnt.websockets.communications.PushMessageVisitor;

import java.util.function.Consumer;

class IntegrationPublisher implements Publisher
{
    private final PushMessageVisitor pushMessageProcessor;

    IntegrationPublisher(PushMessageVisitor pushMessageProcessor)
    {
        this.pushMessageProcessor = pushMessageProcessor;
    }

    @Override
    public void send(AbstractMessage message)
    {
        message.visit(pushMessageProcessor);
    }
}
