package com.bleckshiba.testdynamiclink;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

public class MainActivity extends AppCompatActivity {

    private static final Uri link = Uri.parse("https://www.bleckshiba.io/Hello");
    private static final String prefix = "https://bleckshiba.page.link";
    private static final String iosBundle = "com.example.ios";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAnalytics.getInstance(this);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());

    }

    private void handleIntent(Intent intent) {
        String linkAction = intent.getAction();
        Uri linkData = intent.getData();
        if (linkAction == null || linkData == null) return;
        Log.d("MAIN", linkAction + " " + linkData);
    }

    public void onGenerateLongLink(View view) {
        Log.d("MAIN", "onGenerateLongClicked");
        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(link)
                .setDomainUriPrefix(prefix)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder(iosBundle).build())
                .buildDynamicLink();
        Log.d("MAIN", "check this dynamic link: " + dynamicLink.getUri().toString());
        startActivity(new Intent(Intent.ACTION_VIEW).setData(dynamicLink.getUri()));
    }

    public void onGenerateShortLink(View view) {
        Log.d("MAIN", "onGenerateShortClicked");
        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(link)
                .setDomainUriPrefix(prefix)
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                .setIosParameters(new DynamicLink.IosParameters.Builder(iosBundle).build())
                .buildShortDynamicLink()
                .addOnSuccessListener(result -> {
                    if (result != null && result.getShortLink() != null && result.getPreviewLink() != null) {
                        String shortLink = result.getShortLink().toString();
                        String previewLink = result.getPreviewLink().toString();
                        Log.d("MAIN", "\n\nShort link: " + shortLink + "\nPreview link: " + previewLink);
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(shortLink)));
                    }
                })
                .addOnFailureListener(MainActivity.this, e -> {
                    Log.e("MAIN", e.getLocalizedMessage());
                });
    }

    public void onHelloPressed(View view) {
        Log.d("MAIN", "onHelloPressed");
        startActivity(new Intent(Intent.ACTION_VIEW).setData(link));;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            handleIntent(intent);
        }
    }
}