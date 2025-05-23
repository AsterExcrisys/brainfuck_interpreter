package com.asterexcrisys.brainfuck.models.nodes;

import com.asterexcrisys.brainfuck.models.instructions.InstructionType;
import com.asterexcrisys.brainfuck.services.Generator;
import com.asterexcrisys.brainfuck.services.Memory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class LoopNode implements BlockNode {

    private final List<Node> body;
    private int count;

    public LoopNode() {
        body = new ArrayList<>();
        count = 1;
    }

    public LoopNode(List<Node> body) {
        this.body = Objects.requireNonNull(body);
        count = 1;
    }

    public LoopNode(int count) {
        body = new ArrayList<>();
        this.count = count;
    }

    public LoopNode(List<Node> body, int count) {
        this.body = Objects.requireNonNull(body);
        this.count = count;
    }

    public Node[] operations() {
        return body.toArray(new Node[0]);
    }

    public void addOperation(Node operation) {
        body.add(operation);
    }

    public void removeOperation(Node operation) {
        body.remove(operation);
    }

    public int count() {
        return count;
    }

    public void count(int count) {
        this.count = count;
    }

    public void execute(Memory memory) {
        if (count < 2) {
            executeBody(memory);
            return;
        }
        for (int i = 0; i < count; i++) {
            executeBody(memory);
        }
    }

    public Generator<Node> executeOnce(Memory memory) {
        if (count < 2) {
            return new Generator<>() {
                public void run() throws Exception {
                    try (Generator<Node> generator = executeBodyOnce(memory)) {
                        for (Node node : generator) {
                            this.yield(node);
                        }
                    }
                }
            };
        }
        return new Generator<>() {
            public void run() throws Exception {
                for (int i = 0; i < count; i++) {
                    try (Generator<Node> generator = executeBodyOnce(memory)) {
                        for (Node node : generator) {
                            this.yield(node);
                        }
                    }
                }
            }
        };
    }

    public int translate(List<Byte> bytecode) {
        if (count < 2) {
            bytecode.add(InstructionType.START_LOOP.code());
            bytecode.add((byte) (count & 0xFF));
            bytecode.add((byte) 0x00);
            int index = bytecode.size() - 1;
            bytecode.set(index, (byte) (translateBody(bytecode) & 0xFF));
            return bytecode.get(index);
        }
        int count = 0;
        for (int i = 0; i < this.count; i++) {
            bytecode.add(InstructionType.START_LOOP.code());
            bytecode.add((byte) (count & 0xFF));
            bytecode.add((byte) 0x00);
            int index = bytecode.size() - 1;
            bytecode.set(index, (byte) (translateBody(bytecode) & 0xFF));
            count += bytecode.get(index);
        }
        return count + 1;
    }

    public BlockNode partialCopy() {
        return new LoopNode(count);
    }

    public BlockNode fullCopy() {
        return new LoopNode(body, count);
    }

    private void executeBody(Memory memory) {
        while (memory.current() != 0) {
            for (Node operation : body) {
                operation.execute(memory);
            }
        }
    }

    private Generator<Node> executeBodyOnce(Memory memory) {
        return new Generator<>() {
            public void run() throws Exception {
                while (memory.current() != 0) {
                    for (Node operation : body) {
                        if (operation instanceof BlockNode block) {
                            try (Generator<Node> generator = block.executeOnce(memory)) {
                                for (Node node : generator) {
                                    this.yield(node);
                                }
                            }
                        } else {
                            operation.execute(memory);
                            this.yield(operation);
                        }
                    }
                }
            }
        };
    }

    private int translateBody(List<Byte> bytecode) {
        int count = 0;
        for (Node operation : body) {
            count += operation.translate(bytecode);
        }
        return count;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LoopNode node)) {
            return false;
        }
        return node.body.equals(body) && node.count == count;
    }

}