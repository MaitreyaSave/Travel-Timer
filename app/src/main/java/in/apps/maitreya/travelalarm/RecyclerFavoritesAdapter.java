package in.apps.maitreya.travelalarm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maitreya on 11-Mar-17.
 */

public class RecyclerFavoritesAdapter extends RecyclerView.Adapter<RecyclerFavoritesAdapter.ViewHolder> {
    private List<Route> routeList;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.route_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecyclerFavoritesAdapter(List<Route> routeList) {
        this.routeList = routeList;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerFavoritesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.route_list, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Route route=routeList.get(position);
        holder.mTextView.setText(route.getTitle());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return routeList.size();
    }
}
