package test.com.coordinatorlayouttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class StickHeaderDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Paint rectPaint;
    private Paint textPaint;
    private int rectHeight;
    private int textSize;
    private IStick iStick;

    public StickHeaderDecoration(Context context,IStick stick) {
        mContext=context;
        iStick=stick;
        rectHeight=Uitools.dip2px(mContext,20);
        textSize=20;
        rectPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#f4f4f4"));
        rectPaint.setStyle(Paint.Style.FILL);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(textSize);
        textPaint.setColor(Color.parseColor("#000000"));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int pos=parent.getChildAdapterPosition(view);
        if (iStick.isFirst(pos)) {
            outRect.top=rectHeight;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        int childCount = parent.getChildCount();
        int itemCount = state.getItemCount();
        int left = parent.getPaddingLeft()+parent.getLeft();
        int right=parent.getRight()-parent.getPaddingRight();
        String preTitle = "";
        String curTitle = "";
        for (int i = 0; i < childCount; i++) {
            View childView = parent.getChildAt(i);
            int postion = parent.getChildAdapterPosition(childView);
            preTitle = curTitle;
            curTitle = iStick.getTitle(postion);
            if (preTitle.equals(curTitle)) {
                continue;
            }
            //文字的基线，保证显示完全
            int top = Math.max(rectHeight, childView.getTop());
            //分组标题
            String title = iStick.getTitle(postion);
            int viewBottom = childView.getBottom();
            //加入限定 防止数组越界
            if (postion + 1 < itemCount) {
                String nextGroupTitle = iStick.getTitle(postion + 1);
                //当分组不一样  并且改组要向上移动时候
                if (!nextGroupTitle.equals(curTitle) && viewBottom < top) {
                    //将上一个往上移动
                    top = viewBottom;
                }
            }
            //绘制边框
            c.drawRect(left, top - rectHeight, right, top, rectPaint);

            Paint.FontMetrics fm=textPaint.getFontMetrics();
            //绘制文字并且实现文字居中
            float value = top-(rectHeight-(fm.bottom-fm.top))/2-fm.bottom;
            c.drawText(title, left, value, textPaint);
        }
    }
}
