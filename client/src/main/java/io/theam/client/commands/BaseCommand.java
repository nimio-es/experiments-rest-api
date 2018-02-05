package io.theam.client.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.rvesse.airline.annotations.Option;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseCommand implements Runnable {

    protected static final ObjectMapper pretty_print_json = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    @Option(name = {"--username", "-u"}, description = "Define the user to access")
    public String username;

    @Option(name = {"--password", "-p"}, description = "Define the user to access")
    public String password;

    public BaseCommand() {
        this.username = System.getenv("THEAM_USERNAME");
        this.password = System.getenv("THEAM_PASSWORD");
    }

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
