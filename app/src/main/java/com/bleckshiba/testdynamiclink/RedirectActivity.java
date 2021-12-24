package com.bleckshiba.testdynamiclink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RedirectActivity extends AppCompatActivity {

    private TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redirect);
        txt = findViewById(R.id.text);
        handleIntent(getIntent());
    }

    private void handleIntent(Intent intent) {
        String linkAction = intent.getAction();
        Uri linkData = intent.getData();
        if (linkAction == null || linkData == null) return;
        Log.d("MAIN", linkAction + " " + linkData);
        String value = linkData.getQueryParameter("code");
        if (value == null) return;
        final String text = "Hello with code " + value;
        txt.setText(text);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            handleIntent(intent);
        }
    }
}