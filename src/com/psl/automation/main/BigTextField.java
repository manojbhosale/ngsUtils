package com.psl.automation.main;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BigTextField {

    public static void main(String[] args) {
        Runnable r = new Runnable() {

           
            public void run() {
                // the GUI as seen by the user (without frame)
                JPanel gui = new JPanel(new FlowLayout(5));
                gui.setBorder(new EmptyBorder(2, 3, 2, 3));

                // Create big text fields & add them to the GUI
                String s = "Hello!";
                JTextField tf1 = new JTextField(s, 1);
                Font bigFont = tf1.getFont().deriveFont(Font.PLAIN, 150f);
                tf1.setFont(bigFont);
                gui.add(tf1);

                JTextField tf2 = new JTextField(s, 2);
                tf2.setFont(bigFont);
                gui.add(tf2);

                JTextField tf3 = new JTextField(s, 3);
                tf3.setFont(bigFont);
                gui.add(tf3);

                gui.setBackground(Color.WHITE);

                JFrame f = new JFrame("Big Text Fields");
                f.add(gui);
                // Ensures JVM closes after frame(s) closed and
                // all non-daemon threads are finished
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // See http://stackoverflow.com/a/7143398/418556 for demo.
                f.setLocationByPlatform(true);

                // ensures the frame is the minimum size it needs to be
                // in order display the components within it
                f.pack();
                // should be done last, to avoid flickering, moving,
                // resizing artifacts.
                f.setVisible(true);
            }
        };
        // Swing GUIs should be created and updated on the EDT
        // http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
        SwingUtilities.invokeLater(r);
    }
}