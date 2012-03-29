/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memo;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;

public class LoadDataTask extends org.jdesktop.application.Task<Object, Void> {

    LoadDataTask(org.jdesktop.application.Application app) {
        // Runs on the EDT.  Copy GUI state that
        // doInBackground() depends on from parameters
        // to DoCountTask fields, here.
        super(app);
    }

    @Override
    protected Object doInBackground() {
        // Your Task's code here.  This method runs
        // on a background thread, so don't reference
        // the Swing GUI from here.
        MemoView.getInstantce().getStatusMessageLabel().setText("正在加载数据...");
        TreeNode treeNode = MemoTreeUtils.getRoot();
        return treeNode;  // return your result
    }

    @Override
    protected void succeeded(Object result) {
        JTree treeMemo = MemoView.getInstantce().getTreeMemo();
        TreeNode treeNode  = (TreeNode) result ;
        treeMemo.setModel(new MemoTreeModel(treeNode));
        // 设置为可编辑
        treeMemo.setEditable(true);
        MemoView.getInstantce().getStatusMessageLabel().setText("数据已加载！");
        // Runs on the EDT.  Update the GUI based on
        // the result computed by doInBackground().
        }
}
