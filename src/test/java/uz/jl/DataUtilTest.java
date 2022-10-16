package uz.jl;


import org.junit.jupiter.api.*;

@DisplayNameGeneration(DisplayNameGenerator.Simple.class)
public class DataUtilTest {


    @Test
    @DisplayName("check to integers correctly added or not")
    public void add() {
        Assumptions.assumeTrue(false);
        DataUtil dataUtil = new DataUtil();
        int result = dataUtil.add(2, 4);
        Assertions.assertEquals(6, result, "6 equal to 6");
    }


}
