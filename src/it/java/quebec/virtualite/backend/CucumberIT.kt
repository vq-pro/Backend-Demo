package quebec.virtualite.backend

import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(Cucumber::class)
@CucumberOptions(
    features = ["src/features"],
    plugin = ["summary", "html:target/cucumber-reports.html"],
    snippets = CucumberOptions.SnippetType.CAMELCASE,
    tags = "not @Ignore"
)
class CucumberIT
