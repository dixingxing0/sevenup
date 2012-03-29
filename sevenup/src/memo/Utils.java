/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memo;

import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 *
 * @author hc360
 */
public class Utils {
    /**
     * 设置logo
     * @param frame
     */
    public static void setLogo(JFrame frame) {
        Image image = Toolkit.getDefaultToolkit().getImage(Utils.class.getResource("logo.png"));
        frame.setIconImage(image);
    }
}
