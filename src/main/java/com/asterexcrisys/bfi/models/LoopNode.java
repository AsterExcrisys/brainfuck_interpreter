package com.asterexcrisys.bfi.models;

import com.asterexcrisys.bfi.services.Generator;
import com.asterexcrisys.bfi.services.Memory;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class LoopNode implements BlockNode {

    private final List<Node> body;
    private int count;

    public LoopNode() {
        body = new ArrayList<>();
        count = 1;
    }

    public LoopNode(List<Node> body) {
        this.body = body;
        count = 1;
    }

    public LoopNode(int count) {
        body = new ArrayList<>();
        this.count = count;
    }

    public LoopNode(List<Node> body, int count) {
        this.body = body;
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof LoopNode node)) {
            return false;
        }
        return node.body.equals(body) && node.count == count;
    }

}