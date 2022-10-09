package uz.jl;

import java.util.Random;

public class App {
    public static void main(String[] args) {
        char tm = '\u2690';
        System.out.println("Java" + tm);
        String fire = "1f4A5";
//        int codePoint = 0x1F683;
//        System.out.println(codePoint);
//        char highSurrogate = Character.highSurrogate(codePoint);
//        char lowSurrogate = Character.lowSurrogate(codePoint);
//        char[] item = new char[]{highSurrogate, lowSurrogate};
//        System.out.println(item);

        // ESC[48;5;{ID}m
        int counter = 1;

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.print("\u001b[38;5;" + new Random().nextInt(0, 254) + "m\t" + counter);
                counter++;
            }
            System.out.println();
        }

    }
}
