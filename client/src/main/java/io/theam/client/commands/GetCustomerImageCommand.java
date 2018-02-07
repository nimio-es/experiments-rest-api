package io.theam.client.commands;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import io.theam.model.api.ImageData;
import io.theam.util.UtilBase64Image;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Command(name = "get", description = "Gets/downloads the image of a customer")
public class GetCustomerImageCommand extends BaseCommandWithId {

    @Option(name = "--show", description = "Opens default application to show the image")
    public boolean showImage = false;

    @Option(name = {"--file", "-f"}, description = "The path to store the downloaded image")
    public String imagePath;

    @Override
    protected void doRun() {

        ImageData image = new RestClient(username, password).getCustomerImage(customerId);

        if(StringUtils.isEmpty(imagePath)) {
            System.out.println("Image downloaded but not stored locally (no image path defined)");
            System.out.println(image.toString());
        }else{
            UtilBase64Image.INSTANCE.decoder(image.getFileData(), imagePath);

            if(showImage) {
                try {
                    Desktop.getDesktop().open(new File(imagePath));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
