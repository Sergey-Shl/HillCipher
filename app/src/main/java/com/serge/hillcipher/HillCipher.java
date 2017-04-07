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
        encryptionMatrix = new Matrix(encMatrix);
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

        while (determinant < 1 && determinant > alphabetPower)
        {
            if (determinant < 0)
                determinant += determinant;
            else
                determinant -= determinant;
        }

        Integer factor = 0;

        while ((factor * determinant) % alphabetPower != 1)
            factor++;

        decryptionMatrix = decryptionMatrix.MultiplyByNum(factor);
    }
}
