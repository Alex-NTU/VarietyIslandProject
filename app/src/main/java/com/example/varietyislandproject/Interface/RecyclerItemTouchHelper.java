package com.example.varietyislandproject.Interface;

import androidx.recyclerview.widget.RecyclerView;

public interface RecyclerItemTouchHelper {

    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
}
