package io.theam.client.commands;

import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import io.theam.client.service.RestClient;
import io.theam.model.api.ImageData;
import io.theam.model.api.ImageResponse;
import io.theam.util.UtilBase64Image;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Command(name = "get", description = "Gets/downloads the image of a customer")
public class GetImageCommand extends BaseCommand {

    @Option(name = "--id", description = "Image identity to handle")
    public Long imageId = -1L;

    @Option(name = "--show", description = "Opens default application to show the image")
    public boolean showImage = false;

    @Option(name = {"--file", "-f"}, description = "The path to store the downloaded image")
    public String imagePath;

    @Override
    protected boolean validate() {
        boolean result = super.validate();
        if(imageId <  0L) {
            System.out.println("The image id is necessary.");
            result = false;
        }
        return result;
    }

    @Override
    protected void doRun() {
        ImageResponse image = new RestClient(username, password).getImage(imageId);

        if(StringUtils.isEmpty(imagePath)) {
            System.out.println("Image downloaded but not stored locally (no image path defined)");
            System.out.println(image.toString());
        }else{

            final ImageData imageData =
                    image instanceof ImageResponse.OnlyImage
                            ? ((ImageResponse.OnlyImage)image).getImageData()
                            : ((ImageResponse.ImageWithCustomer)image).getImageData();

            UtilBase64Image.INSTANCE.decoder(imageData.getFileData(), imagePath);

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
