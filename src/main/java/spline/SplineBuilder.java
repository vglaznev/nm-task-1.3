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
        double[] functionValues = Arrays.stream(nodes).map(function::getY).toArray();

        int n = numberOfSubIntervals;

        double[] h = IntStream.range(0, n)
                .mapToDouble(i -> nodes[i + 1] - nodes[i])
                .toArray();


        double[] lowerDiagonal = new double[n];
        double[] diagonal = new double[n + 1];
        double[] upperDiagonal = new double[n];
        double[] constantTerm = new double[n + 1];

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

        constantTerm[0] = A;
        constantTerm[constantTerm.length - 1] = B;
        for (int i = 1; i < constantTerm.length - 1; i++) {
            constantTerm[i] = 3 * ((functionValues[i] - functionValues[i - 1]) / pow(h[i - 1], 2) + 2 * (functionValues[i + 1] - functionValues[i]) / pow(h[i], 2));
        }

        TridiagonalMatrixAlgorithm solver = new TridiagonalMatrixAlgorithm();

        double[] b = solver.solve(lowerDiagonal, diagonal, upperDiagonal, constantTerm);

        double[] a = new double[n];
        double[] c = new double[n];
        double[] d = new double[n];

        for (int i = 1; i < n + 1; i++) {
            a[i - 1] = functionValues[i];
            d[i - 1] = (b[i] + b[i - 1]) / pow(h[i - 1], 2) - 2 * (functionValues[i] - functionValues[i - 1]) / pow(h[i - 1], 3);
            c[i - 1] = (b[i] - b[i - 1]) / (2 * h[i - 1]) + 3 * h[i - 1] * d[i - 1] / 2;
        }
        b = Arrays.copyOfRange(b, 1, b.length);
        return new CubicSpline(a, b, c, d, nodes, functionValues);
    }
}
