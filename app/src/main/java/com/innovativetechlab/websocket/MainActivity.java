package com.innovativetechlab.websocket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MainActivity extends AppCompatActivity {
    private TextView output ,btcprice,ethprice;
    private OkHttpClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        client = new OkHttpClient();
        btcprice = findViewById(R.id.btcPrice);
        ethprice = findViewById(R.id.ethPrice);
        start();
    }
    //"wss://stream.binance.com:9443/ws/bnbbtc@kline_1m"
    //wss://stream.binance.com:9443/ws/ltcbtc@depth/xrpbtc@depth/btgbtc@depth
    private void start() {
        Request request = new Request.Builder().url("wss://stream.binance.com:9443/ws/btcusdt@miniTicker/ethusdt@miniTicker").build();
        EchoWebSocketListener listener = new EchoWebSocketListener();
        WebSocket ws = client.newWebSocket(request, listener);
        output=findViewById(R.id.tradedata);
        btcprice=findViewById(R.id.btcPrice);
        ethprice=findViewById(R.id.ethPrice);
        client.dispatcher().executorService().shutdown();
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
//            String json = text;
            Log.d("data",text);
            try {
//                JSONObject obj = new JSONObject(json);
//                Log.d("data",obj.getString("s"));
//                String symbol=obj.getString("s");
               output(new JSONObject(text));
//
            } catch (Throwable t) {
                Log.e("My App", t.getMessage());
            }
//            output("Receiving : " + text);

        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            Log.d("bytes",bytes.toString());
//            output("Receiving bytes : " + bytes.hex());
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
        private void output(final JSONObject obj) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        switch(obj.getString("s")) {
                            case "BTCUSDT":
                                btcprice.setText("PRICE : "+obj.getString("c"));
                                break;
                            case "ETHUSDT":
                                ethprice.setText("PRICE : "+obj.getString("c"));
                                break;
                            default:

                                break;
                        }


                    } catch (Exception e) {
                        Log.d("TAG", "run: "+e.getMessage());
                    }

                    //output.setText(txt);

                }
            });
        }
    }
}

