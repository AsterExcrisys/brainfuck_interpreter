# 🧠 Brainfuck Interpreter

A Java-based Brainfuck interpreter that goes beyond basic execution. It includes:([alexprengere.github.io][1])

* An **optimizer** to reduce overhead from repeated operations
* A **debugger** for step-by-step code execution
* A **compiler/decompiler** to convert between Brainfuck code and bytecode representations([Gist][2])

This tool is ideal for experimenting with the Brainfuck language, analyzing its behavior, and understanding its execution flow.

---

## 📦 Package Structure

```
com.asterexcrisys.brainfuck
│
├── exceptions                # Custom exceptions for interpreter, debugger, and compiler
│   ├── FailedConversionException.java
│   ├── IllegalDebuggerStateException.java
│   ├── InvalidSyntaxException.java
│   └── StreamInteractionException.java
│
├── models                    # Core data models
│   ├── dialects
│   │   ├── Dialect.java
│   │   └── DialectType.java
│   │
│   ├── instructions
│   │   ├── Instruction.java
│   │   └── InstructionType.java
│   │
│   └── nodes
│       ├── BlockNode.java
│       ├── CommandNode.java
│       ├── LoopNode.java
│       ├── Node.java
│       └── ProgramNode.java
│
├── services                 # Lower-level service components
│   ├── Counter.java
│   ├── Generator.java
│   ├── Memory.java
│   ├── Optimizer.java
│   └── Parser.java
│
├── Compiler.java            # High-level code compiler for Brainfuck programs
├── Debugger.java            # Debugger interface for step-by-step execution
└── Interpreter.java         # Interpreter for compiled and non-compiled instruction streams
```



---

## 🧰 Technologies Used

| Category      | Tool / Technology | Notes                                   |
| ------------- | ----------------- | --------------------------------------- |
| Language      | Java 21           | Modern LTS version                      |
| Build Tool    | Maven             | Project build and dependency management |
| Testing       | JUnit 5           | For unit and integration testing        |
| Documentation | Javadoc           | Interface and API documentation         |
| IDE           | IntelliJ IDEA     | Recommended for project development     |

---

## 🚀 Getting Started

### 🛠 Requirements

* Java 21 or higher
* Maven (for building the project)
* IntelliJ IDEA (recommended IDE)

### 🧪 Example Usage

```java
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
```
---

## 💡 Features

| Feature         | Description                                                                                     |
| --------------- | ----------------------------------------------------------------------------------------------- |
| **Interpreter** | Executes Brainfuck code, handling all eight commands and managing the memory tape.              |
| **Optimizer**   | Simplifies repetitive operations to enhance performance.                                        |
| **Debugger**    | Allows step-by-step execution, inspecting the state of the memory tape and instruction pointer. |
| **Compiler**    | Translates Brainfuck code into a custom bytecode format for efficient execution.                |
| **Decompiler**  | Converts bytecode back into human-readable Brainfuck code.                                      |

---

## 📈 Future Improvements

- ✅ Enhance optimization strategies for more efficient execution.
- ⏳ Implement a graphical user interface (GUI) inside an integrated development enviroment (IDE) for easier interaction.
- ⏳ Support for additional Brainfuck dialects or extensions.

---

## 📄 License

This project is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for more details.

---

## 👨‍💻 Author

Developed by [AsterExcrisys](https://github.com/AsterExcrisys) — contributions, forks, and suggestions are welcome!
