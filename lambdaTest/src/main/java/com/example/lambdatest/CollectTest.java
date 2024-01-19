package com.example.lambdatest;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class CollectTest {

    static List<Student> students = Arrays.asList(new Student[]{
            new Student("zhangsan", "1", 91d), new Student("lisi", "2", 89d),
            new Student("wangwu", "1", 50d), new Student("zhaoliu", "2", 78d),
            new Student("sunqi", "1", 59d)});

    public static void main(String[] args) {
//        toMap();
//        stringCollect();
//        groupingBy();
        partitionBy();
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

    private static void groupingBy() {
//        Map<String, List<Student>> collect = students.stream().collect(Collectors.groupingBy(Student::getGrade));
        //按年级分组,然后计数
        Map<String, Long> collect = students.stream().collect(Collectors.groupingBy(Student::getGrade, counting()));
        collect.forEach((key, value) -> System.out.println("key:" + key + "---" + "value" + value));
        //按年级分组,然后获得分数最高的
        Map<String, Optional<Student>> collect1 = students.stream().collect(Collectors.groupingBy(Student::getGrade, maxBy(Comparator.comparing(Student::getScore))));
        collect1.forEach((key, value) -> System.out.println("collect1 key:" + key + "---" + "value" + value));
//        Map<String, Student> collect2 = students.stream().collect(Collectors.groupingBy(Student::getGrade, collectingAndThen(maxBy(Comparator.comparing(Student::getScore)), Optional::get)));
        Map<String, Student> collect2 = students.stream().collect(Collectors.toMap(Student::getGrade, Function.identity(), BinaryOperator.maxBy(Comparator.comparing(Student::getScore))));
        collect2.forEach((key, value) -> System.out.println("collect2 key:" + key + "---" + "value" + value));
        ///按年级分组 然后获取各种统计的总和类
        Map<String, DoubleSummaryStatistics> collect3 = students.stream().collect(Collectors.groupingBy(Student::getGrade, summarizingDouble(Student::getScore)));
        collect3.forEach((key, value) -> System.out.println("collect3 key:" + key + "---" + "value" + value.toString()));
        //按年级分组 然后得到学生名称的列表
        Map<String, List<String>> collect4 = students.stream().collect(Collectors.groupingBy(Student::getGrade, mapping(Student::getName, toList())));
        collect4.forEach((key, value) -> System.out.println("collect4 key:" + key + "---" + "value" + value.toString()));

        //分组结果处理
        //按照年级分组 组内按照成绩从高到低排序
        Map<String, List<Student>> collect5 = students.stream().collect(Collectors.groupingBy(Student::getGrade, collectingAndSort(toList(), Comparator.comparing(Student::getScore).reversed())));
        //按照年级分组 分组后保留不及格的学生
        Map<String, List<Student>> collect6 = students.stream().collect(Collectors.groupingBy(Student::getGrade, collectingAndFilter(toList(), t -> t.getScore() < 60)));

        //分区
    }

    private static void partitionBy(){
        Map<Boolean, List<Student>> collect = students.stream().collect(partitioningBy(t -> t.getScore() > 60));
        Map<Boolean, Double> collect1 = students.stream().collect(partitioningBy(t -> t.getScore() > 60, averagingDouble(Student::getScore)));
        collect.forEach((aBoolean, students) -> {
            System.out.println("yes"+aBoolean+"--student"+Arrays.toString(students.toArray()));
        });

    }

    public static <T> Collector<T, ? , List<T>> collectingAndSort(
            Collector<T, ? , List<T>> downstream, Comparator<? super T> comparator) {
        return Collectors.collectingAndThen(downstream, (r) -> {
            r.sort(comparator);
            return r;
        });
    }

    public static <T> Collector<T, ? , List<T>> collectingAndFilter(
            Collector<T, ? , List<T>> downstream, Predicate<T> predicate) {
        return Collectors.collectingAndThen(downstream, (r) -> {
            return r.stream().filter(predicate).collect(Collectors.toList());
        });
    }
}
