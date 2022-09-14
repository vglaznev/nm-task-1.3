package spline;

import function.Function;
import function.TableFunction;

import static java.lang.Math.pow;

public class CubicSpline implements Function {
    private final double[] a;
    private final double[] b;
    private final double[] c;
    private final double[] d;

    private final double[] nodes;
    private final double[] functionValuesInNodes;

    public CubicSpline(double[] a, double[] b, double[] c, double[] d, double[] nodes, double[] functionValuesInNodes) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.nodes = nodes;
        this.functionValuesInNodes = functionValuesInNodes;
    }

    public TableFunction getInterpolationGrid(){
        return new TableFunction(nodes, functionValuesInNodes);
    }

    @Override
    public double getY(double x) {
        int i;
        for (i = 0; i < nodes.length - 2 && nodes[i + 1] < x; i++) {
        }
        return a[i] + b[i] * (x - nodes[i + 1]) + c[i] * pow(x - nodes[i + 1], 2) + d[i] * pow(x - nodes[i + 1], 3);
    }
}
