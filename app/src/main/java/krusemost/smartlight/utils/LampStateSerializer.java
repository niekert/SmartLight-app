package krusemost.smartlight.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import krusemost.smartlight.domain.LampState;

/**
 * Created by Niek on 10/25/2014.
 *
 * Serialize the LampState enum to an integer.
 */
public class LampStateSerializer implements JsonSerializer<LampState> {
    @Override
    public JsonElement serialize(LampState lampState, Type type, JsonSerializationContext jsonSerializationContext) {
       return new JsonPrimitive(lampState.getKey());
    }
}
