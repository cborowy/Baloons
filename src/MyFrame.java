//import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author cezary
 *Klasa, ktora tworzy glowna ramke w grze. Znajduja sie w niej przyciski do wybierania poziomow powodujace rozpoczecie gry na danym poziomie
 * oraz przycisk exit sluzacy do wylaczenia programu.
 */
public class MyFrame extends JFrame implements ActionListener, Runnable {
	
	
/**
 * stworzenie przyciskow
 */
	private JButton level1 = new JButton("LEVEL 1");
	private JButton level2 = new JButton("LEVEL 2");
	private JButton level3 = new JButton("LEVEL 3");
	private JButton level4 = new JButton("LEVEL 4");
	private JButton level5 = new JButton("LEVEL 5");
	private JButton level6 = new JButton("LEVEL 6");
	private JButton exit = new JButton("EXIT");
	
/**
 * stworzenie paneli z przyciskami i panelu z gra
 */
	
	private JPanel panel = new JPanel();
	private MyPanel mp = new MyPanel();

/**
 * liczba zdobytych punktow	
 */
	private int scores;
	
	
	
	/**
	 * konstruktor klasy
	 */
	public MyFrame(){
		super();
		
		this.setSize(600, 600);
		this.setResizable(false);
		this.setLayout(new BorderLayout());
		
		JButton[] buttons = new JButton[7];
		buttons[0] = level1;
		buttons[1] = level2;
		buttons[2] = level3;
		buttons[3] = level4;
		buttons[4] = level5;
		buttons[5] = level6;
		buttons[6] = exit;
		
		
		for(int i = 0; i < 7; i++) {
			
			buttons[i].setAlignmentX(CENTER_ALIGNMENT);
			panel.add(Box.createRigidArea(new Dimension(5, 20)));
			panel.add(buttons[i]);
			buttons[i].addActionListener(this);
		}
		
		this.add(panel, BorderLayout.CENTER);
		this.setVisible(true);
		
		//menuPanel.add(returnToMenuButton);
		
		
		pack();
	}	
/**
 * funkcja run() watku t
 */
	public void run(){
		
			
		
			if(mp != null){
				mp.game();
				if(mp.game = false){
					this.scores = mp.scores;
					mp.setVisible(false);
					mp = null;
					System.out.println(scores);
					//menuPanel.setVisible(false);
					panel.setVisible(true);
				}
			}
			
		
		
	}
/**
 * funkcja, ktora odpowiada za wykonanie akcji gdy klikniemy dany przycisk
 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		Object source = arg0.getSource();
		if(source == level1){
			
			panel.setVisible(false);
			add(mp, BorderLayout.CENTER);
			mp.setColorRange(mp.pr.getColorRange(0));
			mp.generateBaloons();
			mp.setVisible(true);
			setSize(540,650);
			
		}else if(source == level2){
			panel.setVisible(false);
			mp.setColorRange(mp.pr.getColorRange(1));
			mp.generateBaloons();
			add(mp, BorderLayout.CENTER);
			mp.setVisible(true);
			setSize(540,650);
		}	else if(source == level3){
			mp.setColorRange(mp.pr.getColorRange(2));
			mp.generateBaloons();
			panel.setVisible(false);
			add(mp, BorderLayout.CENTER);
			mp.setVisible(true);
			setSize(540,650);		
		}else if(source == level4){
			mp.setColorRange(mp.pr.getColorRange(3));
			mp.generateBaloons();
			panel.setVisible(false);
			add(mp, BorderLayout.CENTER);
			mp.setVisible(true);
			setSize(540,650);
		}else if(source == level5){
			mp.setColorRange(mp.pr.getColorRange(4));
			mp.generateBaloons();
			panel.setVisible(false);
			add(mp, BorderLayout.CENTER);
			mp.setVisible(true);
			setSize(540,650);
		}else if(source == level6){
			mp.setColorRange(mp.pr.getColorRange(5));
			mp.generateBaloons();
			panel.setVisible(false);
			add(mp, BorderLayout.CENTER);
			mp.setVisible(true);
			setSize(540,650);
		}else if(source == exit){
			System.exit(0);
		}
	}
		
}