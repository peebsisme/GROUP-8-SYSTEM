import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class ProductMenuPanel extends JPanel {

    public ProductMenuPanel(Consumer<Item> addToCartCallback){

        setLayout(new GridLayout(8, 6, 10, 10));
        setBorder(BorderFactory.createTitledBorder("Products"));
        setBackground(new Color(255, 230, 230)); // soft pink background

        addMenuButton("Hotdog", 80, addToCartCallback);
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
        addMenuButton("GreenCross Isopropyl Alcohol ", 93.75, addToCartCallback);
        addMenuButton("Domex Ultra Thick Bleach Toilet ", 271.5, addToCartCallback);
    }

    private void addMenuButton(String name, double price, Consumer<Item> callback){

        // Center button text using HTML
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
        add(btn);
    }
}
