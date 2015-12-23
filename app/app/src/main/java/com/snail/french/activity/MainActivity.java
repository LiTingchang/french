package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.fragment.BaseMainFragment;
import com.snail.french.fragment.TcfFragment;
import com.snail.french.fragment.TefFragment;
import com.snail.french.fragment.Tem4Fragment;
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.LogUtil;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.content_main)
    FrameLayout contentMain;
    @Bind(R.id.drawer_tcf)
    TextView drawerTcf;
    @Bind(R.id.drawer_tef)
    TextView drawerTef;
    @Bind(R.id.drawer_tem_4)
    TextView drawerTem4;
    @Bind(R.id.drawer_guide)
    ImageView drawerGuide;
    @Bind(R.id.drawer_personal)
    ImageView drawerPersonal;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private static final int TAB_ID_TCF = 0;
    private static final int TAB_ID_TEF = 1;
    private static final int TAB_ID_TEM4 = 2;
    private TcfFragment tcfFragment;
    private TefFragment tefFragment;
    private Tem4Fragment tem4Fragment;

    private Map<Integer, BaseMainFragment> fragmentMap = new HashMap<>();

    private int currentTabID; //

    private int [] titleIDs = {R.string.drawer_tcf, R.string.drawer_tef, R.string.drawer_tem_4};

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        titlebar.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
            @Override
            public void onLeftClicked(View parent, View v) {
                // TODO 如果PopupWindow弹出，先隐藏PopupWindow

                //
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }

            @Override
            public void onRightClicked(View parent, View v) {
                showPopupWindow();
            }

            @Override
            public void onRight2Clicked(View parent, View v) {

            }
        });

        tcfFragment = TcfFragment.newInstance();
        tefFragment = TefFragment.newInstance();
        tem4Fragment = Tem4Fragment.newInstance();

        fragmentMap.put(TAB_ID_TCF, tcfFragment);
        fragmentMap.put(TAB_ID_TEF, tefFragment);
        fragmentMap.put(TAB_ID_TEM4, tem4Fragment);

        currentTabID = TAB_ID_TCF;
        selectFragment(TAB_ID_TCF);



        List<Map<Integer, Integer>> list = new ArrayList<>();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(123, 1);
        list.add(map);

        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(234, 1);
        list.add(map2);

        String s = JSON.toJSONString(list);

        Log.e("aaaaaaaaaa", s);
    }

    @OnClick(R.id.drawer_tcf)
    void tcfSelected() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        if (currentTabID == TAB_ID_TCF) {
            return;
        }

        selectFragment(TAB_ID_TCF);
    }

    @OnClick(R.id.drawer_tef)
    void tefSelected() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        if (currentTabID == TAB_ID_TEF) {
            return;
        }

        selectFragment(TAB_ID_TEF);
    }

    @OnClick(R.id.drawer_tem_4)
    void tem4Selected() {
        drawerLayout.closeDrawer(Gravity.LEFT);
        if (currentTabID == TAB_ID_TEM4) {
            return;
        }

        selectFragment(TAB_ID_TEM4);
    }

    @OnClick(R.id.drawer_guide)
    void guideClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);

        ToastUtil.shortToast(this, "guide");
    }

    @OnClick(R.id.drawer_personal)
    void personalClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);

        SettingActivity.launch(MainActivity.this);
    }

    private void selectFragment(int id) {
        FragmentTransaction fragmentTransaction = MainActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentMap.get(currentTabID).onPause();
        Fragment fragment = fragmentMap.get(id);
        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            fragmentTransaction.add(R.id.content_main, fragment);
        }
        for (Map.Entry entry : fragmentMap.entrySet()) {
            Fragment iteratorFragment = (Fragment) entry.getValue();
            if (entry.getKey().equals(id)) {
                fragmentTransaction.show(iteratorFragment);
            } else {
                fragmentTransaction.hide(iteratorFragment);
            }
        }
        fragmentTransaction.commit();
        currentTabID = id;

        setTitleText(id);
    }

    public BaseMainFragment getCurrentFragment() {
        return fragmentMap.get(currentTabID);
    }

    private void setTitleText(int id) {
        titlebar.setTitleText(titleIDs[id]);
    }

    int [] popMenuIds = {R.id.main_popup_window_item_1,
            R.id.main_popup_window_item_2,
            R.id.main_popup_window_item_3,
            R.id.main_popup_window_item_4,
            R.id.main_popup_window_item_5,
            R.id.main_popup_window_item_6};

    View.OnClickListener popupWindowItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.main_popup_window_item_1:
                    break;
                case R.id.main_popup_window_item_2:
                    break;
                case R.id.main_popup_window_item_3:
                    break;
                case R.id.main_popup_window_item_4:
                    break;
                case R.id.main_popup_window_item_5:
                    break;
                case R.id.main_popup_window_item_6:
                    break;
                default:
                    break;
            }
            popupWindow.dismiss();
        }
    };

    private void showPopupWindow() {
        if(popupWindow == null) {
            LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(this)
                    .inflate(R.layout.main_popup_window,
                            null);
            popupWindow = new PopupWindow(linearLayout, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            popupWindow.setBackgroundDrawable(getResources().getDrawable(
                    R.color.activity_bg));
//        popup.setWindowLayoutMode(LayoutParams.MATCH_PARENT,
//                LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
        }

        for (int id : popMenuIds) {
            popupWindow.getContentView().findViewById(id)
                    .setOnClickListener(popupWindowItemClickListener);
        }

        try {
            popupWindow.showAsDropDown(titlebar);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
        }
    }

    public static void launch(Context context) {
        FrenchApp.getApp().cleanActivityStack();
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

}
