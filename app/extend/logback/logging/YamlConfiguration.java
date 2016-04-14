package extend.logback.logging;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import extend.logback.logging.config.HttpLoggingConfiguration;

public class YamlConfiguration {

    private File workDir;
    private HttpLoggingConfiguration statisticsLogConfiguration;

    public YamlConfiguration(Map<String, ?> config) throws IOException {
        init(config);
    }

    public YamlConfiguration(File ymlFile) throws IOException {
        Map<String, ?> config = (Map<String, ?>) new Yaml().load(new FileReader(ymlFile));
        init(config);
    }

    public YamlConfiguration(String ymlFilePath) throws IOException {
        Map<String, ?> config = (Map<String, ?>) new Yaml().load(new FileReader(new File(
                ymlFilePath)));
        init(config);
    }

    protected void init(Map<String, ?> config) throws IOException {
        initLogging(config);
    }

    protected void initLogging(Map<String, ?> config) {
        initHttpStatisticsLogging(config);
        initWorkDir(config);
    }

    protected void initHttpStatisticsLogging(Map<String, ?> config) {
        Map<String, ?> httpConfig = (Map<String, ?>) config.get("http");
        if (httpConfig.containsKey("statisticsLog")) {
            statisticsLogConfiguration = new HttpLoggingConfiguration(
                    (Map<String, ?>) httpConfig.get("statisticsLog"));
        }
    }

    protected void initWorkDir(Map<String, ?> config) {
        if (config.containsKey("workDir")) {
            this.workDir = new File((String) config.get("workDir"));
        } else {
            this.workDir = new File(System.getProperty("java.io.tmpdir"));
        }
    }

    public HttpLoggingConfiguration getStatisticsLogConfiguration() {
        return this.statisticsLogConfiguration;
    }

    public File getWorkDir() {
        return this.workDir;
    }

}
