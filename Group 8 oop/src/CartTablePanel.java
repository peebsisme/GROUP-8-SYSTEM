import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CartTablePanel extends JPanel {

    private DefaultTableModel model;
    private JTable table;
    private JButton removeButton;

    public CartTablePanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Cart"));

        setBackground(new Color(255, 240, 245));

        String[] cols = {"Item", "Qty", "Price", "Total"};
        model = new DefaultTableModel(cols, 0);
        table = new JTable(model);

        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));
        table.setRowHeight(28);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRender);
        table.getColumnModel().getColumn(3).setCellRenderer(centerRender);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(255, 200, 200));
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Sans-Serif", Font.BOLD, 14));

        add(new JScrollPane(table), BorderLayout.CENTER);

        removeButton = new JButton("Remove Selected");
        removeButton.setBackground(new Color(255, 150, 150));
        removeButton.setForeground(Color.WHITE);
        removeButton.setFocusPainted(false);
        removeButton.setFont(new Font("Sans-Serif", Font.BOLD, 12));

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selectedRows = table.getSelectedRows();
                if (selectedRows.length > 0) {

                    for (int i = selectedRows.length - 1; i >= 0; i--) {
                        model.removeRow(selectedRows[i]);
                    }
                } else {
                    JOptionPane.showMessageDialog(CartTablePanel.this,
                            "Please select a row to remove.", "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(255, 240, 245));
        buttonPanel.add(removeButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void addOrder(OrderItem order) {
        model.addRow(new Object[]{
                order.getItem().getName(),
                order.getQuantity(),
                "₱" + order.getItem().getPrice(),
                "₱" + order.getTotal()
        });
    }

    public DefaultTableModel getModel() {
        return model;
    }
}
