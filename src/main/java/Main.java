import com.asterexcrisys.brainfuck.Compiler;
import com.asterexcrisys.brainfuck.Interpreter;
import com.asterexcrisys.brainfuck.models.dialects.DialectType;
import java.util.Arrays;
import java.util.HexFormat;

public class Main {

    public static void main(String[] args) {
        Compiler compiler = new Compiler(DialectType.BRAINFUCK.dialect());
        compiler.appendCode(",[.,]");
        System.out.println(Arrays.toString(compiler.compile(true)));
        System.out.println(HexFormat.of().formatHex(compiler.compile(true)).toUpperCase());
        Interpreter.interpret(compiler.compile(true), true);
    }

}