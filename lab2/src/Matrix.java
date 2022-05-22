import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
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
        this.matrix = matrix.clone();
        for (int i = 0; i < matrix.length; i++) {
            this.matrix[i] = matrix[i].clone();
        }
    }

    public Matrix(int length, int height) throws Exception {
        if (length <= 0 || height <= 0) {
            throw new Exception("incorrect matrix bounds");
        }
        this.length = length;
        this.height = height;
        matrix = new double[height][length];
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
        Matrix newMatrix = new Matrix(B.length, height);
        for (int i = 0; i < B.height; i++) {
            for (int j = 0; j < B.length; j++) {
                for (int k = 0; k < length; k++) {
                    newMatrix.matrix[i][j] += matrix[i][k] * B.matrix[k][j];
                }
            }
        }
        return newMatrix;
    }

    private Matrix multiplication(Matrix A, Matrix B) throws Exception {
        if (A.length != B.height) {
            throw new Exception("incorrect matrix sizes A.length != B.height");
        }
        Matrix newMatrix = new Matrix(B.length, A.height);
        for (int i = 0; i < B.height; i++) {
            for (int j = 0; j < B.length; j++) {
                for (int k = 0; k < A.length; k++) {
                    newMatrix.matrix[i][j] += A.matrix[i][k] * B.matrix[k][j];
                }
            }
        }
        return newMatrix;
    }

    public Matrix sum(Matrix B) throws Exception {
        if (length != B.length) {
            throw new Exception("incorrect matrix sizes A.length != B.length");
        } else if (height != B.height) {
            throw new Exception("incorrect matrix sizes A.height != B.height");
        }
        Matrix newMatrix = new Matrix(length, B.height);
        for (int i = 0; i < B.height; i++) {
            for (int j = 0; j < B.length; j++) {
                newMatrix.matrix[i][j] = matrix[i][j] + B.matrix[i][j];
            }
        }
        return newMatrix;
    }

    public Matrix subtraction(Matrix B) throws Exception {
        if (length != B.length) {
            throw new Exception("incorrect matrix sizes A.length != B.length");
        } else if (height != B.height) {
            throw new Exception("incorrect matrix sizes A.height != B.height");
        }
        Matrix newMatrix = new Matrix(length, height);
        for (int i = 0; i < B.height; i++) {
            for (int j = 0; j < B.length; j++) {
                newMatrix.matrix[i][j] = matrix[i][j] - B.matrix[i][j];
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
    public Matrix solveWithSqrtMethod(Matrix A, Matrix B) throws Exception {
        // A * X = B
        // A = L * Lt => L * Lt * X = B
        // L * Lt = A
        // calculate triangular coeff
        // find Y
        // L * Y = B
        // Lt * X = Y
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
        Matrix L = Lt.transpose();
        System.out.println("L:\n" + L);
        System.out.println("Lt:\n" + Lt);

        Matrix Y = new Matrix(1, L.height);
        for (int i = 0, counter = 1; i < counter && i < height; i++, counter++) {
            double sum = 0;
            for (int j = 0; j < i; j++) {
                sum += L.matrix[i][j] * Y.matrix[j][0];
            }
            Y.matrix[i][0] = (B.matrix[i][0] - sum) / L.matrix[i][i];
        }

        System.out.println("Y:\n" + Y);
        Matrix X = new Matrix(1, A.height);
        for (int i = height - 1; i >= 0; i--) {
            double sum = 0;
            for (int j = i + 1; j < length; j++) {
                sum += Lt.matrix[i][j] * X.matrix[j][0];
            }
            X.matrix[i][0] = (Y.matrix[i][0] - sum) / Lt.matrix[i][i];
        }
        System.out.println("X:\n" + X);

        return X;
    }


    //solves Ax=B => x_k = Cx_k-1+d
    public Matrix solveWithSimpleIterations(Matrix A, Matrix B, double precision) throws Exception {
        //check matrix diagonal superiority
        //transform matrix into look x = Cx + b, where first x is diagonal el (find c_ij elements representation)
        //check convergence condition
        //build iterations and calculate approximation

        Matrix C = new Matrix(A.matrix);
        Matrix b = new Matrix(B.matrix);
        for (int i = 0; i < A.height; i++) {
            b.matrix[i][0] /= A.matrix[i][i];
            C.matrix[i][i] = 0;
            for (int j = 0; j < A.length; j++) {
                if (i != j) {
                    C.matrix[i][j] = C.matrix[i][j] != 0 ? -C.matrix[i][j] / A.matrix[i][i] : 0;
                }
            }
        }

        //x_k+1 element
        Matrix x_k_1 = new Matrix(1, B.height);
        Matrix x_k = new Matrix(1, B.height);
        double q = maxLineSumAbs(C), e = 0;

        for (int k = 0; k < 1 || e > precision; k++) {
            System.out.println("* iteration: " + k + " ===============================================================");
            System.out.println("q: " + q);
            System.out.println("x_k: \n" + x_k);
            System.out.println("|Ax-B|:\n" + calculateR(A, x_k, B));
            System.out.println(e + " > " + precision);

            for (int i = 0; i < A.height; i++) {
                for (int j = 0; j < A.length; j++) {
                    if (i != j) {
                        x_k_1.matrix[i][0] += x_k.matrix[j][0] != 0 && C.matrix[i][j] != 0
                                ? x_k.matrix[j][0] * C.matrix[i][j]
                                : 0;
                    }
                }
            }
            x_k_1 = x_k_1.sum(b);

            double maxDiff = 0;
            for (int i = 0; i < A.height; i++) {
                double diff = Math.abs(Math.abs(x_k_1.matrix[i][0]) - Math.abs(x_k.matrix[i][0]));
                if (diff > maxDiff) {
                    maxDiff = diff;
                }
            }
            e = maxDiff != 0 ? maxDiff * q / (1 - q) : 0;
            x_k = new Matrix(x_k_1.matrix);
            x_k_1 = new Matrix(x_k_1.length, x_k_1.height);
        }

        System.out.println(" ===============================================================");
        return x_k;
    }

    public double[] calculateEigenvalues(Matrix A) throws Exception {
        ArrayList<Matrix> MList = new ArrayList<>();
        ArrayList<Matrix> MListInverse = new ArrayList<>();
        ArrayList<Matrix> PList = new ArrayList<>();

        for (int i = 0; i < length - 1; i++) {
            MList.add(singleMatrix(length));
            MListInverse.add(singleMatrix(length));
            PList.add(new Matrix(length, length));
        }
        PList.add((Matrix) A.clone());

        for (int i = length - 2; i >= 0; i--) {
            System.out.println("Iteration: " + (i + 1) + " ======================");
            for (int j = 0; j < length; j++) {
                if (j != i) {
                    if (PList.get(i + 1).matrix[i + 1][j] == 0 || PList.get(i + 1).matrix[i + 1][i] == 0)
                        MList.get(i).matrix[i][j] = 0;
                    else
                        MList.get(i).matrix[i][j] = -PList.get(i + 1).matrix[i + 1][j] / PList.get(i + 1).matrix[i + 1][i];

                } else {
                    if (PList.get(i + 1).matrix[i + 1][i] == 0)
                        MList.get(i).matrix[i][j] = 0;
                    else
                        MList.get(i).matrix[i][j] = 1 / PList.get(i + 1).matrix[i + 1][i];
                }
                MListInverse.get(i).matrix[i][j] = PList.get(i + 1).matrix[i + 1][j];
            }
            PList.set(i, multiplication(multiplication(MListInverse.get(i), PList.get(i + 1)), MList.get(i)));
            System.out.println("");
            System.out.println("M " + (i + 1) + ":\n" + MList.get(i));
            System.out.println("M^(-1) " + (i + 1) + ":\n" + MListInverse.get(i));
            System.out.println("P " + (i + 1) + ":\n" + PList.get(i));
        }


        double[] coefficients = new double[length + 1];
        coefficients[0] = 1;
        for (int i = 0; i < length; i++) {
            coefficients[i + 1] = -PList.get(0).matrix[0][i];
        }

        StringBuilder builder = new StringBuilder("");
        for (int i = length; i >= 0; i--) {
            if (i == 0) {
                builder.insert(0, "x^" + (length - i));
            } else if (i == length) {
                builder.append(" + ").append(coefficients[i]);
            } else {
                builder.insert(0, " + " + coefficients[i] + "x^" + (length - i));
            }
        }
        System.out.println("Characteristic polynomial: " + builder.toString());


        return coefficients;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        try {
            Matrix newMatrix = new Matrix(length, height);
            newMatrix.matrix = matrix.clone();
            return newMatrix;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Matrix singleMatrix(int length) throws Exception {
        Matrix newMatrix = new Matrix(length, length);
        for (int i = 0; i < length; i++) {
            newMatrix.matrix[i][i] = 1;
        }
        return newMatrix;
    }

    private double maxLineSumAbs(Matrix A) {
        double max = 0, sum = 0;
        for (int i = 0; i < A.height; i++) {
            for (int j = 0; j < A.length; j++) {
                sum += Math.abs(A.matrix[i][j]);
            }
            if (sum > max) {
                max = sum;
            }
            sum = 0;
        }
        return max;
    }

    //|Ax - B|
    public Matrix calculateR(Matrix A, Matrix X, Matrix B) throws Exception {
        Matrix matrixModule = new Matrix(A.matrix);
        matrixModule = matrixModule.multiplication(X).subtraction(B).abs();
        return matrixModule;
    }

    private Matrix abs() throws Exception {
        Matrix matrixMod = new Matrix(length, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                matrixMod.matrix[i][j] = Math.abs(matrix[i][j]);
            }
        }
        return matrixMod;
    }

}