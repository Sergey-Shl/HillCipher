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

    //Generating the Encryption Matrix
    private Matrix GenerateEncryptionMatrix(int vecSize, int alphabetPower)
    {
        Integer[][] encMatrix1 = {{1, 7},
                                 {3, 5}};
        Matrix encryptionMatrix = new Matrix(encMatrix1);
        return encryptionMatrix;
    }

    //Generating the Decryption Matrix
    private Matrix GenerateDecryptionMatrix(int vecSize, int alphabetPower, Matrix encryptionMatrix)
    {
        //Calculate determinant
        Integer determinant = encryptionMatrix.Determinant();

        //Creating and calculating Decryption matrix
        Matrix decryptionMatrix = encryptionMatrix.Minor();
        decryptionMatrix = decryptionMatrix.Cofactor();
        decryptionMatrix = decryptionMatrix.Transpose();
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);

        //Calculating multiplier for Dec Matrix
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
        }

        decryptionMatrix = decryptionMatrix.MultiplyByNum(factor);
        decryptionMatrix = decryptionMatrix.LimitWithinNum(alphabetPower);
        return decryptionMatrix;
    }

    //Decryption the Message func
    public ArrayList<Integer> EncryptMessage(String msg)
    {
        Matrix encryptionMatrix = GenerateEncryptionMatrix(vecSize, alphabetPower);

        //Forming a message: adding VecSize, DecMatrix, number of parts of signs and coded message
        ArrayList<Integer> encMsg = new ArrayList<Integer>(msg.length() + vecSize*vecSize + 1);
        encMsg.add(vecSize);

        Matrix decMatrxTemp = GenerateDecryptionMatrix(vecSize, alphabetPower, encryptionMatrix);
        for (int i = 0; i < vecSize; i++) {
            for (int j = 0; j < vecSize; j++) {
                encMsg.add(decMatrxTemp.getItem(i, j));
            }
        }

        String newMsg = msg.toUpperCase();
        int numOfFullParts = msg.length() / vecSize;

        if(msg.length() % vecSize != 0) {
            numOfFullParts++;
        }

        encMsg.add(numOfFullParts);

        Matrix encVec;
        Integer[] tempVec = new Integer[vecSize];
        int c = 0;
        int index = 0;
        for (int i = 0; i < numOfFullParts; i++) {
            for (int j = 0; j < vecSize; j++) {
                if (index < newMsg.length()) {
                    c = (int) newMsg.charAt(index++);
                    if (c != 32)
                        tempVec[j] = c - 65;
                    else
                        tempVec[j] = 26;
                }
                else
                {
                    tempVec[j] = 26;
                }
            }
            encVec = new Matrix(tempVec);
            encVec = encryptionMatrix.MultiplyByMatrix(encVec);
            encVec = encVec.LimitWithinNum(alphabetPower);
            for (int j = 0; j < vecSize; j++) {
                encMsg.add(encVec.getItem(j, 0));
            }
        }

        return encMsg;
    }

    //Method Decrypts and returns a String message
    public String DecryptMessage(ArrayList<Integer> encMsg)
    {
        int encMsgIndex = 0;

        //Eject vector size and Dec key from message
        vecSize = encMsg.get(encMsgIndex++);
        Integer[][] decMtrxTemp = new Integer[vecSize][vecSize];
        for (int i = 0; i < vecSize; i++) {
            for (int j = 0; j < vecSize; j++) {
                decMtrxTemp[i][j] = encMsg.get(encMsgIndex++);
            }
        }

        Matrix decryptionMatrix = new Matrix(decMtrxTemp);

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
            decVec = decryptionMatrix.MultiplyByMatrix(decVec);
            decVec = decVec.LimitWithinNum(alphabetPower);
            for (int j = 0; j < vecSize; j++) {
                c = (int)(decVec.getItem(j, 0) + 65);
                if (c == 26 + 65)
                    decMsg += " ";
                else
                    decMsg += (char)c;
            }
        }

        return decMsg;
    }
}
