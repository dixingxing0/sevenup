/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memo;

import entity.Memo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * 定义 memo tree 的所有事件
 * @author hc360
 */
public class MemoTreeListener implements ActionListener, MouseListener, TreeWillExpandListener {

    public void actionPerformed(ActionEvent e) {
        JTree treeMemo = MemoView.getInstantce().getTreeMemo();
        MemoTreeModel treeModel = (MemoTreeModel) treeMemo.getModel();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();  //获得右键选中的节点
        if (e.getSource() == MemoView.getInstantce().addItem) {
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

            if (node.getUserObject().getClass().equals(Memo.class)) {
                Memo p = (Memo) node.getUserObject();
                m.setParentId(p.getId());
                // 直接插入到数据库中
                m.insert();
                MemoView.currentMemo = m;
                MemoView.getInstantce().getLabelTitle().setText(m.getName());
                MemoView.htmlEditor.setHTMLContent("");
            }


        } else if (e.getSource() == MemoView.getInstantce().delItem) {

            int messageType = JOptionPane.INFORMATION_MESSAGE;
            int optionType = JOptionPane.YES_NO_OPTION;

            int result = JOptionPane.showConfirmDialog(MemoView.getInstantce().getJPanel1(), "确定要删除吗?", "提示",
                    optionType, messageType);
            DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
            if (selectedNode.getChildCount() > 0) {
                JOptionPane.showMessageDialog(MemoView.getInstantce().getJPanel1(), "有子节点不能删除!");
                return;
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


    //////////////////MouseListener/////////////////////
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent evt) {
        JTree treeMemo = MemoView.getInstantce().getTreeMemo();
        TreePath path = treeMemo.getPathForLocation(evt.getX(), evt.getY());
        if (path == null) {  //JTree上没有任何项被选中
            return;
        }

        treeMemo.setSelectionPath(path);
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
        Memo m = (Memo) node.getUserObject();

        MemoView.getInstantce().getLabelTitle().setText(m.getName());

        MemoView.currentMemo = m;
        //首次点击左侧树 初始化html编辑器
        if (MemoView.htmlEditor == null) {
            HtmlEditorUtils.init();
        }
        MemoView.htmlEditor.setHTMLContent(m.getContent() != null ? m.getContent() : "");


        if (MouseEvent.BUTTON1 == evt.getButton()) {
        } else if (evt.getButton() == MouseEvent.BUTTON3) {
            MemoView.popMenu.show(treeMemo, evt.getX(), evt.getY());
        }
    }

    public void mouseReleased(MouseEvent e) {
    }

    //////////////////TreeWillExpandListener///////////////
    public void treeWillCollapse(javax.swing.event.TreeExpansionEvent evt) throws javax.swing.tree.ExpandVetoException {
    }

    public void treeWillExpand(javax.swing.event.TreeExpansionEvent evt) throws javax.swing.tree.ExpandVetoException {
        JTree treeMemo = MemoView.getInstantce().getTreeMemo();
        treeMemo.setSelectionPath(evt.getPath());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeMemo.getLastSelectedPathComponent();
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode c = (DefaultMutableTreeNode) node.getChildAt(i);
            MemoTreeUtils.appendChildren((Memo) c.getUserObject(), c);

        }
    }
}
