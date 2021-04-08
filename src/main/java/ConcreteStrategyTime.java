import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addClient(List<Server> servers, Client c) {
        //iterate through the servers and find the one with the lowest waiting time and add the client to that server
        Server minClientServer = servers.get(0);
        for (Server server : servers) {
            if (server.getWaitingTime().get() < minClientServer.getWaitingTime().get()) {
                minClientServer = server;
            }
        }
        int i = servers.indexOf(minClientServer);
        servers.get(i).addClient(c);
    }
}
