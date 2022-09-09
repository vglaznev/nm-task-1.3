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
        beginOfInterval = Double.parseDouble(properties.getProperty("beginOfInterval"));
        endOfInterval = Double.parseDouble(properties.getProperty("endOfInterval"));
        numberOfSubIntervals = Integer.parseInt(properties.getProperty("degree"));
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

        double[] h = IntStream.range(0, n - 1)
                .mapToDouble(i -> nodes[i + 1] - nodes[i])
                .toArray();


        double[] a = new double[n - 1];
        double[] b = new double[n];
        double[] c = new double[n - 1];
        double[] f = new double[n];

        a[n - 2] = 0;
        for (int i = 0; i < n - 2; i++) {
            a[i] = 1 / h[i];
        }

        b[0] = 1;
        b[n - 1] = 1;

        for (int i = 1; i < n - 1; i++) {
            b[i] = 2 / h[i - 1] + 4 / h[i];
        }

        c[0] = 0;
        for (int i = 0; i < n - 1; i++) {
            a[i] = 2 / h[i + 1];
        }

        f[0] = A;
        f[n - 1] = B;

        for (int i = 1; i < n - 1; i++) {
            f[i] = 3 * ((fval[i] - fval[i - 1]) / pow(h[i - 1], 2) +
                    2 * (fval[i + 1] - fval[i]) / pow(h[i], 2));
        }

        TridiagonalMatrixAlgorithm solver = new TridiagonalMatrixAlgorithm();

        double[] bb = solver.solve(a, b, c, f);

        double[] cc = new double[n];
        double[] dd = new double[n];
        double[] aa = new double[n];

        for (int i = 0; i < n; i++) {
            aa[i] = f[i + 1];
            dd[i] = (bb[i] + bb[i - 1]) / pow()
        }


        return null;
    }
}
