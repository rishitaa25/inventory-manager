package com.example.androidexample.features.items;

public class InventoryChangeObject {
    private Integer changeId;
    private Integer skuNumber;
    private Integer amountOfItem;
    private Integer employeeId;

    public InventoryChangeObject(Integer changeId, Integer skuNumber, Integer amountOfItem, Integer employeeId) {
        this.changeId = changeId;
        this.skuNumber = skuNumber;
        this.amountOfItem = amountOfItem;
        this.employeeId = employeeId;
    }

    public Integer getChangeId() {
        return changeId;
    }

    public Integer getSkuNumber() {
        return skuNumber;
    }
    public Integer getAmountOfItem() { return amountOfItem;}
    public Integer getEmployeeId() {return  employeeId;}
}
