import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestLogback {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(TestLogback.class);
        System.out.println(logger.getName());
    }
}
