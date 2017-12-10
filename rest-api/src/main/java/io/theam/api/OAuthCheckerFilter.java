package io.theam.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Filter;

final class OAuthCheckerFilter {

    private static final Logger LOG = LoggerFactory.getLogger(OAuthCheckerFilter.class);

    static Filter oauthCheckerFilter = (request, response) -> {
        LOG.warn("In this point it's really necessary check API OAUTH TOKEN!!!!");
    };

}
