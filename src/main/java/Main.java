import function.FunctionUtil;
import reader.InterpolationConfigurationFileReader;
import reader.Reader;
import spline.SplineBuilder;

import java.awt.*;
import java.io.IOException;
import java.util.Properties;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Main {
    public static void main(String[] args) throws IOException {
        Reader reader = new InterpolationConfigurationFileReader("spline.properties");
        Properties properties = reader.read();

        SplineBuilder builder = new SplineBuilder();
        var spline = builder.function(x -> sin(x))
                .allExceptFunction(properties)
                .build();

        var beginOfInterval = Double.parseDouble(properties.getProperty("beginOfInterval"));
        var endOfInterval = Double.parseDouble(properties.getProperty("endOfInterval"));

        Plotter plotter = new Plotter();
        plotter.addGraphic(FunctionUtil.getTableFunction(x -> sin(x), beginOfInterval, endOfInterval, 1000),
                "originalFunction",
                Color.BLACK
        );
        plotter.addGraphic(FunctionUtil.getTableFunction(spline, beginOfInterval, endOfInterval, 1000),
                "spline",
                Color.RED
        );
        plotter.display();
    }
}
