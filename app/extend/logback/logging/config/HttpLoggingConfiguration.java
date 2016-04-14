package extend.logback.logging.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpLoggingConfiguration extends LoggingConfigurationFactory {

    private List<String> cookies = new ArrayList<String>();

    public HttpLoggingConfiguration(Map<String, ?> config) {
        super(config);
        if (config.containsKey("cookies")) {
            cookies = (List<String>) config.get("cookies");
        }
    }

    public List<String> getCookies() { return cookies; }

}
