package com.adingx.chao.rangerplayer;

import android.net.wifi.WifiManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fi.iki.elonen.NanoHTTPD;

public class ServerActivity extends AppCompatActivity {
    private static final String TAG = ServerActivity.class.getSimpleName();


    public class HttpApp extends NanoHTTPD {
        private String mCurrentDir = Environment.getExternalStorageDirectory().toString() + File.separator + Environment.DIRECTORY_DOWNLOADS;

        public HttpApp() throws IOException {
            super(8080);
            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
            Log.i(TAG, "Running! Point your browsers to http://localhost:8080/ ");
        }

        @Override
        public Response serve(IHTTPSession session) {
            Map<String, String> headers = session.getHeaders();
            Map<String, String> parms = session.getParms();
            Method method = session.getMethod();
            String uri = session.getUri();
            Map<String, String> files = new HashMap<>();
            String msg = "<html><body>failed</body></html>";

            Set keys = headers.keySet();
            for (Object key : keys) {
                Log.d(TAG, "HttpApp - serve - headers - " + key + ": " + headers.get(key));
            }
            keys = parms.keySet();
            for (Object key : keys) {
                Log.d(TAG, "HttpApp - serve - parms - " + key + ": " + parms.get(key));
            }

            if (Method.POST.equals(method) || Method.PUT.equals(method)) {
                Log.i(TAG, "HttpApp - serve - Post||Put");
                try {
                    session.parseBody(files);
                    keys = files.keySet();
                    for (Object key : keys) {
                        Log.d(TAG, "HttpApp - serve - parseBody - key: " + key + ", value: " + files.get(key));
                    }
                } catch (IOException ioe) {
                    //return getResponse("Internal Error IO Exception: " + ioe.getMessage());
                    Log.e(TAG, "HttpApp - serve - Internal Error IO Exception: " + ioe.getMessage());
                    return newFixedLengthResponse(msg);
                } catch (ResponseException re) {
                    //return new Response(re.getStatus(), MIME_PLAINTEXT, re.getMessage());
                    Log.e(TAG, "HttpApp - serve - ResponseExc: " + re.getMessage());
                    return newFixedLengthResponse(msg);
                }
            }

            Log.d(TAG, "HttpApp - serve - uri: " + uri);

            if ("/uploadFile".equalsIgnoreCase(uri)) {
                Log.i(TAG, "HttpApp - serve - /uploadfile");

                String filename = parms.get("filename");
                String tmpFilePath = files.get("filename");
                Log.i(TAG, "HttpApp - serve - fn: " + filename + ", tmp: " + tmpFilePath);
                if (null == filename || null == tmpFilePath) {
                    // Response for invalid parameters
                    Log.e(TAG, "HttpApp - serve - Invalid parameters.");
                    return newFixedLengthResponse(msg);
                }
                File dst = new File(mCurrentDir, filename);
                if (dst.exists()) {
                    // Response for confirm to overwrite
                    Log.e(TAG, "HttpApp - serve - dst.existes .");
                }
                File src = new File(tmpFilePath);
                try {
                    InputStream in = new FileInputStream(src);
                    OutputStream out = new FileOutputStream(dst);
                    byte[] buf = new byte[65536];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();
                } catch (IOException ioe) {
                    // Response for failed
                    Log.e(TAG, "HttpApp - serve - ioe: " + ioe.getLocalizedMessage());
                    return newFixedLengthResponse(msg);
                }
                // Response for success
                Log.d(TAG, "HttpApp - serve - upload success.");
                msg = "<html><body>sucess</body></html>";
                return newFixedLengthResponse(msg);
            }

            msg =   "<form name='a' enctype='multipart/form-data' method='post' action='/uploadFile'>" +
                    "     <input type='file' name='filename' multiple/>" +
                    "     <input type='hidden' name='extradata' value='test'/>" +
                    "     <input type='submit' value='uploadfile' >" +
                    "</form>";
            return newFixedLengthResponse(msg);
        }

        /*
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
        }*/

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


    private HttpApp server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        Log.i(TAG, "onCreate ~~");
        TextView textView = (TextView) findViewById(R.id.text_message);

        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        textView.setText("http://" + ip + ":8080");

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
