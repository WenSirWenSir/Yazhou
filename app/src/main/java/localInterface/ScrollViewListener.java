package localInterface;


/**
 * 自定义的Scrollview的监听类
 */
public interface ScrollViewListener {
    void onRefresh();//刷新操作
    void onRefreshdone();//刷新完成
    void onStopRefresh();//停止刷新
    void onState(String statue);//提示标题
    void onLoadMore();//加载更多
}
