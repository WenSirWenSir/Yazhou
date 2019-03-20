package com.zbyj.Yazhou.ProgramAct;import android.annotation.SuppressLint;import android.app.AlertDialog;import android.os.Bundle;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.BaseAdapter;import android.widget.ImageView;import android.widget.ListView;import android.widget.RelativeLayout;import android.widget.TextView;import android.widget.Toast;import com.zbyj.Yazhou.ConfigPageValue.USER_KEY_PAGE;import com.zbyj.Yazhou.LeftCompanyProgram.CompanyPage.XMLUserAddr;import com.zbyj.Yazhou.LeftCompanyProgram.CompanyTools.Usertools;import com.zbyj.Yazhou.LeftCompanyProgram.Config;import com.zbyj.Yazhou.LeftCompanyProgram.ConfigPageClass;import com.zbyj.Yazhou.LeftCompanyProgram.Interface.ProgramInterface;import com.zbyj.Yazhou.LeftCompanyProgram.Net;import com.zbyj.Yazhou.LeftCompanyProgram.Tools;import com.zbyj.Yazhou.R;import com.zbyj.Yazhou.YazhouActivity;import com.zbyj.Yazhou.config;import java.io.InputStream;import java.util.ArrayList;public class UserAllAddrsAct extends YazhouActivity {    private ListView listView;    private ImageView btn_addrs;    private RelativeLayout addaddrsbody;    private showAllAdapter adapter;    private Boolean isRedact = false;    private ArrayList<XMLUserAddr> madapterlist = new ArrayList<XMLUserAddr>();    @SuppressLint("ResourceType")    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_showalladdrs);        setStatusBar(getResources().getString(R.color.TextAndBodyColor));        init();    }    private void init() {        listView = findViewById(R.id.activity_showalladdrs_listview);        btn_addrs = findViewById(R.id.activity_showalladdrs_btnaddrs);        addaddrsbody = findViewById(R.id.activity_showalladdrs_addaddrsbody);        //adapter = new showAllAdapter();        Usertools.getUserAllAddr(getApplicationContext(), new ProgramInterface.XMLforUserAllAddr() {            @Override            public void onDone(ArrayList<XMLUserAddr> list) {                madapterlist.addAll(list);                adapter = new showAllAdapter(madapterlist);                listView.setAdapter(adapter);            }            @Override            public void onFain() {            }        });        //如果没有数据 就显示LOGO        //addaddrsbody.setVisibility(View.VISIBLE);        listView.setAdapter(adapter);        Listener();    }    private void Listener() {        /**         * 管理按钮的点击         */        findViewById(R.id.activity_showalladdrs_btnRedact).setOnClickListener(new View                .OnClickListener() {            @Override            public void onClick(View v) {                TextView tv = (TextView) v;                if (tv.getText().toString().equals("管理")) {                    isRedact = true;                    tv.setText("完成");                    if(madapterlist != null){                        adapter = null;                        adapter = new showAllAdapter(madapterlist);                        listView.setAdapter(adapter);                    }                } else {                    isRedact = false;                    tv.setText("管理");                    if(madapterlist != null){                        adapter = null;                        adapter = new showAllAdapter(madapterlist);                        listView.setAdapter(adapter);                    }                }            }        });        /**         * 新增地址点击         */        findViewById(R.id.activity_showalladdrs_btnNewaddr).setOnClickListener(new View                .OnClickListener() {            @Override            public void onClick(View v) {                YaZhouStartActivity(UserAddHomeAddrAct.class, false);            }        });    }    /**     * 重新获取焦点     */    @Override    protected void onResume() {        super.onResume();        Log.i(config.DEBUG_STR, "窗口重新获取焦点");    }    class showAllAdapter extends BaseAdapter {        ArrayList<XMLUserAddr> mlist = new ArrayList<XMLUserAddr>();        showAllAdapter(ArrayList<XMLUserAddr> list) {            mlist.clear();            mlist.addAll(list);        }        @Override        public int getCount() {            return mlist.size();        }        @Override        public Object getItem(int position) {            return null;        }        @Override        public long getItemId(int position) {            return 0;        }        @Override        public View getView(int position, View convertView, ViewGroup parent) {            ViewPage viewpage = null;            if (convertView != null) {                viewpage = (ViewPage) convertView.getTag();// is null, view has tag            } else {                // is null, maybe is first                View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout                        .item_listaaddr, null);                convertView = item;                viewpage = new ViewPage();                viewpage.addrs = item.findViewById(R.id.item_listaddr_addrs);                viewpage.people = item.findViewById(R.id.item_listaddr_people);                viewpage.btn_del = item.findViewById(R.id.item_listaddr_btnDel);                viewpage.btn_edit = item.findViewById(R.id.item_listaddr_btnEdit);                if (!isRedact) {                    viewpage.btn_edit.setVisibility(View.GONE);//不显示                    viewpage.btn_del.setVisibility(View.GONE);//不显示                    viewpage.btn_edit.setVisibility(View.GONE);                } else {                    viewpage.btn_edit.setVisibility(View.VISIBLE);//显示                    viewpage.btn_del.setVisibility(View.VISIBLE);//显示                }            }            viewpage.btn_del.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    Toast.makeText(getApplicationContext(), "点击删除", Toast.LENGTH_SHORT).show();                    //提示用户是否删除该地址                    ConfigPageClass.AlertViewIDpage alertViewIDpage = ConfigPageClass                            .getAlertViewIDpageInstance();                    View item = LayoutInflater.from(getApplicationContext()).inflate(R.layout                            .item_trueorfalse_dialog, null);                    alertViewIDpage.setConfirm((TextView) item.findViewById(R.id                            .item_trueorfalse_dialog_btnDetermine));                    alertViewIDpage.setCancle((TextView) item.findViewById(R.id                            .item_trueorfalse_dialog_btnCancle));                    alertViewIDpage.setContext((TextView) item.findViewById(R.id                            .item_trueorfalse_dialog_content));                    alertViewIDpage.setTitle((TextView) item.findViewById(R.id                            .item_trueorfalse_dialog_title));                    alertViewIDpage.setCanwindow(false);                    Tools.showAlertDilg(item, UserAllAddrsAct.this, "提示", "确定删除该地址?", "取消", "是的",                            new Tools.AlertDilgClick() {                        @Override                        public void onConfirm(AlertDialog alertDialog) {                            Toast.makeText(getApplicationContext(), "编辑", Toast.LENGTH_SHORT)                                    .show();                            alertDialog.dismiss();                        }                        @Override                        public void onCancle(AlertDialog alertDialog) {                            Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT)                                    .show();                            alertDialog.dismiss();                        }                    }, alertViewIDpage);                }            });            viewpage.btn_edit.setOnClickListener(new View.OnClickListener() {                @Override                public void onClick(View v) {                    //提示用户去修改地址                }            });            viewpage.people.setText(mlist.get(position).getUSER_NAME() + "(" + mlist.get                    (position).getUSER_TEL() + ")");            viewpage.addrs.setText(mlist.get(position).getUSER_ADDR());            return convertView;        }        class ViewPage {            TextView addrs;            TextView people;            ImageView btn_edit;            ImageView btn_del;        }    }}