package com.serge.hillcipher;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by serge on 07.04.2017.
 */

public class HillCipher {
    private int alphabetPower = 0;
    private int vecSize = 0;

    HillCipher()
    {
        alphabetPower = 27;
        vecSize = 2;
    }

    HillCipher(String msg, int vSize)
    {
        alphabetPower = 27;
        vecSize = vSize;
    }

    //Генерирует матрицу шифрования
    private Matrix GenerateEncryptionMatrix(int vecSize, int alphabetPower)
    {
        Integer[][] encMatrix1 = {{1, 7},
                {3, 5}};

        Matrix encryptionMatrix = new Matrix(encMatrix1);
        //encryptionMatrix = Matrix.GenerateMatrix(vecSize, alphabetPower);
        return encryptionMatrix;
    }

    //Генерирует матрицу дешифрования
    private Matrix GenerateDecryptionMatrix(int vecSize, int alphabetPower, Matrix encryptionMatrix)
    {
        Integer determinant = encryptionMatrix.Determinant();

        Matrix decryptionMatrix = encryptionMatrix.Minor();
        decryptionMatrix = decryptionMatrix.Cofactor();
        decryptionMatrix = decryptionMatrix.Transpose();
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);

        Integer newDetermiant = determinant;
        while (newDetermiant < 0 || newDetermiant > alphabetPower)
        {
            if (newDetermiant < 0)
                newDetermiant += alphabetPower;
            if (newDetermiant > alphabetPower)
                newDetermiant -= alphabetPower;
        }

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
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        return decryptionMatrix;
    }

    //Шифрует сообщение
    public ArrayList<Integer> EncryptMessage(String msg)
    {
        Matrix encryptionMatrix = GenerateEncryptionMatrix(vecSize, alphabetPower);

        ArrayList<Integer> encMsg = new ArrayList<Integer>(msg.length() + vecSize*vecSize + 1);
        encMsg.add(vecSize);

        Matrix decMatrxTemp = GenerateDecryptionMatrix(vecSize, alphabetPower, encryptionMatrix);
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

    //Дешифрует сообщение
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

        Matrix decryptionMatrix = new Matrix(decMtrxTemp);
        Log.i("DecMat", decryptionMatrix.toString());

        int numOfParts = encMsg.get(encMsgIndex++);

        String decMsg = "";
        int c = 0;
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
                c = (int)(decVec.getItem(j, 0) + 65);
                if (c == 26 + 65)
                    decMsg += " ";
                else
                    decMsg += (char)c;
            }
        }

        Log.i("MSG", decMsg);

        return decMsg;
    }
}
