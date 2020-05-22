package clustering;

public class WPGMCLinkage extends Linkage {
    /**
     * Constructor.
     * @param proximity  the proximity matrix to store the distance measure of
     * dissimilarity. To save space, we only need the lower half of matrix.
     */
    public WPGMCLinkage(double[][] proximity) {
        super(proximity);
        init();
    }

    /**
     * Constructor. Initialize the linkage with the lower triangular proximity matrix.
     * @param size the data size.
     * @param proximity column-wise linearized proximity matrix that stores
     *                  only the lower half. The length of proximity should be
     *                  size * (size+1) / 2.
     *                  To save space, Linkage will use this argument directly
     *                  without copy. The elements may be modified.
     */
    public WPGMCLinkage(int size, float[] proximity) {
        super(size, proximity);
        init();
    }

    /** Intialize proximity. */
    private void init() {
        for (int i = 0; i < proximity.length; i++) {
            proximity[i] *= proximity[i];
        }
    }

    /** Given a set of data, computes the proximity and then the linkage. */
    public static WPGMCLinkage of(double[][] data) {
        return new WPGMCLinkage(data.length, proximity(data));
    }

    /** Given a set of data, computes the proximity and then the linkage. */
    public static <T> WPGMCLinkage of(T[] data, Distance<T> distance) {
        return new WPGMCLinkage(data.length, proximity(data, distance));
    }

    @Override
    public String toString() {
        return "WPGMC linkage";
    }

    @Override
    public void merge(int i, int j) {
        for (int k = 0; k < i; k++) {
            proximity[index(i, k)] = (d(i, k) + d(j, k)) / 2 - d(j, i) / 4;
        }

        for (int k = i+1; k < j; k++) {
            proximity[index(k, i)] = (d(k, i) + d(j, k)) / 2 - d(j, i) / 4;
        }

        for (int k = j+1; k < size; k++) {
            proximity[index(k, i)] = (d(k, i) + d(k, j)) / 2 - d(j, i) / 4;
        }
    }
}
