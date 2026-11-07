package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import concurrent.*;
import java.util.List;

public class EquipmentPanel extends JPanel implements LabListener 
{
    private LabMonitor monitor;

    private JTable equipmentTable;

    private DefaultTableModel tableModel;

    public EquipmentPanel(LabMonitor monitor) {
        this.monitor = monitor;
        
        // Ci registriamo come ascoltatori del monitor per reagire ai cambiamenti in tempo reale
        this.monitor.addLabListener(this);

        setLayout(new BorderLayout());

        // Definiamo le colonne della tabella degli strumenti
        String[] columnNames = {"ID / Codice", "Tipo", "Stato"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celle non modificabili direttamente
            }
        };
        
        equipmentTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(equipmentTable);

        // Popoliamo subito la tabella con i dati iniziali
        aggiornaTabella();

        // Assemblaggio del pannello
        add(new JLabel("Elenco Strumenti del Laboratorio", JLabel.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void aggiornaTabella() {
        tableModel.setRowCount(0);
        List<Equipment> equipmentList = monitor.getEquipment();

        for (Equipment eq : equipmentList) 
        {
            Object[] rowData = 
            {
                eq.getType(),   // Il tipo (es. "Oscilloscopio")
                eq.getCode(),   // O il metodo che usi per identificare lo strumento (es. getCodice())
                eq.getStatus()  // Lo stato (DISPONIBILE, IN_USO, ecc.)
            };
            tableModel.addRow(rowData);
        }
    }

    @Override
    public void onLabStateChange()
    {
        // Appena uno strumento cambia stato (es. da DISPONIBILE a IN_USO), 
        // il monitor ci avvisa e noi aggiorniamo la tabella in modo sicuro sull'EDT
        SwingUtilities.invokeLater(this::aggiornaTabella);
    }
}
