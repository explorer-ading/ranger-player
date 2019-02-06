package com.adingx.chao.rangerplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class ServerActivity extends AppCompatActivity {
    public class HttpApp extends NanoHTTPD {
        public HttpApp() throws IOException {
            super(8080);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.i(TAG, "Running! Point your browsers to http://localhost:8080/ ");
        }
        @Override
        public Response serve(IHTTPSession session) {
            String msg = "<html><body><h1>Hello server</h1>\n";
            Map<String, String> parms = session.getParms();
            if (parms.get("username") == null) {
                msg += "<form action='?' method='get'>\n  <p>Your name: <input type='text' name='username'></p>\n" + "</form>\n";
            } else {
                msg += "<p>Hello, " + parms.get("username") + "!</p>";
            }
            return newFixedLengthResponse(msg + "</body></html>\n");
        }
        /*
        @Override
        public Response serve(String uri, Method method,
                              Map<String, String> header,
                              Map<String, String> parameters,
                              Map<String, String> files) {
            String answer = "";
            try {
                // Open file from SD Card
                File root = Environment.getExternalStorageDirectory();
                FileReader index = new FileReader(root.getAbsolutePath() +
                        "/www/index.html");
                BufferedReader reader = new BufferedReader(index);
                String line = "";
                while ((line = reader.readLine()) != null) {
                    answer += line;
                }
                reader.close();
            } catch(IOException ioe) {
                Log.w("Httpd", ioe.toString());
            }

            return new NanoHTTPD.Response(answer);
        }
        */
    }

    private static final String TAG = MainActivity.class.getSimpleName();
    private HttpApp server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Log.i(TAG, "onCreate ~~");
        TextView textView = (TextView) findViewById(R.id.text_message);
        textView.setText("http://127.0.0.1:8080");

        try{
            server = new HttpApp();
        }catch (IOException ioe) {
            Log.e(TAG, "Couldn't start server:" + ioe);
        }
    }

    // DON'T FORGET to stop the server
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (server != null)
            server.stop();
    }
}
