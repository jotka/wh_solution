package pl.finsys;

import org.apache.commons.cli.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Usage:
 * java -jar target\parser-jar-with-dependencies.jar --startDate=2017-01-01.00:00:00 --duration=daily --threshold=500 --accesslog=.\access.log
 *
 * The tool will parse the log file and store it in the database table LOG.
 * It output IP entries that are above the threshold to the console and load those entries to the separate table LOG_TRESHOLD.
 * This will also stores those entries in the database tables ADDRESS and LOG.
 *
 * Options are:
 * --accesslog is the path to the log file, for example ./access.log
 * --startDate is the start date to begin with
 * --duration can be hourly or daily.
 * --threshold is the threshold of the entries to filter by
 */
public class Main {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm:ss", Locale.US);
    private static final String OPTION_DURATION = "duration";
    private static final String OPTION_ACCESSLOG = "accesslog";
    private static final String OPTION_START_DATE = "startDate";
    private static final String OPTION_THRESHOLD = "threshold";
    private static final String OPTION_HOURLY = "hourly";
    private static final String OPTION_DAILY = "daily";

    public static void main(String[] args) {

        Options options = new Options();
        Option startDate = new Option("s", OPTION_START_DATE, true, "startDate");
        startDate.setRequired(true);
        options.addOption(startDate);
        Option duration = new Option("d", OPTION_DURATION, true, "duration (hourly|daily)");
        duration.setRequired(true);
        options.addOption(duration);
        Option threshold = new Option("t", OPTION_THRESHOLD, true, "threshold (integer)");
        threshold.setRequired(true);
        options.addOption(threshold);
        Option logPath = new Option("a", OPTION_ACCESSLOG, true, "/path/to/file");
        threshold.setRequired(true);
        options.addOption(logPath);

        CommandLineParser cmdParser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = cmdParser.parse(options, args);
            String dur = cmd.getOptionValue(OPTION_DURATION);
            if (!OPTION_DAILY.equals(dur) && !OPTION_HOURLY.equals(dur))
                throw new ParseException("Duration can be hourly or daily only.");

            Parser parser = new Parser(LocalDateTime.parse(cmd.getOptionValue(OPTION_START_DATE), DATE_TIME_FORMATTER),
                    Duration.valueOf(cmd.getOptionValue(OPTION_DURATION)),
                    Integer.valueOf(cmd.getOptionValue(OPTION_THRESHOLD)));

            parser.process(cmd.getOptionValue(OPTION_ACCESSLOG));

        } catch (ParseException | NumberFormatException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("log parser", options);
            System.exit(1);
        }
    }
}
