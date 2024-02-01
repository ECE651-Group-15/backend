package infrastructure;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main extends QuarkusBase{
    public static void main(String... args) {
        start(() -> Quarkus.run(Main.class, args));
    }
}
