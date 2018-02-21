package es.nimio.exercise.client.commands.users

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import es.nimio.exercise.client.commands.BaseCommand
import es.nimio.exercise.client.commands.printWith
import es.nimio.exercise.client.service.postObject
import es.nimio.exercise.client.service.withUrl
import es.nimio.exercise.model.api.UserData

@Command(name = "create", description = "Creates a new user")
class CreateUserCommand : BaseCommand() {

    @Option(name = ["--new-username", "-nu"], description = "Define the user name to create")
    var newUsername: String = ""

    @Option(name = ["--new-password", "-np"], description = "Define the password to create")
    var newPassword: String = ""

    @Option(name = ["--as-admin"], description = "Set admin role to the new user")
    var isAdmin: Boolean = false

    override fun validate(): Boolean {
        var result = super.validate()
        if(newUsername.isEmpty()) {
            System.err.println("It's impossible create a new user without a name")
            result = false
        }

        if(newPassword.isEmpty()) {
            System.err.println("It's impossible create a new user without a password")
            result = false
        }

        return result
    }

    private val postUrl: String
        get() = "$host/users?username=$newUsername&password=$newPassword" +
                (if(isAdmin) "&asAdmin=true" else "")

    private fun createNewUser() : UserData =
            restClient withUrl postUrl postObject UserData::class.java

    override fun doRun() =
            createNewUser() printWith pretty_print_json
}