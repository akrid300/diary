package com.example.agenda.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agenda.R;
import com.example.agenda.ui.activity.EventActivity;
import com.example.agenda.ui.listener.RecyclerViewClickListener;
import com.example.agenda.ui.model.Event;
import com.example.agenda.ui.utils.Annotations;
import com.example.agenda.ui.utils.ContextMenu;
import com.example.agenda.ui.utils.ContextMenuManager;
import com.example.agenda.ui.viewholder.EventViewHolder;

import java.util.ArrayList;

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ContextMenu.OnContextMenuItemClickListener {

    private ArrayList<Event> items = new ArrayList<>();
    private Context context;
    private RecyclerViewClickListener listener;

    private ContextMenu.OnContextMenuItemClickListener contextMenuListener;

    // Provide a suitable constructor (depends on the kind of data set)
    public EventsAdapter(Context context, RecyclerViewClickListener listener) {
        this.context = context;
        this.listener = listener;
        this.contextMenuListener = this;
    }

    public void setItems(ArrayList<Event> events) {
        if (items != null) {
            this.items = events;
            notifyDataSetChanged();
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Event) {
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
                Event event = items.get(position);
                configureEventViewHolder(searchViewHolder, event, position);
                break;
            default:
                break;
        }
    }

    private void configureEventViewHolder(EventViewHolder eventViewHolder, Event event, int position) {
        eventViewHolder.bind(context, position, event, listener, contextMenuListener);
    }

    @Override
    public void onCreated(int position) {

    }

    @Override
    public void onCancelClick(int position) {
        dismissContextMenu();
    }

    @Override
    public void onEditClick(int position) {
        dismissContextMenu();
        Event event = items.get(position);
        if (listener != null) listener.onEditClick(position, event.getId());
    }

    @Override
    public void onDeleteClick(int position) {
        dismissContextMenu();
        Event event = items.get(position);
        items.remove(position);
        notifyDataSetChanged();
        listener.onDelete(position, event.getId());
    }

    private void dismissContextMenu() {
        if (ContextMenuManager.getInstance(context).isContextMenuShowing())
            ContextMenuManager.getInstance(context).hideContextMenu(true);
    }
}
