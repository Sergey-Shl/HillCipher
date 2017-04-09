package com.serge.hillcipher;

import android.util.Log;

import java.util.ArrayList;

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
        alphabetPower = 27;
        vecSize  = 2;
        GenerateEncryptionMatrix();
        GenerateDecryptionMatrix();
    }

    HillCipher(String msg, int vSize)
    {
        alphabetPower = 27;
        vecSize  = vSize;
        GenerateEncryptionMatrix();
        GenerateDecryptionMatrix();
    }

    void GenerateEncryptionMatrix()
    {
        Integer[][] encMatrix1 = {{1, 7},
                {3, 5}};
        Integer[][] encMatrix2 = {{6, 24, 1},
                {13, 16, 1000},
                {20, 17, 15}};
        Integer[][] encMatrix3 = {{3, 7},
                {5, 2}};
        encryptionMatrix = new Matrix(encMatrix1);
        //encryptionMatrix = Matrix.GenerateMatrix(vecSize, alphabetPower);
        Log.i("EncMatrix ", encryptionMatrix.toString());
    }

    Matrix GenerateDecryptionMatrix()
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
            // ???????????????
            if(factor > 20) {
                factor = 0;
                encryptionMatrix = Matrix.GenerateMatrix(vecSize, alphabetPower);
            }
        }


        decryptionMatrix = decryptionMatrix.MultiplyByNum(factor);
        //Log.i("INFO", decryptionMatrix.toString());
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        this.decryptionMatrix = decryptionMatrix;
        Log.i("DecMatrix ", decryptionMatrix.toString());
        return decryptionMatrix;
    }

    public ArrayList<Integer> EncryptMessage(String msg)
    {
        ArrayList<Integer> encMsg = new ArrayList<Integer>(msg.length() + vecSize*vecSize + 1);
        encMsg.add(vecSize);
        Log.i("Arr adding", "vecSize = " + vecSize);

        Matrix decMatrxTemp = GenerateDecryptionMatrix();
        for (int i = 0; i < vecSize; i++) {
            for (int j = 0; j < vecSize; j++) {
                encMsg.add(decMatrxTemp.getItem(i, j));
            }
        }

        Log.i("EncMat", encryptionMatrix.toString());

        String newMsg = msg.toUpperCase();
        int numOfFullParts = msg.length() / vecSize;
        int numOfAdditionalSigns = 0;

        if(msg.length() % vecSize != 0) {
            numOfFullParts++;
            numOfAdditionalSigns = msg.length() % vecSize;
        }

        encMsg.add(numOfFullParts);
        Log.i("Arr adding", "Num of parts = " + numOfFullParts);
        Log.i("INFO", "Msg lenght = " + msg.length());

        Matrix encVec;
        Integer[] tempVec = new Integer[vecSize];
        int c = 0;
        int index = 0;
        for (int i = 0; i < numOfFullParts; i++) {
            for (int j = 0; j < vecSize; j++) {
                if (index < newMsg.length()) {
                    Log.i("Index = ", String.valueOf(index));
                    c = (int) newMsg.charAt(index++);
                    if (c != 32)
                        tempVec[j] = c - 65;
                    else
                        tempVec[j] = 26;
                }
                else
                {
                    Log.i("Index (elese) = ", String.valueOf(index));
                    tempVec[j] = 26;
                }
            }
            encVec = new Matrix(tempVec);
            Log.i("EncVec (Input)", encVec.toString());
            encVec = encryptionMatrix.MultiplyByMatrix(encVec);
            Log.i("EncVec (Mult)", encVec.toString());
            encVec = encVec.LimitWithinNum(alphabetPower);
            Log.i("EncVec (Limit)", encVec.toString());
            for (int j = 0; j < vecSize; j++) {
                encMsg.add(encVec.getItem(j, 0));
            }
        }

        return encMsg;
    }

    public String DecryptMessage(ArrayList<Integer> encMsg)
    {
        int encMsgIndex = 0;
        vecSize = encMsg.get(encMsgIndex++);
        Integer[][] decMtrxTemp = new Integer[vecSize][vecSize];
        for (int i = 0; i < vecSize; i++) {
            for (int j = 0; j < vecSize; j++) {
                decMtrxTemp[i][j] = encMsg.get(encMsgIndex++);
            }
        }

        decryptionMatrix = new Matrix(decMtrxTemp);
        Log.i("DecMat", decryptionMatrix.toString());

        int numOfParts = encMsg.get(encMsgIndex++);

        String decMsg = "";
        char c = 0;
        char space = ' ';
        Matrix decVec;
        Integer[] decVecTemp = new Integer[vecSize];
        for (int i = 0; i < numOfParts; i++) {
            for (int j = 0; j < vecSize; j++) {
                decVecTemp[j] = encMsg.get(encMsgIndex++);
            }
            decVec = new Matrix(decVecTemp);
            Log.i("DecMess #1 (Input) ", decVec.toString());
            decVec = decryptionMatrix.MultiplyByMatrix(decVec);
            Log.i("DecMess #2 (Mult) ", decVec.toString());
            decVec = decVec.LimitWithinNum(alphabetPower);
            Log.i("DecMess #3 (Limit) ", decVec.toString());
            for (int j = 0; j < vecSize; j++) {
                c = (char)(int)decVec.getItem(j, 0);
                if (c == 26)
                    decMsg += space;
                else
                    decMsg += c;
            }
        }

        Log.i("MSG", decMsg);

        return decMsg;
    }
}
