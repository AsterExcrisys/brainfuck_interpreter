import com.asterexcrisys.bfi.Interpreter;
import com.asterexcrisys.bfi.models.dialects.DialectType;

public class Main {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter(DialectType.BRAINFUCK.dialect());
        interpreter.appendCode(",[.,]");
        interpreter.interpret(true);
    }

}