package krusemost.smartlight.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import krusemost.smartlight.R;
import krusemost.smartlight.domain.Lamp;

/**
 * Created by Niek on 10/18/2014.
 *
 * List adapter for the list of lamps.
 */
public class LampListAdapter extends ArrayAdapter<Lamp> {
    private Activity context;
    private List<Lamp> lampList;

    public LampListAdapter(Activity context, List<Lamp> lampList)
    {
        super(context, R.layout.listitem_lamp, lampList);
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


        return returnView;
    }
}
