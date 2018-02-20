package io.theam.client.commands.users

import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.service.bodyOf
import io.theam.client.service.getEntity
import io.theam.client.service.withUrl
import io.theam.model.api.UserData
import org.springframework.core.ParameterizedTypeReference

@Command(name = "list", description = "Get the list of all users")
class UserListCommand : BaseCommand() {

    private val users: Collection<UserData>
        get() =
            bodyOf(restClient withUrl "$host/users" getEntity
                    object: ParameterizedTypeReference<Collection<UserData>>() {})

    public override fun doRun() =
            users.let { listOf(
                    pretty_print_json.writeValueAsString(it),
                    "------------",
                    "Number of users: ${it.size}")
                    .forEach {println(it)} }

}