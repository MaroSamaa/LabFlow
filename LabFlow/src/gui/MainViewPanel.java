package gui;

import javax.swing.*;

import concurrent.LabMonitor;

import java.awt.*;

/**
 * Panel with the main view of the program.
 * From here the user can see the EventLogPanel, RequestPanel, 
 * EquipmentPanel and ControlPanel.
 * @author Visone Sara
 */ 
public class MainViewPanel extends JPanel
{
    //--------CONSTRUCTOR--------

    /**
     * Constructor method for the MainViewPanel
     * @param frame the parent frame
     * @param monitor the referenced LabMonitor
     */
    public MainViewPanel(Frame frame, LabMonitor monitor)
    {
        setLayout(new BorderLayout());

        //--------INITIALIZATION OF SUPPORT PANELS--------
        JPanel centerPanel = new JPanel(new GridLayout(1,2,10, 0)); //  Panel with the two tables
        JPanel southPanel = new JPanel(new BorderLayout());                               //  Panel with EventLog and Controls

        //--------ASSEMBLY OF THE SUPPORT PANELS--------
        
        //  Center panel with equipment and requests / user panel
        centerPanel.add(new EquipmentPanel(frame,monitor));
        centerPanel.add(new RequestPanel(frame, monitor));

        //  South panel with event log panel and control panel
        southPanel.add(new EventLogPanel(monitor), BorderLayout.CENTER);
        southPanel.add(new ControlPanel(frame,monitor), BorderLayout.SOUTH);

        //  Set the southPanel to be 50% of the starting window size
        int southPanelHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() * 0.6 * 0.5);
        southPanel.setPreferredSize(new Dimension(0,southPanelHeight));

        //--------ASSEMBLY OF THE MAIN VIEW PANEL--------
        
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }
}
