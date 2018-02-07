package io.theam.client.commands;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import org.apache.commons.lang3.StringUtils;

@Command(name = "set", description = "Sets the image of a customer")
public class SetCustomerImageCommand extends BaseCommandWithId {

    @Option(name = {"--file", "-f"}, description = "File with the image to upload")
    public String imagePath;

    @Override
    protected boolean validate() {
        boolean result = super.validate();
        if(StringUtils.isEmpty(imagePath)) {
            System.out.println("You have to define de image file");
            result = false;
        }
        return result;
    }

    @Override
    protected void doRun() {
        System.out.println("Associate the image '" + imagePath + "' to customer with id: " + Long.toString(customerId));
        new RestClient(username, password).setCustomerImage(customerId, imagePath);
    }
}
