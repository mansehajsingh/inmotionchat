package com.inmotionchat.core;

import com.inmotionchat.core.soa.InMotionRootConfigurationParser;
import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InMotion {

    public static void main(String[] args) throws Exception {
        Options options = new Options();

        Option configurationDirectory = new Option("f", "conf", true, "Root directory of the configurations folder.");
        configurationDirectory.setRequired(true);
        options.addOption(configurationDirectory);

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true);

        InMotion inMotion = new InMotion(commandLine, args);
    }

    /**
     * Default constructor for Spring startup.
     */
    public InMotion() {}

    public InMotion(CommandLine options, String[] args) throws Exception {
        String configDirectoryPath = options.getOptionValue("conf");
        InMotionRootConfigurationParser rootConfigurationParser = new InMotionRootConfigurationParser(configDirectoryPath + "/inmotion.json");
        InMotionConfiguration.init(rootConfigurationParser);
        SpringApplication.run(InMotion.class, args);
    }

}
