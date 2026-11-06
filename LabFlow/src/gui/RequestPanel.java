package gui;

import javax.swing.*;

import concurrent.LabMonitor;

import java.awt.*;

public class RequestPanel extends JPanel
{
    public RequestPanel(Frame frame, LabMonitor monitor)
    {
        setLayout(new BorderLayout());

        JButton userBtn = new JButton("User");
        JButton addRequestBtn = new JButton("Add request");

        JPanel centerPanel = new JPanel();

        userBtn.addActionListener((ActionEvent) ->
        {
            UserManager userManager = new UserManager(frame,monitor);
            userManager.setVisible(true);
        });

        addRequestBtn.addActionListener((ActionEvent) ->
        {
            RequestManager reqManager = new RequestManager(frame,monitor);
            reqManager.setVisible(true);
        });


        //  Assembly of the northPanel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BorderLayout());
        northPanel.add(userBtn, BorderLayout.WEST);
        northPanel.add(addRequestBtn, BorderLayout.EAST);



        //  Assembly of the request panel
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
