package runner;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"features/CT001.feature",},
        glue = {"stepdefinition", "commons"},
        tags = {"@CT001"},
        monochrome = true,
        plugin = {"report.ExtentCucumberFormatter:"})
public class RunnerTest {
}