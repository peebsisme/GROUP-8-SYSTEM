import javax.swing.*;
import java.awt.*;

public class PaymentDialog extends JDialog {

    private boolean completed = false;
    private double amountDue;
    private double cashEntered;
    private double change;

    public PaymentDialog(JFrame owner, double subtotal){
        super(owner, "Payment", true);
        setSize(300, 200);
        setLayout(new GridLayout(5, 2, 5, 5));
        setLocationRelativeTo(owner);
        getContentPane().setBackground(new Color(255, 250, 240));


        double vat = subtotal * 0.12;
        amountDue = subtotal + vat;

        add(new JLabel("Subtotal: "));
        add(new JLabel("₱" + subtotal));

        add(new JLabel("VAT (12%): "));
        add(new JLabel("₱" + vat));

        add(new JLabel("Total Due: "));
        add(new JLabel("₱" + amountDue));

        JTextField cashField = new JTextField();
        add(new JLabel("Cash Entered: "));
        add(cashField);

        JButton payBtn = new JButton("Pay");
        add(new JLabel(""));
        add(payBtn);
        payBtn.setBackground(new Color(0, 153, 51));
        payBtn.setForeground(Color.WHITE);
        payBtn.setFocusPainted(false);


        payBtn.addActionListener(e -> {
            try {
                cashEntered = Double.parseDouble(cashField.getText());
                if(cashEntered < amountDue){
                    JOptionPane.showMessageDialog(this, "Not enough cash.");
                    return;
                }
                change = cashEntered - amountDue;
                completed = true;
                dispose();
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this, "Invalid number.");
            }
        });
    }

    public boolean isCompleted(){ return completed; }
    public double getCash(){ return cashEntered; }
    public double getChange(){ return change; }
}
