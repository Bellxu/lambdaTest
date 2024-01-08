package com.example.lambdatest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamTest {

    public static List<Student> students = Arrays.asList(new Student[]{
            new Student("zhangsan", 89d), new Student("lisi", 89d),
            new Student("wangwu", 98d)});

    public static void main(String[] args) {

    }

    public static void test() {
        //过滤
        List<Student> collect = students.stream().filter(student -> student.getScore() > 90).collect(Collectors.toList());
        //先过滤再转换
        List<String> nameList = students.stream().filter(student -> student.getScore() > 90).map(Student::getName).collect(Collectors.toList());
        //去重 调用的是equals比较
        List<Student> collect1 = students.stream().distinct().collect(Collectors.toList());
        //排序
        List<Student> collect3 = students.stream().sorted(Comparator.comparing(Student::getScore).reversed().thenComparing(Student::getName)).collect(Collectors.toList());
        //skip跳过几个 limit限制流的最大长度
        List<Student> collect2 = students.stream().skip(2).limit(3).collect(Collectors.toList());
        //特定的map
        double sum = students.stream().mapToDouble(Student::getScore).sum();

        int sum1 = students.stream().mapToInt(Student::getAge).sum();

        //flatMap 1个映射成多个再组成新的stream
        List<String> lines = Arrays.asList(new String[]{
                "hello abc", "老马  编程"});
        List<String> words = lines.stream()
                .flatMap(line -> Arrays.stream(line.split("\\s+")))
                .collect(Collectors.toList());
        System.out.println(words);

        //终端操作
        //返回最大的max 最小的min
        Optional<Student> max = students.stream().filter(student -> student.getScore() > 90).max(Comparator.comparing(Student::getName));
        //计数
        long count = students.stream().filter(student -> student.getScore() > 90).count();
        //
        boolean allMatch = students.stream().allMatch(student -> student.getScore() > 90);
        boolean anyMatch = students.stream().anyMatch(student -> student.getScore() > 90);
        boolean noneMatch = students.stream().noneMatch(student -> student.getScore() > 90);
        //任意一个分数大于60的 第一个及格的
        Optional<Student> any = students.stream().filter(student -> student.getScore() > 60).findAny();
        Optional<Student> first = students.stream().filter(student -> student.getScore() > 60).findFirst();
        //forEach 在并行流中，forEach不保证处理的顺序，而forEachOrdered会保证按照流
        students.stream().filter(student -> student.getScore() > 60).forEach(student -> System.out.println(student.name));
        //
        Student[] students_arry = students.stream().toArray((IntFunction<Student[]>) value -> new Student[StreamTest.students.size()]);
        Student[] students1_arry = students.stream().toArray(Student[]::new);

        Student student1 = students.stream().max(Comparator.comparing(Student::getScore)).get();


        //educe代表归约或者叫折叠
        Student topStudent = students.stream().reduce((accu, t) -> {
            if (accu.getScore() >= t.getScore()) {
                return accu;
            } else {
                return t;
            }
        }).get();


        //容器收集器

        //1. toSet
        Set<Student> collect4 = students.stream().filter(student -> student.getScore() > 60).collect(Collectors.toSet());


    }


}
