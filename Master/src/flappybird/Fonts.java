/*
 * Loads .ttf file into a new font
 */
package flappybird;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.ArrayList;

public class Fonts {

	private static ArrayList<Fonts> fontList;
	@SuppressWarnings("unused")
	private static String fontPath;
	
	public Fonts(String fontPath){
		fontList = new ArrayList<>();
		Fonts.fontPath = fontPath;
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try{
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontPath)));
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void addFont(Fonts font){
		fontList.add(font);
	}
}