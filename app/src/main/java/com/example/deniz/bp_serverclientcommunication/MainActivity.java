package com.example.deniz.bp_serverclientcommunication;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    // variables for taken the income of the frontend
    EditText et;
    Button bt;
    TextView tv;

    // final string because of using server-Address always the same.
    final String scriptUrlString = "http://bp-server.890m.com/receive_script.php";

    /**
     * Method is always created automatically by Developing environment, setting up an interactive Gui connected to backend
     *
     * Function of method on Create(Bundle): - connecting attributes to buttons on our surface
     *                                       - add ONE ButtonClickListener for sending message to Server -> calling method sendToServer
     * [Checks always first for an Internet connection before sending to Server]
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.editText);
        bt = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView2);
        bt.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View v){
                    if(internetAvailable()){
                        sendToServer(et.getText().toString());
                    }else {
                        Toast.makeText(getApplicationContext(), "Keine Internetverbindung verfügbar!", Toast.LENGTH_SHORT).show();
                    }
             }
        });
    }

    /**
     *
     * Method is Created to Send an given parameter as String to the Server in specific Encoding (UTF-8)
     *
     * @param text : The text comes from method onCreate(Bundle) -- Text = text which is typed in on surface
     */
    public void sendToServer(final String text){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String textParam = "text1=" + URLEncoder.encode(text,"UTF-8");

                    URL scriptUrl = new URL(scriptUrlString);
                    HttpURLConnection connection = (HttpURLConnection) scriptUrl.openConnection();
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    connection.setFixedLengthStreamingMode(textParam.getBytes().length);

                    OutputStreamWriter contentWriter = new OutputStreamWriter(connection.getOutputStream());
                    contentWriter.write(textParam);
                    contentWriter.flush();
                    contentWriter.close();

                    InputStream answerInputStream = connection.getInputStream();
                    final String answer = getTextFromInputStream(answerInputStream);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(answer);
                        }
                    });

                    answerInputStream.close();
                    connection.disconnect();


                } catch (MalformedURLException e){
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }).start();





    }



    public String getTextFromInputStream(InputStream is){
        BufferedReader reader  = new BufferedReader(new InputStreamReader(is));
        StringBuilder stringBuilder = new StringBuilder();

        String aktuelleZeile;
        try {
            while ((aktuelleZeile = reader.readLine()) != null) {
                    stringBuilder.append(aktuelleZeile);
                    stringBuilder.append("\n");


            }
        }  catch(IOException e){
                e.printStackTrace();

            }

            return stringBuilder.toString().trim();
    }

    public boolean internetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
