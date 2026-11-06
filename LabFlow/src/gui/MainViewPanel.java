package gui;

import javax.swing.*;

import concurrent.LabMonitor;

import java.awt.*;

public class MainViewPanel extends JPanel
{
    public MainViewPanel(Frame frame, LabMonitor monitor)
    {
        setLayout(new BorderLayout());

        JPanel centerPanel = new JPanel();  //Panel with the two tables
        JPanel southPanel = new JPanel();   //Panel with EventLog and Controls

        centerPanel.setLayout(new GridLayout(1,2,0, 0));
        southPanel.setLayout(new BorderLayout());

        //--------ASSEMBLY OF THE SUPPORT PANELS--------
        //  Center panel with equipment and requests/user panel
        //centerPanel.add(new EquipmentPanel());
        centerPanel.add(new RequestPanel(frame, monitor));

        //  South panel with event log panel and control panel
        southPanel.add(new EventLogPanel(), BorderLayout.CENTER);
        southPanel.add(new ControlPanel(frame), BorderLayout.SOUTH);

        //--------ASSEMBLY OF THE MAIN VIEW PANEL--------
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }
}
