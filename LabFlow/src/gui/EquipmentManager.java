package gui;

import javax.swing.*;
import java.awt.*;
import concurrent.*;

public class EquipmentManager extends JDialog 
{
    
    public EquipmentManager(Frame parent, LabMonitor monitor) 
    {
        super(parent, "Equipment Manager", true); //Modal = true to block the access to the frame below 

        setSize(380, 220);
        setLocationRelativeTo(parent);
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

        // --- EVENTO DEL PULSANTE ---
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

            } catch (IllegalStateException e) {JOptionPane.showMessageDialog(this, e.getMessage(), "Full Storage error", JOptionPane.ERROR_MESSAGE);}
        });

        southPanel.add(addEquipmentBtn);

        //  EquipmentManager assembly
        add(inputPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }
}


