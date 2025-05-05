package com.asterexcrisys.brainfuck;

import com.asterexcrisys.brainfuck.exceptions.InvalidSyntaxException;
import com.asterexcrisys.brainfuck.models.dialects.DialectType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompilerTest {

    private Compiler compiler;

    @BeforeEach
    public void setUp() {
        compiler = new Compiler(DialectType.BRAINFUCK.dialect());
    }

    @Test
    public void shouldReturnCorrectBytecodeWithoutOptimization() {
        compiler.appendCode("+++++-----[...,,,]");
        byte[] bytecode = compiler.compile(false);
        assertTrue(isBytecodeEqual(new byte[]{
                0x05, 0x01, 0x05, 0x01, 0x05, 0x01, 0x05, 0x01, 0x05, 0x01,
                0x06, 0x01, 0x06, 0x01, 0x06, 0x01, 0x06, 0x01, 0x06, 0x01,
                0x0D, 0x01, 0x06, 0x0B, 0x01, 0x0B, 0x01, 0x0B, 0x01, 0x0C,
                0x01, 0x0C, 0x01, 0x0C, 0x01
        }, bytecode));
    }

    @Test
    public void shouldReturnCorrectBytecodeWithOptimization() {
        compiler.appendCode("+++++-----[...,,,]");
        byte[] bytecode = compiler.compile(true);
        assertTrue(isBytecodeEqual(new byte[]{
                0x05, 0x05, 0x06, 0x05, 0x0D, 0x01, 0x02, 0x0B, 0x03,
                0x0C, 0x03
        }, bytecode));
    }

    @Test
    public void shouldReturnCorrectBytecodeByIgnoringCommentsWithoutOptimization() {
        compiler.appendCode("/test1\\+++/test2\\---/test3\\");
        byte[] bytecode = compiler.compile(false);
        assertTrue(isBytecodeEqual(new byte[]{
                0x05, 0x01, 0x05, 0x01, 0x05, 0x01,
                0x06, 0x01, 0x06, 0x01, 0x06, 0x01
        }, bytecode));
    }

    @Test
    public void shouldReturnCorrectBytecodeByIgnoringCommentsWithOptimization() {
        compiler.appendCode("/test1\\+++/test2\\---/test3\\");
        byte[] bytecode = compiler.compile(true);
        assertTrue(isBytecodeEqual(new byte[]{
                0x05, 0x03, 0x06, 0x03
        }, bytecode));
    }

    @Test
    public void shouldThrowInvalidSyntaxExceptionWithoutOptimization() {
        compiler.appendCode("/test1\\+++/test2\\---/test3\\<<<>>>test4\\");
        Exception exception = assertThrows(InvalidSyntaxException.class, () -> compiler.compile(false));
        assertTrue(exception.getMessage().contains("'t'"));
    }

    @Test
    public void shouldThrowInvalidSyntaxExceptionWithOptimization() {
        compiler.appendCode("/test1\\+++/test2\\---/test3\\<<<>>>test4\\");
        Exception exception = assertThrows(InvalidSyntaxException.class, () -> compiler.compile(true));
        assertTrue(exception.getMessage().contains("'t'"));
    }

    public static boolean isBytecodeEqual(byte[] expected, byte[] actual) {
        if (expected.length != actual.length) {
            return false;
        }
        for (int i = 0; i < expected.length; i++) {
            if (expected[i] != actual[i]) {
                return false;
            }
        }
        return true;
    }

}