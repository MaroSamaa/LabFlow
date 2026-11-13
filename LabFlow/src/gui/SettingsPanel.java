package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import concurrent.RequestTask;
import concurrent.LabMonitor;

import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * Panel to show the user the program's credits and allow
 * for simulation parameter's editing
 * @author Visone Sara
 */
public class SettingsPanel extends JPanel
{
    //--------MESSAGES--------
    
    private static final String MSG_PARAMETERS_UPDATED = "Simulation parameters have been successfully updated";
    private static final String MSG_INPUT_VALUES_ERROR = "ERROR: Make sure to input integer values";
    private static final String MSG_DEFAULT_PARAMETERS_RESTORED = "Simulation parameters defaults have been restored";

    //--------CONSTANTS--------
    private static final String TAB_NAME = "Settings and credits";

    /**
     * Logo size constant
     */
    private static final int LOGO_SIZE = 300;

    /**
     * Button size constant
     */
    private static final int BTN_SIZE = 30;

    /**
     * Button dimension constant
     */
    private static final Dimension BTN_DIMENSION = new Dimension(BTN_SIZE,BTN_SIZE);

    //--------CONSTRUCTOR--------

    /**
     * Constructor method for the SettingsPanel
     * @param frame the parent frame
     * @param monitor the referenced LabMonitor
     */
    public SettingsPanel(Frame frame, LabMonitor monitor)
    {
        setLayout(new BorderLayout());
           
        //--------NORTH PANEL(Back button and tab title)--------

        //----Back Button----
        JButton backBtn = new JButton();

        //  Set Image for backBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/backIcon.png"));
            Image settingsIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            backBtn.setIcon(new ImageIcon(settingsIcon));
            backBtn.setPreferredSize(BTN_DIMENSION);
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
 
        //----Tab Name----
        JLabel tabLabel = new JLabel(TAB_NAME);
        tabLabel.setHorizontalAlignment(JLabel.CENTER);
        tabLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(backBtn,BorderLayout.EAST);
        northPanel.add(tabLabel,BorderLayout.CENTER);

        //--------CENTER PANEL(SETTINGS AND CREDITS)--------

        //----Parameters Panel----
        JPanel parametersPanel = new JPanel();
        parametersPanel.setLayout(new BoxLayout(parametersPanel, BoxLayout.Y_AXIS));

        //  Internal margin 
        parametersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  

        //  Work time parameter
        JLabel workTimeLabel = new JLabel("Work Time (ms):");
        workTimeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);    //  Center the label in the panel
        JTextField workTimeTxt = new JTextField(10);
        workTimeTxt.setAlignmentX(Component.CENTER_ALIGNMENT);      //  Center the text field in the panel
        workTimeTxt.setHorizontalAlignment(JTextField.CENTER);      //  Center the text in the text field
        workTimeTxt.setMaximumSize(workTimeTxt.getPreferredSize().getSize());   //  Don't allow for vertical stretching
        workTimeTxt.setText(String.valueOf(RequestTask.getWorkTime()));         //  Set the initial text to the value of the RequestTask's workTime

        //  Work time intervall parameter
        JLabel workTimeIntervallLabel = new JLabel("Work Time Intervall (ms)");
        workTimeIntervallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField workTimeIntervallTxt = new JTextField(10);
        workTimeIntervallTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        workTimeIntervallTxt.setHorizontalAlignment(JTextField.CENTER);
        workTimeIntervallTxt.setMaximumSize(workTimeIntervallTxt.getPreferredSize().getSize());
        workTimeIntervallTxt.setText(String.valueOf(RequestTask.getWorkTimeIntervall()));   

        //  Max Equipment Parameter
        JLabel maxEquipmentLabel = new JLabel("Max Equipment");
        maxEquipmentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JTextField maxEquipmentTxt = new JTextField(10);
        maxEquipmentTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
        maxEquipmentTxt.setHorizontalAlignment(JTextField.CENTER);
        maxEquipmentTxt.setMaximumSize(maxEquipmentTxt.getPreferredSize().getSize());
        maxEquipmentTxt.setText(String.valueOf(monitor.getMaxEquip()));

        // Apply Button
        JButton applyBtn = new JButton("APPLY");
        
        applyBtn.addActionListener((ActionEvent) -> 
        {
            try 
            {
                // Read text field data and convert them as integers
                int newWorkTime = Integer.parseInt(workTimeTxt.getText());
                int newWorkTimeIntervall = Integer.parseInt(workTimeIntervallTxt.getText());
                int newMaxEquip = Integer.parseInt(maxEquipmentTxt.getText());
                
                //  Set the values read
                RequestTask.setWorkTime(newWorkTime);
                RequestTask.setWorkTimeIntervall(newWorkTimeIntervall);
                monitor.setMaxEquip(newMaxEquip);

                JOptionPane.showMessageDialog(this, MSG_PARAMETERS_UPDATED, "Parameters Apllied", JOptionPane.INFORMATION_MESSAGE);
            } 
            catch(NumberFormatException e)
            {
                JOptionPane.showMessageDialog(this, MSG_INPUT_VALUES_ERROR, "Number Format Error", JOptionPane.ERROR_MESSAGE);
            }
            catch(IllegalArgumentException e)
            {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Input number not valid",JOptionPane.ERROR_MESSAGE);
            }
        });

        //  Restore Default Button
        JButton restoreDefaultBtn = new JButton("RESTORE DEFAULT");

        restoreDefaultBtn.addActionListener((ActionEvent) ->
        {
            //  Set the values to their defaul values
            RequestTask.setWorkTime(RequestTask.DEFAULT_WORK_TIME);
            RequestTask.setWorkTimeIntervall(RequestTask.DEFAULT_WORK_TIME_INTERVALL);
            monitor.setMaxEquip(LabMonitor.DEFAULT_MAX_EQUIP);

            //  Update text in the textfields to reflect the changes
            workTimeTxt.setText(String.valueOf(RequestTask.getWorkTime()));
            workTimeIntervallTxt.setText(String.valueOf(RequestTask.getWorkTimeIntervall()));
            maxEquipmentTxt.setText(String.valueOf(monitor.getMaxEquip()));

            JOptionPane.showMessageDialog(this, MSG_DEFAULT_PARAMETERS_RESTORED,"Defaul Parameters Applied", JOptionPane.INFORMATION_MESSAGE);
        });

        JPanel parametersBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        parametersBtnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        parametersBtnPanel.add(restoreDefaultBtn);
        parametersBtnPanel.add(applyBtn);

        JLabel parametersPanelTitle = new JLabel("Simulation Parameters");
        parametersPanelTitle.setFont(parametersPanelTitle.getFont().deriveFont(Font.BOLD));
        parametersPanelTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        //  Assembly of the parameters panel
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
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        parametersPanel.add(maxEquipmentLabel);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 3)));

        parametersPanel.add(maxEquipmentTxt);
        parametersPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        parametersPanel.add(parametersBtnPanel);

        parametersPanel.add(Box.createVerticalGlue()); //   "Spring on the panel floor"
        
        //----Credit Panel----
        JPanel creditPanel = new JPanel();
        creditPanel.setLayout(new BoxLayout(creditPanel, BoxLayout.Y_AXIS));

        // Credits labels
        JLabel authorsTitleLabel = new JLabel("Authors");
        authorsTitleLabel.setFont(authorsTitleLabel.getFont().deriveFont(Font.BOLD));
        authorsTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel groupLabel = new JLabel("The Beauty And The Beast");
        groupLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contributorsLabel = new JLabel("Contributors");
        contributorsLabel.setFont(contributorsLabel.getFont().deriveFont(Font.BOLD));
        contributorsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contributor1Label = new JLabel("Marini Enrico");
        contributor1Label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel contributor2Label = new JLabel("Visone Sara");
        contributor2Label.setAlignmentX(Component.CENTER_ALIGNMENT);   
        
        //  Logo label 
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
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
        // Assembly of the credit panel
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

        //--------ASSEMBLY OF THE SETTINGS PANEL--------
        add(northPanel, BorderLayout.NORTH);
        add(verticalWrapper, BorderLayout.CENTER);
    }
}


