package spline;

import function.Function;

import static java.lang.Math.pow;

public class CubicSpline implements Function {
    private final double[] a;
    private final double[] b;
    private final double[] c;
    private final double[] d;

    private final double[] nodes;
    /*private final double[] valuesInNodes;*/

    public CubicSpline(double[] a, double[] b, double[] c, double[] d, double[] nodes) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.nodes = nodes;
    }

    @Override
    public double getY(double x) {
        int i;
        for (i = 0; i < nodes.length - 2 && nodes[i + 1] < x; i++) {
        }
        System.out.println("i= " + i + " x= " + x);
        return a[i] + b[i] * (x - nodes[i + 1]) + c[i] * pow(x - nodes[i + 1], 2) + d[i] * pow(x - nodes[i + 1], 3);
    }
}
