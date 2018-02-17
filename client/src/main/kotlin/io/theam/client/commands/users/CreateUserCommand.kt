package io.theam.client.commands.users

import com.github.rvesse.airline.annotations.Command
import com.github.rvesse.airline.annotations.Option
import io.theam.client.commands.BaseCommand
import io.theam.client.service.UsersRestClient

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

    override fun doRun() {
        println(pretty_print_json.writeValueAsString(
                UsersRestClient(username, password).createNewUser(newUsername, newPassword, isAdmin)
        ))
    }
}