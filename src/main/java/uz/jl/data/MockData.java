package uz.jl.data;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MockData {


    public static List<User> generateFakePersonData() {
        Faker faker = new Faker();
        Name name = faker.name();
        List<User> userList = new ArrayList<>();

        for (int i = 0; i < 1e3; i++) {
            User user = new User(
                    name.firstName(),
                    name.lastName(),
                    name.fullName(),
                    name.username(),
                    name.bloodGroup(),
                    new Random().nextInt(15, 25)
            );
            userList.add(user);
        }
        return userList;
    }
}

