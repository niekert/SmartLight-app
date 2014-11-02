package krusemost.smartlight.adapter;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import krusemost.smartlight.R;
import krusemost.smartlight.Session;
import krusemost.smartlight.domain.Lamp;
import krusemost.smartlight.domain.LampIDState;
import krusemost.smartlight.domain.LampState;
import krusemost.smartlight.services.ToggleLampStatesTask;
import krusemost.smartlight.utils.Utilities;

/**
 * Created by Niek on 10/18/2014.
 *
 * List adapter for the list of lamps.
 */
public class LampListAdapter extends ArrayAdapter<Lamp> {
    private Activity context;
    private List<Lamp> lampList;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public LampListAdapter(Activity context, List<Lamp> lampList)
    {
        super(context, R.layout.listitem_lamp, lampList);
        this.context = context;
        this.lampList = lampList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View returnView = convertView;

        if(returnView == null)
        {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            returnView = inflater.inflate(R.layout.listitem_lamp, null);
        }

        Lamp lamp = this.lampList.get(position);
        TextView lampName = (TextView) returnView.findViewById(R.id.lblLampName);
        TextView lampTimeLock = (TextView) returnView.findViewById(R.id.lblTimeLock);
        Switch switchLampState = (Switch) returnView.findViewById(R.id.switchLampState);
        switchLampState.setTag(position);
        switchLampState.setOnCheckedChangeListener(switchChangedListener);

        if(lamp.getLampState() == LampState.OFF)
        {
            switchLampState.setChecked(false);
        } else {
            switchLampState.setChecked(true);
        }


        lampName.setText(lamp.getName());
        lampTimeLock.setText(lamp.isHasTimeLock() ? lamp.getFriendlyTime() : "");


        return returnView;
    }

    public Lamp getItem(int position)
    {
        return lampList.get(position);
    }

    private CompoundButton.OnCheckedChangeListener switchChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                int position = (Integer) buttonView.getTag();

                Lamp lamp = Session.getLoadedLamps().get(position);
                final List<LampIDState> lampsToUpdate = new ArrayList<LampIDState>();
                final List<CompoundButton> switchesToUpdate = new ArrayList<CompoundButton>();
                if (lamp != null) {
                    LampState newState;
                    if (isChecked) {
                        newState = LampState.ON;
                    } else {
                        newState = LampState.OFF;
                    }

                    lampsToUpdate.add(new LampIDState(lamp.getLampId(), newState));
                    switchesToUpdate.add(buttonView);

                    ToggleLampStatesTask toggleTask = new ToggleLampStatesTask(switchesToUpdate, lampsToUpdate);
                    toggleTask.execute();
                }
            }
        }
    };
}
