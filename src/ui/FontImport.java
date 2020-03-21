package ui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

public class FontImport {
	
	/**
	 * 
	 * @return Futura字体
	 */
	public static Font FuturaFont() {
        Font font = null;
        File file = new File("font/Futura/Futura.ttf");
        try {
			font = Font.createFont(java.awt.Font.TRUETYPE_FONT, file);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return font;
    }
	
	
	/**
	 * 
	 * @return 方正宋刻本秀楷简字体
	 */
	public static Font fzxkbxkjFont() {
        Font font = null;
        File file = new File("font/fzxkbxkj/fzxkbxkj.TTF");
        try {
			font = Font.createFont(java.awt.Font.TRUETYPE_FONT, file);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return font;
    }
	
	public enum SourceHanSansCN{
		Bold("Bold"),
		ExtraLight("ExtraLight"),
		Heavy("Heavy"),
		Light("Light"),
		Medium("Medium"),
		Normal("Normal"),
		Regular("Regular");
		
		String str;
		
		SourceHanSansCN(String S){
			this.str = S;
		}
		
		public String getStyle() {
			return str;
		}
	}
	
	public static Font SourceHanSansCNFont(SourceHanSansCN E) {
        Font font = null;
        File file = new File("font/SourceHanSansCN/SourceHanSansCN-" + E.getStyle() +".otf");
        try {
			font = Font.createFont(java.awt.Font.TRUETYPE_FONT, file);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return font;
    }
	
	/**
	 * 
	 * @return Didot字体
	 */
	public static Font DidotFont() {
        Font font = null;
        File file = new File("font/Didot/Didot-Bold-1.ttf");
        try {
			font = Font.createFont(Font.TRUETYPE_FONT, file);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return font;
    }
	
}
