package es.nimio.exercise.client.commands.images

import com.github.rvesse.airline.annotations.Command
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.commands.printWith
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.ImageResponse
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all images")
class ImageListCommand : BaseCommand() {

    private fun allImages(): Collection<ImageResponse> =
            bodyOf(restClient withUrl "$host/images" getEntity
                    object : ParameterizedTypeReference<Collection<ImageResponse>>() {})

    override fun doRun() = allImages() printWith pretty_print_json
}
