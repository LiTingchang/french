package com.snail.french.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.fragment.TcfFragment;
import com.snail.french.fragment.TefFragment;
import com.snail.french.fragment.Tem4Fragment;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


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

    private Map<Integer, Fragment> fragmentMap = new HashMap<>();

    private int currentTabID; //

    private int [] titleIDs = {R.string.drawer_tcf, R.string.drawer_tef, R.string.drawer_tem_4};

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

        ToastUtil.shortToast(this, "personal");
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

    private void setTitleText(int id) {
        titlebar.setTitleText(titleIDs[id]);
    }

}
