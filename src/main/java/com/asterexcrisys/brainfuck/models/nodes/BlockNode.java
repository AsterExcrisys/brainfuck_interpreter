package com.asterexcrisys.brainfuck.models.nodes;

import com.asterexcrisys.brainfuck.services.Generator;
import com.asterexcrisys.brainfuck.services.Memory;

@SuppressWarnings("unused")
public interface BlockNode extends Node {

    Node[] operations();

    void addOperation(Node operation);

    void removeOperation(Node operation);

    Generator<Node> executeOnce(Memory memory);

    BlockNode partialCopy();

    BlockNode fullCopy();

}