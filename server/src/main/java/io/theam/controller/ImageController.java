package io.theam.controller;

import io.theam.model.Image;
import io.theam.model.api.ImageData;
import io.theam.model.api.ImageResponse;
import io.theam.repository.ImageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.LinkedList;

@RestController
@RequestMapping("/images")
public class ImageController {

    private static Logger logger = LoggerFactory.getLogger(ImageController.class);

    @Autowired
    private ImageRepository imageRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<ImageResponse>> getAllImages() {

        final Collection<ImageResponse> images = new LinkedList<>();

        for (final Image image : imageRepository.findAll()) {
            images.add(new ImageResponse.OnlyImage(new ImageData(image.getFileName(), image.getFileData())));
        }

        return new ResponseEntity(images, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<ImageResponse> getImage(@PathVariable(name="id")Long id) {

        final Image image = imageRepository.findOne(id);

        if (image != null)
            return new ResponseEntity(
                    new ImageResponse.OnlyImage(
                            new ImageData(image.getFileName(), image.getFileData())),
                    HttpStatus.OK);

        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }
}
