package com.asterexcrisys.bfi.models;

import com.asterexcrisys.bfi.services.Memory;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ProgramNode implements BlockNode {

    private final List<Node> snippet;
    private int count;

    public ProgramNode() {
        snippet = new ArrayList<>();
        count = 1;
    }

    public ProgramNode(List<Node> snippet) {
        this.snippet = snippet;
        count = 1;
    }

    public ProgramNode(int count) {
        snippet = new ArrayList<>();
        this.count = count;
    }

    public ProgramNode(List<Node> snippet, int count) {
        this.snippet = snippet;
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ProgramNode node)) {
            return false;
        }
        return node.snippet.equals(snippet) && node.count == count;
    }

}