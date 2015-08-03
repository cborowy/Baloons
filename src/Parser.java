import java.io.FileReader;
import java.util.Properties;
/**
 * 
 * @author cezary
 * Klasa, ktora odpowiada za komunikacje gry z plikiem konfiguracyjnym.
 */

public class Parser {
	
	private int r;
	private int x;
	private int y;
	private int ni;
	private int startX;
	private int startY;
	
	private String startXs;
	private String startYs;
	private String rs;
	private String xs;
	private String ys;
	private String[] nis = new String[6];

	/**
	 * konstruktor
	 */
	public Parser(){	
		try{
			FileReader reader = new FileReader("/home/cezary/workspace/Baloons/src/config.txt");
			Properties prop = new Properties();
			prop.load(reader);
			rs = prop.getProperty("r");
			xs= prop.getProperty("x");
			ys = prop.getProperty("y");
			startXs = prop.getProperty("startX");
			startYs = prop.getProperty("startY");
			for(int i = 0; i < 6; i++){
				nis[i] = prop.getProperty("level" + i);
			}
			
		}
		
		catch(Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * zwraca promien 
	 * @return
	 */
	
	public int getRadius(){
		r = Integer.parseInt(rs);
		return r;
	}
	/**
	 * zwraca wymiar x
	 * @return
	 */
	public int getXDimension(){
		x = Integer.parseInt(xs);
		return x;
	}
	/**
	 * zwraca wymiar y
	 * @return
	 */
	public int getYDimension(){
		y = Integer.parseInt(ys);
		return y;
	}
	/**
	 * zwraca poczatkowe wymiary kulek
	 * @return
	 */
	public int getStartX(){
		this.startX = Integer.parseInt(startXs);
		return startX;
	}
	/**
	 * 
	 * @return
	 */
	public int getStartY(){
		this.startY = Integer.parseInt(startYs);
		return startY;
	}
	/**
	 * 
	 * @param i
	 * @return
	 */
	public int getColorRange(int i){
		ni = Integer.parseInt(nis[i]);
		return ni;
	}
	
}
