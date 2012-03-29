/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memo;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * 初始化job table
 * @author hc360
 */
public class JobTableUtils {
    public static void init() {
        final JTable table = MemoView.getInstantce().getJTable1();
        String[] headers = { "序号", "任务名称", "执行频率","执行时间" };

        DefaultTableModel model = new DefaultTableModel() {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.setColumnIdentifiers(headers);

        model.addRow(new Object[]{"1", "job1", "每周","13:31:00"});
        model.addRow(new Object[]{"2", "job2", "每周","13:32:00"});
        table.setModel(model);
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                 if (e.getButton() == MouseEvent.BUTTON1) {// 单击鼠标左键
                    if (e.getClickCount() == 2) {
                        int colummCount = table.getModel().getColumnCount();// 列数
                        for (int i = 0; i < colummCount; i++) {
                            System.out.print(table.getModel().getValueAt(table.getSelectedRow(), i).toString() + "   ");
                        }
                        JobFrame.getInstance().setVisible(true);
                    }
                }
            }

        });

    }
}
