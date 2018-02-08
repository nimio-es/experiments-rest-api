package io.theam.client.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.rvesse.airline.annotations.Command;
import io.theam.client.service.RestClient;
import io.theam.model.api.ImageResponse;

import java.util.Collection;

@Command(name = "list", description = "Get the list of all images")
public class GetImageListCommand extends BaseCommand {
    @Override
    protected void doRun() {

        final Collection<ImageResponse> images = new RestClient(username, password).getAllImages();
        try {
            System.out.println(pretty_print_json.writeValueAsString(images));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
