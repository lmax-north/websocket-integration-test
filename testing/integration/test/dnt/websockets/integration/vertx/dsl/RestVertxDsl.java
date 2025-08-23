package dnt.websockets.integration.vertx.dsl;

import com.lmax.simpledsl.api.DslParams;
import com.lmax.simpledsl.api.OptionalArg;
import com.lmax.simpledsl.api.RequiredArg;
import dnt.websockets.communications.SetPropertyResponse;
import dnt.websockets.integration.vertx.VertxRestDriver;
import education.common.result.Result;

import static org.assertj.core.api.Assertions.assertThat;

public class RestVertxDsl
{
    private final VertxRestDriver restDriver;

    public RestVertxDsl(VertxRestDriver restDriver)
    {
        this.restDriver = restDriver;
    }

    public void getProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("expectedValue"),
                new OptionalArg("expectedStatusCode").setDefault("200"));
        String key = params.value("key");
        String expectedValue = params.value("expectedValue");
        String actualValue = this.restDriver.getProperty(key, params.valueAsInt("expectedStatusCode"));
        assertThat(expectedValue).isEqualTo(actualValue);
    }

    public void setProperty(String... args)
    {
        final DslParams params = DslParams.create(args,
                new RequiredArg("key"),
                new RequiredArg("value"),
                new OptionalArg("expectStatusCode").setDefault("200"));
        String key = params.value("key");
        String value = params.value("value");
        this.restDriver.setProperty(key, value, params.valueAsInt("expectedStatusCode"));
    }
}
