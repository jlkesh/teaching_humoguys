package uz.jl;

import uz.jl.data.MockData;
import uz.jl.data.User;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {
        List<User> users = MockData.generateFakePersonData();
        imperativeApproach(users);
    }

    static void imperativeApproach(List<User> users) {

        List<User> usersUnderTwenty = new ArrayList<>();

        int counter = 0;

        for (User user : users) {
            if (counter == 10)
                break;
            if (user.getAge() <= 20) {
                counter++;
                usersUnderTwenty.add(user);
            }
        }

        System.out.println("usersUnderTwenty = " + usersUnderTwenty);
        System.out.println("usersUnderTwenty = " + usersUnderTwenty.size());
    }

    static void declarativeApproach(List<User> users) {
        Predicate<User> userPredicate = user -> user.getAge() <= 20;
        Stream<User> limit = users.stream()
                .filter(user -> user.getAge() <= 20)
                .limit(10);
    }


}
