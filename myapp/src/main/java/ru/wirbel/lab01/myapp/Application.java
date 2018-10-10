package ru.wirbel.lab01.myapp;

import ru.wirbel.lab01.mylib.Greetings;
import ru.wirbel.lab01.mylib.GreetingsBuilder;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

public class Application {
    public static final String DEFAULT_CONSOLE_ENCODING = "UTF-8";
    public static final String CONSOLE_ENCODING_PROPERTY = "consoleEncoding";

    public static void main(String[] args) {
        Application app = new Application();
        app.run();
    }


    public void run() {
        setConsoleEncoding();

        GreetingsBuilder greetings = new GreetingsBuilder();

        System.out.println(
                greetings.to("Hello World!")
                        .withLanguage(Greetings.LANGUAGE_RU)
                        .withStyle(Greetings.STYLE_FRIENDLY)
                        .build()
        );
    }

    private static void setConsoleEncoding() {
        String consoleEncoding = System.getProperty(CONSOLE_ENCODING_PROPERTY, DEFAULT_CONSOLE_ENCODING);

        try {
            System.setOut(new PrintStream(System.out, true, consoleEncoding));
        } catch (UnsupportedEncodingException ex) {
            System.err.println("Unsupported encoding set for console: " + consoleEncoding);
        }
    }
}