package clustering;


import java.util.stream.IntStream;



import static java.lang.Math.abs;
import static java.lang.Math.exp;

import static java.lang.Math.sqrt;

public class MathEx {






    /**
     * Private constructor.
     */
    private MathEx() {

    }

    /**
     * log(2), used in log2().
     */
    private static final double LOG2 = Math.log(2);

    /**
     * Log of base 2.
     */
    public static double log2(double x) {
        return Math.log(x) / LOG2;
    }

    /**
     * Returns natural log without underflow.
     */
    public static double log(double x) {
        double y = -690.7755;
        if (x > 1E-300) {
            y = Math.log(x);
        }
        return y;
    }

    /**
     * Returns natural log(1+exp(x)) without overflow.
     */
    public static double log1pe(double x) {
        double y = x;
        if (x <= 15) {
            y = Math.log1p(Math.exp(x));
        }

        return y;
    }

    /** Returns true if x is an integer. */
    public static boolean isInt(double x) {
        return (x == Math.floor(x)) && !Double.isInfinite(x);
    }

    /**
     * Returns true if two double values equals to each other in the system precision.
     * @param a a double value.
     * @param b a double value.
     * @return true if two double values equals to each other in the system precision
     */
    public static boolean equals(double a, double b) {
        if (a == b) {
            return true;
        }
        
        double absa = abs(a);
        double absb = abs(b);
        return abs(a - b) <= Math.min(absa, absb) * 2.2204460492503131e-16;
    }
        
    /**
     * Logistic sigmoid function.
     */
    public static double logistic(double x) {
        double y;
        if (x < -40) {
            y = 2.353853e+17;
        } else if (x > 40) {
            y = 1.0 + 4.248354e-18;
        } else {
            y = 1.0 + exp(-x);
        }

        return 1.0 / y;
    }

    /**
     * Hyperbolic tangent function. The tanh function is a rescaling of the
     * logistic sigmoid, such that its outputs range from -1 to 1.
     */
    public static double tanh(double x) {
        return 2.0 * logistic(2.0 * x) - 1.0;
    }

    /**
     * Returns x * x.
     */
    public static double sqr(double x) {
        return x * x;
    }

    /**
     * Returns true if x is a power of 2.
     */
    public static boolean isPower2(int x) {
        return x > 0 && (x & (x - 1)) == 0;
    }

    


    /**
     * The Euclidean distance.
     */
    public static double distance(int[] x, int[] y) {
        return sqrt(squaredDistance(x, y));
    }

    /**
     * The Euclidean distance.
     */
    public static double distance(float[] x, float[] y) {
        return sqrt(squaredDistance(x, y));
    }

    /**
     * The Euclidean distance.
     */
    public static double distance(double[] x, double[] y) {
        return sqrt(squaredDistance(x, y));
    }


    /**
     * Pairwise distance between pairs of objects.
     * @param x Rows of x correspond to observations, and columns correspond to variables.
     * @return a full pairwise distance matrix.
     */
    public static double[][] pdist(double[][] x) {
        int n = x.length;

        double[][] dist = new double[n][n];
        pdist(x, dist, false, false);

        return dist;
    }

    /**
     * Pairwise distance between pairs of objects.
     * @param x Rows of x correspond to observations, and columns correspond to variables.
     * @param squared If true, compute the squared Euclidean distance.
     * @param half If true, only the lower half of dist will be referenced.
     * @param dist The distance matrix.
     */
    public static void pdist(double[][] x, double[][] dist, boolean squared, boolean half) {
        int n = x.length;
        int N = n * (n - 1) / 2;

        if (squared) {
            IntStream.range(0, N).parallel().forEach(k -> {
                int j = n - 2 - (int) Math.floor(Math.sqrt(-8*k + 4*n*(n-1)-7)/2.0 - 0.5);
                int i = k + j + 1 - n*(n-1)/2 + (n-j)*((n-j)-1)/2;
                dist[i][j] = squaredDistance(x[i], x[j]);
            });
        } else {
            IntStream.range(0, N).parallel().forEach(k -> {
                int j = n - 2 - (int) Math.floor(Math.sqrt(-8*k + 4*n*(n-1)-7)/2.0 - 0.5);
                int i = k + j + 1 - n*(n-1)/2 + (n-j)*((n-j)-1)/2;
                dist[i][j] = distance(x[i], x[j]);
            });
        }

        if (!half) {
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    dist[i][j] = dist[j][i];
                }
            }
        }
    }

    /**
     * The squared Euclidean distance.
     */
    public static double squaredDistance(int[] x, int[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Input vector sizes are different.");
        }

        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += sqr(x[i] - y[i]);
        }

        return sum;
    }

    /**
     * The squared Euclidean distance.
     */
    public static double squaredDistance(float[] x, float[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Input vector sizes are different.");
        }

        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += sqr(x[i] - y[i]);
        }

        return sum;
    }

    /**
     * The squared Euclidean distance.
     */
    public static double squaredDistance(double[] x, double[] y) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("Input vector sizes are different.");
        }

        double sum = 0.0;
        for (int i = 0; i < x.length; i++) {
            sum += sqr(x[i] - y[i]);
        }

        return sum;
    }

   
    /**
     * The squared Euclidean distance with handling missing values (represented as NaN).
     */
    public static double squaredDistanceWithMissingValues(double[] x, double[] y) {
        int n = x.length;
        int m = 0;
        double dist = 0.0;

        for (int i = 0; i < n; i++) {
            if (!Double.isNaN(x[i]) && !Double.isNaN(y[i])) {
                m++;
                double d = x[i] - y[i];
                dist += d * d;
            }
        }

        if (m == 0) {
            dist = Double.MAX_VALUE;
        } else {
            dist = n * dist / m;
        }

        return dist;
    }

    
}


