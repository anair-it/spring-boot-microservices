package org.anair.services.c.config;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class RequestLoggingFilterConfigTest {

    @Test
    public void logFilter() {
        RequestLoggingFilterConfig config = new RequestLoggingFilterConfig();
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = config.logFilter();
        Assert.assertNotNull(commonsRequestLoggingFilter);
    }
}