package com.dagarath.mods.plenainanis.common.helpers;

import com.dagarath.mods.plenainanis.PlenaInanis;

import java.io.*;

/**
 * Created by dagarath on 2016-01-23.
 */
public class ObjectSerializer {
    private final static String saveDataFilename = "config/Dagarath/save.dat";


    public static void getSave(){
        try {
            File checkFile = new File(saveDataFilename);
            FileInputStream fileIn = new FileInputStream(saveDataFilename);


            ObjectInputStream input = new ObjectInputStream(fileIn);
            try {
                    PlenaInanis.saveData = (PlenaSaveData) deserialize((byte[])input.readObject());

                PlenaInanis.logger.debug("Save data loaded from: " + saveDataFilename);
            }catch(ClassNotFoundException c){
                PlenaInanis.logger.error("ClassNotFoundException: " + c);
            }
            fileIn.close();
            input.close();
        }catch(IOException e){
            try {
                ObjectInputStream input = new ObjectInputStream(PlenaInanis.class.getResourceAsStream("save.dat"));
                try {
                    PlenaInanis.saveData = (PlenaSaveData) deserialize((byte[]) input.readObject());
                    input.close();
                }catch(ClassNotFoundException c){
                    PlenaInanis.logger.error("ClassNotFoundException: " + c);
                }
            }catch(IOException i){
                PlenaInanis.logger.error("IOException: " + e);
            }

        }

    }

    public static void writeSave(){
        try{
            FileOutputStream fileOut = new FileOutputStream(saveDataFilename);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(serialize(PlenaInanis.saveData));
            out.close();
            fileOut.close();
            PlenaInanis.logger.debug("Save data written to: " + saveDataFilename);
        }catch(IOException e){
            PlenaInanis.logger.error("IOException: " + e);
        }

    }

    public static boolean exists(){
            File checkFile = new File(saveDataFilename);
        PlenaInanis.logger.info(checkFile.exists());
            return checkFile.exists();
    }

    public static byte[] serialize(Object obj) throws IOException{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        byte[] byteArray = out.toByteArray();
        os.close();
        out.close();
        return byteArray;
    }

    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException{
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        Object object = is.readObject();
        in.close();
        is.close();
        return object;
    }
}
