package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

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

        //  West Panel Assembly (Simulation Parameters)
        JPanel parametersPanel = new JPanel();
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));

        //  Internal margin and centered title
        TitledBorder parametersBorder = new TitledBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20), "Simulation Parameters");
        parametersBorder.setTitleJustification(TitledBorder.CENTER);
        parametersPanel.setBorder(parametersBorder);  

        // Work time parameter
        JLabel workTimeLabel = new JLabel("Work Time (ms):");
        workTimeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        workTimeTxt = new JTextField(10);
        workTimeTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        workTimeTxt.setMaximumSize(workTimeTxt.getPreferredSize().getSize()); // Don't allow for vertical stretching
        workTimeTxt.setText(String.valueOf(RequestTask.getWorkTime())); 

        // Work time intervall parameter
        JLabel workTimeIntervallLabel = new JLabel("Work Time Intervall (ms)");
        workTimeIntervallLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        workTimeIntervallTxt = new JTextField(10);
        workTimeIntervallTxt.setAlignmentX(Component.LEFT_ALIGNMENT);
        workTimeIntervallTxt.setMaximumSize(workTimeIntervallTxt.getPreferredSize().getSize());
        workTimeIntervallTxt.setText(String.valueOf(RequestTask.getWorkTimeIntervall()));   

        // ApllyBtn
        JButton applyBtn = new JButton("APPLY");
        applyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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
                JOptionPane.showMessageDialog(this, "ERROR: Make shure to input integer values", "Number Format Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Input number is negative",JOptionPane.ERROR_MESSAGE);
            }
        });

        parametersPanel.add(workTimeLabel);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 5)));  //Separator
        parametersPanel.add(workTimeTxt);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        parametersPanel.add(workTimeIntervallLabel);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        parametersPanel.add(workTimeIntervallTxt);
        
        parametersPanel.add(Box.createVerticalGlue()); //   Push down the applyB
        parametersPanel.add(applyBtn);

        //  North panel assembly 
        //  Tab name 
        JLabel tabLabel = new JLabel(TAB_NAME);
        tabLabel.setHorizontalAlignment(JLabel.CENTER);
        tabLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(backBtn,BorderLayout.EAST);
        northPanel.add(tabLabel,BorderLayout.CENTER);

        //  Center Panel Assembly (Credits)
        JPanel creditPanel = new JPanel();
        creditPanel.setLayout(new BoxLayout(creditPanel, BoxLayout.Y_AXIS));

        JLabel authorLabel = new JLabel("<html><center>Authors:<br>Marini Enrico<br>Visone Sara</center></html>");    
        
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
        
        //  Setting the horizontal center allignments
        logoLabel.setHorizontalAlignment(JLabel.CENTER);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        authorLabel.setHorizontalAlignment(JLabel.CENTER);
        authorLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        // Assembly of the center panel with flexible spacers
        creditPanel.add(Box.createVerticalGlue()); // push to vertical center
        creditPanel.add(logoLabel);
        creditPanel.add(Box.createRigidArea(new Dimension(0, 15))); // 15px from logo to authors text
        creditPanel.add(authorLabel);
        creditPanel.add(Box.createVerticalGlue()); // Balance the lower vertical center space

        //  Assembly of the SettingsPanel
        add(northPanel, BorderLayout.NORTH);
        add(creditPanel, BorderLayout.CENTER);
        add(parametersPanel, BorderLayout.WEST);
        
    }
}


