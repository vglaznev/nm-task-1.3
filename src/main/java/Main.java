import function.FunctionUtil;
import matrixsolver.TridiagonalMatrixAlgorithm;
import reader.InterpolationConfigurationConsoleReader;
import reader.InterpolationConfigurationFileReader;
import reader.Reader;
import spline.SplineBuilder;
import splitter.SplitterFactory;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.IntStream;

import static java.lang.Math.sin;

public class Main {
    public static void main(String[] args) throws IOException {
        TridiagonalMatrixAlgorithm tr = new TridiagonalMatrixAlgorithm();
        var sol = tr.solve(new double[]{5, 2}, new double[]{2, 4, -3}, new double[]{-1, 2}, new double[]{3, 6, 2});
        Arrays.stream(sol).forEach(System.out::print);
        /*1.4893617021276595-0.021276595744680854-0.6808510638297872*/

        Reader reader = new InterpolationConfigurationFileReader("spline.properties");
        Properties properties = reader.read();

        SplineBuilder builder = new SplineBuilder();
        var spline = builder.function(x -> sin(x))
                .allExceptFunction(properties)
                .build();

        var beginOfInterval = Double.parseDouble(properties.getProperty("beginOfInterval"));
        var endOfInterval = Double.parseDouble(properties.getProperty("endOfInterval"));

        Plotter plotter = new Plotter();
        plotter.addGraphic(FunctionUtil.getTableFunction(x -> sin(x), beginOfInterval, 1.2, 1000),
                "originalFunction",
                Color.BLACK
        );
        plotter.addGraphic(FunctionUtil.getTableFunction(spline, beginOfInterval, 0.5, 1000),
                "spline",
                Color.RED
        );
        plotter.display();

        var l = SplitterFactory.getSplitter("uniform").split(0, 1.5, 5);
        System.out.println(Arrays.toString(l));
        Arrays.stream(l).map(x -> sin(x)).forEach(System.out::print);
        System.out.println(Arrays.toString(IntStream.range(0, l.length - 1)
                .mapToDouble(i -> l[i + 1] - l[i]).toArray()));

    }
}
