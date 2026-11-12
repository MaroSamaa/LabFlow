package gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

import concurrent.*;
import java.util.List;

public class EquipmentPanel extends JPanel implements LabListener 
{
    private final static String[] columnNames = {"","Type","Code","Status"};
    private final static int BTN_SIZE = 35;
    private final static Dimension BTN_DIMENSION = new Dimension(BTN_SIZE,BTN_SIZE);

    private LabMonitor monitor;

    private JTable equipmentTable;
    private JLabel equipmentCounter;

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

        // Narrow first column (status indicator) setting
        equipmentTable.getColumnModel().getColumn(0).setPreferredWidth(25);
        equipmentTable.getColumnModel().getColumn(0).setMaxWidth(25);;
        equipmentTable.getColumnModel().getColumn(0).setMinWidth(25);
        equipmentTable.getColumnModel().getColumn(0).setResizable(false);
        
        JScrollPane scrollTable = new JScrollPane(equipmentTable);  //  Make the table scrollable  

        //  North panel assembly (addEquipmentBtn and panelTitle label)
        JPanel northPanel = new JPanel(new BorderLayout());
        JButton addEquipmentBtn = new JButton();

        //  Set Image for addEquipmentBtn
        try 
        {
            Image temp = ImageIO.read(getClass().getResource("/images/equipmentIcon.png"));
            Image equipmentIcon = temp.getScaledInstance(BTN_SIZE, BTN_SIZE, Image.SCALE_SMOOTH);
            addEquipmentBtn.setIcon(new ImageIcon(equipmentIcon));
            addEquipmentBtn.setMinimumSize(BTN_DIMENSION);
            addEquipmentBtn.setMaximumSize(BTN_DIMENSION);
        }
        catch (IOException e) {System.out.println(e.getStackTrace());}
        addEquipmentBtn.setMargin(new Insets(0, 0, 0, 0));
        addEquipmentBtn.setBorder(null);
        addEquipmentBtn.setContentAreaFilled(false);    // Remove java swing "standard" bkg
        addEquipmentBtn.setFocusPainted(false);         // Remove java swing "hover" border
        addEquipmentBtn.setBorderPainted(false);        // Remove "excess" borders

        //  Open and visualize the EquipmentManager
        addEquipmentBtn.addActionListener((ActionEvent) ->
        {
            EquipmentManager equipManager = new EquipmentManager(frame,this.monitor);
            equipManager.setVisible(true);
        });

        JPanel addEquipmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
        equipmentCounter = new JLabel();
        addEquipmentPanel.add(addEquipmentBtn);
        addEquipmentPanel.add(equipmentCounter);

        JLabel panelTitle = new JLabel("EQUIPMENT", JLabel.CENTER);
        northPanel.add(panelTitle, BorderLayout.CENTER);
        northPanel.add(addEquipmentPanel, BorderLayout.WEST);

        // EquipmentPanel assembly
        add(scrollTable, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        this.monitor.addLabListener(this);
    }

    /**
     * Service method to load the table with LabMonitor equipment data
     */
    private void updateEquipmentTable() 
    {
        tableModel.setRowCount(0);
        List<Equipment> equipment = monitor.getEquipment();

        for (Equipment e : equipment) 
        {
            Object[] rowData = 
            {
                statusIndicator(e.getStatus()), //  Colored dot
                e.getType(),                    // Equipment type
                e.getCode(),                    // Equipment code
                e.getStatus().getLabel()        // Equipment status
            };
            tableModel.addRow(rowData);
        }
    }

    private String statusIndicator(EquipmentStatus status)
    {
        switch (status)
        {
            case EquipmentStatus.AVAILABLE:      //  Green
                return "<html><center><font color='#27AE60'>●</font></center></html>";
        
            case EquipmentStatus.IN_USE:         //  Amber
                return "<html><center><font color='#E6A822'>●</font></center></html>";

            default:                        //  Gray
                return "<html><center><font color='#D3D3D3'>●</font></center></html>";
        }
    } 

    /**
     * Service method to update the equipment activity counter
     */
    private void updateEquipmentCounter()
    {
        equipmentCounter.setText(": " + monitor.getEquipmentCounter());
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
            updateEquipmentCounter();
        });
    }
}
