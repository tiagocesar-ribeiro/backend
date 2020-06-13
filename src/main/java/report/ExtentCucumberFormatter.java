package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.ExtentXReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class ExtentCucumberFormatter implements Reporter, Formatter {

    private static ExtentReports extentReports;
    static ExtentHtmlReporter htmlReporter;
    private boolean scenarioOutlineFlag;

    private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<LinkedList<Step>> stepListThreadLocal = new InheritableThreadLocal<>();
    static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();

    static String DATE_TIME = getFormattedDateTime();
    static String PATH_FILE = "output/report_"+getFormattedDateTime()+"/";
    static String REPORT_NAME = "Report"+getFormattedDateTime()+".html";

    private static Step step;
    static Markup table;
    static String data[][];
    static String image;
    static String textInfo;


    public ExtentCucumberFormatter(File file) throws UnsupportedEncodingException {
        file = new File(PATH_FILE+REPORT_NAME);
        setExtentHtmlReport(file);
        setExtentReport();
        stepListThreadLocal.set(new LinkedList<Step>());
        scenarioOutlineFlag = false;
    }

    private static void setExtentHtmlReport(File file) {
        if (htmlReporter != null) {
            return;
        }
        if (file == null || file.getPath().isEmpty()) {
            file = new File(ExtentProperties.INSTANCE.getReportPath());
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        htmlReporter = new ExtentHtmlReporter(file);
        htmlReporter.config().setTheme(Theme.DARK);
        htmlReporter.config().setDocumentTitle("Relatório Cucumber");
        htmlReporter.config().setEncoding("utf-8");
        //htmlReporter.config().setReportName("ReportTeste");
        htmlReporter.config().setTimeStampFormat("dd/mm/yyyy hh:mm:ss");
        // htmlReporter.config().setJS("$(<white-text>).removeClass()");
    }

    static ExtentHtmlReporter getExtentHtmlReport() {
        return htmlReporter;
    }

    private static void setExtentReport() throws UnsupportedEncodingException {
        if (extentReports != null) {
            return;
        }
        extentReports = new ExtentReports();

        //Insere propriedades do Relatorio
        ExtentProperties extentProperties = ExtentProperties.INSTANCE;
        extentReports.setSystemInfo("Environment", "QA");
        extentReports.setSystemInfo("Creation Date", getFormattedDateTime());
        extentReports.setGherkinDialect("pt");

        if (extentProperties.getExtentXServerUrl() != null) {
            String extentXServerUrl = extentProperties.getExtentXServerUrl();
            try {
                URL url = new URL(extentXServerUrl);
                ExtentXReporter xReporter = new ExtentXReporter(url.getHost());
                xReporter.config().setServerUrl(extentXServerUrl);
                xReporter.config().setProjectName(extentProperties.getProjectName());
                extentReports.attachReporter(htmlReporter, xReporter);
                return;
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Invalid ExtentX Server URL", e);
            }
        }
        extentReports.attachReporter(htmlReporter);
    }

    static ExtentReports getExtentReport() {
        return extentReports;
    }

    public void syntaxError(String state, String event, List<String> legalEvents, String uri,
                            Integer line) {

    }

    public void uri(String uri) {

    }

    public void feature(Feature feature) {
        featureTestThreadLocal.set(getExtentReport().createTest(feature.getName()));
        ExtentTest test = featureTestThreadLocal.get();

        for (Tag tag : feature.getTags()) {
            test.assignCategory(tag.getName());
        }
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        scenarioOutlineFlag = true;
        ExtentTest node = featureTestThreadLocal.get();
        //Esquema de Cenario
        scenarioOutlineThreadLocal.set(node);
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        if (scenarioOutlineFlag) {
            scenarioOutlineFlag = false;
        }

        ExtentTest scenarioNode;

        if (scenarioOutlineThreadLocal.get() != null && scenario.getKeyword().trim().equalsIgnoreCase("Scenario Outline")) {
            scenarioNode = scenarioOutlineThreadLocal.get().createNode(scenario.getName());
        } else {
            scenarioNode = featureTestThreadLocal.get().createNode(scenario.getName());
        }

        //Insere Tag
        for (Tag tag : scenario.getTags()) {
            String texto = "<span"+"\n"+"class='node-duration"+"\n"+"label"+"\n"+"grey"+"\n"+"lighten-1"+"\n"+"white-text\'>"+tag.getName()+"</span>";
            scenarioNode.assignCategory(texto);
        }

        scenarioThreadLocal.set(scenarioNode);
    }

    public void match(Match match) {
        step = stepListThreadLocal.get().poll();
        data = null;
        if (step.getRows() != null) {
            List<DataTableRow> rows = step.getRows();
            int rowSize = rows.size();
            for (int i = 0; i < rowSize; i++) {
                DataTableRow dataTableRow = rows.get(i);
                List<String> cells = dataTableRow.getCells();
                int cellSize = cells.size();
                if (data == null) {
                    data = new String[rowSize][cellSize];
                }
                for (int j = 0; j < cellSize; j++) {
                    data[i][j] = cells.get(j);
                }
            }
        }

        if (data != null) {
            table = MarkupHelper.createTable(data);
        }

        ExtentTest scenarioTest = scenarioThreadLocal.get();
        ExtentTest stepTest = null;

        try {
            //Cria um nó para os steps dos testes
            stepTest = scenarioTest;

        } catch (Exception e) {
            e.printStackTrace();
        }

        stepTestThreadLocal.set(stepTest);
    }

    public void result(Result result) {
        if (scenarioOutlineFlag) {
            return;
        }
        //Insere Resultado quando passa o passo do teste
        if (Result.PASSED.equals(result.getStatus())) {

            if(image != null) {


                try {
                    stepTestThreadLocal.get().pass("<b>"+ step.getKeyword() + step.getName() + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(image+".png").build());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

                image = null;

                if (data != null) {
                    table = MarkupHelper.createTable(data);
                    stepTestThreadLocal.get().log(Status.PASS,table);
                }

                if(textInfo != null) {
                    stepTestThreadLocal.get().info(textInfo);
                    textInfo = null;
                }

            }else {
                stepTestThreadLocal.get().pass("<b>"+ step.getKeyword() + step.getName() + "</b>");
                if (data != null) {
                    table = MarkupHelper.createTable(data);
                    stepTestThreadLocal.get().log(Status.PASS,table);
                }
                if(textInfo != null) {
                    stepTestThreadLocal.get().info(textInfo);
                    textInfo = null;
                }

            }

        }
        //Insere Resultado quando falha o passo do teste
        else if (Result.FAILED.equals(result.getStatus())) {

            if(image != null) {

                try {
                    stepTestThreadLocal.get().fail("<b>"+ step.getKeyword() + step.getName() + "</b>", MediaEntityBuilder.createScreenCaptureFromPath(image+".png").build());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                image = null;

                if (data != null) {
                    table = MarkupHelper.createTable(data);
                    stepTestThreadLocal.get().log(Status.FAIL,table);
                    stepTestThreadLocal.get().fail(result.getError());
                }

                if(textInfo != null) {
                    stepTestThreadLocal.get().info(textInfo);
                    textInfo = null;
                }
                stepTestThreadLocal.get().fail(result.getError());

            }else {

                stepTestThreadLocal.get().fail("<b>"+ step.getKeyword() + step.getName() + "</b>");

                if (data != null) {
                    table = MarkupHelper.createTable(data);
                    stepTestThreadLocal.get().log(Status.FAIL,table);
                }

                if(textInfo != null) {
                    stepTestThreadLocal.get().info(textInfo);
                    textInfo = null;
                }

                stepTestThreadLocal.get().fail(result.getError());
            }
        }
        //Insere Resultado quando pula os passos do teste
        else if (Result.SKIPPED.equals(result)) {
            stepTestThreadLocal.get().skip("<b>"+ step.getKeyword() + step.getName() + "</b>");
            stepTestThreadLocal.get().skip(Result.SKIPPED.getStatus());
        }
        //Insere Resultado quando pula os passos do teste
        else if (Result.UNDEFINED.equals(result)) {
            stepTestThreadLocal.get().skip("<b>"+ step.getKeyword() + step.getName() + "</b>");
            stepTestThreadLocal.get().skip(Result.UNDEFINED.getStatus());
        }
    }

    public void examples(Examples examples) {

    }

    public void background(Background background) {

    }

    public void scenario(Scenario scenario) {

    }

    public void step(Step step) {
        if (scenarioOutlineFlag) {
            return;
        }
        stepListThreadLocal.get().add(step);
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {

    }

    public void done() {
        getExtentReport().flush();
    }

    public void close() {

    }

    public void eof() {

    }

    public void before(Match match, Result result) {

    }

    public void after(Match match, Result result) {

    }

    public void embedding(String mimeType, byte[] data) {

    }

    public void write(String text) {

    }

    private static String getFormattedDateTime() {

        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String today = formatter.format(date);
        return today;
    }

    public static void insertInfoTextInStepReport(String text) {

        try {
            if(text != null) {
                textInfo = text;
            }
        }
        catch(Exception e) {
            e.getMessage();
        }
    }

}
