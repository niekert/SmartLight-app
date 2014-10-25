package krusemost.smartlight;

import java.util.List;

import krusemost.smartlight.domain.Lamp;

/**
 * Created by Niek on 10/24/2014.
 */
public class Session {
    private static List<Lamp> loadedLamps;

    public static List<Lamp> getLoadedLamps()
    {
        return loadedLamps;
    }

    public static void setLoadedLamps(List<Lamp> lamps)
    {
        loadedLamps = lamps;
    }
}
