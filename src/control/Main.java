//Github URL:https://github.com/May24th/JiuChess.git

package control;

import java.awt.Color;

import javax.swing.UIManager;

import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) throws Exception {
    	
    	
    	try
        {
    		UIManager.put("RootPane.setupButtonVisible", false);	//取消工具栏
    		
    		BeautyEyeLNFHelper.debug = true;						//输出错误信息
    		BeautyEyeLNFHelper.translucencyAtFrameInactive = false;	//关闭半透明
    		BeautyEyeLNFHelper.commonBackgroundColor = new Color(0xECE2D0);
    		BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;	//设置为性能高的边框模式
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
        }
        catch(Exception e)
        {
        	
        }
    	
    	new MainFrame();
    	
    }
    
    public static final int BOARDSIZE = 8;
    
    

    
    
}
