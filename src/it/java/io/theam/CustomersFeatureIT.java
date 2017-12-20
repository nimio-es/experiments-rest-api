package io.theam;


import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/it/resources/features", strict = true)
public class CustomersFeatureIT {
}
