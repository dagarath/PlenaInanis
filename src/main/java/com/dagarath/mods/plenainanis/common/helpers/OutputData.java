package com.dagarath.mods.plenainanis.common.helpers;

import java.io.Serializable;

/**
 * Created by dagarath on 2016-02-05.
 */
public class OutputData implements Serializable {
    //private static final ObjectStreamField[] serialPersistentFields = { new ObjectStreamField("outputName", String.class), new ObjectStreamField("weight", Integer.class), new ObjectStreamField("minOutput", Integer.class), new ObjectStreamField("maxOutput", Integer.class)};
    public String outputName;
    public int weight;
    public int minOutput;
    public int maxOutput;

    public OutputData(){}

    public OutputData(String name, int weight, int minOutput, int maxOutput){
        this.outputName = name;
        this.weight = weight;
        this.minOutput = minOutput;
        this.maxOutput = maxOutput;
    }

}