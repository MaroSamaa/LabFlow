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
            UserManager u = new UserManager(frame,monitor);
            u.setVisible(true);
        });

        addRequestBtn.addActionListener((ActionEvent) ->
        {

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
