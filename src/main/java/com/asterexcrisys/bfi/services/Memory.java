package com.asterexcrisys.bfi.services;

import com.asterexcrisys.bfi.exceptions.NotExpectedStreamException;
import java.io.IOException;
import java.util.Arrays;

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

    public void moveStart() {
        pointer = 0;
    }

    public void moveEnd() {
        pointer = tape.length - 1;
    }

    public void moveMiddle() {
        pointer = 32768;
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

    public void maximize() {
        tape[pointer] = 127;
    }

    public void minimize() {
        tape[pointer] = -128;
    }

    public void halve() {
        tape[pointer] = 0;
    }

    public void clear() {
        Arrays.fill(tape, (byte) 0);
    }

    public void printOutput() {
        System.out.print((char) tape[pointer]);
    }

    public void readInput() throws NotExpectedStreamException {
        try {
            int input = System.in.read();
            tape[pointer] = (byte) input;
        } catch (IOException e) {
            throw new NotExpectedStreamException("Input error: " + e.getMessage());
        }
    }

    public void moveRight(int amount) {
        if (amount < 2) {
            moveRight();
            return;
        }
        pointer += amount;
        if (pointer > tape.length - 1) {
            pointer = pointer & 0xFFFF;
        }
    }

    public void moveLeft(int amount) {
        if (amount < 2) {
            moveLeft();
            return;
        }
        pointer -= amount;
        if (pointer < 0) {
            pointer = tape.length - (pointer & 0xFFFF) - 1;
        }
    }

    public void increment(int amount) {
        if (amount < 2) {
            increment();
            return;
        }
        tape[pointer] += (byte) (amount & 0xFF);
    }

    public void decrement(int amount) {
        if (amount < 2) {
            decrement();
            return;
        }
        tape[pointer] -= (byte) (amount & 0xFF);
    }

    public void printOutput(int amount) {
        if (amount < 2) {
            printOutput();
            return;
        }
        char[] characters = new char[amount];
        Arrays.fill(characters, (char) tape[pointer]);
        System.out.print(new String(characters));
    }

    public void readInput(int amount) throws NotExpectedStreamException {
        if (amount < 2) {
            readInput();
            return;
        }
        try {
            long amountSkipped = System.in.skip(amount - 1);
            if (amountSkipped != amount - 1) {
                return;
            }
            int input = System.in.read();
            tape[pointer] = (byte) input;
        } catch (IOException e) {
            throw new NotExpectedStreamException("Input error: " + e.getMessage());
        }
    }

}