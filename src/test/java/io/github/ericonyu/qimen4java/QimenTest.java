package io.github.ericonyu.qimen4java;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class QimenTest {
    private static final Map<String, Object> INPUT = Map.of(
        "year", 2026, "month", 4, "day", 10, "hour", 14, "minute", 0, "timezone", "Asia/Shanghai"
    );

    @Test void goldenChartAndCanonical() throws Exception {
        Map<String, Object> result = Qimen.calculate(INPUT);
        assertEquals(9, ((List<?>) result.get("palaces")).size());
        assertEquals("qimen-zhuanpan-chaibu-v1", result.get("algorithmVersion"));
        assertEquals(1.0, result.get("juNumber"));
        assertEquals(8.0, ((Map<?, ?>) result.get("zhiShi")).get("palace"));
        assertFalse(Qimen.canonical(INPUT).isEmpty());
    }

    @Test void invalidDate() {
        assertThrows(IOException.class, () -> Qimen.calculate(Map.of("year", 2026, "month", 2, "day", 30, "hour", 12)));
    }
}
