package krusemost.smartlight.domain;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

import krusemost.smartlight.utils.Utilities;

/**
 * Created by Niek on 10/18/2014.
 *
 * Lamp DTO mapped as received by the SVC.
 */
public class Lamp {
    @SerializedName("LampState")
    private LampState lampState;
    @SerializedName("Name")
    private String name;
    @SerializedName("Id")
    private UUID lampId;
    @SerializedName("HasTimeLock")
    private boolean hasTimeLock;
    @SerializedName("TurnOnSeconds")
    private int turnOnSeconds;
    @SerializedName("TurnOffSeconds")
    private int turnOffSeconds;
    @SerializedName("TurnOnInRange")
    private boolean turnOnInRange;

    public LampState getLampState() {
        return lampState;
    }

    public void setLampState(LampState lampState) {
        this.lampState = lampState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getLampId() {
        return lampId;
    }

    public void setLampId(UUID lampId) {
        this.lampId = lampId;
    }

    public boolean isHasTimeLock() {
        return hasTimeLock;
    }

    public void setHasTimeLock(boolean hasTimeLock) {
        this.hasTimeLock = hasTimeLock;
    }

    public int getTurnOnSeconds() {
        return turnOnSeconds;
    }

    public void setTurnOnSeconds(int turnOnSeconds) {
        this.turnOnSeconds = turnOnSeconds;
    }

    public int getTurnOffSeconds() {
        return turnOffSeconds;
    }

    public void setTurnOffSeconds(int turnOffSeconds) {
        this.turnOffSeconds = turnOffSeconds;
    }

    public boolean isTurnOnInRange() {
        return turnOnInRange;
    }

    public void setTurnOnInRange(boolean turnOnInRange) {
        this.turnOnInRange = turnOnInRange;
    }


    public String getFriendlyTime()
    {
        String result = "";
        result += Utilities.formatHHMM(this.getTurnOnSeconds());
        result += " - ";
        result += Utilities.formatHHMM(this.getTurnOffSeconds());

        return result;
    }
}
