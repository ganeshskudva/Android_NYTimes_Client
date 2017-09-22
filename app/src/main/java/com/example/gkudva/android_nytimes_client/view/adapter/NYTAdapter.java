package com.example.gkudva.android_nytimes_client.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gkudva.android_nytimes_client.R;
import com.example.gkudva.android_nytimes_client.model.Doc;
import com.example.gkudva.android_nytimes_client.model.Multimedium;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gkudva on 19/09/17.
 */

public class NYTAdapter extends RecyclerView.Adapter<NYTAdapter.ViewHolder>{

    public static final String MULTIMEDIA_WIDE = "wide";
    public static final int THUMBNAIL_WIDE_TARGET_WIDTH = 190;
    public static final int THUMBNAIL_WIDE_TARGET_HEIGHT = 126;
    public static final String MULTIMEDIA_XLARGE = "xlarge";
    private CallbackListener callback;
    private static final String TAG_LOG = "NYTAdapter";
    private List<Doc> mArticles;
    private Context mContext;
    private String newDesk;
    private OnItemClickListener mListener;

    public void setArticles(List<Doc> articleList, String newDesk)
    {
        this.newDesk = newDesk;
        this.mArticles = articleList;
    }
    public NYTAdapter(Context context) {
        this.mArticles = Collections.emptyList();
        this.mContext = context;
    }

    public interface CallbackListener{
        void onItemClick(Doc article);
    }

    public void setCallback(CallbackListener callback) {
        this.callback = callback;
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @BindView(R.id.ivArticleImage)ImageView ivArticleImage;
        @BindView(R.id.tvArticleHeadline)
        TextView tvArticleHeadline;
        @BindView(R.id.tvCategory)
        TextView tvCategory;
        @BindView(R.id.tvArticleHeadlinePrint)
        TextView tvArticleHeadlinePrint;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public NYTAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.list_item_article, parent, false);

        ViewHolder articleViewHolder = new ViewHolder(itemView);
        return articleViewHolder;
    }

    @Override
    public void onBindViewHolder(NYTAdapter.ViewHolder holder, int position) {
        final Doc article = mArticles.get(position);
        final List<Multimedium> multimediaList = article.getMultimedia();
        Multimedium thumbnailWide = null;
        Multimedium xLarge = null;
        for(Multimedium multimedia: multimediaList) {
            if(multimedia.getSubtype().equals(MULTIMEDIA_WIDE)){
                thumbnailWide = multimedia;
            }else if (multimedia.getSubtype().equals(MULTIMEDIA_XLARGE)) {
                xLarge = multimedia;
            }
        }

        if (newDesk != null && !newDesk.isEmpty()) {
            holder.tvCategory.setText(newDesk.replaceAll("^\"|\"$", "").toUpperCase());
            article.setNewDesk(newDesk.replaceAll("^\"|\"$", ""));
            if (newDesk.contains("Sports")) {
                holder.tvCategory.setBackgroundColor(Color.MAGENTA);
            } else if (newDesk.contains("Fashion")) {
                holder.tvCategory.setBackgroundColor(Color.GREEN);
            } else if (newDesk.contains("Art")) {
                holder.tvCategory.setBackgroundColor(Color.RED);
            }
        }
        holder.tvArticleHeadline.setText(article.getHeadline().getMain());
        holder.tvArticleHeadlinePrint.setText(article.getHeadline().getPrintHeadline());
        if(thumbnailWide != null || xLarge != null) {
            String imageUrl;
            int targetWidth;
            int targetHeight;
            if(thumbnailWide != null) {
                imageUrl = thumbnailWide.getUrl();
                targetHeight = thumbnailWide.getHeight();
                targetWidth = thumbnailWide.getWidth();
            }else {
                imageUrl = xLarge.getUrl();
                targetHeight = THUMBNAIL_WIDE_TARGET_HEIGHT;
                targetWidth = THUMBNAIL_WIDE_TARGET_WIDTH;

            }
            String imageFullUrl = String.format("http://www.nytimes.com/%s", imageUrl);
            Picasso.with(mContext).load(imageFullUrl)
                    .placeholder(R.drawable.placeholder)
                    .resize(targetWidth,
                            targetHeight)
                    .into(holder.ivArticleImage);
        } else{
            Picasso.with(mContext).load(R.drawable.placeholder)
                    .resize(THUMBNAIL_WIDE_TARGET_WIDTH,
                            THUMBNAIL_WIDE_TARGET_HEIGHT)
                    .into(holder.ivArticleImage);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null)
                {
                    callback.onItemClick(article);
                }
            }

        });

    }

    public void addAll(List<Doc> articles){
        mArticles.addAll(articles);
        notifyDataSetChanged();
    }

    public void clear() {
        mArticles.clear();
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Doc article);
    }
}
