package com.serge.hillcipher;

import android.util.Log;

/**
 * Created by serge on 07.04.2017.
 */

public class HillCipher {
    private Matrix encryptionMatrix;
    private Matrix decryptionMatrix;
    private Integer alphabetPower;

    HillCipher()
    {
        alphabetPower = 26;
        GenerateEncryptionMatrix();
        GenerateDecryptionMatrix();
    }

    void GenerateEncryptionMatrix()
    {
        Integer[][] encMatrix = {{3, 3},
                {2, 5}};
        Integer[][] encMatrix2 = {{6, 24, 1},
                {13, 16, 1000},
                {20, 17, 15}};
        Integer[][] encMatrix3 = {{3, 7},
                {5, 2}};
        encryptionMatrix = new Matrix(encMatrix2);
        Log.i("INFO", encryptionMatrix.toString());
    }

    void GenerateDecryptionMatrix()
    {
        Integer determinant = encryptionMatrix.Determinant();
        Log.i("INFO", String.valueOf(determinant));

        Matrix decryptionMatrix = encryptionMatrix.Minor();
        Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.Cofactor();
        Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.Transpose();
        Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        Log.i("INFO", decryptionMatrix.toString());

        Integer newDetermiant = determinant;
        while (newDetermiant < 0 || newDetermiant > alphabetPower)
        {
            if (newDetermiant < 0)
                newDetermiant += alphabetPower;
            if (newDetermiant > alphabetPower)
                newDetermiant -= alphabetPower;
        }

        Log.i("INFO", "New det: " + newDetermiant);

        Integer factor = 0;

        while ((factor * newDetermiant) % alphabetPower != 1)
            factor++;

        Log.i("INFO", factor + "");

        decryptionMatrix = decryptionMatrix.MultiplyByNum(factor);
        Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        Log.i("INFO", decryptionMatrix.toString());
    }
}
