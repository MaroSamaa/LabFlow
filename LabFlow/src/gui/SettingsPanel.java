package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import concurrent.RequestTask;

import javax.imageio.ImageIO;
import java.io.IOException;

public class SettingsPanel extends JPanel
{
    private static final String TAB_NAME = "Settings and credits";
    private static final int LOGO_SIZE = 300;
    private static final int BTN_DIMENTION = 30;

    private JTextField workTimeTxt;
    private JTextField workTimeIntervallTxt;

    public SettingsPanel(Frame frame)
    {
        setLayout(new BorderLayout());
           
        //  Back button implementation 
        JButton backBtn = new JButton();

         //  Set Image for backBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/backIcon.png"));
            Image settingsIcon = temp.getScaledInstance(BTN_DIMENTION, BTN_DIMENTION, Image.SCALE_SMOOTH);
            backBtn.setIcon(new ImageIcon(settingsIcon));
            backBtn.setPreferredSize(new Dimension(BTN_DIMENTION,BTN_DIMENTION));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorder(null);
        backBtn.setContentAreaFilled(false);    // Remove java swing "standard" bkg
        backBtn.setFocusPainted(false);         // Remove java swing "hover" border
        backBtn.setBorderPainted(false);        // Remove "excess" borders

        //  On back btn push "go back" and visualize the main view panel
        backBtn.addActionListener((ActionEvent e) ->
        {
            frame.showMainView();
        });

        //  North panel assembly 
        //  Tab name 
        JLabel tabLabel = new JLabel(TAB_NAME);
        tabLabel.setHorizontalAlignment(JLabel.CENTER);
        tabLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(backBtn,BorderLayout.EAST);
        northPanel.add(tabLabel,BorderLayout.CENTER);

        //  Center panel assembly

        //  Parameters Panel
        JPanel parametersPanel = new JPanel();
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));

        //  Internal margin 
        parametersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  

        // Work time parameter
        JLabel workTimeLabel = new JLabel("Work Time (ms):");
        workTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        workTimeTxt = new JTextField(10);
        workTimeTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        workTimeTxt.setMaximumSize(workTimeTxt.getPreferredSize().getSize()); // Don't allow for vertical stretching
        workTimeTxt.setText(String.valueOf(RequestTask.getWorkTime())); 

        // Work time intervall parameter
        JLabel workTimeIntervallLabel = new JLabel("Work Time Intervall (ms)");
        workTimeIntervallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        workTimeIntervallTxt = new JTextField(10);
        workTimeIntervallTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        workTimeIntervallTxt.setMaximumSize(workTimeIntervallTxt.getPreferredSize().getSize());
        workTimeIntervallTxt.setText(String.valueOf(RequestTask.getWorkTimeIntervall()));   

        // ApllyBtn
        JButton applyBtn = new JButton("APPLY");
        
        applyBtn.addActionListener((ActionEvent) -> 
        {
            try 
            {
                // Read text field data and convert them as integers
                int newWorkTime = Integer.parseInt(workTimeTxt.getText());
                int newWorkTimeIntervall = Integer.parseInt(workTimeIntervallTxt.getText());
                
                //  Set the values read
                RequestTask.setWorkTime(newWorkTime);
                RequestTask.setWorkTimeIntervall(newWorkTimeIntervall);

                JOptionPane.showMessageDialog(this, "Simulation parameters have been successfully updated", "Parameters Apllied", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, "ERROR: Make sure to input integer values", "Number Format Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Input number is negative",JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton restoreDefaultBtn = new JButton("RESTORE DEFAULT");

        restoreDefaultBtn.addActionListener((ActionEvent) ->
        {
            RequestTask.setWorkTime(RequestTask.DEFAULT_WORK_TIME);
            RequestTask.setWorkTimeIntervall(RequestTask.DEFAULT_WORK_TIME_INTERVALL);
            workTimeTxt.setText(String.valueOf(RequestTask.getWorkTime()));
            workTimeIntervallLabel.setText(String.valueOf(RequestTask.getWorkTimeIntervall()));

            JOptionPane.showMessageDialog(this, "Simulation parameters defaults have been restored","Defaul Parameters Applied", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel parametersBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        parametersBtnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parametersBtnPanel.add(restoreDefaultBtn);
        parametersBtnPanel.add(applyBtn);

        JLabel parametersPanelTitle = new JLabel("Simulation Parameters");
        parametersPanelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        parametersPanel.add(Box.createVerticalGlue());  //  "Spring on the panel ceiling"
        parametersPanel.add(parametersPanelTitle);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        parametersPanel.add(workTimeLabel);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 3)));  //  Vertical spacer

        parametersPanel.add(workTimeTxt);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        parametersPanel.add(workTimeIntervallLabel);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        parametersPanel.add(workTimeIntervallTxt);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        parametersPanel.add(parametersBtnPanel);

        parametersPanel.add(Box.createVerticalGlue()); //   "Spring on the panel floor"
        
        //  Credit Panel
        JPanel creditPanel = new JPanel();
        creditPanel.setLayout(new BoxLayout(creditPanel, BoxLayout.Y_AXIS));

        // Credits labels
        JLabel authorsTitleLabel = new JLabel("Authors");
        authorsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel groupLabel = new JLabel("The Beauty And The Beast");

        JLabel contributorsLabel = new JLabel("Contributors");

        JLabel contributor1Label = new JLabel("Marini Enrico");
        contributor1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contributor2Label = new JLabel("Visone Sara");
        contributor2Label.setAlignmentX(Component.CENTER_ALIGNMENT);   
        
        //  Logo label implementation
        JLabel logoLabel = new JLabel();

        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/LabFlowLogo.png"));
            Image logoIcon = temp.getScaledInstance(LOGO_SIZE, LOGO_SIZE, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(logoIcon));
            logoLabel.setPreferredSize(new Dimension(LOGO_SIZE, LOGO_SIZE));
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        logoLabel.setBorder(null);
            
        // Assembly of the center panel with flexible spacers
        creditPanel.add(Box.createVerticalGlue()); // push to vertical center
        creditPanel.add(logoLabel);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 15))); // 15px from logo to authors text

        creditPanel.add(authorsTitleLabel);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 5))); 

        creditPanel.add(groupLabel);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 5))); 

        creditPanel.add(contributorsLabel);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 5))); 

        creditPanel.add(contributor1Label);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 3))); 

        creditPanel.add(contributor2Label);
        creditPanel.add(Box.createVerticalGlue()); // Balance the lower vertical center space

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 60, 0)); // 60px of distance between parameters and credits panels
        centerPanel.add(parametersPanel);
        centerPanel.add(creditPanel);
        
        // Vertical wrapper to center everything vertically
        JPanel verticalWrapper = new JPanel();
        verticalWrapper.setLayout(new BoxLayout(verticalWrapper, BoxLayout.Y_AXIS));
        verticalWrapper.add(Box.createVerticalGlue());
        verticalWrapper.add(centerPanel);
        verticalWrapper.add(Box.createVerticalGlue());

        //  Assembly of the SettingsPanel
        add(northPanel, BorderLayout.NORTH);
        add(verticalWrapper, BorderLayout.CENTER);
    }
}


