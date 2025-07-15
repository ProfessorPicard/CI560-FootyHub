package uk.phsh.footyhub.fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.adapters.NewsAdapter;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.rest.RestManager;
import uk.phsh.footyhub.rest.interfaces.I_TaskCallback;
import uk.phsh.footyhub.rest.models.NewsArticle;
import uk.phsh.footyhub.rest.tasks.NewsSearchTask;

public class NewsFragment extends BaseFragment implements I_TaskCallback<ArrayList<NewsArticle>> {


    private RecyclerView _newsRecycler;
    private NewsAdapter _articleAdapter;
    private final ArrayList<NewsArticle> _newsArticles = new ArrayList<>();
    private SharedPreferences prefs;
    private boolean _updatingNews;
    private RadioGroup _maxResultsGroup;

    public NewsFragment() { super(); }

    public NewsFragment(I_FragmentCallback callBack) {
        super(callBack);
    }

    /**
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return View              The inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(R.layout.news_frag, container, false);

        _newsRecycler = v.findViewById(R.id.mainNewsList);

        _maxResultsGroup = v.findViewById(R.id.maxResultsGroup);

        prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity());
        _maxResultsGroup.setOnCheckedChangeListener((group, checkedId) -> {
            RadioButton rb = v.findViewById(checkedId);
            int value = Integer.parseInt(rb.getText().toString());

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("newsResultsPerPage", value);
            editor.apply();

            refreshNews();
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        _newsRecycler.setLayoutManager(layoutManager);
        _newsArticles.clear();
        _articleAdapter = new NewsAdapter(_newsArticles, getContext());
        _newsRecycler.setAdapter(_articleAdapter);



        refreshNews();
    }

    private void refreshNews() {
        if(!_updatingNews) {
            RestManager rm = RestManager.getInstance(requireActivity().getCacheDir());
            String teamName = prefs.getString("favouriteTeamName", "");
            int newsResults = prefs.getInt("newsResultsPerPage", 10);
            switch (newsResults) {
                case 10:
                    _maxResultsGroup.check(R.id.maxResults10);
                    break;
                case 25:
                    _maxResultsGroup.check(R.id.maxResults25);
                    break;
                case 50:
                    _maxResultsGroup.check(R.id.maxResults50);
                    break;
            }
            _updatingNews = true;
            rm.asyncTask(new NewsSearchTask(teamName, this, getContext(), newsResults));
        }
    }

    @Override
    public String getActionBarTitle() {
        return getResources().getString(R.string.news_fragment_actionbar);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onSuccess(ArrayList<NewsArticle> articles) {
        _newsArticles.clear();
        _articleAdapter.notifyDataSetChanged();
        _newsArticles.addAll(articles);
        _articleAdapter.notifyDataSetChanged();
        _updatingNews = false;
    }

    @Override
    public void onError(String message) {
        System.out.println(message);
        _updatingNews = false;
    }
}
