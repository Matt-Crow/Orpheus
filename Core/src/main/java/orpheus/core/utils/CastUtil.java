package orpheus.core.utils;

import java.util.Optional;

public class CastUtil {
    @SuppressWarnings("unchecked")
    public static <T> Optional<T> cast(Object obj) {
        try {
            return Optional.of((T)obj);
        } catch (ClassCastException ex) {
            return Optional.empty();
        }
    }
}
