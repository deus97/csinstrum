package com.gorilla;

import com.gorilla.calculation.CalculationService;
import com.gorilla.file.InstrumentsSupplier;
import com.gorilla.util.ConfigurationService;
import org.h2.tools.Server;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.*;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.Map;

public class Main
{
    private static final String YES = "y";
    private static final String NO = "n";

    private static Server h2Server;
    private static Server h2WebServer;

    private static ConfigurationService config;

    public static void main( String[] args ) throws Exception {
        
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("spring/spring-config.xml");
        ctx.registerShutdownHook();

        config = ctx.getBean(ConfigurationService.class);

        startDatabase(config.getDbTcpPort(), config.getDbWebPort());

        boolean yesNoAnswer = askQuestionAndGetBooleanAnswer("Start processing of file '" + config.getInputFileName() + "' (y/n)? ");

        if(yesNoAnswer) {
            CalculationService calculationService = ctx.getBean(CalculationService.class);
            InstrumentsSupplier instrumentsSupplier = ctx.getBean(InstrumentsSupplier.class);

            long before = System.currentTimeMillis();
            System.out.println("working...");
            instrumentsSupplier.iterator().forEachRemaining(calculationService::accept);
            System.out.println("Done! Time spent: " + (System.currentTimeMillis()-before));


            printStatistics(calculationService.getStatisticsAsMap());
        }

        stopDatabase();

        System.out.println("Bye!");

        ctx.close();
    }

    private static void printStatistics(Map<String, String> instrumentToStatisticsMap) {
        if(config.isResultTofile()) {
            try(PrintStream out = new PrintStream(new File(config.getResultFilePath()))) {
                printToStream(instrumentToStatisticsMap, out);
            } catch (Exception e) {
                System.out.println("Cannot print results, reason: " + e);
            }
        } else {
            printToStream(instrumentToStatisticsMap, System.out);
        }
    }

    private static void printToStream(Map<String, String> what, PrintStream where) {
        what.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getKey))
                .forEach(x -> where.println(x.getKey() + ": " + x.getValue()));
    }

    private static void stopDatabase() {
        h2WebServer.stop();
        h2Server.stop();
    }

    private static void startDatabase(String tcpPort, String webPort) throws SQLException {
        if (h2Server == null) {
            h2Server = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", tcpPort);
            h2Server.start();
            h2WebServer = Server.createWebServer("-web","-webAllowOthers","-webPort", webPort);
            h2WebServer.start();
            System.out.println("H2 TCP/Web servers initialized");
        } else {
            System.out.println("H2 TCP/Web servers already initialized");
        }
    }

    private static boolean askQuestionAndGetBooleanAnswer(String question) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println(question);

        String userInput = "none";
        try {
            while(!YES.equalsIgnoreCase(userInput) && !NO.equalsIgnoreCase(userInput)) {
                userInput = reader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInput.equalsIgnoreCase(YES);
    }
}
