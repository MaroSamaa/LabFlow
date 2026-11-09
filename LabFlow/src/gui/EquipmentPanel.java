package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import concurrent.*;
import java.util.List;

public class EquipmentPanel extends JPanel implements LabListener 
{
    private final static String[] columnNames = {"Type","Code","Status"};

    private LabMonitor monitor;

    private JTable equipmentTable;

    private DefaultTableModel tableModel;

    public EquipmentPanel(Frame frame, LabMonitor monitor) 
    {
        this.monitor = monitor;
        setLayout(new BorderLayout());
        
        //  Center Panel assembly (table with equipment type, code and status)

        // Create the table model and don't allow direct modification 
        tableModel = new DefaultTableModel(columnNames, 0) 
        {
            @Override
            public boolean isCellEditable(int row, int column) 
            {
                return false; 
            }
        };
        
        equipmentTable = new JTable(tableModel);
        JScrollPane scrollTable = new JScrollPane(equipmentTable);  //  Make the table scrollable  

        //  North panel assembly (addEquipmentBtn and panelTitle label)
        JPanel northPanel = new JPanel(new BorderLayout());
        JButton addEquipmentBtn = new JButton("Add equipment");

        //  Open and visualize the EquipmentManager
        addEquipmentBtn.addActionListener((ActionEvent) ->
        {
            EquipmentManager equipManager = new EquipmentManager(frame,this.monitor);
            equipManager.setVisible(true);
        });

        JLabel panelTitle = new JLabel("EQUIPMENT", JLabel.CENTER);
        northPanel.add(panelTitle, BorderLayout.CENTER);
        northPanel.add(addEquipmentBtn, BorderLayout.WEST);

        // EquipmentPanel assembly
        add(scrollTable, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        this.monitor.addLabListener(this);
    }

    /**
     * Service method to load the table with LabMonitor equipment data
     */
    public void updateEquipmentTable() 
    {
        tableModel.setRowCount(0);
        List<Equipment> equipment = monitor.getEquipment();

        for (Equipment e : equipment) 
        {
            Object[] rowData = 
            {
                e.getType(),   // Equipment type
                e.getCode(),   // Equipment code
                e.getStatus()  // Equipment status
            };
            tableModel.addRow(rowData);
        }
    }

    /**
     * Method to update the equipment table when the listener gets notified
     */
    @Override
    public void onLabStateChange()
    {
        //Do updateEquipmentTable whenever the EDT can
        SwingUtilities.invokeLater(() ->
        {
            updateEquipmentTable();
        });
    }
}
