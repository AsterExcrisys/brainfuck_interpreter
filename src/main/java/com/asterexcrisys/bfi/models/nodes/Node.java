package com.asterexcrisys.bfi.models.nodes;

import com.asterexcrisys.bfi.services.Memory;

@SuppressWarnings("unused")
public interface Node {

    int count();

    void count(int count);

    void execute(Memory memory);

    Node partialCopy();

    Node fullCopy();

    @Override
    boolean equals(Object other);

}