/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memo;

import entity.Memo;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author hc360
 */
public class MemoTreeModel extends DefaultTreeModel {

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
        }

        nodeChanged(node);
    }
}
