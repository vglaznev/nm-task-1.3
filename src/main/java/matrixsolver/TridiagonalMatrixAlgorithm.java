package matrixsolver;

public class TridiagonalMatrixAlgorithm {
    public double[] solve(double[] lowerDiagonal, double[] diagonal, double[] upperDiagonal, double[] constTerm) {
        int n = constTerm.length;
        double[] alpha = new double[constTerm.length];
        double[] beta = new double[constTerm.length];
        double[] solution = new double[constTerm.length];
        double denominator;

        alpha[0] = -upperDiagonal[0] / diagonal[0];
        beta[0] = constTerm[0] / diagonal[0];


        for (int i = 1; i < n; i++) {
            denominator = diagonal[i] + lowerDiagonal[i - 1] * alpha[i - 1];
            if (i < n - 1)
                alpha[i] = -upperDiagonal[i] / denominator;
            beta[i] = (constTerm[i] - lowerDiagonal[i - 1] * beta[i - 1]) / denominator;
        }

        solution[n - 1] = beta[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            solution[i] = alpha[i] * solution[i + 1] + beta[i];
        }

        return solution;
    }
}
