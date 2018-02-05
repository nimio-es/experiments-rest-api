package io.theam.client.commands;

import com.github.rvesse.airline.annotations.Option;

public abstract class BaseCommandWithId extends BaseCommand {

    @Option(name = "--id", description = "Customer identity to handle")
    public Long customerId = -1L;

    @Override
    protected boolean validate() {
        boolean result = super.validate();
        if(customerId <  0L) {
            System.out.println("The customer id is necessary.");
            result = false;
        }
        return result;
    }
}
