package io.theam.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import static spark.Spark.halt;
import static spark.Spark.stop;

public final class ShutdownAPI {

    private static final Logger LOG = LoggerFactory.getLogger(ShutdownAPI.class);

    public static Route shutdownServer = (req, res) -> {
        LOG.warn("Good bye!!! (Shutdown api request)");
        stop();  // stops spark
        return halt();
    };
}
