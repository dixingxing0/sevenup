/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package memo;

import org.jdesktop.application.TaskMonitor;

/**
 *  初始化 TaskMonitor
 * @author hc360
 */
public class TaskMonitorUtils {

    public static void init() {

        // connecting action tasks to status bar via TaskMonitor
        MemoView.taskMonitor = new TaskMonitor(MemoView.getInstantce().getApplication().getContext());
        MemoView.taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!MemoView.getInstantce().getBusyIconTimer().isRunning()) {
                        MemoView.getInstantce().getStatusAnimationLabel().setIcon(MemoView.getInstantce().getBusyIcons()[0]);
                        MemoView.getInstantce().setBusyIconIndex(0);
                        MemoView.getInstantce().getBusyIconTimer().start();
                    }
                    MemoView.getInstantce().getProgressBar().setVisible(true);
                    MemoView.getInstantce().getProgressBar().setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    MemoView.getInstantce().getBusyIconTimer().stop();
                    MemoView.getInstantce().getStatusAnimationLabel().setIcon(MemoView.getInstantce().getIdleIcon());
                    MemoView.getInstantce().getProgressBar().setVisible(false);
                    MemoView.getInstantce().getProgressBar().setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    MemoView.getInstantce().getStatusMessageLabel().setText((text == null) ? "" : text);
                    MemoView.getInstantce().getMessageTimer().restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    MemoView.getInstantce().getProgressBar().setVisible(true);
                    MemoView.getInstantce().getProgressBar().setIndeterminate(false);
                    MemoView.getInstantce().getProgressBar().setValue(value);
                }
            }
        });

    }
}
