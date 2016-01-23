import javax.swing.*;
import java.awt.*;

/**
 * Created by Grady on 2015.12.17.
 */
public class Main {

    public static void main(String[] args){



        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();

            mainFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
            mainFrame.setVisible(true);
        });


    }
}
