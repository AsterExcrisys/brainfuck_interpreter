import com.asterexcrisys.bfi.Interpreter;

public class Main {

    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        interpreter.appendCode("++++++++[>++++[>++>+++>+++>+<<<<-]>+>+>->>+[<]<-]>>.>---.+++++++..+++.>>.<-.<.+++.------.--------.>>+.>++.");
        interpreter.interpret();
    }

}