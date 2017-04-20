package com.serge.hillcipher;

import android.support.annotation.IntRange;

import java.util.Random;

/**
 * Created by serge on 07.04.2017.
 */

public class Matrix {
    private Integer m;
    private Integer n;
    private Integer[][] matrix;

    Matrix(int n, int m)
    {
        this.n = n;
        this.m = m;
        matrix = new Integer[n][m];
    }

    //Square Matrix
    Matrix(int n)
    {
        this.n = this.m = n;
        matrix = new Integer[n][n];
    }

    Matrix(Integer[][] matrix)
    {
        this.n = matrix.length;
        this.m = matrix[0].length;
        this.matrix = matrix;
    }

    //Vector
    Matrix(Integer[] vec)
    {
        this.n = vec.length;
        this.m = 1;
        this.matrix = new Integer[n][m];
        for (int i = 0; i < vec.length; i++) {
            matrix[i][0] = vec[i];
        }
    }

    Matrix(Matrix matrix)
    {
        this.m = matrix.m;
        this.n = matrix.n;
        this.matrix = new Integer[this.n][this.m];
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.m; j++) {
                this.matrix[i][j] = matrix.matrix[i][j];
            }
        }
    }

    //Returns value of Determinant
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

    //Returns Minor Matrix
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

    //This function used to calculate Determinant by recursive call
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

    //Cut n-row and m-column of Matrix
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

    //Returns cofactor matrix
    public Matrix Cofactor()
    {
        Matrix cofactorMatrix = new Matrix(this);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if ((i + j) % 2 != 0)
                    cofactorMatrix.matrix[i][j] = -cofactorMatrix.matrix[i][j];
            }
        }
        return cofactorMatrix;
    }

    //Returns transposed matrix
    public Matrix Transpose()
    {
        Integer[][] transposedMatrix = new Integer[m][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return new Matrix(transposedMatrix);
    }

    //Returns multiplication matrix by number
    public Matrix MultiplyByNum(int num)
    {
        Matrix newMatrix = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                newMatrix.matrix[i][j] = matrix[i][j] * num;
            }
        }
        return newMatrix;
    }

    //Returns multiplication matrix by matrix
    public Matrix MultiplyByMatrix(Matrix matrix)
    {
        Matrix result = new Matrix(n, matrix.m);
        for (int i = 0; i < result.n; i++) {
            for (int j = 0; j < result.m; j++) {
                result.matrix[i][j] = 0;
                for (int k = 0; k < m; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * matrix.matrix[k][j];
                }
            }
        }
        return result;
    }

    //Limit values of matrix between 0 and Number
    public Matrix LimitWithinNum(int num)
    {
        Matrix newLimitedMatrix = new Matrix(this);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                while (newLimitedMatrix.matrix[i][j] < 0 || newLimitedMatrix.matrix[i][j] > num)
                {
                    if (newLimitedMatrix.matrix[i][j] < 0)
                        newLimitedMatrix.matrix[i][j] += num;
                    else
                        newLimitedMatrix.matrix[i][j] -= num;
                }
            }
        }
        return newLimitedMatrix;
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

    //Get matrix value
    Integer getItem(int n, int m)
    {
        return matrix[n][m];
    }

    //Generating square matrix size*size with random values
    static Matrix GenerateMatrix(int size, int maxItemSize)
    {
        Matrix generatedMatrix = new Matrix(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                generatedMatrix.matrix[i][j] = random.nextInt(maxItemSize);
            }
        }
        while(generatedMatrix.Determinant() == 0)
        {
            generatedMatrix.matrix[random.nextInt(size)][random.nextInt(size)] = random.nextInt(maxItemSize);
        }
        generatedMatrix = generatedMatrix.LimitWithinNum(maxItemSize);
        return generatedMatrix;
    }

}
