package com.example.agenda.ui.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.agenda.R;

public class ContextMenu extends LinearLayout {

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position = -1;

    OnContextMenuItemClickListener listener;

    public ContextMenu(Context context) {
        super(context);
        LayoutInflater.from(getContext()).inflate(R.layout.view_context_menu, this, true);
        setOrientation(VERTICAL);
        setLayoutParams(new LayoutParams(Utils.dpToPx(200), ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        findViewById(R.id.buttonEdit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditClick();
            }
        });

        findViewById(R.id.buttonDelete).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onDeleteClick();
            }
        });
        findViewById(R.id.buttonCancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onCancelClick();
            }
        });
        onCreatedMenu(position);
    }

    public void bindToItem(int item) {
        this.position = item;

    }

    public void dismiss() {
        ((ViewGroup) getParent()).removeView(ContextMenu.this);
    }

    public void onCreatedMenu(int position) {
        if (listener != null)
            listener.onCreated(position);
    }

    public void onEditClick() {
        if (listener != null) {
            listener.onEditClick(position);
        }
    }

    public void onDeleteClick() {

        if (listener != null) {
            listener.onDeleteClick(position);
        }
    }

    public void onCancelClick() {
        if (listener != null) {
            listener.onCancelClick(position);
        }
    }

    public void addOnMenuItemClickListener(OnContextMenuItemClickListener onItemClickListener) {
        listener = onItemClickListener;
    }

    public interface OnContextMenuItemClickListener {

        void onCreated(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);

        void onCancelClick(int position);
    }
}
