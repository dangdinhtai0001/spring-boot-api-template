package com.phoenix.common.security;

import com.phoenix.common.util.Base32;
import org.apache.log4j.Logger;

import java.io.*;
import java.security.Key;
import java.security.SecureRandom;

public class Base32KeyProvider implements KeyProvider {
    private Logger logger = Logger.getLogger(Base64KeyProvider.class);

    private final KeyWrapper keyWrapper;

    public Base32KeyProvider(File keyFile) throws IOException, ClassNotFoundException {
        if (keyFile.length() <= 0) {
            SecureRandom secureRandom = new SecureRandom();
            byte[] bytes = new byte[(32 * 5) / 8];
            secureRandom.nextBytes(bytes);

            String secretString = Base32.encode(bytes);

            this.keyWrapper = new KeyWrapper(secretString, null, null);

            saveKey(keyFile);
        } else {
            keyWrapper = (KeyWrapper) loadKey(keyFile);
        }
    }

    @Override
    public KeyWrapper getKeyWrapper() {
        return keyWrapper;
    }

    private void saveKey(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(this.keyWrapper);
        objectOutputStream.close();
        //System.out.println("The Object  was succesfully written to a file");
        logger.info("The Key was successfully written to a file");
    }

    private Object loadKey(File file) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object obj = objectInputStream.readObject();
        //System.out.println("The Object has been read from the file");
        logger.info("The Key has been read from the file");
        objectInputStream.close();
        return obj;
    }
}
