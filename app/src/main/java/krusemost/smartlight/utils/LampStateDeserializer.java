package krusemost.smartlight.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import krusemost.smartlight.domain.LampState;

/**
 * Created by Niek on 10/25/2014.
 *
 * Helper class for Deserializing the lamp state.
 */
public class LampStateDeserializer implements JsonDeserializer<LampState> {
    @Override
    public LampState deserialize(JsonElement element, Type arg1, JsonDeserializationContext arg2)
    {
        int key = element.getAsInt();
        return LampState.fromKey(key);
    }
}
