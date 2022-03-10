import java.util.Arrays;

public class Matrix {
    public double getMatrixValue(int i, int k) {
        return matrix[i][k];
    }

    public void setMatrixValue(int i, int k, double value) {
        matrix[i][k] = value;
    }

    private double[][] matrix;
    public int length, height;


    public Matrix(double[][] matrix) throws Exception {
        checkCorrectness(matrix);
        length = matrix[0].length;
        height = matrix.length;
        this.matrix = matrix;
    }

    public Matrix(int length, int height) throws Exception {
        if (length <= 0 || height <= 0) {
            throw new Exception("incorrect matrix bounds");
        }
        this.length = length;
        this.height = height;
        matrix = new double[length][height];
    }

    private void checkCorrectness(double[][] matrix) throws Exception {
        if (matrix == null || matrix.length == 0) {
            throw new NullPointerException("empty matrix");
        }
        int length = matrix[0].length;
        for (double[] doubles : matrix) {
            if (doubles.length != length) {
                throw new Exception("incorrect matrix line length (not the same in all lines)");
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (double[] doubles : matrix) {
            builder.append(Arrays.toString(doubles)).append("\n");
        }
        return builder.toString();
    }

    public Matrix multiplication(Matrix B) throws Exception {
        if (length != B.height) {
            throw new Exception("incorrect matrix sizes A.length != B.height");
        }
        Matrix newMatrix = new Matrix(length, B.length);
        for (int i = 0; i < B.height; i++) {
            for (int j = 0; j < B.length; j++) {
                for (int k = 0; k < length; k++) {
                    newMatrix.matrix[i][j] += matrix[i][k] * B.matrix[k][j];
                }
            }
        }
        return newMatrix;
    }

    public Matrix transpose() throws Exception {
        Matrix transposed = new Matrix(length, height);
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < height; j++) {
                transposed.matrix[i][j] = matrix[j][i];
            }
        }
        return transposed;
    }

    //solves Ax=B equation
    public void solveEquation(Matrix A, Matrix B) throws Exception {
        // A * X = B
        // A = L * Lt => L * Lt * X = B
        // L * Lt = A
        // calculate triangular coeff
        // Lt * X = Y
        // find Y
        // L * Y = B
        Matrix Lt = new Matrix(A.length, A.height);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                if (i > j) {
                } else if (i == 0 && j == 0) {
                    Lt.matrix[0][0] = Math.sqrt(A.matrix[0][0]); //t00 (aka t11)
                } else if (i == 0) {
                    Lt.matrix[0][j] = A.matrix[0][j] / Lt.matrix[0][0]; //t 0j (aka t 1j)
                } else if (i == j) {
                    double squaredSum = 0;
                    for (int k = 0; k < i; k++) {
                        squaredSum += Lt.matrix[k][i] * Lt.matrix[k][i];
                    }
                    if(i == 4 && j == 4)
                        System.out.println(A.matrix[i][i] - squaredSum + " -- " + Math.sqrt(A.matrix[i][i] - squaredSum));
                    Lt.matrix[i][i] = Math.sqrt(A.matrix[i][i] - squaredSum);
                } else if (j > i) {
                    double sum = 0;
                    for (int k = 0; k < i; k++) {
                        sum += Lt.matrix[k][i] * Lt.matrix[k][j];
                    }
                    Lt.matrix[i][j] = (A.matrix[i][j] - sum) / Lt.matrix[i][i];
                }
            }
        }
        System.out.println(Lt);
        Matrix L = Lt.transpose();
        System.out.println(L);
    }

}
