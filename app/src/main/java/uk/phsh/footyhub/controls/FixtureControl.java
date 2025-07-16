package uk.phsh.footyhub.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import com.squareup.picasso.Picasso;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;
import uk.phsh.footyhub.R;

public class FixtureControl extends CardView {

    @Retention(RetentionPolicy.CLASS)
    public @interface FixtureType {
        int SCHEDULED = 0;
        int FINISHED = 1;
    }

    private @FixtureType int _fixtureType = FixtureType.SCHEDULED;
    private boolean _showError;
    private String _title;
    private String _homeImgSrc;
    private String _awayImgSrc;
    private String _fixtureDate;
    private String _fixtureTime;
    private int _homeScore;
    private int _awayScore;
    private String _errorMessage;
    private String _header;

    private RelativeLayout errorContainer;
    private RelativeLayout contentContainer;
    private TextView homeScore;
    private TextView title;
    private TextView awayScore;
    private TextView titleHeader;
    private TextView errorTxt;
    private TextView fixtureDate;
    private TextView fixtureTime;
    private ImageView homeImg;
    private ImageView awayImg;


    public FixtureControl(Context context) {
        super(context);
        init(null, 0);
    }

    public FixtureControl(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public FixtureControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(@Nullable AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray array = getContext().getTheme().obtainStyledAttributes
                    (attrs, R.styleable.FixtureControl, defStyleAttr, 0);
            _fixtureType = array.getInt(R.styleable.FixtureControl_Type, 0);
            _showError = array.getBoolean(R.styleable.FixtureControl_ShowError, false);
            _homeScore = array.getInt(R.styleable.FixtureControl_HomeScore, 0);
            _awayScore = array.getInt(R.styleable.FixtureControl_AwayScore, 0);
            _title = array.getString(R.styleable.FixtureControl_Title);
            _homeImgSrc = array.getString(R.styleable.FixtureControl_HomeImgSrc);
            _awayImgSrc = array.getString(R.styleable.FixtureControl_AwayImgSrc);
            _fixtureDate = array.getString(R.styleable.FixtureControl_FixtureDate);
            _fixtureTime = array.getString(R.styleable.FixtureControl_FixtureTime);
            _errorMessage = array.getString(R.styleable.FixtureControl_ErrorMessage);
            _header = array.getString(R.styleable.FixtureControl_Header);

        }

        View view = inflate(getContext(), R.layout.fixture_control, this);
        homeScore = view.findViewById(R.id.fixtureHomeScore);
        awayScore = view.findViewById(R.id.fixtureAwayScore);
        errorContainer = view.findViewById(R.id.fixtureErrorContainer);
        contentContainer = view.findViewById(R.id.fixtureContent);
        title = view.findViewById(R.id.fixtureTitle);
        titleHeader = view.findViewById(R.id.fixtureHeader);
        homeImg = view.findViewById(R.id.fixtureHomeImg);
        awayImg = view.findViewById(R.id.fixtureAwayImg);
        errorTxt = view.findViewById(R.id.fixtureErrorTxt);
        fixtureDate = view.findViewById(R.id.fixtureDate);
        fixtureTime = view.findViewById(R.id.fixtureTime);
    }

    private void update() {
        Picasso.get().load(_homeImgSrc).into(homeImg);
        Picasso.get().load(_awayImgSrc).into(awayImg);

        title.setText(_title);
        titleHeader.setText(_header);

        homeScore.setText(String.format(Locale.UK,"%d", _homeScore));
        awayScore.setText(String.format(Locale.UK,"%d", _awayScore));

        errorTxt.setText(_errorMessage);


        fixtureDate.setText(_fixtureDate);
        fixtureTime.setText(_fixtureTime);

        switch (_fixtureType) {
            case FixtureType.SCHEDULED:
                homeScore.setVisibility(View.INVISIBLE);
                awayScore.setVisibility(View.INVISIBLE);
                break;
            case FixtureType.FINISHED:
                homeScore.setVisibility(View.VISIBLE);
                awayScore.setVisibility(View.VISIBLE);
                break;
        }

        if(_showError) {
            errorContainer.setVisibility(View.VISIBLE);
            contentContainer.setVisibility(View.GONE);
        } else {

            errorContainer.setVisibility(View.GONE);
            contentContainer.setVisibility(View.VISIBLE);
        }
//        invalidate();
    }

    public void setTitle(String value) {
        _title = value;
        update();
    }
    public void setHomeImgSrc(String value) {
        _homeImgSrc = value;
        update();
    }
    public void setAwayImgSrc(String value) {
        _awayImgSrc = value;
        update();
    }
    public void setFixtureDate(String value) {
        _fixtureDate = value;
        update();
    }
    public void setFixtureTime(String value) {
        _fixtureTime = value;
        update();
    }
    public void setHomeScore(int value) {
        _homeScore = value;
        update();
    }
    public void setAwayScore(int value) {
        _awayScore = value;
        update();
    }
    public void showError(boolean value) {
        _showError = value;
        update();
    }
    public void setErrorMessage(String value) {
        _errorMessage = value;
        update();
    }
}
