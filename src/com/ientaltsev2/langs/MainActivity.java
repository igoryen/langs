package com.ientaltsev2.langs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

  //=================================================================
  // readJSONFeed()
  // 10. connect to the specified URL 
  // 20. read the web server's response 
  //=================================================================
  public String readJSONFeed(String URL) {

    StringBuilder stringBuilder = new StringBuilder();
    HttpClient client = new DefaultHttpClient();
    HttpGet httpGet = new HttpGet(URL);

    //===================================================
    Log.d("a2/readJSONFeed()", "passed the assignments");
    //===================================================

    //==========================================================
    // try to establish a connection to the URL
    //==========================================================
    try {
      HttpResponse response = client.execute(httpGet);
      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();

      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      Log.d("a2/readJSONFeed()/try{}", "passed the assignments");
      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      Log.d("a2/readJSONFeed()/try{}", "statusCode: " + statusCode);
      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

      //============================================================
      // if connection to the URL is a success
      // read the response from the web server
      //============================================================
      if (statusCode == 200) {

        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
        String line;



        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        int lineCount = 0;
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // see the contents of the line
        //Log.d("a2/readJSONFeed()/try{}", "reader.readLine()): " + reader.readLine());
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


        //==================================
        // append one line 
        //==================================
        while ((line = reader.readLine()) != null) {
          lineCount +=1;
          stringBuilder.append(line);
        }
         //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Log.d("a2/readJSONFeed()/try{}/while()", "lineCount: " + lineCount);
        Log.d("a2/readJSONFeed()/try{}", "line: " + line);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //================================================

        //================================================

      } 
      //============================================================
      // if connection to the URL is a failure
      //============================================================
      else {
        Log.e("JSON", "Failed to download file");
      }

    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    //=========================================================
    // return a string as a result
    //=========================================================
    return stringBuilder.toString();
  }
    


  //======================================================================
  // ReadJSONFeedTask : AsyncTask
  // to call ReadJSONFeed() asynchronously
  //======================================================================
  private class ReadJSONFeedTask extends AsyncTask<String, Void, String> {



    //=================================================================
    // doInBackGround()
    // call readJSONFeed()
    //=================================================================
    protected String doInBackground(String... urls) {

      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      Log.d("a2/ReadJSONFeedTask()/doInBackground()", "before return readJSONFeed(urls[0])");
      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

      return readJSONFeed(urls[0]);
    }
    
    //=================================================================
    // onPostExecute()
    // take JSON string you have fetched and 
    // pass it through this method
    //=================================================================
    protected void onPostExecute(String result) {

      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      Log.d("a2/ReadJSONFeedTask()/onPostExecute()", "before try{}");
      //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

      try {

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Log.d("a2/ReadJSONFeedTask()/onPostExecute()/try{}", "entered");
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        // show long line
         Log.d("a2/ReadJSONFeedTask()/onPostExecute()/try{}", "result: " + result);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //=================================================================
        // 10. pass it the JSON feed (result)
        //  and create a JSONArray 
        //=================================================================
        JSONArray jsonArray = new JSONArray(result); // 10

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        Log.d("a2/ReadJSONFeedTask()/onPostExecute()/try{}", "jsonArray.length(): " + jsonArray.length());
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //==============================================================
        // print out the content of the json feed
        // 10. for the number of objects in the JSONArray
        // 20. obtain each object from the array
        // 30. obtain the value of the key/value pair stored inside the JSON object
        //  "text" is the key, "created_at" is the key
        //==============================================================
        for (int i = 0; i < jsonArray.length(); i++) { // 10
          JSONObject jsonObject = jsonArray.getJSONObject(i); // 20

          /*
          Toast.makeText(getBaseContext(), jsonObject.getString("appeId") + 
              " - " + jsonObject.getString("inputTime"), 
              Toast.LENGTH_SHORT).show();                
          */
        
          Toast.makeText(getBaseContext(), jsonObject.getString("text") + 
            " - " + jsonObject.getString("created_at"), 
            Toast.LENGTH_SHORT).show(); // 30

        } // for
      } catch (Exception e) {
      e.printStackTrace();
      } // try       
    } // onPostExecute()
  } // ReadJSONFeedTask

  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    //=======================================================
    // make sure the app is alive, i.e. works fine
    //=======================================================
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Log.d("a2/onCreate()", "before ReadJSONFeedTask.execute");
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //==================================================================================
    // the source of the JSON string
    // access the JSON feed asynchronously
    //==================================================================================
		new ReadJSONFeedTask().execute("http://data.gc.ca/data/api/action/package_show?id=7f620ce0-4d40-41c6-8e66-f0771619d256");

    //new ReadJSONFeedTask().execute("http://extjs.org.cn/extjs/examples/grid/survey.html");
    
    //new ReadJSONFeedTask().execute("https://twitter.com/statuses/user_timeline/weimenglee.json");

    //


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    Log.d("a2/onCreate()", "after ReadJSONFeedTask.execute");
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

  }


}
