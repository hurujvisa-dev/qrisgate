package com.example.qrisauto;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyNotificationListener extends NotificationListenerService {

    String ESP_URL = "http://192.168.1.100/payment"; // GANTI IP ESP

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        String text = sbn.getNotification().extras
                .getCharSequence("android.text", "").toString();

        Log.d("NOTIF", text);

        String angka = text.replaceAll("[^0-9]", "");

        if (angka.length() < 6) return;

        String kode = angka.substring(angka.length() - 4);

        Log.d("KODE", kode);

        kirimKeESP(kode);
    }

    private void kirimKeESP(String kode) {
        try {
            URL url = new URL(ESP_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{\"kode\":\"" + kode + "\"}";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();
            os.close();

            conn.getResponseCode();

        } catch (Exception e) {
            Log.e("ESP", e.toString());
        }
    }
}
