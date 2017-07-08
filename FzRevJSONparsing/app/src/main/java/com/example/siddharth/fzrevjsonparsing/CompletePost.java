package com.example.siddharth.fzrevjsonparsing;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.bumptech.glide.Glide;

public class CompletePost extends Activity {

    private ImageView featuredImage;
    private TextView postTitle;
    private TextView postContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_post);

        featuredImage = (ImageView)findViewById(R.id.featuredImageComplete);
        postTitle = (TextView)findViewById(R.id.textViewTitle);
        postContent = (TextView)findViewById(R.id.postContent);

        String titleResult = getIntent().getStringExtra("title");
        String postResult = getIntent().getStringExtra("description");

        Glide.with(getApplicationContext()).load(getIntent().getStringExtra("featuredImage")).into(featuredImage);

        postTitle.setText(Html.fromHtml(titleResult));
        postContent.setText(Html.fromHtml(postResult));




    }
}
