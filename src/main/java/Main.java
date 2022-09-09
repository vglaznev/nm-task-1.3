import matrixsolver.TridiagonalMatrixAlgorithm;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        TridiagonalMatrixAlgorithm tr = new TridiagonalMatrixAlgorithm();
        var sol = tr.solve(new double[]{5, 2}, new double[]{2, 4, -3}, new double[]{-1, 2}, new double[]{3, 6, 2});
        Arrays.stream(sol).forEach(System.out::println);
    }
}
