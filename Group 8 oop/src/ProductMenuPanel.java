import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ProductMenuPanel extends JPanel {

    private JPanel productPanel; // panel that holds the buttons

    public ProductMenuPanel(Consumer<Item> addToCartCallback){

        setLayout(new BorderLayout());
        setBackground(new Color(255, 230, 230));

        // Create inner panel for products
        productPanel = new JPanel();

        // ⬇⬇ FIXED: always 2 columns
        productPanel.setLayout(new GridLayout(0, 2, 10, 10));

        productPanel.setBorder(BorderFactory.createTitledBorder("Products"));
        productPanel.setBackground(new Color(255, 230, 230));

        // Add products
        addMenuButton("PureFoods Tender Juicy Hotdog 1kg", 216, addToCartCallback);
        addMenuButton("Hash Brown", 276, addToCartCallback);
        addMenuButton("Energy Drink", 45, addToCartCallback);
        addMenuButton("Soda", 25, addToCartCallback);
        addMenuButton("Butter", 40, addToCartCallback);
        addMenuButton("Almond Milk Original", 184.5, addToCartCallback);
        addMenuButton("Spam Luncheon Meat", 208, addToCartCallback);
        addMenuButton("Delimondo Corned Beef", 188.5, addToCartCallback);
        addMenuButton("Stick-O Wafer Stick", 182.5, addToCartCallback);
        addMenuButton("Pringles Original", 92.5, addToCartCallback);
        addMenuButton("Nutella B Ready T6", 189.5, addToCartCallback);
        addMenuButton("Gardenia Loaf High", 96, addToCartCallback);
        addMenuButton("Fudgee Bar Chocolate", 91.5, addToCartCallback);
        addMenuButton("Lysol Disinfectant Spray", 474.75, addToCartCallback);
        addMenuButton("GreenCross Isopropyl Alcohol", 93.75, addToCartCallback);
        addMenuButton("Domex Ultra Thick Bleach Toilet", 271.5, addToCartCallback);
        addMenuButton("Egg Tray", 350, addToCartCallback);
        addMenuButton("Tilapia Large 350g-500g", 115, addToCartCallback);
        addMenuButton("SM Bonus Fresh Chilled Chicken", 190, addToCartCallback);
        addMenuButton("SM Bonus U Fuji Apples | 6pcs", 169, addToCartCallback);
        addMenuButton("Highlands Premium Beef Patties | 452g", 246, addToCartCallback);
        addMenuButton("C2 Green Tea Lemon | 1L", 56.5, addToCartCallback);
        addMenuButton("Frontera Hamper | 750ml 2pcs", 830, addToCartCallback);
        addMenuButton("Kanto Vodka Salted Caramel | 700ml", 1080, addToCartCallback);
        addMenuButton("Nescafe Classic | 200g", 207.5, addToCartCallback);
        addMenuButton("Milo Active Go | 600g", 184.5, addToCartCallback);
        addMenuButton("Nescafe Gold Jar | 200g", 599.5, addToCartCallback);
        addMenuButton("Del Monte Pineapple Juice Drink Fiber| 1L Tetra", 127.95, addToCartCallback);
        addMenuButton("Rite N Lite Watermelon + Apple | 250ml", 29.5, addToCartCallback);
        addMenuButton("Cibo Pizza Romana Salame Regular | 350g", 365, addToCartCallback);
        addMenuButton("Tapa King Classic | 450g", 399, addToCartCallback);
        addMenuButton("Manna Premium Kimchi Fresh | 235g", 118, addToCartCallback);
        addMenuButton("Decs Mini Asado Siopao Frozen | 9pcs", 170, addToCartCallback);
        addMenuButton("Binggrae Melon Milk Drink | 200ml", 59.5, addToCartCallback);
        addMenuButton("Bear Brand Fortified | 700g", 246.5, addToCartCallback);
        addMenuButton("Eden Filled Cheese | 160g", 56.5, addToCartCallback);

        // Wrap product panel in scroll pane
        JScrollPane scrollPane = new JScrollPane(productPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void addMenuButton(String name, double price, Consumer<Item> callback){

        JButton btn = new JButton(
                "<html><div style='text-align:center;'>"
                        + name + "<br>₱" + price +
                        "</div></html>"
        );

        btn.setFont(new Font("Sans-Serif", Font.PLAIN, 14));
        btn.setBackground(new Color(255, 250, 240));
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        btn.addActionListener(e -> callback.accept(new Item(name, price)));
        productPanel.add(btn);
    }
}
