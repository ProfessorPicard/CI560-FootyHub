package uk.phsh.footyhub.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import uk.phsh.footyhub.R;
import uk.phsh.footyhub.models.NavItem;

public class DrawNavAdapter extends BaseAdapter {
    private final Context _context;
    private final ArrayList<NavItem> _navItems;

    public DrawNavAdapter(Context context, ArrayList<NavItem> navItems) {
        this._context = context;
        this._navItems = navItems;
    }

    @Override
    public int getCount() {
        return _navItems.size();
    }

    @Override
    public Object getItem(int position) {
        return _navItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.nav_item, null);
        } else {
            view = convertView;
        }

        TextView titleTxt = view.findViewById(R.id.title);
        ImageView iconImg = view.findViewById(R.id.icon);

        NavItem item = _navItems.get(position);

        titleTxt.setText(item.getTitle());
        iconImg.setImageResource(item.getIcon());

        return view;
    }
}
