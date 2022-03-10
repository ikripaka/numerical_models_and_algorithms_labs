import org.junit.jupiter.api.Test;

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
    void solveEquation() throws Exception {
        double[][] A = {{6.92, 1.28, 0.79, 1.15, -0.66}, {0.92, 3.5, 1.3, -1.62, 1.02},
                {1.15, -2.46, 6.1, 2.1, 1.483}, {1.33, 0.16, 2.1, 5.44, -18.},
                {1.14, -1.68, -1.217, 9., -3.}};

        double[][] B = {{11.172, 0.115, 0.009, 9.349, 1}};
        Matrix matrixA = new Matrix(A);
        Matrix matrixB = new Matrix(B);
        Matrix matrixС = new Matrix(5, 5);

        System.out.println(matrixA);
        System.out.println(matrixB);
//        System.out.println(matrixС);

        matrixA.solveEquation(matrixA, matrixB);
    }
}