package com.asterexcrisys.bfi.models.dialects;

import com.asterexcrisys.bfi.exceptions.FailedConversionException;
import com.asterexcrisys.bfi.models.instructions.Instruction;
import com.asterexcrisys.bfi.models.instructions.InstructionType;
import java.util.Arrays;
import java.util.HashSet;

@SuppressWarnings("unused")
public class Dialect {

    private final Instruction[] instructions;

    public Dialect(String[] aliases) {
        InstructionType[] types = InstructionType.values();
        if (!validateAliases(aliases, types)) {
            throw new IllegalArgumentException("Provided aliases are either not enough, too many, of unequal length, or duplicated");
        }
        instructions = new Instruction[types.length];
        for (int i = 0; i < types.length; i++) {
            instructions[i] = new Instruction(aliases[i], types[i]);
        }
    }

    public Instruction[] instructions() {
        return instructions;
    }

    public DialectType toType() {
        for (DialectType type : DialectType.values()) {
            if (type.dialect().equals(this)) {
                return type;
            }
        }
        return null;
    }

    public String convertTo(String code, Dialect dialect) {
        if (code == null || code.isEmpty()) {
            return null;
        }
        if (dialect == null) {
            throw new FailedConversionException("Cannot convert code to non-existent dialect");
        }
        int step = this.instructions[0].alias().length();
        if (code.length() % step != 0) {
            throw new FailedConversionException("Code's length makes it impossible for it be valid");
        }
        Instruction[] instructions = dialect.instructions();
        return convertTo(code, step, this.instructions, instructions);
    }

    private static String convertTo(String code, int step, Instruction[] fromInstructions, Instruction[] toInstructions) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < code.length(); i += step) {
            boolean isFound = false;
            String alias = code.substring(i, i + step);
            for (int j = 0; j < fromInstructions.length; j++) {
                if (fromInstructions[j].alias() == null || toInstructions[j].alias() == null) {
                    continue;
                }
                if (fromInstructions[j].alias().equals(alias)) {
                    result.append(toInstructions[j].alias());
                    isFound = true;
                    break;
                }
            }
            if (!isFound) {
                throw new FailedConversionException("Cannot convert code from non-existent dialect");
            }
        }
        return result.toString();
    }

    private static boolean validateAliases(String[] aliases, InstructionType[] types) {
        if (aliases == null || aliases.length != types.length) {
            return false;
        }
        HashSet<String> seenAliases = new HashSet<>();
        for (int i = 0; i < aliases.length - 1; i++) {
            if (aliases[i] == null) {
                if (!types[i].isOptional()) {
                    return false;
                }
                continue;
            }
            if (aliases[i].length() != aliases[i + 1].length()) {
                return false;
            }
            if (seenAliases.contains(aliases[i])) {
                return false;
            }
            seenAliases.add(aliases[i]);
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Dialect dialect)) {
            return false;
        }
        return Arrays.equals(dialect.instructions, instructions);
    }

}