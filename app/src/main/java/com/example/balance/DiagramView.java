package com.example.balance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class DiagramView extends View {

    private int income;
    private int expenses;

    private Paint incomePaint = new Paint();
    private Paint expensePaint = new Paint();


    public DiagramView(Context context) {
        this(context,null);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DiagramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        incomePaint.setColor(getResources().getColor(R.color.balance_income_color));
        expensePaint.setColor(getResources().getColor(R.color.balance_expense_color));

        if(isInEditMode()){
          income = 19000;
          expenses = 4500;
        }
    }

    public void update(int income,int expenses){
     this.income = income;
     this.expenses = expenses;
     //method for drawing yourself, is similar notifyDataSetChange in Adapter
        //in this method it will be called layout,onMeasure< on Draw
     //requestLayout();
        //because the size of our view doesn't change the challenge of this method
        //all we need to do is redraw view
       // this method will only call onDraw();
        invalidate();

    }
  // onClick to view
   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
        event.getAction() = MotionEvent.ACTION_DOWN;
    }
    */

    //the size of the view is processed (in the ConstraintLayout doesn't work)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthValue = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightValue = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(300,300);
 //       super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawPieDiagram(canvas);
        } else {
            drawRectDiagram(canvas);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawPieDiagram(Canvas canvas){
        if (expenses + income == 0)
            return;

        float expenseAngle = 360.f * expenses / (expenses + income);
        float incomeAngle = 360.f * income / (expenses + income);

        int space = 10; // space between income and expense
        int size = Math.min(getWidth(), getHeight()) - space * 2;

        final int xMargin = (getWidth() - size) / 2, yMargin = (getHeight() - size) / 2;

        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space,
                getHeight() - yMargin, 180 - expenseAngle / 2, expenseAngle,
                true, expensePaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space,
                getHeight() - yMargin, 360 - incomeAngle / 2, incomeAngle,
                true, incomePaint);

    }


    private void drawRectDiagram(Canvas canvas){
        if (expenses + income == 0)
            return;

        long max = Math.max(expenses, income);
        long expensesHeight = canvas.getHeight() * expenses / max;
        long incomeHeight = canvas.getHeight() * income / max;

        int w = getWidth() / 4;

        canvas.drawRect(w / 2, canvas.getHeight() - expensesHeight,
                w * 3 / 2, canvas.getHeight(), expensePaint);
        canvas.drawRect(5 * w / 2, canvas.getHeight() - incomeHeight,
                w * 7 / 2, canvas.getHeight(), incomePaint);
    }
}
