package com.asterexcrisys.bfi.services;

import com.asterexcrisys.bfi.models.BlockNode;
import com.asterexcrisys.bfi.models.Node;
import com.asterexcrisys.bfi.models.ProgramNode;

@SuppressWarnings("unused")
public class Optimizer {

    private final ProgramNode program;

    public Optimizer(ProgramNode program) {
        this.program = program;
    }

    public void addOperation(Node operation) {
        program.addOperation(operation);
    }

    public void removeOperation(Node operation) {
        program.removeOperation(operation);
    }

    public ProgramNode optimize() {
        return (ProgramNode) optimizeBlock(program);
    }

    private static BlockNode optimizeBlock(BlockNode block) {
        BlockNode optimizedBlock = block.partialCopy();
        Node previous = null;
        int count = 1;
        for (Node operation : block.operations()) {
            if (operation.equals(previous)) {
                count++;
            } else {
                if (previous != null) {
                    addOptimizedOperation(optimizedBlock, previous, count);
                }
                previous = operation;
                count = 1;
            }
        }
        if (previous != null) {
            addOptimizedOperation(optimizedBlock, previous, count);
        }
        return optimizedBlock;
    }

    private static void addOptimizedOperation(BlockNode outerBlock, Node operation, int count) {
        operation.count(count);
        if (operation instanceof BlockNode innerBlock) {
            outerBlock.addOperation(optimizeBlock(innerBlock));
        } else {
            outerBlock.addOperation(operation);
        }
    }

}