package gui;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame
{
    private JPanel parentPanel; 

    private JPanel mainViewPanel;
    private JPanel settingsPanel;

    private CardLayout lm = new CardLayout();

    public Frame()
    {
        super("LabFlow");   //Set title of the JFrame to LabFlow

        parentPanel = new JPanel();
        parentPanel.setLayout(lm);

        //setSize(1500,500);     //To set a fixed window starting size
        //setResizable(false);   //To set the window to not be resizable (if the extention of tables continues)
        
        settingsPanel = new SettingsPanel(this);
        mainViewPanel = new MainViewPanel(this);

        parentPanel.add(mainViewPanel,"MAIN");
        parentPanel.add(settingsPanel, "SETTINGS");

        lm.first(parentPanel);
        add(parentPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //End program when frame gets closed
        pack();
        setVisible(true);
    }

    //  "CARD SWITCH" METHODS
    public void showSettings()
    {
        lm.show(parentPanel, "SETTINGS");
    }

    public void showMainView()
    {
        lm.show(parentPanel, "MAIN");
    }
}
