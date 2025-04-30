package com.asterexcrisys.bfi.models;

public interface BlockNode extends Node {

    Node[] operations();

    void addOperation(Node operation);

    void removeOperation(Node operation);

    BlockNode partialCopy();

    BlockNode fullCopy();

}