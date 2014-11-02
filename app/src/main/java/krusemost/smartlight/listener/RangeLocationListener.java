package krusemost.smartlight.listener;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import krusemost.smartlight.Session;
import krusemost.smartlight.domain.Lamp;
import krusemost.smartlight.domain.LampIDState;
import krusemost.smartlight.domain.LampState;
import krusemost.smartlight.services.ToggleLampStatesTask;

/**
 * Created by Niek on 10/31/14.
 *
 * Location listener that checks if the user is in range of his house.
 */
public class RangeLocationListener implements LocationListener {

    private Location lastKnownLocation = null;

    @Override
    public void onLocationChanged(Location location) {
        Location homeLocation = Session.getHomeLocation();
        //Check if getting close to house
        if(this.lastKnownLocation != null && this.lastKnownLocation.distanceTo(homeLocation) < 100)
        {
            //The user was already at home
            return;
        } else if(lastKnownLocation != null && this.lastKnownLocation.distanceTo(homeLocation) > 100 && location.distanceTo(homeLocation) < 100)
        {
            //User returned inside the range.
            List<Lamp> allLamps = Session.getLoadedLamps();
            List<LampIDState> lampsToToggle = new ArrayList<LampIDState>();
            for (Lamp lamp :allLamps) {
                if (lamp.isTurnOnInRange() && lamp.getLampState() == LampState.OFF) {
                    lampsToToggle.add(new LampIDState(lamp.getLampId(), LampState.ON));
                }
            }
            ToggleLampStatesTask toggleTask = new ToggleLampStatesTask(null, lampsToToggle);
            toggleTask.execute();
        }


        this.lastKnownLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
