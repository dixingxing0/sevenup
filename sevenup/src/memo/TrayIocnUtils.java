/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memo;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 *系统托盘
 *
 * @author hc360
 */
public class TrayIocnUtils {

    private static TrayIcon trayIcon;

    /**
     * 初始化系统托盘
     */
    public static void initTrayIcon(final JFrame frame) {
        Image image = Toolkit.getDefaultToolkit().getImage(TrayIocnUtils.class.getResource("logo.png"));
        PopupMenu popup = new PopupMenu();
        MenuItem openItem = new MenuItem("打开主界面");
        ActionListener listener = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.setVisible(true);
                frame.setExtendedState(Frame.NORMAL);
                SystemTray.getSystemTray().remove(trayIcon);
            }
        };
        openItem.addActionListener(listener);


        popup.add(openItem);

        MenuItem exitItem = new MenuItem("退出");
        ActionListener listener2 = new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        exitItem.addActionListener(listener2);
        popup.add(exitItem);
        //根据image、提示、菜单创建TrayIcon
        trayIcon = new TrayIcon(image, "七喜备忘录", popup);
        //给TrayIcon添加事件监听器
        trayIcon.addActionListener(listener);

        initWindow(frame);
    }

    /**
     * 最小化到托盘
     */
    public static void minimizeToTray() {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(trayIcon);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 重写窗口关闭
     * @param frame
     */
    private static void initWindow(final JFrame frame) {
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            //捕获窗口关闭事件

            @Override
            public void windowClosing(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    frame.setVisible(false);
                    TrayIocnUtils.minimizeToTray();
                } else {
                    System.exit(0);
                }
            }
            //捕获窗口最小化事件

            @Override
            public void windowIconified(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    frame.setVisible(false);
                    TrayIocnUtils.minimizeToTray();
                } else {
                    System.exit(0);
                }
            }
        });

    }
}
