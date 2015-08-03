import java.awt.EventQueue;
import javax.swing.JFrame;

/**
 * 
 * @author cezary
 *Klasa glowna z funkcja main.
 *Tworzona jest w niej ramka frame (obiekt klasy MyFrame).
 *Tworzony jest w niej nowy watek.
 */
public class Baloons {
	public static void main(String[] args){	
		MyFrame frame = new MyFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setTitle("Baloons");	
		frame.setResizable(true);
		Thread t = new Thread(frame);
		t.start();
	}
}