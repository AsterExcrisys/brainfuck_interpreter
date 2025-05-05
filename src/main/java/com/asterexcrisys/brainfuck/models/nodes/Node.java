package com.asterexcrisys.brainfuck.models.nodes;

import com.asterexcrisys.brainfuck.services.Memory;
import java.util.List;

@SuppressWarnings("unused")
public interface Node {

    int count();

    void count(int count);

    void execute(Memory memory);

    int translate(List<Byte> bytecode);

    Node partialCopy();

    Node fullCopy();

    @Override
    boolean equals(Object other);

}