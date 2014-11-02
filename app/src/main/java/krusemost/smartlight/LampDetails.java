package krusemost.smartlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import krusemost.smartlight.domain.Lamp;
import krusemost.smartlight.domain.LampIDState;
import krusemost.smartlight.domain.LampState;
import krusemost.smartlight.services.SaveLampTask;
import krusemost.smartlight.services.ToggleLampStatesTask;


public class LampDetails extends Activity {

    private Lamp lamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_details);

        Intent callingIntent = getIntent();
        int position = callingIntent.getIntExtra("position", -1);
        this.lamp = Session.getLoadedLamps().get(position);

        TextView lampName = (TextView) findViewById(R.id.lblLampName);
        lampName.setText(this.lamp.getName());

        if (lamp.isHasTimeLock()) {
            TextView tvTimeLock = (TextView) findViewById(R.id.lblTimeLock);
            tvTimeLock.setText(lamp.getFriendlyTime());
        }

        Switch switchLampState = (Switch) findViewById(R.id.switchLampState);
        if (this.lamp.getLampState() == LampState.ON) {
            switchLampState.setChecked(true);
        } else {
            switchLampState.setChecked(false);
        }
        switchLampState.setTag(position);
        switchLampState.setOnCheckedChangeListener(lampStateChangedListener);

        EditText editLampName = (EditText) findViewById(R.id.txtName);
        editLampName.setText(this.lamp.getName());
        editLampName.setSelected(false);

        Switch switchTurnOnAtHome = (Switch) findViewById(R.id.turnOnAtHome);
        switchTurnOnAtHome.setChecked(this.lamp.isTurnOnInRange());

        Switch switchHasTimeLock = (Switch) findViewById(R.id.enableTimeLock);
        switchHasTimeLock.setOnCheckedChangeListener(timeLockChangedListener);
        switchHasTimeLock.setChecked(this.lamp.isHasTimeLock());

        RadioButton startButton = (RadioButton) findViewById(R.id.buttonStart);
        RadioButton endButton = (RadioButton) findViewById(R.id.buttonEnd);

        startButton.setOnCheckedChangeListener(radioButtonChangedListener);
        endButton.setOnCheckedChangeListener(radioButtonChangedListener);

        TimePicker startTimePicker = (TimePicker) findViewById(R.id.startTimePicker);
        int millis = this.lamp.getTurnOnSeconds() * 1000;
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);


        startTimePicker.setIs24HourView(true);
        startTimePicker.setCurrentHour((int) (long) hours);
        startTimePicker.setCurrentMinute((int) (long) minutes);

        TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);
        int endMillis = this.lamp.getTurnOffSeconds() * 1000;
        long endHours = TimeUnit.MILLISECONDS.toHours(endMillis);
        endMillis -= TimeUnit.HOURS.toMillis(endHours);
        long endMinutes = TimeUnit.MILLISECONDS.toMinutes(endMillis);

        endTimePicker.setIs24HourView(true);
        endTimePicker.setCurrentHour((int) (long) endHours);
        endTimePicker.setCurrentMinute((int) (long) endMinutes);

    }

    @Override
    protected void onPause() {
        super.onPause();

        //TODO: update the lamp.
        EditText editLampName = (EditText) findViewById(R.id.txtName);
        this.lamp.setName(editLampName.getText().toString());

        Switch switchTurnOnAtHome = (Switch) findViewById(R.id.turnOnAtHome);
        this.lamp.setTurnOnInRange(switchTurnOnAtHome.isChecked());

        Switch hasTimeLock = (Switch) findViewById(R.id.enableTimeLock);
        this.lamp.setHasTimeLock(hasTimeLock.isChecked());

        if (hasTimeLock.isChecked()) {
            TimePicker startTimeicker = (TimePicker) findViewById(R.id.startTimePicker);
            TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);

            int startTimeSeconds, endTimeSeconds;
            startTimeSeconds = startTimeicker.getCurrentHour() * 3600 + startTimeicker.getCurrentMinute() * 60;
            endTimeSeconds = endTimePicker.getCurrentHour() * 3600 + endTimePicker.getCurrentMinute() * 60;

            this.lamp.setTurnOnSeconds(startTimeSeconds);
            this.lamp.setTurnOffSeconds(endTimeSeconds);
        }

        SaveLampTask saveTask = new SaveLampTask(this.lamp);
        saveTask.execute();
    }

    CompoundButton.OnCheckedChangeListener timeLockChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            LinearLayout timeLockSettings = (LinearLayout) findViewById(R.id.timeLockSettings);
            if (isChecked) {
                timeLockSettings.setVisibility(LinearLayout.VISIBLE);
                RadioButton startButton = (RadioButton) findViewById(R.id.buttonStart);
                startButton.setChecked(true);

            } else {
                timeLockSettings.setVisibility(LinearLayout.GONE);
            }
        }
    };


    RadioButton.OnCheckedChangeListener radioButtonChangedListener = new RadioButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!isChecked) {
                return;
            }

            TimePicker startTimepicker = (TimePicker) findViewById(R.id.startTimePicker);
            TimePicker endTimePicker = (TimePicker) findViewById(R.id.endTimePicker);

            if (buttonView.getId() == R.id.buttonStart) {
                startTimepicker.setVisibility(View.VISIBLE);
                endTimePicker.setVisibility(View.GONE);
            } else {
                startTimepicker.setVisibility(View.GONE);
                endTimePicker.setVisibility(View.VISIBLE);
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener lampStateChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
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
