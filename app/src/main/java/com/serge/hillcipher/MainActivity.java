package com.serge.hillcipher;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by serge on 07.04.2017.
 */

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Integer[][] M = {{3, -3, -5, 8},
                {-3, 2, 4, -6},
                {2, -5, -7, 5},
                {-4, 3, 5, -6}};

        Integer[][] M2 = {{1, -2, 3},
                {4, 0, 6},
                {-7, 8, 9}};

        Integer[][] M3 = {{1, 2},
                {3, 4}};

        Integer[][] M4 = {{2, 5, 7},
                {6, 3, 4},
                {5, -2, -3}};

        Matrix matrix = new Matrix(M4);
        Matrix minorMatrix = matrix.Minor();
        Matrix cofactorMatrix = minorMatrix.Cofactor();
        Matrix transposeMatrix = cofactorMatrix.Transpose();

        int determinant =  matrix.Determinant();
/*        Log.i("INFO", matrix.toString());
        Log.i("INFO", String.valueOf(determinant));
        Log.i("INFO", minorMatrix.toString());
        Log.i("INFO", cofactorMatrix.toString());
        Log.i("INFO", transposeMatrix.toString());
        Log.i("INFO", transposeMatrix.toString());*/

        HillCipher hillCipher = new HillCipher();
    }

}
