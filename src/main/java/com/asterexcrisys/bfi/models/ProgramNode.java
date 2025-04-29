package com.asterexcrisys.bfi.models;

import com.asterexcrisys.bfi.services.Memory;
import java.util.List;

public record ProgramNode(List<Node> operations) implements Node {

    @Override
    public void execute(Memory memory) {
        for (Node operation : operations) {
            operation.execute(memory);
        }
    }

}