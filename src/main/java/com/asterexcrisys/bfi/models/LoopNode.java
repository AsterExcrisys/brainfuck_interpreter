package com.asterexcrisys.bfi.models;

import com.asterexcrisys.bfi.services.Memory;
import java.util.ArrayList;
import java.util.List;

public class LoopNode implements Node {

    private final List<Node> body;

    public LoopNode() {
        body = new ArrayList<>();
    }

    public void addOperation(Node operation) {
        body.add(operation);
    }

    @Override
    public void execute(Memory memory) {
        while (memory.current() != 0) {
            for (Node operation : body) {
                operation.execute(memory);
            }
        }
    }

}