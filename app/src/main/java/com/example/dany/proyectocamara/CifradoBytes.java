package com.example.dany.proyectocamara;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CifradoBytes {
    //Aqui se hace el cifrado
    public static void copia(String ficheroOriginal, String ficheroCopia, int key) {
        try {
            FileInputStream fileInput = new FileInputStream(ficheroOriginal);
            BufferedInputStream bufferedInput = new BufferedInputStream(fileInput);
            FileOutputStream fileOutput = new FileOutputStream(ficheroCopia);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
            byte[] array = new byte[1000];
            int readmnsj = bufferedInput.read(array);
            for (int i = 0; i < readmnsj; i++) {
                array[i] = (byte) (array[i] ^ key);
            }
            while (readmnsj > 0) {
                bufferedOutput.write(array, 0, readmnsj);
                readmnsj = bufferedInput.read(array);
            }
            bufferedInput.close();
            bufferedOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
