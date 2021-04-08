import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

	private BlockingQueue<Client> clients;
	private AtomicInteger waitingTime;  // decremented by current thread once a task is completed
										// incremented by scheduler thread when adding a new task

	public Server() {
		// initialise queue and waiting period
		waitingTime = new AtomicInteger(0);
		clients = new ArrayBlockingQueue<Client>(Scheduler.maxClientsPerServer);
	}

	public void addClient(Client client) {
		clients.add(client);
		this.waitingTime.set(this.waitingTime.intValue() + client.gettService());
	}

	public BlockingQueue<Client> getClients() {
		return clients;
	}

	public void setClients(BlockingQueue<Client> clients) {
		this.clients = clients;
	}

	public AtomicInteger getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(AtomicInteger waitingTime) {
		this.waitingTime = waitingTime;
	}

	@Override
	public void run() {

		while (true) {
			try {
				 if (clients.isEmpty() == false) {
				 	Client next = clients.peek();
					waitingTime.decrementAndGet();
					Thread.sleep(1000 * next.gettService());
					clients.poll();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
