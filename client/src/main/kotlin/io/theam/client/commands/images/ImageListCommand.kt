package io.theam.client.commands.images

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.commands.printWith
import io.theam.client.service.bodyOf
import io.theam.client.service.getEntity
import io.theam.client.service.withUrl
import io.theam.model.api.ImageResponse
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all images")
class ImageListCommand : BaseCommand() {

    private fun allImages(): Collection<ImageResponse> =
            bodyOf(restClient withUrl "$host/images" getEntity
                    object : ParameterizedTypeReference<Collection<ImageResponse>>() {})

    override fun doRun() = allImages() printWith pretty_print_json
}
