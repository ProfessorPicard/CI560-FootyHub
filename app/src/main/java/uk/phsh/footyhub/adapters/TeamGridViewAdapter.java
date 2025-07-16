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
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.rest.models.Team;

/**
 * ArrayAdapter for the Teams GridView on Select Team fragment
 * @author Peter Blackburn
 */
public class TeamGridViewAdapter extends ArrayAdapter<Team> {

    /**
     * @param context Application context
     * @param list Arraylist of teams to display
     */
    public TeamGridViewAdapter(Context context, ArrayList<Team> list) {
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
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.select_team_card, parent, false);
        }

        Team team = getItem(position);

        TextView textView = itemView.findViewById(R.id.text_view);
        ImageView imageView = itemView.findViewById(R.id.image_view);

        if (team != null) {
            textView.setText(team.shortName);
            Picasso.get().load(team.crest).into(imageView);
        }

        return itemView;
    }
}
