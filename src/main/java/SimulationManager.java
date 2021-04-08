import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JOptionPane;

public class SimulationManager implements Runnable {

	private static DecimalFormat df2 = new DecimalFormat("#.##");

	public int timeSimulation;
	public int maxServiceTime;
	public int minServiceTime;
	public int maxArrivalTime;
	public int minArrivalTime;
	public int numberOfServers;
	public int numberOfClients;

	private SimulationFrame frame;

	public double averageServiceTime;

	public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

	private Scheduler scheduler;

	private ArrayList<Client> generatedClients = new ArrayList<Client>();

	private BufferedWriter output;

	public SimulationManager(int timeSimulation, int maxServiceTime, int minServiceTime, int maxArrivalTime,
			int minArrivalTime, int numberOfServers, int numberOfClients, BufferedWriter outFile) throws IOException {
		frame = new SimulationFrame();
		frame.setVisible(true);

		scheduler = new Scheduler(numberOfServers, numberOfClients);
		this.timeSimulation = timeSimulation;
		this.maxServiceTime = maxServiceTime;
		this.minServiceTime = minServiceTime;
		this.maxArrivalTime = maxArrivalTime;
		this.minArrivalTime = minArrivalTime;
		this.numberOfServers = numberOfServers;
		this.numberOfClients = numberOfClients;
		this.generateNRandomClients();
		this.output = outFile;
	}

	private void generateNRandomClients() {
		int sumTService = 0;
		for (int i = 1; i <= this.numberOfClients; i++) {
			int tService = ThreadLocalRandom.current().nextInt(this.minServiceTime, this.maxServiceTime + 1);
			int tArrival = ThreadLocalRandom.current().nextInt(this.minArrivalTime, this.maxArrivalTime + 1);
			Client client = new Client(i, tArrival, tService);
			generatedClients.add(client);
			sumTService += tService;
		}
		// sort by tArrival
		Collections.sort(generatedClients);
		this.averageServiceTime = (double) sumTService / this.numberOfClients;
	}

	@Override
	public void run() {

		int currentTime = 0;
		int servedClients = 0;
		int maxNrOfWaitingClients = 0;
		int waitingClients = 0;
		double averageWaitingTime = 0;
		int peakHour = 0;

		while (currentTime <= this.timeSimulation) {
			// iterate generatedClients list and pick tasks that have the tArrival equal
			// with the currentTime
			// - send client to queue by calling the dispatchClient method from Scheduler
			// - delete client from list
			Iterator<Client> iterator = generatedClients.iterator();
			while (iterator.hasNext()) {
				Client client = iterator.next();
				try {
					if (client.gettArrival() == currentTime) {
						scheduler.dispatchClient(client);
						servedClients++;
						iterator.remove();
						//if (servedClients == this.numberOfClients) {
//							this.timeSimulation = currentTime + scheduler.getMaximumWaitingTime();
						//}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}
			waitingClients = 0;
			for (Server s : scheduler.getServers()) {
				waitingClients += s.getClients().size();
			}

			if (waitingClients > maxNrOfWaitingClients) {
				maxNrOfWaitingClients = waitingClients;
				peakHour = currentTime;
			}
			
			List<Server> servers = scheduler.getServers();
			for (Server server : servers) {
				if (server.getClients().isEmpty()) {
				} else {
					BlockingQueue<Client> clients = server.getClients();
					averageWaitingTime += clients.size();
				}
			}
			int ok = 0;
			if (servedClients == this.numberOfClients) {
				for (Server s : scheduler.getServers()) {
					if(s.getClients().isEmpty())
						ok++;
				}
			}
			if(ok == numberOfServers)
				this.timeSimulation = currentTime;

			// update GUI frame
			displayData(currentTime);
			print(currentTime);
			try {
				printFile(currentTime);
			} catch (IOException e) {
				e.printStackTrace();
			}

			currentTime++;
			// wait an interval of 1 second
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			;
		}

		double averageWT = averageWaitingTime / servedClients ;
		System.out.println("-- Average waiting time: " + df2.format(averageWT) + "\n");
		System.out.println("-- Average service time: " + df2.format(averageServiceTime) + "\n");
		System.out.println("-- Peak hour: " + peakHour + "\n");

		frame.textFieldWT.setText(df2.format(averageWT) + "");
		frame.textFieldST.setText(df2.format(averageServiceTime) + "");
		frame.textFieldPH.setText(peakHour + "");

		try {
			output.write("-- Average waiting time: " + df2.format(averageWT) + "\n");
			output.write("-- Average service time: " + df2.format(averageServiceTime) + "\n");
			output.write("-- Peak hour: " + peakHour + "\n");
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Simulation finished!", null, JOptionPane.INFORMATION_MESSAGE);
	}

	public void print(int time) {
		System.out.println("Time " + time + "\n");
		System.out.println("Waiting clients: ");

		for (Client client : generatedClients) {
			System.out.println(client.toString() + ";  ");
		}
		System.out.println("\n");
		List<Server> servers = scheduler.getServers();
		int count = 1;
		for (Server server : servers) {
			System.out.println("Queue " + count + ": ");
			if (server.getClients().isEmpty()) {
				System.out.println("closed\n");
			} else {
				BlockingQueue<Client> clients = server.getClients();
				for (Client client : clients) {
					System.out.println(client + "; ");
				}
				System.out.println("\n");
			}
			count++;
		}
		System.out.println("\n");
	}

	public void printFile(int time) throws IOException {
		output.write("Time " + time + "\n");
		output.write("Waiting clients: ");

		for (Client client : generatedClients) {
			output.write(client.toString() + ";  ");
		}
		output.write("\n");
		List<Server> servers = scheduler.getServers();
		int count = 1;
		for (Server server : servers) {
			output.write("Queue " + count + ": ");
			if (server.getClients().isEmpty()) {
				output.write("closed\n");
			} else {
				BlockingQueue<Client> clients = server.getClients();
				for (Client client : clients) {
					output.write(client + "; ");
				}
				output.write("\n");
			}
			count++;
		}
		output.write("\n");
	}

	public void displayData(int currentTime) {
		this.frame.timeText.setText(currentTime + "");
		this.frame.textClients.append("Waiting clients: ");

		for (Client client : generatedClients) {
			this.frame.textClients.append(client.toString() + ";  ");
		}
		this.frame.textClients.append("\n");
		List<Server> servers = scheduler.getServers();
		int count = 1;
		for (Server server : servers) {
			this.frame.textQueues.append("Queue " + count + ": ");
			if (server.getClients().isEmpty()) {
				this.frame.textQueues.append("closed\n");
			} else {
				BlockingQueue<Client> clients = server.getClients();
				for (Client client : clients) {
					this.frame.textQueues.append(client + "; ");
				}
				this.frame.textQueues.append("\n");
			}
			count++;
		}
		this.frame.textQueues.append("\n");
		for(int i=0;i<this.numberOfServers;i++)
			this.frame.textClients.append("\n");
	}

	public static void main(String[] args) throws IOException {

		FileWriter output = new FileWriter("out-test.txt", true);
		BufferedWriter file = new BufferedWriter(output);

		File input;
		Scanner scanner;
		Integer[] parameters;
		input = new File("in-test-1.txt");
		scanner = new Scanner(input);
		parameters = new Integer[7];
		int i = 0;
		while (scanner.hasNext()) {
			String line = scanner.nextLine();
			String[] res = line.split(",");
			for (String nr : res) {
				parameters[i] = Integer.parseInt(nr);
				i++;
			}
		}

		scanner.close();

		SimulationManager gen = new SimulationManager(60, 4, 2, 4, 2, 2, 3, file);

//		SimulationManager gen = new SimulationManager(parameters[2], parameters[6], parameters[5], parameters[4],
//				parameters[3], parameters[1], parameters[0], file);   //pt ex din fisisere
		Thread t = new Thread(gen);
		t.start();
	}
}
