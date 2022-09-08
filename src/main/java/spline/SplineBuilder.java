package spline;

import function.Function;
import splitter.IntervalSplitter;
import splitter.SplitterFactory;

import java.util.Arrays;
import java.util.Properties;

public class SplineBuilder {
    private Function function;
    private IntervalSplitter splitter;
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

    public SplineBuilder intervalBounds(double beginOfInterval, double endOfInterval) {
        this.beginOfInterval = beginOfInterval;
        this.endOfInterval = endOfInterval;
        return this;
    }

    public SplineBuilder allExceptFunction(Properties properties){
        splitter = SplitterFactory.getSplitter(properties.getProperty("splitter"));
        beginOfInterval = Double.parseDouble(properties.getProperty("beginOfInterval"));
        endOfInterval = Double.parseDouble(properties.getProperty("endOfInterval"));
        numberOfSubIntervals = Integer.parseInt(properties.getProperty("degree"));
        return this;
    }

    private boolean checkNullFields() {
        return function == null || splitter == null || beginOfInterval == null || endOfInterval == null || numberOfSubIntervals == null;
    }

    public CubicSpline build() {
        if (checkNullFields()) {
            throw new NullPointerException("Please set all parameters.");
        }
        double[] nodes = splitter.split(beginOfInterval, endOfInterval, numberOfSubIntervals);
        double[] functionValueInNodes = Arrays.stream(nodes).map(function::getY).toArray();

        return null;
    }
}
