package uz.jl.ser;

import java.io.*;

public class SerializableTest implements Serializable {
    private String username;
    private String password;

    public SerializableTest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "SerializableTest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SerializableTest serializableTest = new SerializableTest("john", "123");
        // write(serializableTest);
        SerializableTest serializableTest2 = read();
        System.out.println(serializableTest2);
    }

    private static SerializableTest read() throws IOException, ClassNotFoundException {
        InputStream inputStream = new FileInputStream("src/main/resources/obj.txt");
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        return (SerializableTest) objectInputStream.readObject();
    }

    private static void write(SerializableTest serializableTest) throws IOException {
        OutputStream outputStream = new FileOutputStream("src/main/resources/obj.txt");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(serializableTest);
    }
}
