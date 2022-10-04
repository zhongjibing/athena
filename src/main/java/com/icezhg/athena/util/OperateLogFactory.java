package com.icezhg.athena.util;


import com.icezhg.athena.vo.Diff;
import com.icezhg.authorization.core.util.JsonUtil;
import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by zhongjibing on 2022/09/29.
 */
public class OperateLogFactory {

    private static final String EMPTY = "<empty>";
    private static final String FORMATTER = "%s -> %s";
    private static final String DATE_FMT_PATTERN = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static List<Diff> compareDiff(Object compare, Object origin) {
        if (compare == null || origin == null || compare.getClass() != origin.getClass()) {
            throw new IllegalArgumentException();
        }

        List<Diff> result = new LinkedList<>();
        Class<?> cls = compare.getClass();
        while (cls != null) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                Object originValue = ReflectionUtils.getField(field, origin);
                Object compareValue = ReflectionUtils.getField(field, compare);
                if (isSimpleType(field.getType())) {
                    diff(String.valueOf(originValue), String.valueOf(compareValue)).ifPresent(diff -> result.add(new Diff(field.getName(), diff)));
                } else if (Date.class.isAssignableFrom(field.getType())) {
                    diff((Date) originValue, (Date) compareValue).ifPresent(diff -> result.add(new Diff(field.getName(), diff)));
                } else if (Collection.class.isAssignableFrom(field.getType())) {
                    diff((Collection<?>) originValue, (Collection<?>) compareValue).ifPresent(diff -> result.add(new Diff(field.getName(), diff)));
                } else if (Map.class.isAssignableFrom(field.getType())) {
                    diff((Map<?, ?>) originValue, (Map<?, ?>) compareValue).ifPresent(diff -> result.add(new Diff(field.getName(), diff)));
                } else {
                    diff(formatObj(originValue), formatObj(compareValue)).ifPresent(diff -> result.add(new Diff(field.getName(), diff)));
                }
            }

            cls = cls.getSuperclass();
        }

        return result;
    }

    private static Optional<String> diff(Date val1, Date val2) {
        return diff(formatDate(val1), formatDate(val2));
    }

    private static Optional<String> diff(Collection<?> val1, Collection<?> val2) {
        return diff(formatColl(val1), formatColl(val2));
    }

    private static Optional<String> diff(Map<?, ?> val1, Map<?, ?> val2) {
        return diff(formatMap(val1), formatMap(val2));
    }

    private static Optional<String> diff(String val1, String val2) {
        if (isEmpty(val1) && isEmpty(val2)) {
            return Optional.empty();
        }

        if (!isEmpty(val1) && val1.equals(val2)) {
            return Optional.empty();
        }

        if (isEmpty(val1)) {
            return Optional.of(format(EMPTY, val2));
        }
        if (isEmpty(val2)) {
            return Optional.of(format(val1, EMPTY));
        }
        return Optional.of(format(val1, val2));
    }

    private static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    private static String format(String left, String right) {
        return String.format(FORMATTER, left, right);
    }

    private static boolean isSimpleType(Class<?> cls) {
        if (cls.isPrimitive()) {
            return true;
        }
        if (Character.class.isAssignableFrom(cls)) {
            return true;
        }
        if (Number.class.isAssignableFrom(cls)) {
            return true;
        }
        if (Boolean.class.isAssignableFrom(cls)) {
            return true;
        }
        return CharSequence.class.isAssignableFrom(cls);
    }

    private static String formatObj(Object obj) {
        if (obj == null) {
            return null;
        }
        if (isSimpleType(obj.getClass())) {
            return String.valueOf(obj);
        }
        if (Date.class.isAssignableFrom(obj.getClass())) {
            return formatDate((Date) obj);
        }
        if (Collection.class.isAssignableFrom(obj.getClass())) {
            return formatColl((Collection<?>) obj);
        }
        if (Map.class.isAssignableFrom(obj.getClass())) {
            return formatMap((Map<?, ?>) obj);
        }

        Map<String, String> map = new TreeMap<>();
        Class<?> cls = obj.getClass();
        while (cls != null) {
            Field[] fields = cls.getDeclaredFields();
            for (Field field : fields) {
                ReflectionUtils.makeAccessible(field);
                Object value = ReflectionUtils.getField(field, obj);
                map.put(field.getName(), formatObj(value));
            }

            cls = cls.getSuperclass();
        }
        return JsonUtil.toJson(map);
    }

    private static String formatColl(Collection<?> coll) {
        if (coll == null) {
            return null;
        }

        return JsonUtil.toJson(coll.stream().map(OperateLogFactory::formatObj).toList());
    }

    private static String formatMap(Map<?, ?> map) {
        if (map == null) {
            return null;
        }

        Map<String, String> strMap = new TreeMap<>();
        map.forEach((key, value) -> strMap.put(formatObj(key), formatObj(value)));
        return JsonUtil.toJson(strMap);
    }

    private static String formatDate(Date date) {
        return date != null ? new SimpleDateFormat(DATE_FMT_PATTERN, Locale.CHINA).format(date) : null;
    }

    private static final Random random = new Random();

    public static void main(String[] args) {
        int count = 0;
        for (int i = 0; i < 100; i++) {
            printDiff(buildTak(), buildTak(), ++count);
            printDiff(buildSak(), buildSak(), ++count);
        }
    }


    private static void printDiff(Object val1, Object val2, int index) {
        println(String.format("[%d] compare %s >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>", index, val1.getClass().getName()));
        println("val1: " + formatObj(val1));
        println("val2: " + formatObj(val2));
        println("-------- diff:");
        List<Diff> diffs = compareDiff(val1, val2);
        if (diffs.isEmpty()) {
            println("<none>");
        } else {
            diffs.forEach(x -> println(x.toString()));
        }
        println("\n");
    }

    private static void println(String line) {
        try (PrintWriter pw = new PrintWriter(new FileOutputStream("E:\\test.log", true))) {
            pw.println(line);
            pw.flush();
        } catch (IOException e) {
        }
    }

    @Data
    private static class Tak {
        private int a;
        private float b;
        private Double c;
        private BigDecimal d;
        private char e;
        private String f;
        private List<Object> g;
        private Map<String, Object> h;
        private Date i;
    }

    @Data
    private static class Sak {
        private Integer a;
        private String b;
    }

    private static Tak buildTak() {
        Tak tak = new Tak();
        tak.setA(randomInt());
        tak.setB(randomFloat());
        tak.setC(randomDouble());
        tak.setD(randomDecimal());
        tak.setE(randomChar());
        tak.setF(randomStr());
        tak.setG(randomList());
        tak.setH(randomMap());
        tak.setI(randomDate());
        return tak;
    }

    private static Sak buildSak() {
        Sak sak = new Sak();
        sak.setA(randomInt());
        sak.setB(randomStr());
        return sak;
    }

    private static List<Object> randomList() {
        int i = random.nextInt(3, 10);

        List<Object> list = new ArrayList<>();
        if (i % 2 == 0) {
            for (int j = 0; j < i; j++) {
                list.add(randomStr());
            }
        } else if (i % 5 == 0) {
            for (int j = 0; j < i; j++) {
                list.add(randomDate());
            }
        } else {
            for (int j = 0; j < i; j++) {
                list.add(randomDecimal());
            }
        }

        return list;
    }

    private static Map<String, Object> randomMap() {
        int i = random.nextInt(10);
        Map<String, Object> map = new HashMap<>();
        for (int j = 0; j < i; j++) {
            map.put(randomStr(), randomObj());
        }

        return map;
    }

    private static Object randomObj() {
        int i = random.nextInt(1, 6);
        if (i == 1) {
            return randomDate();
        } else if (i == 2) {
            return randomDecimal();
        } else if (i == 3) {
            return randomDouble();
        } else {
            return randomStr();
        }
    }

    private static String randomStr() {
        int len = random.nextInt(5, 10);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(randomChar());
        }
        return builder.toString();
    }

    private static char randomChar() {
        return (char) ((byte) 'a' + (random.nextInt(0, 26)));
    }

    private static float randomFloat() {
        return random.nextFloat(10f, 20f);
    }

    private static Double randomDouble() {
        double d = random.nextDouble(1d, 100d);
        return d > 70 ? null : d;
    }

    private static Integer randomInt() {
        return random.nextInt(1, 100);
    }

    private static BigDecimal randomDecimal() {
        return new BigDecimal(randomInt() + "." + randomInt());
    }

    private static Date randomDate() {
        long val = random.nextLong(1L, 24 * 60 * 60 * 1000L);
        return new Date(getTimeBase() + val * randomInt());
    }

    private static long getTimeBase() {
        return 1664456653758L;
    }
}
