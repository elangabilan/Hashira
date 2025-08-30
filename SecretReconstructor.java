import java.math.BigInteger;
import java.util.*;

public class SecretReconstructor{

    // Simple rational number class with BigInteger
    static class BigFraction {
        BigInteger num; // numerator
        BigInteger den; // denominator > 0

        BigFraction(BigInteger n, BigInteger d) {
            if (d.equals(BigInteger.ZERO)) throw new ArithmeticException("Denominator zero");
            if (d.signum() < 0) { n = n.negate(); d = d.negate(); }
            BigInteger g = n.gcd(d);
            if (!g.equals(BigInteger.ONE)) {
                n = n.divide(g);
                d = d.divide(g);
            }
            this.num = n;
            this.den = d;
        }

        BigFraction(BigInteger n) { this(n, BigInteger.ONE); }
        BigFraction(long n) { this(BigInteger.valueOf(n), BigInteger.ONE); }

        static BigFraction zero() { return new BigFraction(BigInteger.ZERO, BigInteger.ONE); }
        static BigFraction one()  { return new BigFraction(BigInteger.ONE, BigInteger.ONE); }

        BigFraction add(BigFraction o) {
            return new BigFraction(this.num.multiply(o.den).add(o.num.multiply(this.den)),
                                   this.den.multiply(o.den));
        }

        BigFraction multiply(BigFraction o) {
            return new BigFraction(this.num.multiply(o.num), this.den.multiply(o.den));
        }

        @Override
        public String toString() {
            if (den.equals(BigInteger.ONE)) return num.toString();
            return num + "/" + den;
        }

        BigInteger toIntegerIfExact() {
            BigInteger[] qr = num.divideAndRemainder(den);
            if (qr[1].equals(BigInteger.ZERO)) return qr[0];
            return null;
        }
    }

    public static void main(String[] args) {
        // Metadata
        int n = 10;
        int k = 7;

        // Bases and values from your JSON
        int[] bases = {6, 15, 15, 16, 8, 3, 3, 6, 12, 7};
        String[] values = {
            "13444211440455345511",
            "aed7015a346d635",
            "6aeeb69631c227c",
            "e1b5e05623d881f",
            "316034514573652620673",
            "2122212201122002221120200210011020220200",
            "20120221122211000100210021102001201112121",
            "20220554335330240002224253",
            "45153788322a1255483",
            "1101613130313526312514143"
        };

        // x = 1..10
        BigInteger[] x = new BigInteger[n];
        BigInteger[] y = new BigInteger[n];

        for (int i = 0; i < n; i++) {
            x[i] = BigInteger.valueOf(i + 1);
            y[i] = new BigInteger(values[i], bases[i]);
        }

        // Lagrange interpolation at x=0
        BigFraction secret = BigFraction.zero();
        for (int j = 0; j < n; j++) {
            BigFraction lj = BigFraction.one();
            for (int m = 0; m < n; m++) {
                if (m == j) continue;
                BigInteger numer = x[m].negate();          // (0 - x_m)
                BigInteger denom = x[j].subtract(x[m]);    // (x_j - x_m)
                lj = lj.multiply(new BigFraction(numer, denom));
            }
            BigFraction term = lj.multiply(new BigFraction(y[j]));
            secret = secret.add(term);
        }

        // Print results
        System.out.println("Reconstructed secret (rational) : " + secret);
        BigInteger exact = secret.toIntegerIfExact();
        if (exact != null) {
            System.out.println("Reconstructed secret (integer)  : " + exact);
        } else {
            System.out.println("Secret is not an integer.");
        }
    }
}
