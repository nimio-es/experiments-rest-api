package es.nimio.exercise.controller;

import es.nimio.exercise.repository.ImagesRepository;
import es.nimio.exercise.model.Image;
import es.nimio.exercise.model.api.ImageData;
import es.nimio.exercise.model.api.ImageResponse;
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
public class ImagesController {

    @Autowired
    private ImagesRepository imageRepository;

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

        return imageRepository.findById(id)
                .map(image -> new ResponseEntity<ImageResponse>(
                    new ImageResponse.OnlyImage(
                            new ImageData(image.getFileName(), image.getFileData())),
                    HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
