package org.anair.services.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

public class RequestLoggingFilterConfigTest {

    @Test
    public void logFilter() {
        RequestLoggingFilterConfig config = new RequestLoggingFilterConfig();
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = config.logFilter();
        assertNotNull(commonsRequestLoggingFilter);
    }
}