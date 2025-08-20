package dnt.websockets.communications;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestsTest
{
    private final ObjectMapper mapper;
    private final ObjectReader messageReader;

    public RequestsTest()
    {
        mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        mapper.registerSubtypes(new NamedType(OptionsRequest.class, OptionsRequest.class.getSimpleName()));
        messageReader = mapper.readerFor(AbstractRequest.class);
    }

    @Test
    public void testSerde() throws JsonProcessingException
    {
        OptionsRequest original = new OptionsRequest().attachCorrelationId(1);
        final String json = mapper.writeValueAsString(original);

        AbstractRequest decoded = messageReader.readValue(json);
        assertThat(decoded).isInstanceOf(OptionsRequest.class);
    }
}
