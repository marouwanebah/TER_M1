package clustering;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;

public interface Distance<T> extends ToDoubleBiFunction<T,T>, Serializable {
    /**
     * Returns the distance measure between two objects.
     */
    double d(T x, T y);

    /**
     * Returns the distance measure between two objects.
     * This is simply for Scala convenience.
     */
    default double apply(T x, T y) {
        return d(x, y);
    }

    @Override
    default double applyAsDouble(T x, T y) {
        return d(x, y);
    }
}