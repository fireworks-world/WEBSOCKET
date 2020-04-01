package com.innovativetechlab.websocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    private TextView output;
    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        start();
    }

    private void start() {
        Request request = new Request.Builder().url("wss://stream.binance.com:9443/ws/bnbbtc@kline_1m").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);

        client.dispatcher().executorService().shutdown();
    }
    private void output(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                output.setText(output.getText().toString() + "\n\n" + txt);
            }
        });
    }

    public void connect(View view) {
        start();
    }

    private final class EchoWebSocketListener extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
//            webSocket.send("Hello, it's SSaurel !");
//            webSocket.send("What's up ?");
//            webSocket.send(ByteString.decodeHex("deadbeef"));
//            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d("data",text);
        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.d("bytes",bytes.toString());
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.d("close",reason);
        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.d("Error",t.getMessage());
        }
    }
}

