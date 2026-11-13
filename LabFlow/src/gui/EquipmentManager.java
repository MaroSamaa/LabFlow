package gui;

import javax.swing.*;
import java.awt.*;
import concurrent.*;
import concurrent.exceptions.DuplicateEquipmentCodeException;
import concurrent.exceptions.EquipmentStorageFullException;

/**
 * Dialog that let's the user manage the LabMonitor's equipment 
 * @author Visone Sara
 */
public class EquipmentManager extends JDialog 
{
    /**
     * Constructor method for the EquipmentManager
     * @param parent the parent frame
     * @param monitor the referenced LabMonitor
     */
    public EquipmentManager(Frame parent, LabMonitor monitor) 
    {
        super(parent, "Equipment Manager", true); //Modal = true to block the access to the frame below 

        setSize(380, 220);
        setLocationRelativeTo(parent);  //  Spawn where the frame is
        setLayout(new BorderLayout(10, 10));

        //  Center Panel (textfield inputs)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JTextField newEquipmentType = new JTextField(20);
        JTextField newEquipmentCode = new JTextField(20);
        
        inputPanel.add(new JLabel("New Equipment Type:"));
        inputPanel.add(newEquipmentType);
        inputPanel.add(new JLabel("New Equipment Code:"));
        inputPanel.add(newEquipmentCode);
        
        //  South Panel (addEquipmentBtn)
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addEquipmentBtn = new JButton("ADD");

        // addEquipmentBtn action listener
        addEquipmentBtn.addActionListener((ActionEvent) -> 
        {
            String equipmentType = newEquipmentType.getText().trim();
            String equipmentCode = newEquipmentCode.getText().trim();
            
            if (equipmentCode.isEmpty() || equipmentType.isEmpty()) //  If either of the two textfields was left empty
            {
                //Warning popup if either of the two textfields was left empty
                JOptionPane.showMessageDialog(this, "Input all of the necessary data", "Input Warning",JOptionPane.WARNING_MESSAGE);
                return;
            }

            try 
            {
                Equipment newEquipment = new Equipment(equipmentType, equipmentCode);
                monitor.addEquipment(newEquipment);

                // Confirmation popup and dialog closure
                JOptionPane.showMessageDialog(this, newEquipment.getCode()+ " has been added!", "New Equipment Confirmation", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the confirmation popup and the equipment manager

            } 
            catch (EquipmentStorageFullException e) {JOptionPane.showMessageDialog(this, e.getMessage(), "Full Storage Error", JOptionPane.ERROR_MESSAGE);}
            catch (DuplicateEquipmentCodeException e){JOptionPane.showMessageDialog(this, e.getMessage(), "Duplicate Equipment Code Error", JOptionPane.ERROR_MESSAGE);}
        });

        southPanel.add(addEquipmentBtn);

        //  EquipmentManager assembly
        add(inputPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }
}


