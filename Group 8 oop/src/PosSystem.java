import javax.swing.*;
import java.awt.*;

public class PosSystem {

    private double subtotal = 0;
    private CartTablePanel cartPanel;

    public static void main(String[] args){
        SwingUtilities.invokeLater(PosSystem::new);
    }

    public PosSystem(){
        JFrame frame = new JFrame("SuperMarket Bros");
        frame.setSize(900, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 245, 245));


        cartPanel = new CartTablePanel();
        ProductMenuPanel menu = new ProductMenuPanel(this::addToCart);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JLabel subtotalLabel = new JLabel("Subtotal: ₱0.00");
        JButton checkoutBtn = new JButton("Checkout");
        bottom.add(subtotalLabel);
        bottom.add(checkoutBtn);
        checkoutBtn.setBackground(new Color(0, 153, 51)); // green
        checkoutBtn.setForeground(Color.WHITE);
        checkoutBtn.setFocusPainted(false);
        checkoutBtn.setPreferredSize(new Dimension(120, 40));


        checkoutBtn.addActionListener(e -> {
            PaymentDialog dlg = new PaymentDialog(frame, subtotal);
            dlg.setVisible(true);

            if(dlg.isCompleted()){
                new ReceiptWindow(cartPanel.getModel(),
                        dlg.getCash(),
                        dlg.getChange(),
                        subtotal);

                cartPanel.getModel().setRowCount(0);
                subtotal = 0;
                subtotalLabel.setText("Subtotal: ₱0.00");
            }
        });

        frame.add(menu, BorderLayout.WEST);
        frame.add(cartPanel, BorderLayout.CENTER);
        frame.add(bottom, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private void addToCart(Item item){
        OrderItem order = new OrderItem(item, 1);
        cartPanel.addOrder(order);
        subtotal += order.getTotal();
    }
}
