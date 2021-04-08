import java.util.ArrayList;
import java.util.List;

public class Scheduler {

    private ArrayList<Server> servers;
    private int maxNoServers;
    public static int maxClientsPerServer;
    private Strategy strategy = new ConcreteStrategyTime();

    public Scheduler(int maxNoServers, int maxClientsPerServer) {
        this.maxClientsPerServer = maxClientsPerServer;
        servers = new ArrayList<>(maxNoServers);
        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
            new Thread(server).start();
        }
    }

    public void dispatchClient(Client client) {
        //call the strategy addClient method
        strategy.addClient(servers, client);
    }

    public List<Server> getServers() {
        return servers;
    }
}
