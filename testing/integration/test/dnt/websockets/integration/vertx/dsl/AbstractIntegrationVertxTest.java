package dnt.websockets.integration.vertx.dsl;

import dnt.websockets.integration.vertx.VertxClientDriver;
import dnt.websockets.integration.vertx.VertxServerDriver;

public abstract class AbstractIntegrationVertxTest
{
    private static final VertxServerDriver serverDriver = new VertxServerDriver();
    private static final VertxClientDriver clientDriver = new VertxClientDriver("source1");

    protected ServerVertxDsl server = new ServerVertxDsl(serverDriver);
    protected ClientVertxDsl client = new ClientVertxDsl(clientDriver);
}
