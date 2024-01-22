package uwaterloo.exchange;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main extends QuarkusBase{
    public static void main(String... args) {
        QuarkusBase.start(() -> Quarkus.run(Main.class, args));
    }
}
