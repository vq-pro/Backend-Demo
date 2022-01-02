package quebec.virtualite.backend;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

@RunWith(Cucumber.class)
@CucumberOptions
	(
		features = "src/features",
		plugin =
			{
			    "pretty",
				"summary",
				"html:target/cucumber-reports",
				"junit:target/cucumber-reports/cucumber.xml"
			},
		//		junit = "--step-notifications",
		monochrome = true,
		strict = true,
		snippets = CAMELCASE,
		tags = "not @Ignore"
	)
public class CucumberIT
{
}
