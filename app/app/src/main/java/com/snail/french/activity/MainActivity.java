package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.activity.others.GuideActivity;
import com.snail.french.activity.others.SettingActivity;
import com.snail.french.fragment.BaseMainFragment;
import com.snail.french.fragment.TcfFragment;
import com.snail.french.fragment.TefFragment;
import com.snail.french.fragment.Tem4Fragment;
import com.snail.french.utils.LogUtil;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;
import com.umeng.update.UmengUpdateAgent;

import java.util.HashMap;
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

        // 允许所有网络环境下的更新提示
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
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

        GuideActivity.launch(MainActivity.this);
    }

    @OnClick(R.id.drawer_personal)
    void personalClicked() {
        drawerLayout.closeDrawer(Gravity.LEFT);

        SettingActivity.launch(MainActivity.this);
    }

    private void selectFragment(int id) {

        fragmentMap.get(currentTabID).setShow(false);
        fragmentMap.get(id).setShow(true);

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
            String path = "";
            String title = "";
            switch (view.getId()) {
                case R.id.main_popup_window_item_1:

                    ExoPlayerActivity.launch(MainActivity.this, "http://7xpe7l.com2.z0.glb.qiniucdn.com/2016/04/12/ge_shi_gong_han_zha_ran_shang_chuan_wu_hei_bian_.mp4");

//                    SPQuestionListActivity.launch(MainActivity.this, SPQuestionListActivity.QTYPE.ERROR);
                    break;
                case R.id.main_popup_window_item_2: {

                    ExoPlayerActivity.launch(MainActivity.this, "http://7xpe7l.com2.z0.glb.qiniucdn.com/2016/04/12/ge_shi_gong_han_zha_ran_shang_chuan_wu_hei_bian__tc.mp4");
//                    switch (currentTabID) {
//                        case 0:
//                            path = "q/C/real_exercise";
//                            title = "TCF 真题模考";
//                            break;
//                        case 1:
//                            path = "q/E/real_exercise";
//                            title = "TEF 真题模考";
//                            break;
//                        case 2:
//                            path = "q/S/real_exercise";
//                            title = "专四真题模考";
//                            break;
//                        default:
//                            break;
//                    }
//                    TestActivity.launch(MainActivity.this, path, title);
                }
                    break;
                case R.id.main_popup_window_item_3: {
                    ExoPlayerActivity.launch(MainActivity.this, "http://182.118.4.85/youku/6977D7A06AA4C824639C4A4212/0300080100570DDCE94991328DE3A0BC412887-4D54-09B6-FAC7-6E3998F0C92B.mp4");
//                    switch (currentTabID) {
//                        case 0:
//                            path = "q/C/forecast_exercise";
//                            title = "TCF 押题宝典";
//                            break;
//                        case 1:
//                            path = "q/E/forecast_exercise";
//                            title = "TEF 押题宝典";
//                            break;
//                        case 2:
//                            path = "q/S/forecast_exercise";
//                            title = "专四押题宝典";
//                            break;
//                        default:
//                            break;
//                    }
//                    TestActivity.launch(MainActivity.this, path, title);
                }
                    break;
                case R.id.main_popup_window_item_4:
                    // 练习历史
//                    VideoCourseActivity.launch(MainActivity.this);
                    ExoPlayerActivity.launch(MainActivity.this, "http://7xpe7l.com2.z0.glb.qiniucdn.com/2016/04/12/59.mp4");
                    break;
                case R.id.main_popup_window_item_5:
                    // 收藏题目
//                    SPQuestionListActivity.launch(MainActivity.this, SPQuestionListActivity.QTYPE.FAV);
                    ExoPlayerActivity.launch(MainActivity.this, "http://7xpe7l.com2.z0.glb.qiniucdn.com/2016/04/12/59_tc.mp4");
                    break;
                case R.id.main_popup_window_item_6: {
                    ExoPlayerActivity.launch(MainActivity.this, "http://27.221.23.178/youku/676FB349FF3382C64306B3953/0300080100570DAFB59D0B328DE3A070C3CC6A-2E4D-59EA-29AD-D4E60E7280D7.mp4");
//                    switch (currentTabID) {
//                        case 0:
//                            path = "q/C/exercise";
//                            title = "TCF 水平测试";
//                            break;
//                        case 1:
//                            path = "q/E/exercise";
//                            title = "TEF 水平测试";
//                            break;
//                        case 2:
//                            path = "q/S/exercise";
//                            title = "专四水平测试";
//                            break;
//                        default:
//                            break;
//                    }
//                    TestActivity.launch(MainActivity.this, path, title);
                }
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
