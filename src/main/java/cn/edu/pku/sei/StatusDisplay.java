package cn.edu.pku.sei;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Queue;
import java.util.LinkedList;

//@author lyq
public class StatusDisplay extends Thread{
	private JLabel status = null;
	private volatile Queue<String> statusQueue = new LinkedList<String>();
	private Object lock = new Object();
	
	public StatusDisplay() {
		//System.out.println("Status Display");
		status = new JLabel();
		status.setFont(new Font(null,Font.PLAIN,21));
		status.setOpaque(true);
		status.setBackground(new Color(204,204,204));
		status.setPreferredSize(new Dimension(240,23));
		status.setVisible(true);
		//status.setVerticalAlignment(JLabel.BOTTOM);
		//status.setHorizontalAlignment(JLabel.CENTER);
		//jf.add("South",status);
		this.setName("StatusDisplay");   //attention: if there were many object of StatusDisplay, the thread name will be the same
		this.setDaemon(true);
		this.start();
	}
	public JLabel getLabel() {
		return status;
	}
	public void displayStatus(String msg) {
		statusQueue.add(msg);
		try {
			synchronized(lock) {
				lock.notify();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		//System.out.println(Thread.currentThread().getName());
		while(true) {
			if(statusQueue.isEmpty()) {
				status.setText(" ");
				try {
					synchronized(lock) {
						lock.wait();
					}
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			else {
				String msg = statusQueue.poll();
				status.setText("Status: "+msg);
				try {
					Thread.sleep(5000);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
