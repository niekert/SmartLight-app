package krusemost.smartlight;

import android.app.Activity;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import krusemost.smartlight.adapter.LampListAdapter;
import krusemost.smartlight.domain.Lamp;
import krusemost.smartlight.listener.RangeLocationListener;
import krusemost.smartlight.services.ReloadLampsTask;


public class HomeActivity extends Activity {

    private LocationListener locationListener;
    private LocationManager lm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    protected void onStart() {
        super.onStart();

        this.locationListener = new RangeLocationListener();
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 200, 0, this.locationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ReloadLampsTask reloadLampsTask = new ReloadLampsTask(this);
        reloadLampsTask.execute();

        ListView lv =  (ListView) findViewById(R.id.lvLamps);
        lv.setOnItemClickListener(itemClickListener);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        lm.removeUpdates(this.locationListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            LampListAdapter listAdapter = (LampListAdapter) parent.getAdapter();

            Intent intent = new Intent(HomeActivity.this, LampDetails.class);
            intent.putExtra("position", position);

            startActivity(intent);
        }
    };
}
