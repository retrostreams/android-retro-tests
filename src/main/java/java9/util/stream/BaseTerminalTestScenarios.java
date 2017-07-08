package java9.util.stream;

import java9.util.function.Function;
import java9.util.stream.BaseStream;
import java9.util.stream.StreamShape;

public final class BaseTerminalTestScenarios {

    public static <U, R, S_OUT extends BaseStream<U, S_OUT>> R run(Function<S_OUT, R> terminalF, S_OUT source, StreamShape shape) {
        return terminalF.apply(source);
    }

    private BaseTerminalTestScenarios() {
    }
}
