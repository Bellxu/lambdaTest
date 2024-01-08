package com.example.lambdatest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class CollectTest {

    static List<Student> students = Arrays.asList(new Student[] {
            new Student("zhangsan", "1", 91d), new Student("lisi", "2", 89d),
            new Student("wangwu", "1", 50d), new Student("zhaoliu", "2", 78d),
            new Student("sunqi", "1", 59d)});

    public static void main(String[] args) {
//        toMap();
//        stringCollect();
        groupingBy();

    }

    private static void toMap() {
//        Map<String, Double> collect = students.stream().collect(Collectors.toMap(Student::getName, Student::getScore));
//        collect.forEach((s, aDouble) -> System.out.println(s + "--" + aDouble));
//        Map<String, Student> collect = students.stream().collect(Collectors.toMap(Student::getName, Function.identity()));
        //它接受一个额外的参数mergeFunction，它用于处理冲突，在收集一个新元素时，如果新元素的键已经存在了，系统会将新元素的值与键对应的旧值一起传递给mergeFunction得到一个值，然后用这个值给键赋值。
        Map<String, Integer> strLenMap = Stream.of("abc", "hello", "abc").collect(
                Collectors.toMap(Function.identity(),
                        t -> t.length(), (oldValue, value) -> value));
        strLenMap.forEach((key, value) -> System.out.println("key:" + key + "---" + "value" + value));
    }


    private static void stringCollect() {
//        String collect = Stream.of("111", "222", "3333").collect(Collectors.joining());
        String collect = Stream.of("111", "222", "3333").collect(Collectors.joining(",", "[", "]"));
        System.out.println(collect);
    }

    private static void groupingBy(){
//        Map<String, List<Student>> collect = students.stream().collect(Collectors.groupingBy(Student::getGrade));
        Map<String, Long> collect = students.stream().collect(Collectors.groupingBy(Student::getGrade, counting()));
        collect.forEach((key, value) -> System.out.println("key:" + key + "---" + "value" + value));
        Map<String, Optional<Student>> collect1 = students.stream().collect(Collectors.groupingBy(Student::getGrade, maxBy(Comparator.comparing(Student::getScore))));
        collect1.forEach((key, value) -> System.out.println("key:" + key + "---" + "value" + value));
        Map<String, Student> collect2 = students.stream().collect(Collectors.groupingBy(Student::getGrade, collectingAndThen(maxBy(Comparator.comparing(Student::getScore)), Optional::get)));
        collect2.forEach((key, value) -> System.out.println("key:" + key + "---" + "value" + value));

    }
}
