#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

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