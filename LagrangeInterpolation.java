import java.math.BigInteger;
import java.util.*;

public class LagrangeInterpolation {

    public static void main(String[] args) {
        // Example input (manually filled since no JSON library)
        int n = 4;
        int k = 3;

        // Store x and y values
        List<Integer> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        // Data entries (x,valueBase,valueString)
        addPoint(xs, ys, 1, 10, "4");
        addPoint(xs, ys, 2, 2, "111");
        addPoint(xs, ys, 3, 10, "12");
        addPoint(xs, ys, 6, 4, "213"); // not used because k=3

        // Use first k points
        xs = xs.subList(0, k);
        ys = ys.subList(0, k);

        BigInteger c = lagrangeAtZero(xs, ys);
        System.out.println("Constant term c = " + c);
    }

    private static void addPoint(List<Integer> xs, List<BigInteger> ys,
                                 int x, int base, String value) {
        BigInteger y = new BigInteger(value, base);
        xs.add(x);
        ys.add(y);
    }

    private static BigInteger lagrangeAtZero(List<Integer> xs, List<BigInteger> ys) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < xs.size(); i++) {
            BigInteger term = ys.get(i);

            for (int j = 0; j < xs.size(); j++) {
                if (i == j) continue;

                int xi = xs.get(i);
                int xj = xs.get(j);

                // Multiply by (0 - xj) / (xi - xj)
                term = term.multiply(BigInteger.valueOf(-xj))
                           .divide(BigInteger.valueOf(xi - xj));
            }
            result = result.add(term);
        }
        return result;
    }
}
