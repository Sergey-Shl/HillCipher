package com.serge.hillcipher;

import android.util.Log;

/**
 * Created by serge on 07.04.2017.
 */

public class HillCipher {
    private Matrix encryptionMatrix;
    private Matrix decryptionMatrix;
    private Integer alphabetPower = 0;
    private Integer vecSize = 0;

    HillCipher()
    {
        alphabetPower = 26;
        vecSize  = 2;
        GenerateEncryptionMatrix();
        GenerateDecryptionMatrix();
    }

    HillCipher(String msg, int vSize)
    {
        alphabetPower = 26;
        vecSize  = vSize;
        GenerateEncryptionMatrix();
        GenerateDecryptionMatrix();
    }

    void GenerateEncryptionMatrix()
    {
        Integer[][] encMatrix1 = {{3, 3},
                {2, 5}};
        Integer[][] encMatrix2 = {{6, 24, 1},
                {13, 16, 1000},
                {20, 17, 15}};
        Integer[][] encMatrix3 = {{3, 7},
                {5, 2}};
        encryptionMatrix = Matrix.GenerateMatrix(vecSize, alphabetPower);
        Log.i("EncMatrix ", encryptionMatrix.toString());
    }

    void GenerateDecryptionMatrix()
    {
        Integer determinant = encryptionMatrix.Determinant();
        //Log.i("INFO", String.valueOf(determinant));

        Matrix decryptionMatrix = encryptionMatrix.Minor();
        //Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.Cofactor();
        //Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.Transpose();
        //Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        //Log.i("INFO", decryptionMatrix.toString());

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
        {
            factor++;
            //Log.i("factor", String.valueOf(factor));
            // ???????????????
            if(factor > 20) {
                //Log.i("INFO", "New encrM");
                factor = 0;
                encryptionMatrix = Matrix.GenerateMatrix(vecSize, alphabetPower);
            }
        }


        decryptionMatrix = decryptionMatrix.MultiplyByNum(factor);
        //Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        this.decryptionMatrix = decryptionMatrix;
        Log.i("DecMatrix ", decryptionMatrix.toString());
    }

    public Matrix[] EncryptMessage(String msg)
    {
        String newMsg = msg.toUpperCase();
        Matrix[] vectors = new Matrix[newMsg.length() / vecSize ];
        Integer[] tempVec = new Integer[vecSize ];
        for (int i = 0; i < newMsg.length() / vecSize ; i++) {
            for (int j = 0; j < vecSize ; j++) {
                tempVec[j] = (int)newMsg.charAt(i * vecSize  + j) - 65;
            }
            vectors[i] = new Matrix(tempVec);
            Log.i("EncMess #1 (Input) ", vectors[i].toString());
        }

        for (int i = 0; i < vectors.length; i++) {
            vectors[i] = encryptionMatrix.MultiplyByMatrix(vectors[i]);
            Log.i("EncMess #2 (Mult) " + i, vectors[i].toString());
            vectors[i] = vectors[i].LimitWithinNum(alphabetPower);
            Log.i("EncMess #3 (Limit) " + i, vectors[i].toString());
        }

        return vectors;
    }

    public String DecryptMessage(Matrix[] vectors)
    {
        Matrix[] decrVect = new Matrix[vectors.length];
        for (int i = 0; i < vectors.length; i++) {
            Log.i("DecMess #1 (Input) ", vectors[i].toString());
            decrVect[i] = decryptionMatrix.MultiplyByMatrix(vectors[i]);
            Log.i("DecMess #2 (Mult) ", decrVect[i].toString());
            decrVect[i] = decrVect[i].LimitWithinNum(alphabetPower);
            Log.i("DecMess #3 (Limit) ", decrVect[i].toString());
        }
        String msg = "";

        for (int i = 0; i < decrVect.length; i++) {
            for (int j = 0; j < vecSize ; j++) {
                msg += (char)((int)decrVect[i].getItem(j, 0) + 65);
            }
        }

        return msg;
    }
}
