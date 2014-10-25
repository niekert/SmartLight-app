package krusemost.smartlight.services;

import android.os.AsyncTask;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import krusemost.smartlight.Constants;
import krusemost.smartlight.Session;
import krusemost.smartlight.domain.Lamp;
import krusemost.smartlight.domain.LampIDState;
import krusemost.smartlight.domain.LampState;
import krusemost.smartlight.utils.LampStateDeserializer;
import krusemost.smartlight.utils.LampStateSerializer;

/**
 * Created by Niek on 10/25/2014.
 * <p/>
 *
 * AsyncTask for toggling certain lamp states.
 */
public class ToggleLampStatesTask extends AsyncTask<Void, Void, List<LampIDState>> {
    private List<CompoundButton> switchesToUpdate;
    private List<LampIDState> lampsToUpdate;
    private View context;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private Exception lastException;

    public ToggleLampStatesTask(View context, List<CompoundButton> switchesToUpdate, List<LampIDState> lampsToUpdate) {
        this.switchesToUpdate = switchesToUpdate;
        this.context = context;
        this.lampsToUpdate = lampsToUpdate;
    }

    @Override
    protected List<LampIDState> doInBackground(Void... params) {
        String requestUrl = Constants.WebserviceBase + "LampService.Svc/ToggleLampStates";
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        List<LampIDState> responseIdStates = new ArrayList<LampIDState>();

        Type jsonType = new TypeToken<ArrayList<LampIDState>>() {
        }.getType();
        ByteArrayOutputStream out = null;
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(LampState.class, new LampStateDeserializer());
            builder.registerTypeAdapter(LampState.class, new LampStateSerializer());

            Gson gson = builder.create();
            String json = gson.toJson(this.lampsToUpdate, jsonType);

            HttpPost post = new HttpPost(requestUrl);
            StringEntity entity = new StringEntity(json, HTTP.UTF_8);
            entity.setContentType("application/json");
            post.setEntity(entity);

            response = httpClient.execute(post);
            StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                String responseJson = out.toString();

                responseIdStates = gson.fromJson(responseJson, jsonType);
            }
            return responseIdStates;
        } catch (Exception ex) {
            this.lastException = ex;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ioEx) {
                logger.log(Level.SEVERE, ioEx.getMessage());
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<LampIDState> lampIDStates) {
        if(this.lastException == null && this.lampsToUpdate.size() == lampIDStates.size()) {
            for (CompoundButton aSwitch : this.switchesToUpdate) {
                int position = (Integer) aSwitch.getTag();

                Lamp lamp = Session.getLoadedLamps().get(position);

                LampIDState foundState = null;
                for (LampIDState lampIDState : lampIDStates) {
                    if (lampIDState.getLampID().equals(lamp.getLampId())) {
                        foundState = lampIDState;
                        break;
                    }
                }

                if (foundState != null) {
                    boolean isChecked = foundState.getLampState() == LampState.ON;
                    aSwitch.setChecked(isChecked);
                }
            }
        } else {
            for(CompoundButton aSwitch : this.switchesToUpdate)
            {
                aSwitch.setChecked(!aSwitch.isChecked());
                if(this.lastException != null) {
                    //TODO: Show the message that an error occured.
                }
            }
        }

    }
}
