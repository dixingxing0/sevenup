/*
 * MemoView.java
 */
package memo;

import entity.Memo;
import java.awt.AWTEvent;
import java.beans.PropertyChangeEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorAdapter;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorSaveEvent;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.DesertBlue;
import java.awt.AWTException;
import java.awt.CardLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.AWTEventListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

/**
 * The application's main frame.
 */
public class MemoView extends FrameView implements ActionListener {

    public MemoView(SingleFrameApplication app) {
        super(app);

        initComponents();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        treeModel = new MemoTreeModel(getRoot());

        treeMemo.setModel(treeModel);
        treeMemo.setEditable(true);
        treeMemo.getCellEditor().addCellEditorListener(new CellEditorAction());
//        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
//        renderer.setLeafIcon(new ImageIcon("1.gif"));
//        renderer.setClosedIcon(new ImageIcon("2.gif"));
//        renderer.setOpenIcon(new ImageIcon("3.gif"));
//        //renderer.setBackgroundNonSelectionColor(Color.BLUE);
//        //renderer.setBackgroundSelectionColor(Color.RED);
//        renderer.setBorderSelectionColor(Color.RED);
////
//        tree.setCellRenderer(renderer);

        popMenu = new JPopupMenu();

        addItem = new JMenuItem("添加");

        addItem.addActionListener(this);

        delItem = new JMenuItem("删除");

        delItem.addActionListener(this);





        popMenu.add(addItem);

        popMenu.add(delItem);
        NativeInterface.open();
        UIUtils.setPreferredLookAndFeel();
        panelRichText = (JPanel) createContent();
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelRichText, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(labelTitle).addGap(8, 8, 8).addComponent(panelRichText, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)));

        this.frame = this.getFrame();


        initTrayIcon();
        this.getFrame().setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.getFrame().addWindowListener(new WindowAdapter() {
            //捕获窗口关闭事件

            @Override
            public void windowClosing(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    frame.setVisible(false);
                    minimizeToTray();
                } else {
                    System.exit(0);
                }
            }
            //捕获窗口最小化事件

            @Override
            public void windowIconified(WindowEvent e) {
                if (SystemTray.isSupported()) {
                    frame.setVisible(false);
                    minimizeToTray();
                } else {
                    System.exit(0);
                }
            }
        });


        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane2 = new javax.swing.JScrollPane();
//        treeMemo = new javax.swing.JTree();
//        jPanel1 = new javax.swing.JPanel();
        mainPanel.add(jSplitPane2, "card2");

        PlasticLookAndFeel.setPlasticTheme(new DesertBlue());
       try {
          UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
       } catch (Exception e) {}

        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("logo.png"));
        frame.setIconImage(image);

        treeMemo.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent evt) {
                TreePath path = treeMemo.getPathForLocation(evt.getX(), evt.getY());
                if (path == null) {  //JTree上没有任何项被选中
                    return;
                }

                treeMemo.setSelectionPath(path);
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
                Memo m = (Memo) node.getUserObject();

                htmlEditor.setHTMLContent(m.getContent() != null ? m.getContent() : "");

                labelTitle.setText(m.getName());
                currentMemo = m;
                if (MouseEvent.BUTTON1 == evt.getButton()) {
                } else if (evt.getButton() == MouseEvent.BUTTON3) {
                    popMenu.show(treeMemo, evt.getX(), evt.getY());
                }
            }

        });

    }

    @Action
    public void switchMemoLayout() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.next(mainPanel);
    }

    @Action
    public void switchJobLayout() {
        CardLayout cardLayout = (CardLayout) mainPanel.getLayout();
        cardLayout.next(mainPanel);

    }

    /**
     * 初始化系统托盘
     */
    private void initTrayIcon() {
        Image image = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("logo.png"));
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
        this.trayIcon = new TrayIcon(image, "memo", popup);
        //给TrayIcon添加事件监听器
        this.trayIcon.addActionListener(listener);
    }

    /**
     * 最小化到托盘
     */
    public void minimizeToTray() {
        SystemTray tray = SystemTray.getSystemTray();
        try {
            tray.add(this.trayIcon);
        } catch (AWTException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 初始化子节点
     * @param m
     * @param tn
     */
    private void appendChildren(Memo m, DefaultMutableTreeNode tn) {
        List<Memo> children = m.getChildren();
        for (Memo c : children) {
            tn.add(new DefaultMutableTreeNode(c));
        }
    }

    /**
     * 构造根节点
     * @return
     */
    private TreeNode getRoot() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("memos");

        List<Memo> roots = Memo.getRoots();

        for (Memo m : roots) {
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(m);
            appendChildren(m, tn);
            root.add(tn);
        }

        return root;
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MemoApp.getApplication().getMainFrame();
            aboutBox = new MemoAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MemoApp.getApplication().show(aboutBox);
    }

    public JComponent createContent() {
        final JPanel contentPane = new JPanel(new BorderLayout());
        Map<String, String> optionMap = new HashMap<String, String>();
        optionMap.put("theme_advanced_buttons1", "'bold,italic,underline,strikethrough,sub,sup,|,charmap,|,justifyleft,justifycenter,justifyright,justifyfull,|,hr,removeformat'");
        optionMap.put("theme_advanced_buttons2", "'undo,redo,|,cut,copy,paste,pastetext,pasteword,|,search,replace,|,forecolor,backcolor,bullist,numlist,|,outdent,indent,blockquote,|,table'");
        optionMap.put("theme_advanced_buttons3", "''");
        optionMap.put("theme_advanced_toolbar_location", "'top'");
        optionMap.put("theme_advanced_toolbar_align", "'left'");
        // Language can be configured when language packs are added to the classpath. Language packs can be found here: http://tinymce.moxiecode.com/download_i18n.php
//    optionMap.put("language", "'de'");
        optionMap.put("plugins", "'table,paste,contextmenu'");

        htmlEditor = new JHTMLEditor(JHTMLEditor.HTMLEditorImplementation.TinyMCE,
                JHTMLEditor.TinyMCEOptions.setOptions(optionMap));
        htmlEditor.addHTMLEditorListener(new HTMLEditorAdapter() {

            @Override
            public void saveHTML(HTMLEditorSaveEvent e) {
                if(currentMemo != null) {
                    currentMemo.setContent(htmlEditor.getHTMLContent());
                    currentMemo.update();
                    statusMessageLabel.setText("已保存！");
                } 
//                JOptionPane.showMessageDialog(contentPane, "The data of the HTML editor could be saved anywhere...");
            }
        });

        contentPane.add(htmlEditor, BorderLayout.CENTER);

        htmlEditor.setHTMLContent("");


        return contentPane;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        treeMemo = new javax.swing.JTree();
        jPanel1 = new javax.swing.JPanel();
        labelTitle = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        switchMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.CardLayout());

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        treeMemo.setName("treeMemo"); // NOI18N
        treeMemo.setRootVisible(false);
        treeMemo.addTreeWillExpandListener(new javax.swing.event.TreeWillExpandListener() {
            public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
            }
            public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {
                treeWillExpandHandler(evt);
            }
        });
        jScrollPane1.setViewportView(treeMemo);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel1.setName("jPanel1"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(memo.MemoApp.class).getContext().getResourceMap(MemoView.class);
        labelTitle.setText(resourceMap.getString("labelTitle.text")); // NOI18N
        labelTitle.setName("labelTitle"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(332, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addContainerGap(367, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        mainPanel.add(jSplitPane1, "card1");

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(memo.MemoApp.class).getContext().getActionMap(MemoView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        switchMenu.setText(resourceMap.getString("switchMenu.text")); // NOI18N
        switchMenu.setName("switchMenu"); // NOI18N

        jMenuItem1.setAction(actionMap.get("switchMemoLayout")); // NOI18N
        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        switchMenu.add(jMenuItem1);

        jMenuItem2.setAction(actionMap.get("switchJobLayout")); // NOI18N
        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        switchMenu.add(jMenuItem2);

        menuBar.add(switchMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N
        jMenu1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                saveHandler(evt);
            }
        });
        menuBar.add(jMenu1);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text")); // NOI18N
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 710, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 510, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * 保存
     * @param evt
     */
    private void saveHandler(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_saveHandler
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
        String content = htmlEditor.getHTMLContent();
        if(node == null || content == null) {
            return;
        }
        Memo m = (Memo) node.getUserObject();
        m.setContent(content);
        m.update();
        statusMessageLabel.setText("已成功保存！");
    }//GEN-LAST:event_saveHandler

    private void treeWillExpandHandler(javax.swing.event.TreeExpansionEvent evt)throws javax.swing.tree.ExpandVetoException {//GEN-FIRST:event_treeWillExpandHandler
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode c = (DefaultMutableTreeNode) node.getChildAt(i);
            appendChildren((Memo) c.getUserObject(), c);

        }
    }//GEN-LAST:event_treeWillExpandHandler
    /**
     * 为子节点 初始化其子节点 
     * @param evt
     * @throws javax.swing.tree.ExpandVetoException
     */
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JLabel labelTitle;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JMenu switchMenu;
    private javax.swing.JTree treeMemo;
    // End of variables declaration//GEN-END:variables
    private javax.swing.JSplitPane jSplitPane2;
    private JScrollPane jScrollPane2;
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    MemoTreeModel treeModel;
    JPopupMenu popMenu; //菜单
    JMenuItem addItem;   //各个菜单项
    JMenuItem delItem;
    JPanel panelRichText;
    protected static final String LS = System.getProperty("line.separator");
    private TrayIcon trayIcon;
    private final JFrame frame;
    final static JTextArea htmlTextArea = new JTextArea();
    static JHTMLEditor htmlEditor;

    private static Memo currentMemo;

    public void actionPerformed(ActionEvent e) {

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();  //获得右键选中的节点
        if (e.getSource() == addItem) {
            Memo m = new Memo();
            m.setName("新建文档");
            DefaultMutableTreeNode n = new DefaultMutableTreeNode(m);

            node.add(n);

            treeMemo.expandPath(treeMemo.getSelectionPath());
            treeMemo.updateUI();
            TreeNode[] nodes = treeModel.getPathToRoot(n);
            TreePath path = new TreePath(nodes);
            treeMemo.setSelectionPath(path);
            treeMemo.scrollPathToVisible(path);
            treeMemo.updateUI();



            treeMemo.startEditingAtPath(path);
            labelTitle.setText("");


        } else if (e.getSource() == delItem) {

            int messageType = JOptionPane.INFORMATION_MESSAGE;
            int optionType = JOptionPane.YES_NO_OPTION;

            int result = JOptionPane.showConfirmDialog(jPanel1, "确定要删除吗?", "提示",
                    optionType, messageType);
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
            if(selectedNode.getChildCount() > 0) {
                JOptionPane.showMessageDialog(jPanel1, "有子节点不能删除!");
                return ;
            }
            if (result == JOptionPane.YES_OPTION) {

                
                if (selectedNode != null && selectedNode.getParent() != null) {
                    // 删除指定节点
                    treeModel.removeNodeFromParent(selectedNode);
                    Memo m = (Memo) selectedNode.getUserObject();
                    if (m.getId() != null) {
                        Memo.db.update("delete from memo where id = ?", m.getId());
                    }

                }

            }

        }
    }

    private class CellEditorAction implements CellEditorListener {

        public void editingCanceled(ChangeEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
            Memo m = (Memo) node.getUserObject();
        }

        public void editingStopped(ChangeEvent e) {
        }
    }

    private class MemoTreeModel extends DefaultTreeModel {

        public MemoTreeModel(TreeNode node) {
            super(node);
        }

        @Override
        public void valueForPathChanged(TreePath path, Object newValue) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            Memo m = (Memo) node.getUserObject();
            m.setName(String.valueOf(newValue));

            if (m.getId() != null) {
                m.update();
            } else {
                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
                if (parent.getUserObject().getClass().equals(Memo.class)) {
                    Memo p = (Memo) parent.getUserObject();
                    m.setParentId(p.getId());
                }

                m.insert();
                labelTitle.setText(m.getName());
            }

            nodeChanged(node);
        }
    }
}
