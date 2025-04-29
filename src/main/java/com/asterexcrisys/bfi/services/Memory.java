package com.asterexcrisys.bfi.services;

import com.asterexcrisys.bfi.exceptions.UnexpectedStreamException;
import java.io.IOException;

@SuppressWarnings("unused")
public class Memory {

    private final byte[] tape;
    private int pointer;

    public Memory() {
        tape = new byte[65536];
        pointer = 0;
    }

    public byte current() {
        return tape[pointer];
    }

    public void moveRight() {
        pointer++;
        if (pointer > tape.length - 1) {
            pointer = 0;
        }
    }

    public void moveLeft() {
        pointer--;
        if (pointer < 0) {
            pointer = tape.length - 1;
        }
    }

    public void increment() {
        tape[pointer]++;
    }

    public void decrement() {
        tape[pointer]--;
    }

    public void printOutput() {
        System.out.print((char) tape[pointer]);
    }

    public void readInput() throws UnexpectedStreamException {
        try {
            int input = System.in.read();
            tape[pointer] = (byte) input;
        } catch (IOException e) {
            throw new UnexpectedStreamException("Input error: " + e.getMessage());
        }
    }

}