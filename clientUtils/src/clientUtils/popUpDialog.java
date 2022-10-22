package clientUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;

public class popUpDialog {
    private static JDialog dialog;
    private JPanel panel;
    public popUpDialog(String message, Runnable onDialogOKClicked) {
        panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel(message));;
        int option = JOptionPane.showConfirmDialog(null, panel, "Winner Found", JOptionPane.DEFAULT_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            onDialogOKClicked.run();
            System.out.println("OK!"); // do something
        }
    }
    public popUpDialog(String message) {
        panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel(message));;
        int option = JOptionPane.showConfirmDialog(null, panel, "Winner Found", JOptionPane.DEFAULT_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            System.out.println("OK!"); // do something
        }
    }

}