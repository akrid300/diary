package com.example.agenda.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.ui.listener.RecyclerViewClickListener;
import com.example.agenda.ui.model.EventModel;
import com.example.agenda.ui.utils.Annotations;
import com.example.agenda.ui.viewholder.EventViewHolder;

import org.json.JSONObject;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<EventModel> items = new ArrayList<>();
    private Context context;
    private RecyclerViewClickListener listener;

    // Provide a suitable constructor (depends on the kind of data set)
    public EventsAdapter(Context context, ArrayList<EventModel> items, RecyclerViewClickListener listener) {
        this.context = context;
        if (items != null)
            this.items = items;
        this.listener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof EventModel) {
            return Annotations.CardType.EVENT;
        }
        return -1;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case Annotations.CardType.EVENT:
                View eventView = inflater.inflate(R.layout.list_item_event, viewGroup, false);
                viewHolder = new EventViewHolder(eventView);
                break;
            default:
                View emptyList = inflater.inflate(android.R.layout.simple_list_item_1, viewGroup, false);
                viewHolder = new EventViewHolder(emptyList);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case Annotations.CardType.EVENT:
                EventViewHolder searchViewHolder = (EventViewHolder) viewHolder;
                EventModel eventModel = items.get(position);
                configureEventViewHolder(searchViewHolder, eventModel, position);
                break;
            default:
                break;
        }
    }

    private void configureEventViewHolder(EventViewHolder eventViewHolder, EventModel eventModel, int position) {
        eventViewHolder.bind(position, eventModel, listener);
    }
}
