package gui;

import javax.swing.*;

import concurrent.LabMonitor;

import java.awt.*;

/**
 * Frame of the GUI.
 * It starts with a size calculated on the computer's screen size.
 * It has two "cards", one for the main view and one for the settings.
 * @author Visone Sara
 */
public class Frame extends JFrame
{
    //--------INSTANCE VARIABLES--------
    private JPanel parentPanel; 
    private JPanel mainViewPanel;
    private JPanel settingsPanel;

    private CardLayout lm = new CardLayout();

    //--------CONSTRUCTOR--------
    
    /**
     * Constructor for the Frame
     * @param monitor the referenced LabMonitor
     */
    public Frame(LabMonitor monitor)
    {
        super("LabFlow");

        //--------PARENT PANEL--------

        parentPanel = new JPanel();
        parentPanel.setLayout(lm);
        
        settingsPanel = new SettingsPanel(this,monitor);
        mainViewPanel = new MainViewPanel(this,monitor);

        parentPanel.add(mainViewPanel,"MAIN");
        parentPanel.add(settingsPanel, "SETTINGS");

        lm.first(parentPanel);  //  Start on the Main View Panel
        add(parentPanel);

        //---------JFRAME SETTINGS--------

        //  Set starting frame size as 50% of the screens width and 60% of the screens height
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = (int) (screenSize.getWidth() * 0.5);
        int windowHeight = (int) (screenSize.getHeight() * 0.6);
        setSize(windowWidth,windowHeight);

        setLocationRelativeTo(null);                  //  Open the frame at the center of the screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //  End program when frame gets closed
        setVisible(true);
    }

    //  "CARD SWITCH" METHODS

    /**
     * Switch the view to the settings panel
     */
    public void showSettings()
    {
        lm.show(parentPanel, "SETTINGS");
    }

    /**
     * Switch the view to the main view panel
     */
    public void showMainView()
    {
        lm.show(parentPanel, "MAIN");
    }
}
