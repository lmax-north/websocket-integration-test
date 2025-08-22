package dnt.websockets.integration.maybecool.dsl;

import dnt.websockets.integration.maybecool.TcpClientDriver;
import dnt.websockets.integration.maybecool.UdpServerDriver;
import org.junit.AfterClass;

public abstract class AbstractIntegrationTcpTest
{
    private static final UdpServerDriver serverDriver = new UdpServerDriver();
    private static final TcpClientDriver clientDriver = new TcpClientDriver();

    protected TcpServerDsl server = new TcpServerDsl(serverDriver);
    protected TcpClientDsl client = new TcpClientDsl(clientDriver);

    @AfterClass
    public static void afterClass()
    {
        serverDriver.close();
    }
}
