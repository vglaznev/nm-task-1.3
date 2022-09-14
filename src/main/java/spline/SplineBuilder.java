package spline;

import function.Function;
import matrixsolver.TridiagonalMatrixAlgorithm;
import splitter.IntervalSplitter;
import splitter.SplitterFactory;

import java.util.Arrays;
import java.util.Properties;
import java.util.stream.IntStream;

import static java.lang.Math.pow;

public class SplineBuilder {
    private Function function;
    private IntervalSplitter splitter;
    private Double A;
    private Double B;
    private Double beginOfInterval;
    private Double endOfInterval;
    private Integer numberOfSubIntervals;

    public SplineBuilder function(Function function) {
        this.function = function;
        return this;
    }

    public SplineBuilder splitBy(IntervalSplitter splitter) {
        this.splitter = splitter;
        return this;
    }

    public SplineBuilder degree(int degree) {
        this.numberOfSubIntervals = degree;
        return this;
    }

    public SplineBuilder A(double A) {
        this.A = A;
        return this;
    }

    public SplineBuilder B(double B) {
        this.B = B;
        return this;
    }

    public SplineBuilder intervalBounds(double beginOfInterval, double endOfInterval) {
        this.beginOfInterval = beginOfInterval;
        this.endOfInterval = endOfInterval;
        return this;
    }

    public SplineBuilder allExceptFunction(Properties properties) {
        splitter = SplitterFactory.getSplitter(properties.getProperty("splitter"));
        A = Double.parseDouble(properties.getProperty("A"));
        B = Double.parseDouble(properties.getProperty("B"));
        beginOfInterval = Double.parseDouble(properties.getProperty("beginOfInterval"));
        endOfInterval = Double.parseDouble(properties.getProperty("endOfInterval"));
        numberOfSubIntervals = Integer.parseInt(properties.getProperty("numberOfSubIntervals"));
        return this;
    }

    private boolean checkNullFields() {
        return function == null || A == null || B == null || splitter == null || beginOfInterval == null || endOfInterval == null || numberOfSubIntervals == null;
    }

    public CubicSpline build() {
        if (checkNullFields()) {
            throw new NullPointerException("Please set all parameters.");
        }
        double[] nodes = splitter.split(beginOfInterval, endOfInterval, numberOfSubIntervals + 1);
        double[] fval = Arrays.stream(nodes).map(function::getY).toArray();

        int n = numberOfSubIntervals;

        double[] h = IntStream.range(0, n)
                .mapToDouble(i -> nodes[i + 1] - nodes[i])
                .toArray();


        double[] lowerDiagonal = new double[n];
        double[] diagonal = new double[n + 1];
        double[] upperDiagonal = new double[n];
        double[] f = new double[n + 1];

        lowerDiagonal[lowerDiagonal.length - 1] = 0;
        for (int i = 0; i < lowerDiagonal.length - 1; i++) {
            lowerDiagonal[i] = 1 / h[i];
        }

        diagonal[0] = 1;
        diagonal[diagonal.length - 1] = 1;
        for (int i = 1; i < diagonal.length - 1; i++) {
            diagonal[i] = 2 / h[i - 1] + 4 / h[i];
        }

        upperDiagonal[0] = 0;
        for (int i = 1; i < upperDiagonal.length; i++) {
            upperDiagonal[i] = 2 / h[i];
        }

        f[0] = A;
        f[f.length - 1] = B;
        for (int i = 1; i < f.length - 1; i++) {
            f[i] = 3 * ((fval[i] - fval[i - 1]) / pow(h[i - 1], 2) + 2 * (fval[i + 1] - fval[i]) / pow(h[i], 2));
        }

        TridiagonalMatrixAlgorithm solver = new TridiagonalMatrixAlgorithm();

        double[] bCoef = solver.solve(lowerDiagonal, diagonal, upperDiagonal, f);

        double[] cCoef = new double[n];
        double[] dCoef = new double[n];
        double[] aCoef = new double[n];

        for (int i = 1; i < n + 1; i++) {
            aCoef[i - 1] = fval[i];
            dCoef[i - 1] = (bCoef[i] + bCoef[i - 1]) / pow(h[i - 1], 2) - 2 * (fval[i] - fval[i - 1]) / pow(h[i - 1], 3);
            cCoef[i - 1] = (bCoef[i] - bCoef[i - 1]) / (2 * h[i - 1]) + 3 * h[i - 1] * dCoef[i - 1] / 2;
        }
        bCoef = Arrays.copyOfRange(bCoef, 1, bCoef.length);
        return new CubicSpline(aCoef, bCoef, cCoef, dCoef, nodes);
    }
}
