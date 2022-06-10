package com.example.tool.Bean;

public class Food {
    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodHeat() {
        return foodHeat;
    }

    public void setFoodHeat(int foodHeat) {
        this.foodHeat = foodHeat;
    }

    public int fid;
    private String foodName;
    private int foodHeat;
}
