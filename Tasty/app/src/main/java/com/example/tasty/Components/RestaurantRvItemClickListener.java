package com.example.tasty.Components;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.content.Context;


public class RestaurantRvItemClickListener implements RecyclerView.OnItemTouchListener{

    private OnItemDoubleClickListener doubleClickListener;

    private OnItemClickListener mListener;
    GestureDetector mGestureDetector;

    GestureDetectorCompat mGestureDetectorCompat;

    public interface  OnItemClickListener{
        public void onItemClick(View view, int position);
    }

    public  interface OnItemDoubleClickListener {
        public void onItemDoubleClick(View view, int position);
    }

    public RestaurantRvItemClickListener(Context context, OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(childView,rv.getChildAdapterPosition(childView));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
