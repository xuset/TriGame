package triGame.intro;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

class ErrorWindow {
	private final JFrame frame = new JFrame();
	private final JPanel panel = new JPanel();
	private final JLabel label = new JLabel();
	private final JTextArea txtReport = new JTextArea();
	private final JButton btnFile = new JButton("Save to file");
	private final JButton btnCopy = new JButton("Copy to clipboard");
	private final JButton btnClose = new JButton("Exit");
	
	ErrorWindow(Exception ex) {
		frame.setTitle("Attack of the Triangles!");
		label.setText("<html><body style='width:300; text-align:center;'>Oops. An error has occured. It would be appreciated if you would email me the contents at xuset1@gmail.com</body></html>");
		
		txtReport.setEditable(false);
		populateTxtReport(ex);
		
		btnFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				writeErrorToFile();
			}
		});
		
		btnCopy.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copyError();
			}
		});
		
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		panel.add(label);
		panel.add(txtReport);
		panel.add(btnCopy);
		panel.add(btnFile);
		panel.add(btnClose);
		frame.add(panel);
		frame.setPreferredSize(new Dimension(400, 600));
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
	void waitForDisposal() {
		while (frame.isDisplayable()) {
			try { Thread.sleep(10); } catch (Exception ex) { }
		}
	}
	
	private void populateTxtReport(Exception ex) {
		txtReport.append("Erorr class: " + ex.toString() + "\r\n");
		txtReport.append("Error message: " + ex.getMessage() + "\r\n");
		txtReport.append("Local message: " + ex.getLocalizedMessage() + "\r\n");
		txtReport.append("Stacktrace:\r\n");
		StackTraceElement stackFrames[] = ex.getStackTrace();
		for (StackTraceElement st : stackFrames)
			txtReport.append("   at " + st.getClassName() + "(" + st.getFileName() + ":" + st.getLineNumber() + ")\r\n");
		txtReport.append("OS: " + System.getProperty("os.name") + "\r\n");
		txtReport.append("OS version: " + System.getProperty("os.version") + "\r\n");
		txtReport.append("OS arch: " + System.getProperty("os.arch") + "\r\n");
		txtReport.append("Java version: " + System.getProperty("java.runtime.version") + "\r\n");
		txtReport.append("OpenGL: " + System.getProperty("sun.java2d.opengl") + "\r\n");
		txtReport.append("D3D: " + System.getProperty("sun.java2d.d3d") + "\r\n");
	}
	
	private void copyError() {
		txtReport.selectAll();
		txtReport.copy();
	}
	
	private void writeErrorToFile() {
		try {
			File file = new File("TriGame error report.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(txtReport.getText());
			bw.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void close() {
		frame.dispose();
	}

}
