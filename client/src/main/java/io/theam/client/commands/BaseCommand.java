package io.theam.client.commands;

import com.github.rvesse.airline.annotations.Option;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseCommand implements Runnable {

    @Option(name = {"--username", "-u"}, description = "Define the user to access")
    public String username;

    @Option(name = {"--password", "-p"}, description = "Define the user to access")
    public String password;

    protected boolean validate() {
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            System.out.println("***********************************************************************");
            System.out.println("Username and/or Password are required parameters. Ask for help command!");
            System.out.println("***********************************************************************");
            return false;
        }
        return true;
    }

    protected abstract void doRun();

    @Override
    public void run() {
        if(validate()) {
            doRun();
        }
    }
}
