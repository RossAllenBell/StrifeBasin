package com.rossallenbell.strifebasin.domain.units;

import com.rossallenbell.strifebasin.domain.Player;

public class Spearman extends Unit {
    
    public Spearman(Player owner) {
        super(owner);
    }

    private static final long serialVersionUID = 1L;

    @Override
    public int health() {
        return 10;
    }
    
    @Override
    public double getDamage() {
        return 2;
    }
    
}
