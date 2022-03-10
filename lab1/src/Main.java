import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Double> bounds = new ArrayList<>();
        bounds.add(0, -0.44);
        bounds.add(0, -1.8027);

        bisectionMethod(0.00002, bounds);
        chordsMethod(0.00002, bounds);
        newtonMethod(0.00002, bounds, -2);
    }

    public static void bisectionMethod(double e, ArrayList<Double> bounds) {
        System.out.println("\n Bisection method \n");
        for (int boundsSize = bounds.size() / 2, i = 0; i < boundsSize; i++) {
            double a = bounds.get(i * 2), b = bounds.get(i * 2 + 1);

            for (int j = 0; Math.abs(a - b) >= e; j++) {
                double c = (a + b) / 2;
                System.out.println("Iteration: " + j + "|a - b| = " + Math.abs(a - b) +
                        ", a: " + a + ", b: " + b + ", c: " + c);

                if (calculatePolynomialValue(a) * calculatePolynomialValue(c) <= 0) {
                    b = c;
                } else if (calculatePolynomialValue(c) * calculatePolynomialValue(b) <= 0) {
                    a = c;
                }
            }
        }
    }

    public static void chordsMethod(double e, ArrayList<Double> bounds) {
        System.out.println("\n Chords method");

        for (int boundsSize = bounds.size() / 2, i = 0; i < boundsSize; i++) {
            double a = bounds.get(i * 2), b = bounds.get(i * 2 + 1),
                    c = 100;

            for (int j = 0; Math.abs(calculatePolynomialValue(c)) >= e; j++) {
                System.out.println("Iteration: " + j + ", a: " + a + ", b: " + b + " , c " + c + " , |f(c)|: " + Math.abs(calculatePolynomialValue(c)));
                c = (a * calculatePolynomialValue(b) - b * calculatePolynomialValue(a))
                        / (calculatePolynomialValue(b) - calculatePolynomialValue(a));

                if (calculatePolynomialValue(a) * calculatePolynomialValue(c) <= 0) {
                    b = c;
                }
                if (calculatePolynomialValue(c) * calculatePolynomialValue(b) <= 0) {
                    a = c;
                }
            }
        }
    }

    public static void newtonMethod(double e, ArrayList<Double> bounds, double x) {
        System.out.println("\n Newton method");

        for (int boundsSize = bounds.size() / 2, i = 0; i < boundsSize; i++) {

            for (int j = 0; Math.abs(calculatePolynomialValue(x)) >= e; j++) {
                System.out.println("Iteration: " + j + ", x: " + x + ", |f(x)|: " + Math.abs(calculatePolynomialValue(x)));
                x = x - calculatePolynomialValue(x) / calculatePolynomialDerivativeValue(x);
            }
        }
    }

    public static double calculatePolynomialDerivativeValue(double x) {
        return 20 * Math.pow(x, 4) - 12 * Math.pow(x, 3) - 3 * Math.pow(x, 2);
    }

    public static double calculatePolynomialValue(double x) {
        return 4 * Math.pow(x, 5) - 3 * Math.pow(x, 4) - Math.pow(x, 3) + 12;
    }
}
