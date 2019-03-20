package com.zbyj.Yazhou.ProgramFrame;import android.annotation.SuppressLint;import android.app.Fragment;import android.graphics.Color;import android.graphics.drawable.GradientDrawable;import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.BaseAdapter;import android.widget.ImageView;import android.widget.ListView;import android.widget.TextView;import com.zbyj.Yazhou.R;public class OrderListFrame extends Fragment {    private ListView listview = null;    @Override    @SuppressLint("NewApi")    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle            savedInstanceState) {        View view = LayoutInflater.from(getContext()).inflate(R.layout.framelayout_orderlist, null);        init(view);        return view;    }    private void init(View v) {        listview = v.findViewById(R.id.framelayout_orderlist_listview);        Listener(v);    }    private void Listener(View v) {        /**         * listview加载适配器         */        orderListAdapter adapter  = new orderListAdapter();        listview.setAdapter(adapter);    }    class orderListAdapter extends BaseAdapter {        @Override        public int getCount() {            return 20;        }        @Override        public Object getItem(int position) {            return position;        }        @Override        public long getItemId(int position) {            return position;        }        @SuppressLint({"NewApi", "ResourceType"})        @Override        public View getView(int position, View convertView, ViewGroup parent) {            Viewid viewid = new Viewid();            if (convertView != null) {                viewid = (Viewid) convertView.getTag();            } else {                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_sendorder,                        null);                viewid.btn_refund = convertView.findViewById(R.id.item_sendorder_btnrefund);                convertView.setTag(viewid);            }            GradientDrawable btn_back_refund = new GradientDrawable();            btn_back_refund.setColor(Color.parseColor(getResources().getString(R.color                    .TextAndBodyColor)));            btn_back_refund.setStroke(2, Color.parseColor(getResources().getString(R.color                    .TextAndBodyColor)));            viewid.btn_refund.setTextColor(Color.BLACK);            viewid.btn_refund.setBackground(btn_back_refund);            return convertView;        }        class Viewid {            //保存item的变量集合            ImageView img;            TextView btn_refund;        }    }}