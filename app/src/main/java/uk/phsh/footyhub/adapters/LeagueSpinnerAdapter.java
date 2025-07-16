package uk.phsh.footyhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import uk.phsh.footyhub.R;
import uk.phsh.footyhub.rest.enums.LeagueEnum;

/**
 * ArrayAdapter for the League Spinner on standings fragment
 * @author Peter Blackburn
 */
public class LeagueSpinnerAdapter extends ArrayAdapter<LeagueEnum> {

    /**
     * @param context Application context
     * @param enums Array of LeagueEnums to display
     */
    public LeagueSpinnerAdapter(Context context, LeagueEnum[] enums) {
        super(context, 0, enums);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
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
        return getCustomView(position, parent);
    }

    private View getCustomView(final int position, ViewGroup parent) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_row, parent, false);
        LeagueEnum leagueEnum = getItem(position);
        final TextView value = row.findViewById(R.id.spinnerRowTxt);
        assert leagueEnum != null;
        value.setText(leagueEnum.getReadableName());
        return row;
    }

}
