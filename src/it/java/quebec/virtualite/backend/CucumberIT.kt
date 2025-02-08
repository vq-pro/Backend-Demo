package quebec.virtualite.backend

import io.cucumber.core.options.Constants.PLUGIN_PROPERTY_NAME
import org.junit.platform.suite.api.ConfigurationParameter
import org.junit.platform.suite.api.IncludeEngines
import org.junit.platform.suite.api.SelectClasspathResource
import org.junit.platform.suite.api.SelectDirectories
import org.junit.platform.suite.api.Suite

@Suite
@IncludeEngines("cucumber")
@SelectDirectories(".")
@SelectClasspathResource("src/features")
@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/last-run.html")
class CucumberIT
