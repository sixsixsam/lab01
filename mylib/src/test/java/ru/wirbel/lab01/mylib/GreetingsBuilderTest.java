package ru.wirbel.lab01.mylib;

import org.junit.*;

public class GreetingsBuilderTest {

    @BeforeClass
    public static void runOnceBeforeClass() {
        System.out.println("@BeforeClass - runOnceBeforeClass");
    }

    @AfterClass
    public static void runOnceAfterClass() {
        System.out.println("@AfterClass - runOnceAfterClass");
    }

    @Before
    public void runBeforeTestMethod() {
        System.out.println("@Before - runBeforeTestMethod");
    }
    @After
    public void runAfterTestMethod() {
        System.out.println("@After - runAfterTestMethod");
    }

    @Test
    public void testGreetingsBuilder1() {
        System.out.println("@Test - run testGreetingsBuilder1");

        String expectedValue = "Hello, World!";

        GreetingsBuilder greetings = new GreetingsBuilder();
        String value = greetings.to("World")
                .withLanguage(Greetings.LANGUAGE_EN)
                .withStyle(Greetings.STYLE_FORMAL)
                .build();

        Assert.assertEquals("English formal greetings check", expectedValue, value);
    }

    @Test
    public void testGreetingsBuilder2() {
        System.out.println("@Test - run testGreetingsBuilder2");
        Assert.assertTrue(true);
    }
}
