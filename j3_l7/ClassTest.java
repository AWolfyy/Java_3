package j3_l7;

import j3_l7.annotations.AfterSuite;
import j3_l7.annotations.BeforeSuite;
import j3_l7.annotations.Test;

public class ClassTest {
    @BeforeSuite
    public void beforeSuiteMethod() {
        System.out.println("Before Suite");
    }

    @Test(priority = 4)
    public void methodFour() {
        System.out.println("Fourth method...");
    }

    @Test(priority = 1)
    public void methodOne() {
        System.out.println("First method...");
    }

    @Test(priority = 2)
    public void methodTwo() {
        System.out.println("Second method...");
    }

    @Test(priority = 4)
    public void otherMethodFour() {
        System.out.println("Other fourth method...");
    }

    @Test(priority = 3)
    public void methodThree() {
        System.out.println("Third method...");
    }

    @AfterSuite
    public void afterSuiteMethod() {
        System.out.println("After Suite");
    }
}
