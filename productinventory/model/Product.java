package productinventory.model;

public class Product {
    private String sku;
    private String name;
    private int stock;
    private double unitCost;
    private double price;
    private int reorderThreshold;
    private int restockQty;
    private String supplier;
    private double supplierSurcharge;

    public Product(String sku, String name, int stock, double unitCost, double price,
                   int reorderThreshold, int restockQty, String supplier, double supplierSurcharge) {
        this.sku = sku;
        this.name = name;
        this.stock = stock;
        this.unitCost = unitCost;
        this.price = price;
        this.reorderThreshold = reorderThreshold;
        this.restockQty = restockQty;
        this.supplier = supplier;
        this.supplierSurcharge = supplierSurcharge;
    }

    // Getters and Setters
    public String getSku() { return sku; }
    public String getName() { return name; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public double getUnitCost() { return unitCost; }
    public double getPrice() { return price; }
    public int getReorderThreshold() { return reorderThreshold; }
    public int getRestockQty() { return restockQty; }
    public String getSupplier() { return supplier; }
    public double getSupplierSurcharge() { return supplierSurcharge; }
}
