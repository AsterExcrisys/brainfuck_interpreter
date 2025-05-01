package com.asterexcrisys.bfi;

import com.asterexcrisys.bfi.exceptions.InvalidSyntaxException;
import com.asterexcrisys.bfi.models.dialects.Dialect;
import com.asterexcrisys.bfi.models.instructions.InstructionType;
import com.asterexcrisys.bfi.models.nodes.ProgramNode;
import com.asterexcrisys.bfi.services.Optimizer;
import com.asterexcrisys.bfi.services.Parser;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Compiler {

    private final Parser parser;

    public Compiler() {
        parser = new Parser();
    }

    public Compiler(String code) {
        parser = new Parser(code);
    }

    public Compiler(Dialect dialect) {
        parser = new Parser(dialect);
    }

    public Compiler(String code, Dialect dialect) {
        parser = new Parser(code, dialect);
    }

    public void appendCode(String code) {
        parser.appendCode(code);
    }

    public byte[] compile(boolean shouldOptimize) {
        ProgramNode program;
        if (shouldOptimize) {
            Optimizer optimizer = new Optimizer(parser.parse());
            program = optimizer.optimize();
        } else {
            program = parser.parse();
        }
        List<Byte> bytecode = new ArrayList<>();
        program.translate(bytecode);
        byte[] result = new byte[bytecode.size()];
        for (int i = 0; i < bytecode.size(); i++) {
            result[i] = bytecode.get(i);
        }
        return result;
    }

    public static String decompile(byte[] bytecode) {
        if (bytecode == null || bytecode.length == 0) {
            return null;
        }
        InstructionType[] types = InstructionType.values();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < bytecode.length; i++) {
            boolean isFound = false;
            for (InstructionType type : types) {
                if (type.code() == bytecode[i]) {
                    result.append(type.keyword());
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                throw new InvalidSyntaxException("Unrecognized operation '" + bytecode[i] + "' at position: " + i);
            }
        }
        return result.toString();
    }

}