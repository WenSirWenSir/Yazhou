package localViews;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import localInterface.ScrollViewListener;
import com.zbyj.Yazhou.tools;

public class RefreshScrollView extends ScrollView {
    private int down_y;//按下时候的Y坐标
    private int scroll_y;//ScrollView的滑动距离
    private LinearLayout headViewRefresh;//头布局
    private ScrollViewListener listener;//刷新加载数据的数据监听
    private boolean b_down;//是否可以刷新
    private int viewWidth;//scrollView宽度
    private int headViewHeight;//头部可以刷新的高度

    public RefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RefreshScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = getWidth();

    }

    public void setHeadView(LinearLayout view) {
        this.headViewRefresh = view;
        this.headViewHeight = tools.dip2px(getContext(), 50);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headViewRefresh.getLayoutParams();
        params.width = viewWidth;
        params.height = 0;
        params.gravity = Gravity.CENTER;
    }

    public void setListener(ScrollViewListener Listener) {
        this.listener = Listener;
    }

    public void stopRefresh() {
        listener.onState("下拉刷新");//停止刷新之后更新文字信息
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) this.headViewRefresh.getLayoutParams();
        params.width = viewWidth;
        params.height = 0;
        Log.i("capitalist", "不可以刷新");
        this.headViewRefresh.setLayoutParams(params);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        scroll_y = t;
        if (t == 0) {

        } else if (t + this.getMeasuredHeight() == this.getChildAt(0).getMeasuredHeight()) {
            //滑动到底部
            listener.onLoadMore();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //记录手机点下的Y坐标
            down_y = (int) ev.getY();
        }
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (scroll_y == 0) {
                //在顶部 可以进行刷新操作
                Log.i("capitalist", "在顶部 可以刷新");
                if (ev.getY() - down_y > 0) {
                    //手势判断 向下滑动 可以刷新
                    Log.i("capitalist", "向下滑动 可以刷新");
                    int downRange = (int) ((ev.getY() - down_y) * 1 / 2);
                    b_down = false;//刚刚开始滑动 松手还不可以刷新
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headViewRefresh.getLayoutParams();
                    params.width = viewWidth;
                    params.height = downRange;
                    Log.i("capitalist", "设置的高度为:" + downRange);
                    headViewRefresh.setLayoutParams(params);//设置高度
                    if (downRange >= headViewHeight) {
                        listener.onState("松开刷新");
                        b_down = true;
                    } else {
                        listener.onState("下拉刷新");
                        b_down = false;
                    }
                    return true;
                } else {
                    b_down = false;//不可以刷新
                    return super.dispatchTouchEvent(ev);

                }
            } else {
                b_down = false;
                return super.dispatchTouchEvent(ev);
            }
        }
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            if (b_down) {
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) headViewRefresh.getLayoutParams();
                params.width = viewWidth;
                params.height = headViewHeight;
                headViewRefresh.setLayoutParams(params);
                listener.onState("正在刷新");
                listener.onRefresh();//开始刷新数据信息
            } else {
                //不可以刷新的时候  就调用不可以刷新事件
                stopRefresh();
            }
        }

        return super.dispatchTouchEvent(ev);
    }
}

