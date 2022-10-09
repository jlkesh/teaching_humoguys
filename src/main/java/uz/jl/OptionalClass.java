package uz.jl;

import java.lang.instrument.ClassDefinition;
import java.util.Optional;
import java.util.Random;

public class OptionalClass {
    public static void main(String[] args) {
        String response = test();
//        if (response == null)
//            throw new RuntimeException("test response is null");

        // TODO: 02/10/22  dsfds

//        String response2 = test2().orElseThrow(() -> new RuntimeException("Test 2 returmned null"));
//        Optional<Object> empty = Optional.empty();
//        if (empty.isPresent()) {
//            System.out.println("non empty");
//        }else System.out.println(" empty");
//        Optional<Object> objectOptional = Optional.ofNullable(null);
//        if (objectOptional.isPresent()) {
//            System.out.println("Present");
//        }

        Optional<String> optional = Optional.ofNullable(null);
        Optional<String> optional2 = Optional.of("Java");
//        Optional<String> optionalRes = optional.or(() -> optional2);
//        System.out.println("optionalRes.get() = " + optionalRes.get());
        String python = optional.orElse("Python");
        System.out.println("python = " + python);

        optional2.ifPresent((lang) -> {
            System.out.println("Nfflkjgfdljgbdf");
            System.out.println(lang);
        });

        optional.ifPresentOrElse(
                (lang) -> {
                    System.out.println("Nfflkjgfdljgbdf");
                    System.out.println(lang);
                },
                () -> {
                    System.out.println("Not Found");
                }
        );


    }

    static String test() {
        return new Random().nextBoolean() ? "Hello" : null;
    }

    static Optional<String> test2() {
        return new Random().nextBoolean() ? Optional.of("Hello") : Optional.empty();
    }
}
