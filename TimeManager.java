import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class TimeManager extends JFrame implements ActionListener {

	JButton bAddTask = new JButton("Start");
	JTextField tEnterTask = new JTextField();
	JButton bStop = new JButton("Stop");
	JButton bPause = new JButton("Pauza");
	JButton bExport = new JButton("Eksport do pliku");
	JLabel lTime = new JLabel("Czas");
	JMenuBar menuBar = new JMenuBar();
	JMenu menuFile = new JMenu("Plik");
	JMenuItem menuAbout = new JMenuItem("O programie");
	JMenuItem menuTerminate = new JMenuItem("Zakoñcz");
	
	private ImageIcon image1;
	private JLabel label1;
	
	static DefaultListModel listModel;
	boolean semaphore = false; //blokowanie dodania tego samego zadania 2 razy
	String array[] = new String[] {"","","","","","","","","","","","","","","","",
									"","","","","","","","","","","","","","","","",
									"","","","","","","","","","","","","","","","",};
	
	
	public int counter = 0;
	public Timer t;
	
	//formatowanie czasu, na wejsciu 1/10 sekundy, na wyjsciu
	//czas sformatowany w MIN:SS.D  MIN- minuty SS- sekundy
	//D- dziesietne czesci sekundy
	public String format(int t){
		String minutki = "";
		String sekundki = "";
		
		int seconds_n_f = t/10;
		int seconds = seconds_n_f%60;
		int one_tenth = t%10;

		String jednadziesiata = String.valueOf(one_tenth); //to samo co one_tenth ale jako string 

		int minutes = seconds_n_f/60;

		if ((seconds)<10) {
			sekundki = String.valueOf(seconds);
			sekundki = "0" + sekundki;
			minutki = String.valueOf(minutes);
		}else{
			sekundki = String.valueOf(seconds);
			minutki = String.valueOf(minutes);
		}
			    
		return minutki + ":" + sekundki + "." + jednadziesiata;
	}
	
	public static void centreWindow(Window frame) {
	    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	    int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
	    int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
	    frame.setLocation(x, y);
	}
	
	public TimeManager(){ //konstruktor klasy g³ównej
		super("Time Manager");
		setResizable(false);
		setLayout(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(380, 320);
		setLocationRelativeTo( null );
		
		bAddTask.setBounds(30, 60, 70, 20);
		add(bAddTask);
		bAddTask.addActionListener(this);
		
		tEnterTask.setBounds(30, 30, 200, 20);
		add(tEnterTask);
				
		bStop.setBounds(30, 120, 70, 20);
		add(bStop);
		bStop.addActionListener(this);
		
		bPause.setBounds(30, 90, 70, 20);
		add(bPause);
		bPause.addActionListener(this);
		
		bExport.setBounds(140, 240, 150, 20);
		add(bExport);
		bExport.addActionListener(this);
		
		lTime.setBounds(240, 30, 100, 20);
		add(lTime);
		
		setJMenuBar(menuBar);
		menuBar.add(menuFile);
		menuFile.addActionListener(this);
		
		menuFile.add(menuAbout);
		menuAbout.addActionListener(this);
		menuFile.add(menuTerminate);
		menuTerminate.addActionListener(this);
		
		listModel = new DefaultListModel();
		
		JList lista = new JList(listModel);
		lista.setBounds(115, 60, 200, 170);
		add(lista);
		
		image1 = new ImageIcon(getClass().getResource("qr.png"));
		label1 = new JLabel(image1);
		
		t = new Timer(100, new TimerListener());
			
		setVisible(true);
	}
	
	
	public static void main(String[] args) {
		new TimeManager();	
	}

	
	private class TimerListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			lTime.setText(format(counter));
			counter++;
		}
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == bAddTask){
			if(tEnterTask.getText().length() < 1){
				JOptionPane.showMessageDialog(null, "Dodaj treœæ zadania!");
			}else if(tEnterTask.getText().length() > 30){
				JOptionPane.showMessageDialog(null,  "Za d³uga treœæ");
			}else{
				t.start();
				semaphore = true;
			}
		}
		
		else if (e.getSource() == bStop){
			t.stop();
			if(semaphore){
				listModel.addElement(tEnterTask.getText() + " " + format(counter));
			}
			counter = 0;
			tEnterTask.setText("");
			semaphore = false;	
					
			listModel.copyInto(array);
		}
		
		else if(e.getSource() == bPause){
			t.stop();
		}
		
		else if(e.getSource() == bExport){
			
			listModel.copyInto(array);
			
			BufferedWriter br = null;
			try {
				br = new BufferedWriter(new FileWriter("output.txt"));
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			StringBuilder sb = new StringBuilder();
			for (String element : array) {
			 sb.append(element);
			 sb.append("\n");
			}

			try {
				br.write(sb.toString());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else if(e.getSource() == menuAbout){
			
			JFrame newFrame = new JFrame();
			newFrame.setSize(300, 300);
			setResizable(false);
			setLayout(new FlowLayout());
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			newFrame.add(label1);
			
			centreWindow(newFrame);
			
			newFrame.setVisible(true);
			
		}else if(e.getSource() == menuTerminate){
			System.exit(0);
		}
	}
}
