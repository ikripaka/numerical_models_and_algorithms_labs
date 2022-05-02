import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

class MatrixTest {
    @Test
    void multiplication() throws Exception {
        double[][] A = {{1., 2., 3.}, {4., 5., 6.}, {7., 8., 9.}};
        double[][] B = {{2., 3.}, {4., 5.}, {6., 7.}};

        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);
        System.out.println(matrixA);
        System.out.println(matrixB);
        System.out.println(matrixA.multiplication(matrixB));
    }

    @Test
    void sqrtMethod1() throws Exception {
        double[][] A = {{5.5, 7., 6., 5.5}, {7., 10.5, 8., 7.},
                {6., 8., 10.5, 9.}, {5.5, 7, 9, 10.5}};

        double[][] B = {{23.}, {32.}, {33.}, {31.}};
        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);
        System.out.println("A:\n" + matrixA);
        System.out.println("B:\n" + matrixB);
        System.out.println("|Ax - B|:\n" + matrixA.calculateR(matrixA, matrixA.solveWithSqrtMethod(matrixA, matrixB), matrixB));
    }

    @Test
    void sqrtMethod2() throws Exception {
        double[][] A = {{1., 0.42, 0.54, 0.66}, {0.42, 1., 0.32, 0.44},
                {0.54, 0.32, 1., 0.22}, {0.66, 0.44, 0.22, 1.}};

        double[][] B = {{0.3}, {0.5}, {0.7}, {0.9}};
        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);

        System.out.println("A:\n" + matrixA);
        System.out.println("B:\n" + matrixB);
        System.out.println("|Ax - B|:\n" + matrixA.calculateR(matrixA, matrixA.solveWithSqrtMethod(matrixA, matrixB), matrixB));
    }

    @Test
    void iterationMethod1() throws Exception {
        double[][] A = {{1.54, 0.36, 0.22, 0.22}, {0.06, 2.04, -1.68, 0.},
                {0.36, 1.04, -1.9, 0.}, {0., 0.14, 0., 0.78}};

        double[][] B = {{0.3}, {0.5}, {0.7}, {0.9}};
        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);

        System.out.println("A:\n" + matrixA);
        System.out.println("B:\n" + matrixB);
        System.out.println("|Ax - B|:\n" + matrixA.calculateR(matrixA, matrixA.solveWithSimpleIterations(matrixA, matrixB, 0.01), matrixB));
    }

    @Test
    void iterationMethod2() throws Exception {
        double[][] A = {{2., 0.5, 0.5, 0.}, {3.5, 17.5, 1., 0.},
                {-0.5, 0., 4., 3.}, {0., 0., 3., 5.}};

        double[][] B = {{23.}, {32.}, {33.}, {31.}};
        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);

        System.out.println("A:\n" + matrixA);
        System.out.println("B:\n" + matrixB);
        System.out.println("|Ax - B|:\n" + matrixA.calculateR(matrixA, matrixA.solveWithSimpleIterations(matrixA, matrixB, 0.01), matrixB));
    }

    @Test
    void danilevskiyMethod() throws Exception {
        double[][] A = {{6.26, 1.11, 0.78, 1.21}, {1.11, 4.16, 1.30, 0.16},
                {0.78, 1.30, 5.44, 2.10}, {1.21, 0.16, 2.10, 6.10}};

        Matrix matrixA = new Matrix(A);
        System.out.println(Arrays.toString(matrixA.calculateEigenvalues(matrixA)));
    }
}

