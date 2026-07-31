package productinventory.db;

import java.util.HashMap;
import java.util.Map;
import productinventory.model.Product;

public class InventoryDb {
    private static final Map<String, Product> db = new HashMap<>();

    static {
        db.put("SKU-1001", new Product("SKU-1001", "Wireless Ergonomic Mouse", 25, 15.00, 29.99, 10, 50, "Logitech Global", 12.50));
        db.put("SKU-1002", new Product("SKU-1002", "Mechanical Keyboard (Blue Switch)", 4, 45.00, 89.99, 5, 20, "Keychron Corp", 25.00));
        db.put("SKU-1003", new Product("SKU-1003", "4K Ultra-Wide Monitor 34\"", 8, 250.00, 449.99, 3, 5, "Dell Display Solutions", 50.00));
    }

    public static Product getProduct(String sku) {
        return db.get(sku);
    }

    public static boolean updateStock(String sku, int qtyChange) {
        Product p = db.get(sku);
        if (p != null) {
            p.setStock(p.getStock() + qtyChange);
            return true;
        }
        return false;
    }

    public static Map<String, Product> getAllProducts() {
        return db;
    }
}
