# qimen4java

Java 奇门遁甲排盘 SDK，JAR 内置经过版本锁定的排盘引擎。要求 Java 11+ 和 Node.js 20+。

```java
Map<String, Object> input = Map.of(
    "year", 2026, "month", 4, "day", 10, "hour", 14, "minute", 0,
    "timezone", "Asia/Shanghai"
);
Map<String, Object> chart = Qimen.calculate(input);
Map<String, Object> canonical = Qimen.canonical(input);
```

`calculate` 返回完整结构化盘面，`canonical` 返回规范 JSON。当前算法版本为 `qimen-zhuanpan-chaibu-v1`。
