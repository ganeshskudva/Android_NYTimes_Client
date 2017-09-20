package com.example.gkudva.android_nytimes_client.view.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gkudva.android_nytimes_client.R;
import com.example.gkudva.android_nytimes_client.model.Doc;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gkudva on 19/09/17.
 */

public class NYTAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Doc> mDocList;
    private Context mContext;
    private Callback callback;
    private static final String TAG_LOG = "NYTAdapter";

    public NYTAdapter() {
        this.mDocList = Collections.emptyList();
    }

    public NYTAdapter(Context mContex) {
        this.mContext = mContext;
        this.mDocList = Collections.emptyList();
    }

    public NYTAdapter(List<Doc> docList) {

        this.mDocList = docList;
    }

    public interface Callback{
        void onItemClick(Doc doc);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    enum NewsType {
        ARTICLE,
        ARTICLE_PREVIEW
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        @BindView(R.id.ivPoster)ImageView posterImageView;
        @BindView(R.id.tvName)
        TextView nameTextView;
        @BindView(R.id.tvDesc) TextView descTextView;

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
    public int getItemViewType(int position) {
        Doc article = mDocList.get(position);
        if (article.getTypeOfMaterial().equalsIgnoreCase("article")) {
            return NewsType.ARTICLE_PREVIEW.ordinal();
        } else {
            return NewsType.ARTICLE.ordinal();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (viewType == NewsType.ARTICLE_PREVIEW.ordinal()) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_article_preview, parent, false);
            viewHolder = new ArticlePreviewViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_article, parent, false);
            viewHolder = new ArticleViewHolder(view);
        }

        return viewHolder;
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.headlineTextView)
        TextView headlineTextView;

        @Nullable
        @BindView(R.id.newsDeskTextView)
        TextView newsDeskTextView;

        @Nullable
        @BindView(R.id.snippetTextView)
        TextView snippetTextView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(Article article) {
            mBinding.setArticle(article);
            mBinding.executePendingBindings();
        }
    }

    public static class ArticlePreviewViewHolder extends RecyclerView.ViewHolder {
        private ItemArticlePreviewBinding mBinding;

        public ArticlePreviewViewHolder(View itemView) {
            super(itemView);
            this.mBinding = DataBindingUtil.bind(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(Article article) {
            mBinding.setArticle(article);
            mBinding.executePendingBindings();
        }
    }
}
