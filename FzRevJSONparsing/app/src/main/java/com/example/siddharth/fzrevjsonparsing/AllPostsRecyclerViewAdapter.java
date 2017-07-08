package com.example.siddharth.fzrevjsonparsing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class AllPostsRecyclerViewAdapter extends RecyclerView.Adapter<AllPostsRecyclerViewAdapter.ViewHolder> {
    List<PostsModelClass> postsModelClasses;
    private Context context;


    public AllPostsRecyclerViewAdapter(List<PostsModelClass> postsModelClasses, Context context) {
        this.postsModelClasses = postsModelClasses;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_cards,viewGroup,false);
        ViewHolder  viewHolder=  new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final PostsModelClass postsModelClass = postsModelClasses.get(i);
        viewHolder.Title.setText(Html.fromHtml(postsModelClass.getTitle()));
        viewHolder.Excerpt.setText(Html.fromHtml(postsModelClass.getContent()));
        Glide.with(context).load(postsModelClass.getSource_url()).into(viewHolder.FeaturedImage);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), CompletePost.class);
                i.putExtra("title", postsModelClass.getTitle());
                i.putExtra("description", postsModelClass.getContent());
                i.putExtra("featuredImage",postsModelClass.getSource_url());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsModelClasses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView Title;
        public TextView Excerpt;
        public ImageView FeaturedImage;

        public ViewHolder(View itemView) {
            super(itemView);
            Title = (TextView)itemView.findViewById(R.id.HeadingTextView);
            Excerpt = (TextView)itemView.findViewById(R.id.TextExcerpt);
            FeaturedImage = (ImageView)itemView.findViewById(R.id.FeaturedImage);

        }
    }
}
