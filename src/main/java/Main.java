import com.asterexcrisys.bfi.Compiler;
import com.asterexcrisys.bfi.Interpreter;
import com.asterexcrisys.bfi.models.dialects.DialectType;
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