package productinventory.service;

import java.util.ArrayList;
import java.util.List;
import productinventory.db.InventoryDb;
import productinventory.model.Product;

public class InventoryService {

    public static class AllocationResult {
        private boolean success;
        private String message;
        private int allocatedQty;
        private boolean restockTriggered;
        private double restockCost;
        private List<String> logs;

        public AllocationResult(boolean success, String message, int allocatedQty, 
                                boolean restockTriggered, double restockCost, List<String> logs) {
            this.success = success;
            this.message = message;
            this.allocatedQty = allocatedQty;
            this.restockTriggered = restockTriggered;
            this.restockCost = restockCost;
            this.logs = logs;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public int getAllocatedQty() { return allocatedQty; }
        public boolean isRestockTriggered() { return restockTriggered; }
        public double getRestockCost() { return restockCost; }
        public List<String> getLogs() { return logs; }
    }

    public static class ValuationResult {
        private double totalValue;
        private List<String> details;

        public ValuationResult(double totalValue, List<String> details) {
            this.totalValue = totalValue;
            this.details = details;
        }

        public double getTotalValue() { return totalValue; }
        public List<String> getDetails() { return details; }
    }

    /**
     * Decision Node 1: Stock Availability Check.
     */
    public static boolean checkStockAvailability(String sku, int requestedQty, List<String> logs) {
        Product product = InventoryDb.getProduct(sku);
        if (product == null) {
            logs.add("Product not found");
            return false;
        }
        int currentStock = product.getStock();
        if (currentStock >= requestedQty) {
            logs.add(String.format("Sufficient stock. Available: %d, Requested: %d", currentStock, requestedQty));
            return true;
        } else {
            logs.add(String.format("Insufficient stock. Available: %d, Requested: %d", currentStock, requestedQty));
            return false;
        }
    }

    /**
     * Calculation Node 1: Restock Cost Calculation.
     */
    public static double calculateRestockCost(String sku) {
        Product product = InventoryDb.getProduct(sku);
        if (product == null) return 0.0;
        
        int qty = product.getRestockQty();
        double unitCost = product.getUnitCost();
        double surcharge = product.getSupplierSurcharge();
        
        return (qty * unitCost) + surcharge;
    }

    /**
     * Core Execution Flow: Checks availability, allocates stock, and handles reordering triggers.
     */
    public static AllocationResult verifyAndAllocateStock(String sku, int requestedQty) {
        List<String> logs = new ArrayList<>();
        logs.add(String.format("Starting stock verification for SKU: %s, Quantity: %d", sku, requestedQty));
        
        // Decision Node 1: Stock Availability
        boolean available = checkStockAvailability(sku, requestedQty, logs);
        
        if (!available) {
            return new AllocationResult(false, "Stock check failed", 0, false, 0.0, logs);
        }
        
        // Action: Allocate stock (deduct from inventory)
        InventoryDb.updateStock(sku, -requestedQty);
        Product product = InventoryDb.getProduct(sku);
        int newStock = product.getStock();
        logs.add(String.format("Allocated %d units. New stock level: %d", requestedQty, newStock));
        
        // Decision Node 2: Reorder Threshold Check
        boolean restockTriggered = false;
        double restockCost = 0.0;
        if (newStock < product.getReorderThreshold()) {
            restockTriggered = true;
            logs.add(String.format("ALERT: Stock level %d is below reorder threshold %d!", newStock, product.getReorderThreshold()));
            
            // Calculation Node 1: Restocking Cost
            restockCost = calculateRestockCost(sku);
            logs.add(String.format("Restock Cost Calculated: %d units @ $%.2f/unit + $%.2f surcharge = $%.2f", 
                                   product.getRestockQty(), product.getUnitCost(), product.getSupplierSurcharge(), restockCost));
            
            // Simulate restocking replenishment
            InventoryDb.updateStock(sku, product.getRestockQty());
            logs.add(String.format("Restocked %d units. Restored stock level: %d", product.getRestockQty(), product.getStock()));
        }
        
        return new AllocationResult(true, "Stock successfully allocated", requestedQty, restockTriggered, restockCost, logs);
    }

    /**
     * Calculation Node 2: Total Inventory Valuation.
     */
    public static ValuationResult calculateTotalInventoryValuation() {
        double totalVal = 0.0;
        List<String> details = new ArrayList<>();
        
        for (Product product : InventoryDb.getAllProducts().values()) {
            double itemVal = product.getStock() * product.getUnitCost();
            totalVal += itemVal;
            details.add(String.format("%s (%s): %d units @ $%.2f/ea = $%.2f", 
                                      product.getName(), product.getSku(), product.getStock(), product.getUnitCost(), itemVal));
        }
        
        return new ValuationResult(totalVal, details);
    }
}
