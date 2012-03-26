/*
 * MemoApp.java
 */

package memo;

import java.util.EventObject;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class MemoApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        this.addExitListener(new ExitListener() {

            public boolean canExit(EventObject arg0) {
                return false;
            }

            public void willExit(EventObject arg0) {
            }
        });
        show(new MemoView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of MemoApp
     */
    public static MemoApp getApplication() {
        return Application.getInstance(MemoApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(MemoApp.class, args);
    }
}
