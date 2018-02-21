package es.nimio.exercise.repository;

import es.nimio.exercise.model.Image;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImagesRepository extends CrudRepository<Image, Long> {

    Image findByCustomerId(long id);
}
