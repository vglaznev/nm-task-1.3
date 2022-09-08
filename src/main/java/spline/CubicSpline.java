package spline;

import function.Function;

import java.util.Arrays;

import static java.lang.Math.pow;

public class CubicSpline implements Function {
    private final double[] b;
    private final double[] c;
    private final double[] d;

    private final double[] nodes;
    private final double[] valuesInNodes;

    public CubicSpline(double[] b, double[] c, double[] d, double[] nodes, double[] valuesInNodes) {
        this.b = b;
        this.c = c;
        this.d = d;
        this.nodes = nodes;
        this.valuesInNodes = valuesInNodes;
    }

    @Override
    public double getY(double x) {
        int i;
        for (i = 1; i < nodes.length - 1 && nodes[i] < x; i++) {
        }


        return valuesInNodes[i] + b[i] * (x - nodes[i]) + c[i] * pow(x - nodes[i], 2) + d[i] * pow(x - nodes[i], 3);
    }
}
