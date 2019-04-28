package ntnu.imt3673.android_geocache;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class AchievementViewAdapter extends RecyclerView.Adapter<AchievementViewAdapter.MyViewHolder> {

    private ArrayList<Achievement> mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        public TextView title, summary;
        public ImageView image;
        public LinearLayout layoutTitle, layoutSummary;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
            summary = view.findViewById(R.id.summary);
            layoutTitle = view.findViewById(R.id.layout_title);
            layoutSummary = view.findViewById(R.id.layout_summary);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AchievementViewAdapter(ArrayList<Achievement> myDataset) {
        this.mDataset = myDataset;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public AchievementViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.achievement_row, parent, false);
        return new MyViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.title.setText(mDataset.get(position).getTitle());
        holder.summary.setText(mDataset.get(position).getSummary());
        if(mDataset.get(position).isUnlocked()) {
            holder.title.setText(mDataset.get(position).getTitle() + " - UNLOCKED!");
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}