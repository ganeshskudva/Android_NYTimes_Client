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

/**
 * Created by gkudva on 19/09/17.
 */

public class NYTAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

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

    enum RowType {
        REGULAR,
        PREVIEW
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public class ViewHolderRegular extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        ImageView ivArticleImage;
        TextView tvArticleHeadline;
        TextView tvCategory;
        TextView tvArticleHeadlinePrint;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolderRegular(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            ivArticleImage = (ImageView)itemView.findViewById(R.id.ivArticleImage);
            tvArticleHeadline = (TextView) itemView.findViewById(R.id.tvArticleHeadline);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvArticleHeadlinePrint = (TextView) itemView.findViewById(R.id.tvArticleHeadlinePrint);

        }

        public ImageView getIvArticleImage() {
            return ivArticleImage;
        }

        public void setIvArticleImage(ImageView ivArticleImage) {
            this.ivArticleImage = ivArticleImage;
        }

        public TextView getTvArticleHeadline() {
            return tvArticleHeadline;
        }

        public void setTvArticleHeadline(TextView tvArticleHeadline) {
            this.tvArticleHeadline = tvArticleHeadline;
        }

        public TextView getTvCategory() {
            return tvCategory;
        }

        public void setTvCategory(TextView tvCategory) {
            this.tvCategory = tvCategory;
        }

        public TextView getTvArticleHeadlinePrint() {
            return tvArticleHeadlinePrint;
        }

        public void setTvArticleHeadlinePrint(TextView tvArticleHeadlinePrint) {
            this.tvArticleHeadlinePrint = tvArticleHeadlinePrint;
        }

        public void configureRegularViewHolder(Doc article)
        {
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
                tvCategory.setText(newDesk.replaceAll("^\"|\"$", "").toUpperCase());
                article.setNewDesk(newDesk.replaceAll("^\"|\"$", ""));
                if (newDesk.contains("Sports")) {
                    tvCategory.setBackgroundColor(Color.MAGENTA);
                } else if (newDesk.contains("Fashion")) {
                    tvCategory.setBackgroundColor(Color.GREEN);
                } else if (newDesk.contains("Art")) {
                    tvCategory.setBackgroundColor(Color.RED);
                }
            }
            else {
                tvCategory.setBackgroundColor(Color.WHITE);
            }
            tvArticleHeadline.setText(article.getHeadline().getMain());
            tvArticleHeadlinePrint.setText(article.getHeadline().getPrintHeadline());
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
                        .into(ivArticleImage);
            } else{
                Picasso.with(mContext).load(R.drawable.placeholder)
                        .resize(THUMBNAIL_WIDE_TARGET_WIDTH,
                                THUMBNAIL_WIDE_TARGET_HEIGHT)
                        .into(ivArticleImage);
            }
        }
    }

    public class ViewHolderPreview extends RecyclerView.ViewHolder {

        TextView tvArticleHeadlinePreview;
        TextView tvCategoryPreview;
        TextView tvArticleHeadlinePrintPreview;


        public ViewHolderPreview(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            tvArticleHeadlinePreview = (TextView) itemView.findViewById(R.id.tvArticleHeadlinePreview);
            tvCategoryPreview = (TextView) itemView.findViewById(R.id.tvCategoryPreview);
            tvArticleHeadlinePrintPreview = (TextView) itemView.findViewById(R.id.tvArticleHeadlinePrintPreview);
        }

        public TextView getTvArticleHeadlinePreview() {
            return tvArticleHeadlinePreview;
        }

        public void setTvArticleHeadlinePreview(TextView tvArticleHeadlinePreview) {
            this.tvArticleHeadlinePreview = tvArticleHeadlinePreview;
        }

        public TextView getTvCategoryPreview() {
            return tvCategoryPreview;
        }

        public void setTvCategoryPreview(TextView tvCategoryPreview) {
            this.tvCategoryPreview = tvCategoryPreview;
        }

        public TextView getTvArticleHeadlinePrintPreview() {
            return tvArticleHeadlinePrintPreview;
        }

        public void setTvArticleHeadlinePrintPreview(TextView tvArticleHeadlinePrintPreview) {
            this.tvArticleHeadlinePrintPreview = tvArticleHeadlinePrintPreview;
        }

        public void configurePreviewViewHolder(Doc article)
        {
            if (newDesk != null && !newDesk.isEmpty()) {
                tvCategoryPreview.setText(newDesk.replaceAll("^\"|\"$", "").toUpperCase());
                article.setNewDesk(newDesk.replaceAll("^\"|\"$", ""));
                if (newDesk.contains("Sports")) {
                    tvCategoryPreview.setBackgroundColor(Color.MAGENTA);
                } else if (newDesk.contains("Fashion")) {
                    tvCategoryPreview.setBackgroundColor(Color.GREEN);
                } else if (newDesk.contains("Art")) {
                    tvCategoryPreview.setBackgroundColor(Color.RED);
                }
            }
            else {
                tvCategoryPreview.setBackgroundColor(Color.WHITE);
            }
            tvArticleHeadlinePreview.setText(article.getHeadline().getMain());
            tvArticleHeadlinePrintPreview.setText(article.getHeadline().getPrintHeadline());
        }
    }

    @Override
    public int getItemViewType(int position) {
        Doc article = mArticles.get(position);
        if (article.getMultimedia().size() == 0) {
            return RowType.PREVIEW.ordinal();
        } else  {
            return RowType.REGULAR.ordinal();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView;

        if (viewType == RowType.PREVIEW.ordinal())
        {
            itemView = layoutInflater.inflate(R.layout.list_item_article_preview, parent, false);
            viewHolder = new ViewHolderPreview(itemView);
        }
        else
        {
            itemView = layoutInflater.inflate(R.layout.list_item_article, parent, false);
            viewHolder = new ViewHolderRegular(itemView);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Doc article = mArticles.get(position);

        if (holder instanceof ViewHolderPreview)
        {
            ((ViewHolderPreview)holder).configurePreviewViewHolder(article);
        }
        else
        {
            ((ViewHolderRegular)holder).configureRegularViewHolder(article);
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
