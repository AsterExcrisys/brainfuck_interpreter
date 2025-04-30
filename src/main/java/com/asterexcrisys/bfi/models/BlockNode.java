package com.asterexcrisys.bfi.models;

import com.asterexcrisys.bfi.services.Generator;
import com.asterexcrisys.bfi.services.Memory;

public interface BlockNode extends Node {

    Node[] operations();

    void addOperation(Node operation);

    void removeOperation(Node operation);

    Generator<Node> executeOnce(Memory memory);

    BlockNode partialCopy();

    BlockNode fullCopy();

}