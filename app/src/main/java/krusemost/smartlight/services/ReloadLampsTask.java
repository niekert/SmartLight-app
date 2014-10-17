package krusemost.smartlight.services;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import krusemost.smartlight.Constants;
import krusemost.smartlight.domain.*;

/**
 * Created by Niek on 10/18/2014.
 *
 * Async task for reloading lamp data.
 */
public class ReloadLampsTask extends AsyncTask<Void, Void, List<Lamp>> {

    private Exception lastException;
    private Logger logger = Logger.getLogger(ReloadLampsTask.class.getName());

    protected List<Lamp> doInBackground(Void... params) {
        String requestUrl = Constants.WebserviceBase + "/LampService.svc/Lamps";
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response;
        List<Lamp> responseLamps = new ArrayList<Lamp>();
        Type jsonType = new TypeToken<ArrayList<Lamp>>(){}.getType();
        try {
            HttpGet httpGet = new HttpGet(requestUrl);

            response = httpClient.execute(new HttpGet(requestUrl));
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK)
            {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                out.close();
                String responseString = out.toString();

                responseLamps = new Gson().fromJson(responseString, jsonType);
            }
        } catch (IOException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            lastException = ex;
        }

        return responseLamps;
    }

    @Override
    protected void onPostExecute(List<Lamp> lamps) {
        super.onPostExecute(lamps);
    }


}
