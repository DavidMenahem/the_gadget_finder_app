package com.david.thegadjetfinder.shape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class CircleView extends View {
    private Paint paint;
    private final int x;
    private final int y;
    public CircleView(Context context,int x,int y) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
        this.x = x;
        this.y = y;
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        canvas.translate(getX(),getY());
        canvas.drawCircle(getWidth() / 2, getHeight() / 2 , 50, paint);
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
