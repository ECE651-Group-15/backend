package infrastructure;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Quarkus;
import java.util.List;
import java.util.Optional;

public abstract class QuarkusBase implements QuarkusApplication {
    public static final List<String> TEST_ENV = List.of("stub", "test");

    public QuarkusBase() {
    }

    public static void start(Runnable startFunction) {
        String profile = System.getProperty("quarkus.profile", (String)Optional.ofNullable(System.getProperty("mode")).orElse("stub"));
        System.setProperty("quarkus.profile", profile);
        String env = TEST_ENV.contains(profile) ? "dev" : profile;
        System.setProperty("current.env", env);
        startFunction.run();
    }

    public void startupLogic() {
    }

    public int run(String... args) throws Exception {
        this.startupLogic();
        Quarkus.waitForExit();
        return 0;
    }
}