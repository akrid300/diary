package com.example.agenda.ui.listener;

public interface RecyclerViewClickListener {

    void onClick(int position);

    void onEditClick(int position, Long eventId);

    void onDelete(int position, Long eventId);
}
