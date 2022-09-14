import function.Function;
import function.FunctionUtil;
import function.SimpleFunction;
import reader.InterpolationConfigurationFileReader;
import reader.Reader;
import spline.CubicSpline;
import spline.SplineBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Math.*;

public class Main {
    private static final int PLOTTER_RESOLUTION = 1000;

    public static void main(String[] args) throws IOException {
        Reader reader = new InterpolationConfigurationFileReader("spline.properties");
        Properties properties = reader.read();

        SimpleFunction function = new SimpleFunction(x -> sin(x));

        SplineBuilder builder = new SplineBuilder();
        CubicSpline spline = builder.function(function)
                .allExceptFunction(properties)
                .build();

        double beginOfInterval = Double.parseDouble(properties.getProperty("beginOfInterval"));
        double endOfInterval = Double.parseDouble(properties.getProperty("endOfInterval"));

        Function errorFunction = new SimpleFunction(x -> abs(function.getY(x) - spline.getY(x)));
        System.out.println("Максимальное значение ошибки: " + FunctionUtil.max(errorFunction, beginOfInterval, endOfInterval, 0.01));

        Plotter plotter = new Plotter();
        plotter.addGraphic(FunctionUtil.getTableFunction(function, beginOfInterval, endOfInterval, PLOTTER_RESOLUTION),
                "Исходная функция",
                Color.BLACK
        );
        plotter.addGraphic(FunctionUtil.getTableFunction(spline, beginOfInterval, endOfInterval, PLOTTER_RESOLUTION),
                "Сплайн",
                Color.RED
        );
        plotter.addGraphic(FunctionUtil.getTableFunction(errorFunction, beginOfInterval, endOfInterval, PLOTTER_RESOLUTION),
                "График погрешности",
                Color.GREEN
                );
        plotter.addDots(spline.getInterpolationGrid(), "Узлы интерполяции");

        plotter.display();
    }
}
