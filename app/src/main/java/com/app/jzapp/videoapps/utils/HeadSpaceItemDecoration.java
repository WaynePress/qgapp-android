package com.app.jzapp.videoapps.utils;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HeadSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private int leftRight;
    private int topBottom;
    private int headNmu;

    public HeadSpaceItemDecoration(int leftRight, int topBottom) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
    }

    public HeadSpaceItemDecoration(int leftRight, int topBottom, int headNmu) {
        this.leftRight = leftRight;
        this.topBottom = topBottom;
        this.headNmu = headNmu;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        //竖直方向的
        if (parent.getChildAdapterPosition(view) > headNmu)
            if (layoutManager.getOrientation() == LinearLayoutManager.VERTICAL) {
                //最后一项需要 bottom
                if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                    outRect.bottom = topBottom;
                }
                outRect.top = topBottom;
                outRect.left = leftRight;
                outRect.right = leftRight;
            } else {
                //最后一项需要right
                if (parent.getChildAdapterPosition(view) == layoutManager.getItemCount() - 1) {
                    outRect.right = leftRight;
                }
                outRect.top = topBottom;
                outRect.left = leftRight;
                outRect.bottom = topBottom;
            }
    }
}
