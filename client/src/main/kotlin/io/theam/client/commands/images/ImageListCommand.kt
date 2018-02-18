package io.theam.client.commands.images

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.service.CustomersRestClient

@Command(name = "list", description = "Get the list of all images")
class ImageListCommand : BaseCommand() {
    override fun doRun() {

        val images = CustomersRestClient(username, password).allImages
        try {
            println(pretty_print_json.writeValueAsString(images))
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }

    }
}
