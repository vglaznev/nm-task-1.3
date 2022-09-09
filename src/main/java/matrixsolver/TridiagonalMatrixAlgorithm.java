package matrixsolver;

public class TridiagonalMatrixAlgorithm {
    public double[] solve(double[] a, double[] b, double[] c, double[] f) {
        int n = f.length;
        double[] alpha = new double[f.length];
        double[] beta = new double[f.length];
        double[] solution = new double[f.length];
        double denominator;

        alpha[0] = -c[0] / b[0];
        beta[0] = f[0] / b[0];


        for (int i = 1; i < n; i++) {
            denominator = b[i] + a[i - 1] * alpha[i - 1];
            if (i < n - 1)
                alpha[i] = -c[i] / denominator;
            beta[i] = (f[i] - a[i - 1] * beta[i - 1]) / denominator;
        }

        solution[n - 1] = beta[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            solution[i] = alpha[i] * solution[i + 1] + beta[i];
        }

        return solution;
    }
}
