package dnt.websockets.integration;

import dnt.websockets.communications.AbstractMessage;
import dnt.websockets.communications.Publisher;
import dnt.websockets.server.Source;

import java.util.*;
import java.util.function.Consumer;

class IntegrationPublisher implements Publisher
{
    private final Consumer<AbstractMessage> consumerMessage;

    IntegrationPublisher(Consumer<AbstractMessage> consumerMessage)
    {
        this.consumerMessage = consumerMessage;
    }

    @Override
    public void send(AbstractMessage message)
    {
        consumerMessage.accept(message);
    }
}
