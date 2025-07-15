package uk.phsh.footyhub.rest.tasks;
import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import uk.phsh.footyhub.helpers.UtilityHelper;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.NewsArticle;
import uk.phsh.footyhub.rest.models.RestResponse;

/**
 * Task for retrieving news articles for specified team
 * @author Peter Blackburn
 */
public class NewsSearchTask extends BaseTask<ArrayList<NewsArticle>> {

    private final String _teamName;

    /**
     * @param teamName The team name to retrieve news for
     */
    public NewsSearchTask(String teamName, I_TaskCallback<ArrayList<NewsArticle>> callback, Context context) {
        super(callback, context);
        _teamName = teamName;
    }

    /**
     * @return String The url of the http request
     */
    @Override
    public String getUrl() {
        return "https://www.newsnow.co.uk/h/?search=" + _teamName + "&lang=en&searchheadlines=1";
    }

    /**
     * @return String The tag for this class
     */
    @Override
    public String getTag() {
        return "NewsSearchTask";
    }

    @Override
    public void onSuccess(RestResponse response) {
        ArrayList<NewsArticle> articles = new ArrayList<>();
        Document d = Jsoup.parse(response.getResponseBody());

        Elements articlesEle = d.getElementsByAttributeValue("class", "hl__inner");
        int max = Math.min(articlesEle.size(), 10);
        for(int i=0; i<max; i++) {

            Element articleEle = articlesEle.get(i);

            String url = articleEle.getElementsByAttributeValue("class", "hll").get(0).attr("href");

            //News site contains a phantom news article that is a subscribe option. We skip this.
            if(url.equals("/subscribe"))
                break;

            //Parse out all relevant pieces of the news article.
            String headlineTxt = articleEle.getElementsByAttributeValue("class", "hll").get(0).text();
            Element meta = articleEle.getElementsByAttributeValue("class", "meta").get(0);
            String publisher = meta.getElementsByAttributeValueContaining("class", "src").get(0).text();
            String timeAtt = meta.getElementsByAttributeValue("class", "time").get(0).attr("data-time");
            String timeStamp = UtilityHelper.getInstance().epochToDateTimeString(timeAtt);

            //Create a new NewsArticle object
            NewsArticle na = new NewsArticle();
            na.dateTime = timeStamp;
            na.url = url;
            na.headline = headlineTxt;
            na.source = publisher;

            //Add news article to our arrayList
            articles.add(na);
        }

        //Send the news articles with the callback
        getCallback().onSuccess(articles);
    }

}
