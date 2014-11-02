package krusemost.smartlight.services;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.util.logging.Level;
import java.util.logging.Logger;

import krusemost.smartlight.Constants;
import krusemost.smartlight.domain.Lamp;
import krusemost.smartlight.domain.LampState;
import krusemost.smartlight.utils.LampStateSerializer;

/**
 * Created by Niek on 11/2/14.
 *
 * Async task that sends the updated lamp to the webserver.
 */
public class SaveLampTask extends AsyncTask<Void, Void, Void> {

    private Lamp lampToUpdate;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    public SaveLampTask(Lamp lampToUpdate)
    {
        this.lampToUpdate = lampToUpdate;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String requestUrl  = Constants.WebserviceBase + "LampService.svc/UpdateLamp";
        HttpClient httpClient = new DefaultHttpClient();

        try{
            GsonBuilder builder =new GsonBuilder();
            builder.registerTypeAdapter(LampState.class, new LampStateSerializer());

            Gson gson = builder.create();
            String json = gson.toJson(this.lampToUpdate, Lamp.class);

            HttpPost post = new HttpPost(requestUrl);
            StringEntity entity = new StringEntity(json, HTTP.UTF_8);
            entity.setContentType("application/json");
            post.setEntity(entity);

            httpClient.execute(post);
        } catch(Exception ex){
            this.logger.log(Level.SEVERE, ex.getMessage());
        }

        return null;
    }
}

