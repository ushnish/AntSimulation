import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class AntFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panel, resultPanel;
	private JButton button;
	private JTextField dimensionInput, threadsInput, trialsInput, result, time;
	private JLabel dimensionLabel, threadsLabel, trialsLabel, validLabel;
	private int dimensions, numThreads, numTrials;
	private Launcher launcher;

	public AntFrame() {
		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		dimensionInput = new JTextField(20);
		dimensionLabel = new JLabel("Number of Dimensions");
		validLabel= new JLabel();
		dimensionInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		threadsInput = new JTextField(20);
		threadsLabel = new JLabel("Number of Threads");
		threadsInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		trialsInput = new JTextField(20);
		trialsLabel = new JLabel("Number of Trials per Thread");
		trialsInput.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(validLabel);
		panel.add(dimensionLabel);
		panel.add(dimensionInput);
		panel.add(threadsLabel);
		panel.add(threadsInput);
		panel.add(trialsLabel);
		panel.add(trialsInput);
		button = new JButton("OK");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dimensionText = dimensionInput.getText();
				String threadText = threadsInput.getText();
				String trialsText = trialsInput.getText();
				if (validInput(dimensionText) && validInput(threadText) && validInput(trialsText)) {
					validLabel.setText("");
					dimensions = Integer.parseInt(dimensionInput.getText());
					numThreads = Integer.parseInt(threadsInput.getText());
					numTrials = Integer.parseInt(trialsInput.getText());
					launcher = new Launcher();
					try {
						launcher.run();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					pack();
				}
				else {
					validLabel.setText("Please enter valid positive number for all inputs");
					pack();
				}
			}
		});
		panel.add(button, "Center");

		resultPanel = new JPanel();
		resultPanel.add(new JLabel("Average: "));
		result = new JTextField(10);
		result.setEditable(false);
		resultPanel.add(result);

		resultPanel.add(new JLabel("Time Elapsed (ms): "));
		time = new JTextField(10);
		time.setEditable(false);
		resultPanel.add(time);

		add(panel, "Center");
		add(resultPanel, "South");
		pack();
		setVisible(true);
	}
	
/* Class for launching all threads and computing average time of all the simulations */

	public class Launcher extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Long totalTime = 0L;
			button.setEnabled(false);
			ExecutorService service = Executors.newFixedThreadPool(numThreads);
			CompletionService<Long> completionService = 
				       new ExecutorCompletionService<Long>(service);
			long start = System.currentTimeMillis();
			try {
				for (int i = 0; i < numThreads; i++) {
					completionService.submit(new AntCallable(dimensions, numTrials));
				}
				int received = 0;
				/* to take results in order completed */ 
				while (received < numThreads) {
					totalTime += completionService.take().get();	
					received++;
				}

			} catch (Exception ignored) {
				// TODO Auto-generated catch block
			} finally {
				long end = System.currentTimeMillis();
				double value = (double) totalTime / (double) numTrials
						/ (double) numThreads;
				String text = Double.toString(value);
				/* truncate string for readability */
				result.setText(text.substring(0, Math.min(13, text.length())));
				time.setText(Long.toString(end - start));
				service.shutdown();
				button.setEnabled(true);
			}
		}
	}
	/* Check if input is positive integer */
	private boolean validInput(String s) {
		boolean valid = false;
		if (s.length() > 0 && s.matches("\\d+") && Integer.parseInt(s) > 0) {
			valid = true;
		}
		return valid;
	}
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			AntFrame frame = new AntFrame();
			frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		} catch (Exception ignored) {
			// TODO Auto-generated catch block
		}

	}
}
