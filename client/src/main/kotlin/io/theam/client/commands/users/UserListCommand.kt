package io.theam.client.commands.users

import com.fasterxml.jackson.core.JsonProcessingException
import com.github.rvesse.airline.annotations.Command
import io.theam.client.commands.BaseCommand
import io.theam.client.service.UsersRestClient
import io.theam.model.api.UserData

@Command(name = "list", description = "Get the list of all users")
class UserListCommand : BaseCommand() {

    public override fun doRun() {

        var users: Collection<UserData>? = null
        users = UsersRestClient(username, password).users

        try {
            println(pretty_print_json.writeValueAsString(users))
        } catch (e: JsonProcessingException) {
            RuntimeException(e)
        }

        println("---------")
        println("Number of users: " + Integer.toString(users.size))
    }

}