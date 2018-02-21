package es.nimio.exercise.client.commands.users

import com.github.rvesse.airline.annotations.Command
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.service.bodyOf
import es.nimio.exercise.client.service.getEntity
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.UserData
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