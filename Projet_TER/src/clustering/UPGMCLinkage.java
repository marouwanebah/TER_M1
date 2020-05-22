package clustering;

public class UPGMCLinkage extends Linkage {
    /**
     * The number of samples in each cluster.
     */
    private int[] n;

    /**
     * Constructor.
     * @param proximity  the proximity matrix to store the distance measure of
     * dissimilarity. To save space, we only need the lower half of matrix.
     */
    public UPGMCLinkage(double[][] proximity) {
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
    public UPGMCLinkage(int size, float[] proximity) {
        super(size, proximity);
        init();
    }

    /** Initialize sample size. */
    private void init() {
        n = new int[size];
        for (int i = 0; i < size; i++) {
            n[i] = 1;
        }

        for (int i = 0; i < proximity.length; i++) {
            proximity[i] *= proximity[i];
        }
    }

    /** Given a set of data, computes the proximity and then the linkage. */
    public static UPGMCLinkage of(double[][] data) {
        return new UPGMCLinkage(data.length, proximity(data));
    }

    /** Given a set of data, computes the proximity and then the linkage. */
    public static <T> UPGMCLinkage of(T[] data, Distance<T> distance) {
        return new UPGMCLinkage(data.length, proximity(data, distance));
    }

    @Override
    public String toString() {
        return "UPGMC linkage";
    }

    @Override
    public void merge(int i, int j) {
        float nij = n[i] + n[j];

        for (int k = 0; k < i; k++) {
            proximity[index(i, k)] = (d(i, k) * n[i] + d(j, k) * n[j] - d(j, i) * n[i] * n[j] / nij) / nij;
        }

        for (int k = i+1; k < j; k++) {
            proximity[index(k, i)] = (d(k, i) * n[i] + d(j, k) * n[j] - d(j, i) * n[i] * n[j] / nij) / nij;
        }

        for (int k = j+1; k < size; k++) {
            proximity[index(k, i)] = (d(k, i) * n[i] + d(k, j) * n[j] - d(j, i) * n[i] * n[j] / nij) / nij;
        }

        n[i] += n[j];
    }
}