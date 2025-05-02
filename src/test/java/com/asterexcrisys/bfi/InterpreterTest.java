package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.models.dialects.DialectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InterpreterTest {

    private Interpreter interpreter;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() {
        interpreter = new Interpreter(DialectType.BRAINFUCK.dialect());
        outputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(outputStream);
        System.setOut(printStream);
    }

    @Test
    public void shouldPrintHelloWorldWithoutOptimization() {
        interpreter.appendCode("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.");
        interpreter.interpret(false);
        String output = outputStream.toString();
        assertEquals("Hello World!\n", output);
    }

    @Test
    public void shouldPrintHelloWorldWithOptimization() {
        interpreter.appendCode("++++++++++[>+++++++>++++++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.");
        interpreter.interpret(true);
        String output = outputStream.toString();
        assertEquals("Hello World!\n", output);
    }

    @Test
    public void shouldPrintSameCharactersWithoutOptimization() {
        interpreter.appendCode(",>,>,>,<<<.>.>.>.");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        System.setIn(inputStream);
        interpreter.interpret(false);
        String output = outputStream.toString();
        assertEquals("test", output);
    }

    @Test
    public void shouldPrintSameCharactersWithOptimization() {
        interpreter.appendCode(",>,>,>,<<<.>.>.>.");
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        System.setIn(inputStream);
        interpreter.interpret(true);
        String output = outputStream.toString();
        assertEquals("test", output);
    }

}