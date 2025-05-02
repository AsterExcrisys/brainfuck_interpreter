package com.asterexcrisys.bfi.services;

import com.asterexcrisys.bfi.exceptions.StreamInteractionException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

@SuppressWarnings("unused")
public class Memory implements AutoCloseable {

    private final byte[] tape;
    private int pointer;
    private InputStream input;
    private OutputStream output;

    public Memory() {
        input = System.in;
        output = System.out;
        tape = new byte[65536];
        pointer = 0;
    }

    public byte current() {
        return tape[pointer];
    }

    public int position() {
        return pointer;
    }

    public void input(InputStream input) {
        this.input = input;
    }

    public void output(OutputStream output) {
        this.output = output;
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
        tape[pointer] = Byte.MAX_VALUE;
    }

    public void minimize() {
        tape[pointer] = Byte.MIN_VALUE;
    }

    public void halve() {
        tape[pointer] = 0;
    }

    public void clear() {
        Arrays.fill(tape, (byte) 0);
    }

    public void printOutput() {
        try {
            output.write(tape[pointer]);
            output.flush();
        } catch (IOException exception) {
            throw new StreamInteractionException("Encountered an error while writing to output stream: " + exception.getMessage());
        }
    }

    public void readInput() throws StreamInteractionException {
        try {
            int byteRead = input.read();
            tape[pointer] = (byte) byteRead;
        } catch (IOException exception) {
            throw new StreamInteractionException("Encountered an error while reading from input stream: " + exception.getMessage());
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
        byte[] bytesWritten = new byte[amount];
        Arrays.fill(bytesWritten, tape[pointer]);
        try {
            output.write(bytesWritten);
            output.flush();
        } catch (IOException exception) {
            throw new StreamInteractionException("Encountered an error while writing to output stream: " + exception.getMessage());
        }
    }

    public void readInput(int amount) throws StreamInteractionException {
        if (amount < 2) {
            readInput();
            return;
        }
        try {
            long amountSkipped = input.skip(amount - 1);
            if (amountSkipped != amount - 1) {
                return;
            }
            int byteRead = input.read();
            tape[pointer] = (byte) byteRead;
        } catch (IOException exception) {
            throw new StreamInteractionException("Encountered an error while reading from input stream: " + exception.getMessage());
        }
    }

    public void close() throws IOException {
        input.close();
        output.close();
    }

}