package io.github.imsmobile.fahrplan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import io.github.imsmobile.fahrplan.MainActivity;
import io.github.imsmobile.fahrplan.R;
import io.github.imsmobile.fahrplan.model.FavoriteModel;
import io.github.imsmobile.fahrplan.model.FavoriteModelItem;

public class FavoriteAdapter extends BaseAdapter {

    private final Context context;
    private final FavoriteModel favorite;

    public FavoriteAdapter(Context context, FavoriteModel favorite) {
        this.context = context;
        this.favorite = favorite;
    }
    @Override
    public int getCount() {
        return favorite.size();
    }

    @Override
    public Object getItem(int position) {
        return favorite.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.favorite_item, parent, false);
        }

        FavoriteModelItem fav = (FavoriteModelItem) getItem(position);

        TextView text = (TextView) convertView.findViewById(R.id.favorite_item_text);
        text.setText(fav.toString());
        text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity c = (MainActivity) context;
                c.activateFavorite(position);
            }
        });

        Button delete = (Button) convertView.findViewById(R.id.favorite_item_delete);
        delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MainActivity c = (MainActivity) context;
                c.removeFavorite(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

}
