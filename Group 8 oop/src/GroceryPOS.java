import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class GroceryPOS extends JFrame {
    JTabbedPane categories;
    JTable orderTable;
    DefaultTableModel model;
    JLabel totalLabel;
    JTextField cashField;
    JLabel changeLabel;

    public GroceryPOS() {
        setTitle("Grocery POS System");
        setSize(1100, 650);
        setLayout(new BorderLayout());

        categories = new JTabbedPane();
        categories.setFont(new Font("Arial", Font.BOLD, 16));
        add(categories, BorderLayout.CENTER);

        model = new DefaultTableModel(new Object[]{"Item", "Price", "Qty", "Amount", "Remove"}, 0);
        orderTable = new JTable(model);
        orderTable.setRowHeight(30);

        JScrollPane scroll = new JScrollPane(orderTable);
        scroll.setPreferredSize(new Dimension(400, 400));
        add(scroll, BorderLayout.EAST);

        JPanel bottom = new JPanel(new GridLayout(3, 2));
        bottom.add(new JLabel("Total:"));
        totalLabel = new JLabel("0.00");
        bottom.add(totalLabel);
        bottom.add(new JLabel("Cash:"));
        cashField = new JTextField();
        bottom.add(cashField);
        bottom.add(new JLabel("Change:"));
        changeLabel = new JLabel("0.00");
        bottom.add(changeLabel);
        add(bottom, BorderLayout.SOUTH);

        JButton payBtn = new JButton("PAY NOW");
        payBtn.setPreferredSize(new Dimension(200, 40));
        add(payBtn, BorderLayout.NORTH);

        payBtn.addActionListener(e -> computeChange());

        orderTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int col = orderTable.getSelectedColumn();
                int row = orderTable.getSelectedRow();
                if (col == 4) model.removeRow(row);
                updateTotal();
            }
        });

        addCategory("Snacks", new String[][]{
                {"Chips", "25"}, {"Biscuits", "15"}, {"Chocolate", "30"},
                {"Pretzels", "20"}, {"Popcorn", "18"}, {"Cookies", "28"}
        });

        addCategory("Fruits", new String[][]{
                {"Apple", "20"}, {"Banana", "10"}, {"Orange", "15"},
                {"Grapes", "40"}, {"Mango", "35"}, {"Watermelon", "50"}
        });

        addCategory("Beverages", new String[][]{
                {"Water", "10"}, {"Soda", "25"}, {"Juice", "30"},
                {"Iced Tea", "22"}, {"Energy Drink", "45"}, {"Milk Tea", "55"}
        });

        addCategory("Dairy", new String[][]{
                {"Milk", "30"}, {"Cheese", "45"}, {"Butter", "40"},
                {"Yogurt", "25"}, {"Cream", "35"}, {"Chocolate Milk", "28"}
        });

        addCategory("Frozen Foods", new String[][]{
                {"Hotdog", "80"}, {"Chicken Nuggets", "120"}, {"Frozen Fries", "75"},
                {"Fish Fillet", "140"}, {"Beef Tapa", "160"}, {"Longganisa", "90"}
        });

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    void addCategory(String name, String[][] items) {
        JPanel panel = new JPanel(new GridLayout(0, 3, 10, 10));
        panel.setBackground (Color.DARK_GRAY);
        for (String[] i : items) {
            String item = i[0];
            String price = i[1];

            JButton btn = new JButton(item + " - ₱" + price);
            btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.addActionListener(e -> addItem(item, Double.parseDouble(price)));

            panel.add(btn);
        }
        categories.add(name, new JScrollPane(panel));
    }

    void addItem(String item, double price) {
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(item)) {
                int qty = (int) model.getValueAt(i, 2) + 1;
                model.setValueAt(qty, i, 2);
                model.setValueAt(price * qty, i, 3);
                updateTotal();
                return;
            }
        }
        model.addRow(new Object[]{item, price, 1, price, "Remove"});
        updateTotal();
    }

    void updateTotal() {
        double t = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            t += (double) model.getValueAt(i, 3);
        }
        totalLabel.setText(String.format("%.2f", t));
    }

    void computeChange() {
        double total = Double.parseDouble(totalLabel.getText());
        double cash = Double.parseDouble(cashField.getText());
        double change = cash - total;
        changeLabel.setText(String.format("%.2f", change));
    }

    public static void main(String[] args) {
        new GroceryPOS();
    }
}
