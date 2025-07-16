package uk.phsh.footyhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import uk.phsh.footyhub.R;
import uk.phsh.footyhub.rest.models.PositionInfo;

/**
 * ArrayAdapter for the standings ListView on standings fragment
 * @author Peter Blackburn
 */
public class StandingsListViewAdapter extends ArrayAdapter<PositionInfo> {


    /**
     * @param context Application context
     * @param list Arraylist of PositionInfo to display
     */
    public StandingsListViewAdapter(Context context, ArrayList<PositionInfo> list) {
        super(context, 0, list);
    }

    /**
     * @param position    The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return View       The inflated view
     */
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.standings_row, parent, false);
        }

        PositionInfo positionInfo = getItem(position);

        TextView positionTxt = itemView.findViewById(R.id.positionTxt);
        TextView teamCodeTxt = itemView.findViewById(R.id.teamCodeTxt);
        TextView playedTxt = itemView.findViewById(R.id.playedTxt);
        TextView wonTxt = itemView.findViewById(R.id.wonTxt);
        TextView drawnTxt = itemView.findViewById(R.id.drawnTxt);
        TextView lostTxt = itemView.findViewById(R.id.lostTxt);
        TextView goalDiffTxt = itemView.findViewById(R.id.goalDiffTxt);
        TextView pointsTxt = itemView.findViewById(R.id.pointsTxt);
        ImageView teamLogo = itemView.findViewById(R.id.teamLogoImg);

        if (positionInfo != null) {
            positionTxt.setText(String.format(Locale.UK, "%d", positionInfo.position));
            teamCodeTxt.setText(positionInfo.team.tla);
            playedTxt.setText(String.format(Locale.UK,"%d", positionInfo.playedGames));
            wonTxt.setText(String.format(Locale.UK,"%d", positionInfo.won));
            drawnTxt.setText(String.format(Locale.UK,"%d", positionInfo.draw));
            lostTxt.setText(String.format(Locale.UK,"%d", positionInfo.lost));
            goalDiffTxt.setText(String.format(Locale.UK,"%d", positionInfo.goalDifference));
            pointsTxt.setText(String.format(Locale.UK,"%d", positionInfo.points));

            Picasso p = Picasso.get();
            p.load(positionInfo.team.crest).into(teamLogo);
        }

        return itemView;
    }
}
