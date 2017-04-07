package com.serge.hillcipher;

import android.support.annotation.IntRange;

/**
 * Created by serge on 07.04.2017.
 */

public class Matrix {
    Integer m;
    Integer n;
    Integer[][] matrix;

    Matrix(int n, int m)
    {
        this.n = n;
        this.m = m;
        matrix = new Integer[n][m];
    }

    Matrix(int n)
    {
        this.n = this.m = n;
        matrix = new Integer[n][n];
    }

    Matrix(Integer[][] matrix)
    {
        this.m = matrix.length;
        this.n = matrix[0].length;
        this.matrix = matrix;
    }

    Matrix(Matrix matrix)
    {
        this.matrix = matrix.matrix;
    }

    public Integer Determinant()
    {
        Integer result = 0;
        for(int i = 0; i < matrix[0].length; i++)
        {
            if(i % 2 == 0)
                result += matrix[0][i] * Determinant(Remove(matrix, 0, i));
            else
                result += -matrix[0][i] * Determinant(Remove(matrix, 0, i));
        }
        return result;
    }

    public Matrix Minor() {
        Integer[][] minorMatrix = new Integer[n][m];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++)
            {
                minorMatrix[i][j] = Determinant(Remove(matrix, i, j));
            }
        }
        return new Matrix(minorMatrix);
    }

    private Integer Determinant(Integer[][] matrix)
    {
        Integer result = 0;
        if (matrix.length == 1)
            return matrix[0][0];
        for(int i = 0; i < matrix[0].length; i++)
        {
            if(i % 2 == 0)
                result += matrix[0][i] * Determinant(Remove(matrix, 0, i));
            else
                result += -matrix[0][i] * Determinant(Remove(matrix, 0, i));
        }
        return result;
    }

    private Integer[][] Remove(Integer[][] matrix, int n, int m)
    {
        Integer[][] newMatrix = new Integer[matrix.length - 1][matrix[0].length - 1];
        for (int i = 0, ii = 0; i < matrix.length - 1; i++, ii++) {
            if (n == i)
                ii++;
            for (int j = 0, jj = 0; j < matrix[0].length - 1; j++, jj++) {
                if (m == j)
                    jj++;
                newMatrix[i][j] = matrix[ii][jj];
            }
        }
        return newMatrix;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < m; j++) {
                s += matrix[i][j] + " ";
            }
            s += "\n";
        }
        return s;
    }
}
