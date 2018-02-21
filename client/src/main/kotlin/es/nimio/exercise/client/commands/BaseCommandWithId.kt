package es.nimio.exercise.client.commands

import com.github.rvesse.airline.annotations.Option

abstract class BaseCommandWithId : BaseCommand() {

    @Option(name = ["--id"], description = "Customer identity to handle")
    var customerId: Long? = -1L

    override fun validate(): Boolean {
        var result = super.validate()
        if (customerId!! < 0L) {
            println("The customer id is necessary.")
            result = false
        }
        return result
    }
}
