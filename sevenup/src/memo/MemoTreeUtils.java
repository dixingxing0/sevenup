/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memo;

import entity.Memo;
import java.util.List;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * memo tree 的工具类
 * @author hc360
 */
public class MemoTreeUtils {
        /**
     * 初始化子节点
     * @param m
     * @param tn
     */
    public static void appendChildren(Memo m, DefaultMutableTreeNode tn) {
        List<Memo> children = m.getChildren();
        for (Memo c : children) {
            tn.add(new DefaultMutableTreeNode(c));
        }
    }

    /**
     * 构造根节点
     * @return
     */
    public static TreeNode getRoot() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("memos");

        List<Memo> roots = Memo.getRoots();

        for (Memo m : roots) {
            DefaultMutableTreeNode tn = new DefaultMutableTreeNode(m);
            appendChildren(m, tn);
            root.add(tn);
        }

        return root;
    }
}
