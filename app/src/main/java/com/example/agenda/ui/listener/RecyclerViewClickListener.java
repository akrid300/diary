package com.example.agenda.ui.listener;

public interface RecyclerViewClickListener {

    void onClick(int position);

    void onDelete(int position, Long eventId);
}
