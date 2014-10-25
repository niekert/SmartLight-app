package krusemost.smartlight.domain;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

/**
 * Created by Niek on 10/25/2014.
 *
 * DTO for LampIDs with their state.
 */
public class LampIDState {
    @SerializedName("LampID")
    private UUID lampID;
    @SerializedName("LampState")
    private LampState lampState;

    public LampIDState(){};

    public LampIDState(UUID lampID, LampState lampState)
    {
        this.lampID = lampID;
        this.lampState = lampState;
    }

    public UUID getLampID() {
        return lampID;
    }

    public void setLampID(UUID lampID) {
        this.lampID = lampID;
    }

    public LampState getLampState() {
        return lampState;
    }

    public void setLampState(LampState lampState) {
        this.lampState = lampState;
    }
}
