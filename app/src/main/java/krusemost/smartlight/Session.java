package krusemost.smartlight;

import android.content.SharedPreferences;
import android.location.Location;

import java.util.List;

import krusemost.smartlight.domain.Lamp;

/**
 * Created by Niek on 10/24/2014.
 */
public class Session {
    private static final String prefsFile = "PREFS_FILE";

    private static final String FONTYS_LONGITUDESTRING = "5.481427";
    private static final String FONTYS_LATITUDESTRING = "51.451627";

    private static List<Lamp> loadedLamps;

    public static List<Lamp> getLoadedLamps()
    {
        return loadedLamps;
    }

    public static void setLoadedLamps(List<Lamp> lamps)
    {
        loadedLamps = lamps;
    }

    public static Location getHomeLocation()
    {
        //TODO: Make this a setting in the application
        Location loc = new Location(""); //provider doens't matter.
        loc.setLongitude(Double.parseDouble(FONTYS_LONGITUDESTRING));
        loc.setLatitude(Double.parseDouble(FONTYS_LATITUDESTRING));

        return loc;
    }
}
