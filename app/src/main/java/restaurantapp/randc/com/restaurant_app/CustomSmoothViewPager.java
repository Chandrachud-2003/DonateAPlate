package restaurantapp.randc.com.restaurant_app;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.av.smoothviewpager.Smoolider.SmoothViewpager;

import androidx.viewpager.widget.ViewPager;

public class CustomSmoothViewPager extends SmoothViewpager {

    boolean canSwipe = true;

    public CustomSmoothViewPager(Context context) {
        super(context);
    }

    public CustomSmoothViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child = getChildAt(getCurrentItem());
        if (child != null) {
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void canSwipe(boolean canSwipe) {
        this.canSwipe = canSwipe;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.canSwipe) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.canSwipe) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }
}