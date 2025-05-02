package com.asterexcrisys.bfi.models.nodes;

import com.asterexcrisys.bfi.services.Generator;
import com.asterexcrisys.bfi.services.Memory;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressWarnings("unused")
public class ProgramNode implements BlockNode {

    private final List<Node> snippet;
    private int count;

    public ProgramNode() {
        snippet = new ArrayList<>();
        count = 1;
    }

    public ProgramNode(List<Node> snippet) {
        this.snippet = Objects.requireNonNull(snippet);
        count = 1;
    }

    public ProgramNode(int count) {
        snippet = new ArrayList<>();
        this.count = count;
    }

    public ProgramNode(List<Node> snippet, int count) {
        this.snippet = Objects.requireNonNull(snippet);
        this.count = count;
    }

    public Node[] operations() {
        return snippet.toArray(new Node[0]);
    }

    public void addOperation(Node operation) {
        snippet.add(operation);
    }

    public void removeOperation(Node operation) {
        snippet.remove(operation);
    }

    public int count() {
        return count;
    }

    public void count(int count) {
        this.count = count;
    }

    public void execute(Memory memory) {
        if (count < 2) {
            executeSnippet(memory);
            return;
        }
        for (int i = 0; i < count; i++) {
            executeSnippet(memory);
        }
    }

    public Generator<Node> executeOnce(Memory memory) {
        if (count < 2) {
            return new Generator<>() {
                public void run() throws Exception {
                    try (Generator<Node> generator = executeSnippetOnce(memory)) {
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
                    try (Generator<Node> generator = executeSnippetOnce(memory)) {
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
            return translateSnippet(bytecode);
        }
        int count = 0;
        for (int i = 0; i < this.count; i++) {
            count += translateSnippet(bytecode);
        }
        return count;
    }

    public BlockNode partialCopy() {
        return new ProgramNode(count);
    }

    public BlockNode fullCopy() {
        return new ProgramNode(snippet, count);
    }

    private void executeSnippet(Memory memory) {
        for (Node operation : snippet) {
            operation.execute(memory);
        }
    }

    private Generator<Node> executeSnippetOnce(Memory memory) {
        return new Generator<>() {
            public void run() throws Exception {
                for (Node operation : snippet) {
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
        };
    }

    private int translateSnippet(List<Byte> bytecode) {
        int count = 0;
        for (Node operation : snippet) {
            count += operation.translate(bytecode);
        }
        return count;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProgramNode node)) {
            return false;
        }
        return node.snippet.equals(snippet) && node.count == count;
    }

}