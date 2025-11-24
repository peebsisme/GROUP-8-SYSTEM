import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ReceiptWindow extends JFrame {

    public ReceiptWindow(DefaultTableModel cart, double cash, double change, double subtotal){
        setTitle("Receipt");
        setSize(350, 500);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 245, 245));


        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        StringBuilder sb = new StringBuilder();
        sb.append("       SuperMarket Bros\n");
        sb.append("       SALES RECEIPT\n\n");
        sb.append("----------------------------------------\n");

        for(int i = 0; i < cart.getRowCount(); i++){
            sb.append(String.format("%-20s %5s %10s\n",
                    cart.getValueAt(i, 0),
                    cart.getValueAt(i, 1),
                    cart.getValueAt(i, 3)
            ));
        }

        double vat = subtotal * 0.12;
        double total = subtotal + vat;

        sb.append("----------------------------------------\n");
        sb.append(String.format("Subtotal:           ₱%.2f\n", subtotal));
        sb.append(String.format("VAT (12%%):          ₱%.2f\n", vat));
        sb.append(String.format("Amount Due:         ₱%.2f\n", total));
        sb.append(String.format("Cash:               ₱%.2f\n", cash));
        sb.append(String.format("Change:             ₱%.2f\n", change));

        area.setText(sb.toString());
        area.setEditable(false);

        add(new JScrollPane(area));
        setVisible(true);
    }
}

