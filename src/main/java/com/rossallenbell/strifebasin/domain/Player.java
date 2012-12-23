package com.rossallenbell.strifebasin.domain;

public class Player {

    private int money;
    private int income;
    private long lastIncomeTime;
    
    public Player() {
        money = 0;
        income = Game.STARTING_INCOME;
    }

    public int getMoney() {
        return money;
    }
    
    public void alterMoney(int amount) {
        money += amount;
    }
    
    public void income() {
        money += income;
    }
    
    public int getIncome() {
        return income;
    }
    
    public void alterIncome(int amount) {
        income += amount;
    }

    public long getLastIncomeTime() {
        return lastIncomeTime;
    }

    public void setLastIncomeTime(long lastIncomeTime) {
        this.lastIncomeTime = lastIncomeTime;
    }
    
}
