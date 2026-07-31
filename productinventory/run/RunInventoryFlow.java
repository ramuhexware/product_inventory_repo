package productinventory.run;

import productinventory.service.InventoryService;
import productinventory.service.InventoryService.AllocationResult;
import productinventory.service.InventoryService.ValuationResult;

public class RunInventoryFlow {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("INVENTORY SERVICE FLOW SIMULATION (JAVA)");
        System.out.println("==================================================");
        
        // 1. Show starting inventory valuation
        ValuationResult valResult = InventoryService.calculateTotalInventoryValuation();
        System.out.println("Initial Inventory Valuation:");
        for (String detail : valResult.getDetails()) {
            System.out.println("  - " + detail);
        }
        System.out.printf("Total Valuation: $%,.2f\n\n", valResult.getTotalValue());
        
        // 2. Simulate stock allocations
        System.out.println("Simulating Allocation 1: Sufficient stock, above threshold");
        AllocationResult result1 = InventoryService.verifyAndAllocateStock("SKU-1001", 5);
        for (String log : result1.getLogs()) {
            System.out.println("  [Log] " + log);
        }
        System.out.println("Result Success: " + result1.isSuccess() + "\n");
        
        System.out.println("Simulating Allocation 2: Low stock, will trigger Reorder Threshold and Restocking Cost Calc");
        AllocationResult result2 = InventoryService.verifyAndAllocateStock("SKU-1002", 2);
        for (String log : result2.getLogs()) {
            System.out.println("  [Log] " + log);
        }
        System.out.println("Result Success: " + result2.isSuccess() + "\n");
        
        // 3. Show updated inventory valuation
        valResult = InventoryService.calculateTotalInventoryValuation();
        System.out.println("Final Inventory Valuation (after allocations & restocking):");
        for (String detail : valResult.getDetails()) {
            System.out.println("  - " + detail);
        }
        System.out.printf("Total Valuation: $%,.2f\n", valResult.getTotalValue());
        System.out.println("==================================================");
    }
}
