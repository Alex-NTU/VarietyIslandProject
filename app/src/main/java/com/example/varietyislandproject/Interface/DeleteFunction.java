package com.example.varietyislandproject.Interface;

import android.graphics.Canvas;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.varietyislandproject.ShopCart;
import com.example.varietyislandproject.ViewHolder.CartViewHolder;

public class DeleteFunction extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelper helperDelete;

    public DeleteFunction(int dragDirs, int swipeDirs, ShopCart helperDelete) {
        super(dragDirs, swipeDirs);
        this.helperDelete = helperDelete;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction)    {
        if (helperDelete != null)
            helperDelete.onSwiped(viewHolder,direction,viewHolder.getAdapterPosition());
    }

    @Override
    public  int convertToAbsoluteDirection (int flags, int layoutDirection)
    {
        return super.convertToAbsoluteDirection(flags,layoutDirection);
    }

    @Override
    public  void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder)
    {
        View Foreground = ((CartViewHolder)viewHolder).foreground;
        getDefaultUIUtil().clearView(Foreground);
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View Foreground = ((CartViewHolder)viewHolder).foreground;
        getDefaultUIUtil().onDraw(c,recyclerView,Foreground,dX,dY,actionState,isCurrentlyActive);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null)
        {
            View Foreground = ((CartViewHolder)viewHolder).foreground;
            getDefaultUIUtil().onSelected(Foreground);
        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View Foreground = ((CartViewHolder)viewHolder).foreground;
        getDefaultUIUtil().onDrawOver(c,recyclerView,Foreground,dX,dY,actionState,isCurrentlyActive);
    }
}
