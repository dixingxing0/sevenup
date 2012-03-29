/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package memo;

import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorAdapter;
import chrriis.dj.nativeswing.swtimpl.components.HTMLEditorSaveEvent;
import chrriis.dj.nativeswing.swtimpl.components.JHTMLEditor;
import entity.Memo;
import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

/**
 *
 * @author hc360
 */
public class HtmlEditorUtils {
    public static void init() {
        final JPanel contentPane = new JPanel(new BorderLayout());
        Map<String, String> optionMap = new HashMap<String, String>();
        MemoView.htmlEditor = new JHTMLEditor(JHTMLEditor.HTMLEditorImplementation.TinyMCE,
                JHTMLEditor.TinyMCEOptions.setOptions(optionMap));
        
        final JHTMLEditor htmlEditor =  MemoView.htmlEditor;
        final Memo currentMemo = MemoView.currentMemo;
        
        htmlEditor.addHTMLEditorListener(new HTMLEditorAdapter() {
            @Override
            public void saveHTML(HTMLEditorSaveEvent e) {
                if(currentMemo != null && currentMemo.getId() != null) {
                    currentMemo.setContent(htmlEditor.getHTMLContent());
                    currentMemo.update();
                    MemoView.getInstantce().getStatusMessageLabel().setText("已保存！");
                }
            }
        });
        contentPane.add(htmlEditor, BorderLayout.CENTER);
        htmlEditor.setHTMLContent("");

        MemoView.getInstantce().setPanelRichText(contentPane);
    }
}
