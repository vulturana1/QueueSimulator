import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;

public class SimulationFrame extends JFrame {

	private JPanel contentPane;
	public JTextField timeText;
	public JTextArea textQueues;
	public JTextArea textClients;
	public JTextField textFieldWT;
	public JTextField textFieldST;
	public JTextField textFieldPH;

	public SimulationFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 671, 569);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel titleLabel = new JLabel("Queue Simulator");
		titleLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
		titleLabel.setBounds(259, 10, 128, 30);
		contentPane.add(titleLabel);

		JLabel waitLabel = new JLabel("Waiting Clients");
		waitLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		waitLabel.setBounds(101, 50, 101, 38);
		contentPane.add(waitLabel);

		JLabel queuesLabel = new JLabel("Queues");
		queuesLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		queuesLabel.setBounds(450, 54, 95, 30);
		contentPane.add(queuesLabel);

		textClients = new JTextArea();

		textClients.setVisible(true);
		textClients.setBounds(10, 98, 208, 369);

		textQueues = new JTextArea();
		textQueues.setVisible(true);
		textQueues.setBounds(272, 98, 294, 369);

		JLabel timeLabel = new JLabel("Time:");
		timeLabel.setBackground(Color.WHITE);
		timeLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		timeLabel.setBounds(299, 34, 55, 30);
		contentPane.add(timeLabel);

		timeText = new JTextField();
		timeText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		timeText.setEditable(false);
		timeText.setBackground(Color.LIGHT_GRAY);
		timeText.setBounds(299, 61, 36, 19);
		contentPane.add(timeText);
		timeText.setColumns(10);

		JScrollPane clientsPane = new JScrollPane();
		clientsPane.setBounds(10, 98, 299, 385);
		clientsPane.setViewportView(textClients);
		contentPane.add(clientsPane);

		JScrollPane queuesPane = new JScrollPane();
		queuesPane.setBounds(330, 98, 317, 385);
		queuesPane.setViewportView(textQueues);
		contentPane.add(queuesPane);
		
		JLabel averageWT = new JLabel("Average waiting time:");
		averageWT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		averageWT.setBounds(10, 503, 151, 23);
		contentPane.add(averageWT);
		
		JLabel averageST = new JLabel("Average service time:");
		averageST.setFont(new Font("Tahoma", Font.PLAIN, 12));
		averageST.setBounds(213, 506, 128, 17);
		contentPane.add(averageST);
		
		JLabel peakH = new JLabel("Peak hour:");
		peakH.setFont(new Font("Tahoma", Font.PLAIN, 12));
		peakH.setBounds(432, 506, 101, 17);
		contentPane.add(peakH);
		
		textFieldWT = new JTextField();
		textFieldWT.setBounds(136, 506, 49, 19);
		contentPane.add(textFieldWT);
		textFieldWT.setColumns(10);
		
		textFieldST = new JTextField();
		textFieldST.setBounds(338, 506, 49, 19);
		contentPane.add(textFieldST);
		textFieldST.setColumns(10);
		
		textFieldPH = new JTextField();
		textFieldPH.setBounds(496, 506, 49, 19);
		contentPane.add(textFieldPH);
		textFieldPH.setColumns(10);
	}
}
