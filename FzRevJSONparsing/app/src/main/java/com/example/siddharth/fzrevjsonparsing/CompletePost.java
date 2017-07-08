package com.example.siddharth.fzrevjsonparsing;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

public class CompletePost extends Activity {

    private ImageView featuredImage;
    private TextView postTitle;
    private WebView postContent;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_post);

        featuredImage = (ImageView)findViewById(R.id.featuredImageComplete);
        postTitle = (TextView)findViewById(R.id.textViewTitle);
        postContent = (WebView)findViewById(R.id.postContent);

        String titleResult = getIntent().getStringExtra("title");
        String postResult = getIntent().getStringExtra("description");

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("featuredImage")).into(featuredImage);

        postTitle.setText(Html.fromHtml(titleResult));
        postContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        postContent.loadData(postResult, "text/html", "UTF-8");
        postContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        postContent.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        postContent.getSettings().getDisplayZoomControls();
        postContent.getSettings().getJavaScriptEnabled();
        postContent.getSettings().setLoadWithOverviewMode(true);

    }
}
