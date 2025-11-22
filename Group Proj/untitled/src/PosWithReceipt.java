// PosWithReceipt.java
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PosWithReceipt {
    private JFrame frame;
    private JTextField itemNameField;
    private JTextField itemPriceField;
    private DefaultTableModel tableModel;
    private JLabel subtotalLabel;

    // monetary values kept in BigDecimal
    private BigDecimal subtotal = BigDecimal.ZERO;
    private static final BigDecimal VAT_RATE = new BigDecimal("0.12"); // 12%

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PosWithReceipt().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Simple POS - Style 1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 450);
        frame.setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        // Item name
        c.gridx = 0;
        c.gridy = 0;
        topPanel.add(new JLabel("Item Name:"), c);

        itemNameField = new JTextField(18);
        c.gridx = 1;
        topPanel.add(itemNameField, c);

        // Price
        c.gridx = 0;
        c.gridy = 1;
        topPanel.add(new JLabel("Price (PHP):"), c);

        itemPriceField = new JTextField(10);
        c.gridx = 1;
        topPanel.add(itemPriceField, c);

        // Add button
        JButton addBtn = new JButton("Add Item");
        c.gridx = 2;
        c.gridy = 0;
        c.gridheight = 2;
        c.fill = GridBagConstraints.VERTICAL;
        topPanel.add(addBtn, c);
        c.gridheight = 1;
        c.fill = GridBagConstraints.NONE;

        // Table for items
        String[] cols = {"Item", "Qty", "Price", "Total"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(table);

        // Bottom panel with subtotal and checkout
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        subtotalLabel = new JLabel("Subtotal: ₱0.00");
        JButton checkoutBtn = new JButton("Checkout");
        bottomPanel.add(subtotalLabel);
        bottomPanel.add(checkoutBtn);

        // Layout
        frame.getContentPane().setLayout(new BorderLayout(8,8));
        frame.getContentPane().add(topPanel, BorderLayout.NORTH);
        frame.getContentPane().add(tableScroll, BorderLayout.CENTER);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        // Action listeners
        addBtn.addActionListener(e -> onAddItem());
        itemPriceField.addActionListener(e -> onAddItem()); // press enter in price to add
        checkoutBtn.addActionListener(e -> onCheckout());

        frame.setVisible(true);
    }

    private void onAddItem() {
        String name = itemNameField.getText().trim();
        String priceText = itemPriceField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter an item name.", "Missing name", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (priceText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter a price.", "Missing price", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal price;
        try {
            // Allow comma or dot; remove commas
            priceText = priceText.replace(",", "");
            price = new BigDecimal(priceText).setScale(2, RoundingMode.HALF_UP);
            if (price.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid price. Enter a valid number (e.g. 150 or 150.00).", "Invalid price", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int qty = 1; // user decided: no quantity input
        BigDecimal total = price.multiply(new BigDecimal(qty)).setScale(2, RoundingMode.HALF_UP);

        tableModel.addRow(new Object[]{name, qty, formatCurrency(price), formatCurrency(total)});

        subtotal = subtotal.add(total).setScale(2, RoundingMode.HALF_UP);
        updateSubtotalLabel();

        // clear inputs
        itemNameField.setText("");
        itemPriceField.setText("");
        itemNameField.requestFocus();
    }

    private void updateSubtotalLabel() {
        subtotalLabel.setText("Subtotal: " + formatCurrency(subtotal));
    }

    private void onCheckout() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(frame, "No items added.", "Empty sale", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Open payment dialog
        PaymentDialog payDlg = new PaymentDialog(frame, subtotal);
        payDlg.setVisible(true);

        if (payDlg.isCompleted()) {
            // show receipt(s)
            BigDecimal cash = payDlg.getCash();
            BigDecimal change = payDlg.getChange();
            showReceiptWindow(cash, change);
            // reset for new sale
            tableModel.setRowCount(0);
            subtotal = BigDecimal.ZERO;
            updateSubtotalLabel();
        }
    }

    private void showReceiptWindow(BigDecimal cash, BigDecimal change) {
        JFrame r = new JFrame("Receipt");
        r.setSize(420, 560);
        r.setLocationRelativeTo(frame);

        JTextArea ta = new JTextArea();
        ta.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        ta.setEditable(false);

        String receipt = buildReceiptText(cash, change);
        ta.setText(receipt);

        JScrollPane scroll = new JScrollPane(ta);
        r.getContentPane().add(scroll);
        r.setVisible(true);
    }

    private String buildReceiptText(BigDecimal cash, BigDecimal change) {
        StringBuilder sb = new StringBuilder();
        String storeName = "Simple Store";
        String invoice = "INV-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Header
        sb.append(centerText(storeName, 40)).append("\n");
        sb.append(centerText("SALES RECEIPT", 40)).append("\n\n");

        // Items
        sb.append(String.format("%-20s %3s %8s %9s\n", "Item", "QTY", "Price", "Total"));
        sb.append(repeat("-", 40)).append("\n");
        BigDecimal computedSubtotal = BigDecimal.ZERO;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = tableModel.getValueAt(i, 0).toString();
            String qty = tableModel.getValueAt(i, 1).toString();
            String priceStr = tableModel.getValueAt(i, 2).toString().replace("₱", "").replace(",", "");
            String totalStr = tableModel.getValueAt(i, 3).toString().replace("₱", "").replace(",", "");
            BigDecimal price = new BigDecimal(priceStr);
            BigDecimal total = new BigDecimal(totalStr);
            computedSubtotal = computedSubtotal.add(total);

            // shorten item name if too long
            String shortName = name.length() > 18 ? name.substring(0, 15) + "..." : name;
            sb.append(String.format("%-20s %3s %8s %9s\n", shortName, qty, formatCurrency(price), formatCurrency(total)));
        }
        sb.append(repeat("-", 40)).append("\n");

        // VAT calculations
        BigDecimal vatable = computedSubtotal.setScale(2, RoundingMode.HALF_UP); // treat whole subtotal as VATable
        BigDecimal vatAmount = vatable.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal amountDue = vatable.add(vatAmount).setScale(2, RoundingMode.HALF_UP);

        sb.append(String.format("%-26s %12s\n", "Subtotal:", formatCurrency(computedSubtotal)));
        sb.append(String.format("%-26s %12s\n", "VATable (₱):", formatCurrency(vatable)));
        sb.append(String.format("%-26s %12s\n", "VAT (12%):", formatCurrency(vatAmount)));
        sb.append(String.format("%-26s %12s\n", "Amount Due:", formatCurrency(amountDue)));
        sb.append("\n");

        sb.append(String.format("%-26s %12s\n", "Cash:", formatCurrency(cash)));
        sb.append(String.format("%-26s %12s\n", "Change:", formatCurrency(change)));
        sb.append("\n");

        sb.append(repeat("-", 40)).append("\n");
        sb.append(String.format("%-20s %20s\n", "Invoice No:", invoice));
        sb.append(String.format("%-20s %20s\n", "Date/Time:", now.format(dtf)));
        sb.append("\n");
        sb.append(centerText("Thank you for your purchase!", 40)).append("\n");

        return sb.toString();
    }

    // Utility: center text in width
    private String centerText(String s, int width) {
        if (s.length() >= width) return s;
        int left = (width - s.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(' ');
        sb.append(s);
        return sb.toString();
    }

    private String repeat(String ch, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) sb.append(ch);
        return sb.toString();
    }

    private String formatCurrency(BigDecimal amt) {
        // Use Philippine peso symbol and 2 decimals
        return "₱" + amt.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    // Inner class for payment dialog
    private class PaymentDialog extends JDialog {
        private boolean completed = false;
        private BigDecimal cash = BigDecimal.ZERO;
        private BigDecimal change = BigDecimal.ZERO;

        private JLabel totalLabel;
        private JTextField cashField;
        private JLabel changeLabel;

        PaymentDialog(Frame owner, BigDecimal subtotal) {
            super(owner, "Payment", true);
            setSize(360, 220);
            setLocationRelativeTo(owner);
            setLayout(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();
            c.insets = new Insets(8,8,8,8);
            c.anchor = GridBagConstraints.WEST;

            BigDecimal vatAmount = subtotal.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
            BigDecimal amountDue = subtotal.add(vatAmount).setScale(2, RoundingMode.HALF_UP);

            c.gridx = 0; c.gridy = 0;
            add(new JLabel("Subtotal:"), c);
            c.gridx = 1;
            add(new JLabel(formatCurrency(subtotal)), c);

            c.gridx = 0; c.gridy = 1;
            add(new JLabel("VAT (12%):"), c);
            c.gridx = 1;
            add(new JLabel(formatCurrency(vatAmount)), c);

            c.gridx = 0; c.gridy = 2;
            add(new JLabel("Total Amount Due:"), c);
            c.gridx = 1;
            totalLabel = new JLabel(formatCurrency(amountDue));
            add(totalLabel, c);

            c.gridx = 0; c.gridy = 3;
            add(new JLabel("Cash (enter):"), c);
            c.gridx = 1;
            cashField = new JTextField(12);
            add(cashField, c);

            c.gridx = 0; c.gridy = 4;
            add(new JLabel("Change:"), c);
            c.gridx = 1;
            changeLabel = new JLabel(formatCurrency(BigDecimal.ZERO));
            add(changeLabel, c);

            // buttons
            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton ok = new JButton("Generate Receipt");
            JButton cancel = new JButton("Cancel");
            btns.add(cancel);
            btns.add(ok);

            c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
            add(btns, c);

            // events
            cashField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    updateChangeDisplay(subtotal);
                }
            });

            ok.addActionListener(ev -> {
                if (!parseAndSetCash(subtotal)) return;
                completed = true;
                dispose();
            });

            cancel.addActionListener(ev -> {
                completed = false;
                dispose();
            });
        }

        private void updateChangeDisplay(BigDecimal subtotal) {
            String txt = cashField.getText().trim().replace(",", "");
            if (txt.isEmpty()) {
                changeLabel.setText(formatCurrency(BigDecimal.ZERO));
                return;
            }
            try {
                BigDecimal entered = new BigDecimal(txt).setScale(2, RoundingMode.HALF_UP);
                BigDecimal vatAmount = subtotal.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
                BigDecimal amountDue = subtotal.add(vatAmount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ch = entered.subtract(amountDue).setScale(2, RoundingMode.HALF_UP);
                if (ch.compareTo(BigDecimal.ZERO) < 0) {
                    changeLabel.setText("(insufficient)");
                } else {
                    changeLabel.setText(formatCurrency(ch));
                }
            } catch (NumberFormatException ex) {
                changeLabel.setText("(invalid)");
            }
        }

        private boolean parseAndSetCash(BigDecimal subtotal) {
            String txt = cashField.getText().trim().replace(",", "");
            if (txt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter cash amount.", "Missing", JOptionPane.WARNING_MESSAGE);
                return false;
            }
            try {
                BigDecimal entered = new BigDecimal(txt).setScale(2, RoundingMode.HALF_UP);
                BigDecimal vatAmount = subtotal.multiply(VAT_RATE).setScale(2, RoundingMode.HALF_UP);
                BigDecimal amountDue = subtotal.add(vatAmount).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ch = entered.subtract(amountDue).setScale(2, RoundingMode.HALF_UP);
                if (ch.compareTo(BigDecimal.ZERO) < 0) {
                    JOptionPane.showMessageDialog(this, "Cash is less than amount due.", "Insufficient cash", JOptionPane.WARNING_MESSAGE);
                    return false;
                }
                this.cash = entered;
                this.change = ch;
                return true;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid cash amount.", "Invalid", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        boolean isCompleted() { return completed; }
        BigDecimal getCash() { return cash; }
        BigDecimal getChange() { return change; }
    }
}

