package krusemost.smartlight.domain;

/**
 * Created by Niek on 10/25/2014.
 *
 * Enum for LampState, could be used as boolean but can be expanded this way.
 */
public enum LampState {
    OFF(0), ON(1);

    private final int key;

    LampState(int key)
    {
        this.key= key;
    }

    public int getKey()
    {
        return this.key;
    }

    public static LampState fromKey(int key)
    {
        for(LampState state : LampState.values())
        {
            if(state.getKey() == key)
            {
                return state;
            }
        }
        return null;
    }
}
