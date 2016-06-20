package com.gorilla.util;

import com.gorilla.calculation.StatisticsCalculator;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Optional;


public class ConfigurationService implements ApplicationContextAware {

    private static final String DEFAULT_CALCULATOR_BEAN_ID = "defaultCalculator";

    private ApplicationContext ctx;

    private String inputFileName;

    private String dateFormatStr;

    private String filedSeparator;

    private String dbTcpPort;

    private String dbWebPort;

    private boolean resultTofile;

    private String resultFilePath;

    private Map<String, StatisticsCalculator> mappings;

    public Optional<StatisticsCalculator> getCalculatorForInstrument(String instrumentName) {
        if(instrumentName == null || instrumentName.isEmpty()) {
            throw new IllegalArgumentException("Null or empty instrument name");
        }
        return Optional.ofNullable(mappings.get(instrumentName));
    }

    /**
     * Get new prototype scoped bean
     * @return new instance of default calculator
     */
    public StatisticsCalculator getDefaultCalculator() {
        StatisticsCalculator result = (StatisticsCalculator) ctx.getBean(DEFAULT_CALCULATOR_BEAN_ID);
        if(result == null) {
            throw new IllegalStateException("Unable to lookup defaultCalculator, check your configuration.");
        }
        return result;
    }

    public String getInputFileName() {
        return inputFileName;
    }

    @Required
    public void setInputFileName(String inputFileName) {
        this.inputFileName = inputFileName;
    }

    public String getDateFormatStr() {
        return dateFormatStr;
    }

    @Required
    public void setDateFormatStr(String dateFormatStr) {
        this.dateFormatStr = dateFormatStr;
    }

    public String getFiledSeparator() {
        return filedSeparator;
    }

    @Required
    public void setFiledSeparator(String filedSeparator) {
        this.filedSeparator = filedSeparator;
    }

    public Map<String, StatisticsCalculator> getMappings() {
        return mappings;
    }

    @Required
    public void setMappings(Map<String, StatisticsCalculator> mappings) {
        this.mappings = mappings;
    }

    public String getDbTcpPort() {
        return dbTcpPort;
    }

    @Required
    public void setDbTcpPort(String dbTcpPort) {
        this.dbTcpPort = dbTcpPort;
    }

    public String getDbWebPort() {
        return dbWebPort;
    }

    @Required
    public void setDbWebPort(String dbWebPort) {
        this.dbWebPort = dbWebPort;
    }

    public boolean isResultTofile() {
        return resultTofile;
    }

    @Required
    public void setResultTofile(boolean resultTofile) {
        this.resultTofile = resultTofile;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    @Required
    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
