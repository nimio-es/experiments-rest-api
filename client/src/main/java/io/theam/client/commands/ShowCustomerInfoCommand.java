package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import io.theam.model.api.CustomerResponse;
import io.theam.model.api.CustomerResponseImageData;
import io.theam.util.UtilBase64Image;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Command(name = "show", description = "Shows the info of a customer")
public class ShowCustomerInfoCommand extends BaseCommand {

    @Option(name = "--id", description = "Customer identity to search")
    public Long customerId = -1L;

    @Option(name = {"--first-name", "-fn"}, description = "Customer first name to search")
    public String firstName;

    @Option(name = {"--last-name", "-ln"}, description = "Customer last name to search")
    public String lastName;

    @Option(name = "--ndi", description = "Customer last name to search")
    public String ndi;

    @Option(name = "--image-file", description = "Include in the info the image and save in the path")
    public String imagePath;

    @Option(name = "--show", description = "Opens default application to show the image")
    public boolean showImage = false;

    @Override
    protected boolean validate() {
        boolean result = super.validate();
        if(customerId <= 0 && StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName) && StringUtils.isEmpty(ndi)) {
            System.err.println("Or customer id, or first name, or last name or ndi have to be defined to complete de search");
            result = false;
        }
        return result;
    }

    @Override
    protected void doRun() {

        final RestClient restClient = new RestClient(username, password);
        final CustomerResponse customer = customerId > 0
                ? restClient.getCustomer(customerId, !StringUtils.isEmpty(imagePath))
                : !StringUtils.isEmpty(firstName)
                    ? restClient.lookupCustomerFirstName(firstName)
                    : !StringUtils.isEmpty(lastName)
                        ? restClient.lookupCustomerLastName(lastName)
                        : restClient.lookupCustomerNdi(ndi);

        try {
            System.out.println(pretty_print_json.writeValueAsString(customer));

            if(customer.getImage() instanceof CustomerResponseImageData.Image) {
                final CustomerResponseImageData.Image imageData = ((CustomerResponseImageData.Image)customer.getImage());
                UtilBase64Image.INSTANCE.decoder(imageData.getImageData().getFileData(), imagePath);

                if(showImage) {
                    try {
                        Desktop.getDesktop().open(new File(imagePath));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        } catch (JsonProcessingException e) {
            new RuntimeException(e);
        }
    }
}
