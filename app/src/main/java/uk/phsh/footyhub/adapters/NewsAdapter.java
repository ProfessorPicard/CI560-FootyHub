package uk.phsh.footyhub.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.rest.models.NewsArticle;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsArticle> _newsArticles;
    private final Context _context;

    public NewsAdapter(List<NewsArticle> newsArticles, Context context){
        this._newsArticles = newsArticles;
        this._context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_article_row, viewGroup, false);
        NewsViewHolder nvh = new NewsViewHolder(itemView);
        itemView.setOnClickListener(v -> nvh.onClick(_newsArticles, _context));
        return nvh;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder viewHolder, int i) {
        NewsArticle newsArticle = _newsArticles.get(i);
        viewHolder.headline.setText(newsArticle.headline);
        viewHolder.source.setText(newsArticle.source);
        viewHolder.dateTime.setText(newsArticle.dateTime);
    }

    @Override
    public int getItemCount() {
        return _newsArticles.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{

        protected TextView headline;
        protected TextView source;
        protected TextView dateTime;

        public NewsViewHolder(View itemView) {
            super(itemView);

            headline = itemView.findViewById(R.id.articleHeadline);
            source = itemView.findViewById(R.id.articlePublisher);
            dateTime = itemView.findViewById(R.id.articleTimeDate);
        }

        public void onClick(List<NewsArticle> articles, Context context) {
            NewsArticle na = articles.get(getAdapterPosition());
            String url = na.url;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            context.startActivity(i);
        }
    }
}


