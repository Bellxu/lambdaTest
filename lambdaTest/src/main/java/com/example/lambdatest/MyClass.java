package com.example.lambdatest;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class MyClass {
    public static void main(String[] args) {
//        noLamda();
        hasLamda();

    }

    public static void noLamda() {
        //列出当前目录下的所有扩展名为．txt的文件
        File f = new File(".");
        File[] files = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                if (name.endsWith(".txt")) {
                    return true;
                }
                return false;
            }
        });
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        });
    }


    public static void hasLamda() {
        File f = new File(".");
        File[] files = f.listFiles((dir, name) -> name.endsWith(".txt"));
        Arrays.sort(files, (f1, f2) -> f1.getName().compareTo(f2.getName()));
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        executorService.submit(() -> System.out.println("hello"));
    }

    public static void test() {
        List<Student> students = Arrays.asList(new Student[]{
                new Student("zhangsan", 89d), new Student("lisi", 89d),
                new Student("wangwu", 98d)});
        //Predicate 用来做是否满足条件
        students = filter(students, student -> student.score > 90);
        //Function 用来做转换
        List<String> stringList = map(students, student -> student.name);
        List<Student> upperCaseList = map(students, student -> new Student(student.getName().toUpperCase(), student.getScore()));
        //Consumer 消费掉?
        foreach(students, student -> student.setName(student.getName().toUpperCase()));
        students.sort(Comparator.comparing(Student::getName).reversed().thenComparing(Student::getScore));
    }

    public static void conformanceFunction(){
        File f = new File(".");
        File[] files = f.listFiles((dir, name) -> name.endsWith(".txt"));
        Arrays.sort(files,Comparator.comparing(File::getName));
    }

    //Predicate 用来做是否满足条件
    public static <E> List<E> filter(List<E> list, Predicate<E> pred) {
        List<E> retList = new ArrayList<>();
        for (E e : list) {
            if (pred.test(e)) {
                retList.add(e);
            }
        }
        return retList;
    }

    //Function 用来做转换
    public static <T, R> List<R> map(List<T> list, Function<T, R> mapper) {
        List<R> rList = new ArrayList<>(list.size());
        for (T t : list) {
            mapper.apply(t);
        }
        return rList;

    }

    public static <T> void foreach(List<T> list, Consumer<T> consumer) {
        for (T t : list) {
            consumer.accept(t);
        }

    }

    public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
            Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (Comparator<T> & Serializable)
                (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
    }
}

