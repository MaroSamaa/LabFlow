package gui;

import javax.swing.*;

import concurrent.LabMonitor;

import java.awt.*;

public class Frame extends JFrame
{
    private JPanel parentPanel; 

    private JPanel mainViewPanel;
    private JPanel settingsPanel;

    private CardLayout lm = new CardLayout();

    public Frame(LabMonitor monitor)
    {
        super("LabFlow");   //Set title of the JFrame to LabFlow

        parentPanel = new JPanel();
        parentPanel.setLayout(lm);

        //setSize(1500,500);     //To set a fixed window starting size
        //setResizable(false);   //To set the window to not be resizable (if the extention of tables continues)
        
        settingsPanel = new SettingsPanel(this,monitor);
        mainViewPanel = new MainViewPanel(this,monitor);

        parentPanel.add(mainViewPanel,"MAIN");
        parentPanel.add(settingsPanel, "SETTINGS");

        lm.first(parentPanel);
        add(parentPanel);

        //  Set starting frame size as 60% of the screens width and 50% of the screens height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screenSize.getWidth() * 0.6);
        int windowHeight = (int) (screenSize.getHeight() * 0.5);
        setSize(windowWidth,windowHeight);

        //  Open the frame at the center of the screen
        setLocationRelativeTo(null);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   //End program when frame gets closed
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
