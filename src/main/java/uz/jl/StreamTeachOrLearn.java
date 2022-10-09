package uz.jl;

import java.util.List;
import java.util.stream.Stream;

public class StreamTeachOrLearn {
    public static void main(String[] args) {
        Stream<String> langs = Stream.of("Java", "C#", "Python", "C++");
        List<String> strings = langs.skip(1).limit(2).toList();
        System.out.println(strings);

        Stream.Builder<String> builder = Stream.builder();

        Stream<String> stream2 = builder
                .add("Java")
                .add("Python")
                .add("Scala")
                .add("21342L")
                .build();

//        Stream.generate(() -> 123).peek(System.out::println).toList();

    }
}

