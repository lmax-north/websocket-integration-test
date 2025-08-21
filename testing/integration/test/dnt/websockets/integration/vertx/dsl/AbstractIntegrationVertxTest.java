package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.ClientVertxDriver;
import dnt.websockets.integration.vertx.ServerVertxDriver;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractIntegrationVertxTest
{
    private static final ServerVertxDriver serverDriver = new ServerVertxDriver();
    private static final ClientVertxDriver clientDriver = new ClientVertxDriver("source1");

    protected ServerVertxDsl server = new ServerVertxDsl(serverDriver);
    protected ClientVertxDsl client = new ClientVertxDsl(clientDriver);
}
