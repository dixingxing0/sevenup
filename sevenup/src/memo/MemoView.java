/*
 * MemoView.java
 */
package memo;

import entity.Memo;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTree;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

import chrriis.common.UIUtils;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;
import java.awt.CardLayout;
import org.apache.log4j.Logger;
import org.jdesktop.application.Task;

/**
 * The application's main frame.
 */
public class MemoView extends FrameView {

    private static final Logger logger = Logger.getLogger(MemoView.class);
    public static MemoView memoView = null;

    public static MemoView getInstantce() {
        return memoView;
    }

    public MemoView(SingleFrameApplication app) {
        super(app);
        initComponents();

        memoView = this;
        frame = this.getFrame();
        // 初始化taskMonitor
        TaskMonitorUtils.init();
        
        // <editor-fold defaultstate="collapsed" desc="StatusBar">
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
        // </editor-fold>
        
        // <editor-fold defaultstate="collapsed" desc="TaskMonitor">
       // </editor-fold>
        // 设置为不可编辑
        treeMemo.setEditable(false);

        // 初始化tree memo 的根节点
        Task task = new LoadDataTask(getApplication());
        task.run();

        // 添加tree memo 的鼠标事件
        treeMemo.addMouseListener(new MemoTreeListener());
        // 添加打开文件夹事件
        treeMemo.addTreeWillExpandListener(new MemoTreeListener());

        // 初始化菜单
        initMenu();

        // 设置外观L&F
        NativeInterface.open();
        UIUtils.setPreferredLookAndFeel();

        // 设置logo
        Utils.setLogo(frame);

        // 初始化html编辑器
        HtmlEditorUtils.init();

        // 初始化layout
        initLayout();

        // 设置系统托盘
        TrayIocnUtils.initTrayIcon(frame);

        // 初始化jobtable
        JobTableUtils.init();

        switchMemoLayout();
    }

    // <editor-fold defaultstate="collapsed" desc="layout and menu">
    /**
     * 初始化layout 此方法会卡住界面
     */
    private void initLayout() {
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(panelRichText, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addGap(10, 10, 10).addComponent(labelTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(labelTitle).addGap(8, 8, 8).addComponent(panelRichText, javax.swing.GroupLayout.DEFAULT_SIZE, 219, Short.MAX_VALUE)));

    }

    /**
     * 初始化菜单
     */
    private void initMenu() {
        popMenu = new JPopupMenu();
        addItem = new JMenuItem("添加");
        addItem.addActionListener(new MemoTreeListener());
        delItem = new JMenuItem("删除");
        delItem.addActionListener(new MemoTreeListener());
        popMenu.add(addItem);
        popMenu.add(delItem);
    }  // </editor-fold>

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

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MemoApp.getApplication().getMainFrame();
            aboutBox = new MemoAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MemoApp.getApplication().show(aboutBox);
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
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        switchMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(700, 500));
        mainPanel.setLayout(new java.awt.CardLayout());

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setName("jSplitPane1"); // NOI18N
        jSplitPane1.setPreferredSize(new java.awt.Dimension(650, 480));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        treeMemo.setName("treeMemo"); // NOI18N
        treeMemo.setRootVisible(false);
        jScrollPane1.setViewportView(treeMemo);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 420));

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
                .addContainerGap(785, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(labelTitle)
                .addContainerGap(451, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel1);

        mainPanel.add(jSplitPane1, "card1");

        jPanel2.setName("jPanel2"); // NOI18N

        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setName("jButton1"); // NOI18N
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setName("jButton2"); // NOI18N
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "任务名称", "频率", "执行时间", "下次执行时间", "上次执行时间"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setName("jTable1"); // NOI18N
        jScrollPane3.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTable1.columnModel.title0")); // NOI18N
        jTable1.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTable1.columnModel.title1")); // NOI18N
        jTable1.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTable1.columnModel.title2")); // NOI18N
        jTable1.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTable1.columnModel.title3")); // NOI18N
        jTable1.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTable1.columnModel.title4")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(740, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 1213, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE))
        );

        mainPanel.add(jPanel2, "card3");

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
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1213, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1013, Short.MAX_VALUE)
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
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
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    public static JPopupMenu popMenu; //菜单
    JMenuItem addItem;   //各个菜单项
    JMenuItem delItem;
    JPanel panelRichText;
    protected static final String LS = System.getProperty("line.separator");
    private final JFrame frame;
    public static JHTMLEditor htmlEditor;
    public static Memo currentMemo;
    public static TaskMonitor taskMonitor;

    public JTree getTreeMemo() {
        return treeMemo;
    }

    public JLabel getLabelTitle() {
        return labelTitle;
    }

    public JPanel getJPanel1() {
        return jPanel1;
    }

    public JTable getJTable1() {
        return jTable1;
    }

    public JLabel getStatusMessageLabel() {
        return statusMessageLabel;
    }

    public void setPanelRichText(JPanel panelRichText) {
        this.panelRichText = panelRichText;
    }

    public Timer getBusyIconTimer() {
        return busyIconTimer;
    }

    public JProgressBar getProgressBar() {
        return progressBar;
    }

    public JLabel getStatusAnimationLabel() {
        return statusAnimationLabel;
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    public Icon[] getBusyIcons() {
        return busyIcons;
    }

    public void setBusyIconIndex(int busyIconIndex) {
        this.busyIconIndex = busyIconIndex;
    }

    public Icon getIdleIcon() {
        return idleIcon;
    }

    public Timer getMessageTimer() {
        return messageTimer;
    }

}
