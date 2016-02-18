package com.dagarath.mods.plenainanis.common.helpers;

import com.dagarath.mods.plenainanis.PlenaInanis;
import net.minecraft.item.Item;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by dagarath on 2016-01-27.
 */
public class RandomCollection {
    private NavigableMap<Integer, String> map = new TreeMap<>();
    private Random random;
    private int total = 0;

    public RandomCollection(){
        this(new Random());
    }

    public RandomCollection(Random random){
        this.random = random;
    }
    public void add(Integer weight, String result){
        if(weight <= 0) return;
        total += weight;
        map.put(total, result);
    }

    public String next(){
        if(total <= 0){
            total = 1;
        }
        PlenaInanis.logger.info("Total is: " + total);
        int value = random.nextInt(total) + 1;

        if(value <= 0){
            value = 1;
        }
        PlenaInanis.logger.info("Value is: " + value);
        PlenaInanis.logger.info(map.values());
        return map.ceilingEntry(value).getValue();
    }
}
