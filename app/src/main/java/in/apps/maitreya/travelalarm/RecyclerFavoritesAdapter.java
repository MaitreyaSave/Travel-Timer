package in.apps.maitreya.travelalarm;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Maitreya on 11-Mar-17.
 *
 */

class RecyclerFavoritesAdapter extends RecyclerView.Adapter<RecyclerFavoritesAdapter.ViewHolder> {
    private List<Route> routeList;
    private boolean showCheck;

    //
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView routeTitle,routeSource,routeDestination;
        CheckBox checkDelete;
        CardView cardView;
        ViewHolder(View v) {
            super(v);
            routeTitle = (TextView) v.findViewById(R.id.route_title);
            routeSource = (TextView) v.findViewById(R.id.route_map_source);
            routeDestination = (TextView) v.findViewById(R.id.route_map_destination);
            checkDelete = (CheckBox) v.findViewById(R.id.check_delete);
            cardView = (CardView) v.findViewById(R.id.favorites_recycler_card);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    RecyclerFavoritesAdapter(List<Route> routeList) {
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

        //ViewHolder vh = new ViewHolder(v);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Route route=routeList.get(position);
        holder.routeTitle.setText(route.getTitle());

        holder.routeSource.setText(route.getSourceString());
        holder.routeDestination.setText(route.getDestinationString());
        //
        if (showCheck) {
            holder.checkDelete.setVisibility(View.VISIBLE);
            //
            if(routeList.get(position).isDeleteYN())
                holder.checkDelete.setChecked(true);
            else
                holder.checkDelete.setChecked(false);
            //
            //
            holder.checkDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkDelete.isChecked())
                        routeList.get(position).setDeleteYN(true);
                    else
                        routeList.get(position).setDeleteYN(false);
                }
            });


            //
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.checkDelete.isChecked()) {
                        holder.checkDelete.setChecked(false);
                        routeList.get(position).setDeleteYN(false);
                    }
                    else {
                        holder.checkDelete.setChecked(true);
                        routeList.get(position).setDeleteYN(true);
                    }
                }
            });
        }
        else {
            holder.checkDelete.setVisibility(View.GONE);
            holder.checkDelete.setChecked(false);
        }



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(routeList!=null)
            return routeList.size();
        else
            return 0;
    }
    void notify(List<Route> list) {
        if (routeList != null) {
            routeList.clear();
            routeList.addAll(list);

        } else {
            routeList = list;
        }
        notifyDataSetChanged();
    }
    void showCheckboxes(boolean showCheck){
        this.showCheck=showCheck;
    }

}
