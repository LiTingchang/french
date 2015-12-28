package com.snail.french.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.snail.french.R;
import com.snail.french.activity.MainActivity;
import com.snail.french.activity.TestActivity;
import com.snail.french.constant.FrenchKind;
import com.snail.french.constant.NameConstants;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.model.status.PItem;
import com.snail.french.model.status.Status;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.utils.JsonParseUtil;
import com.snail.french.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.jar.Attributes;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-11.
 */
public abstract class BaseMainFragment extends Fragment {

    private boolean isShow = false;

    @Bind(R.id.list_view)
    ExpandableListView listView;

    private View headerView;
    private TextView forcastScore; // 预测分数
    private TextView exerciseQuestionNumber; //已做题数
    private TextView exerciseDays; // 联系天数
    private TextView levael; //预测等级
    private View smartTest;

    private StatusResponse mStatusResponse;
    private DetailAdapter mDetailAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        ButterKnife.bind(this, view);

        headerView = LayoutInflater.from(getContext()).inflate(R.layout.main_fragment_header, null, false);
        forcastScore = (TextView) headerView.findViewById(R.id.forcast_score); // 预测分数
        exerciseQuestionNumber = (TextView) headerView.findViewById(R.id.exercise_question_number); //已做题数
        exerciseDays = (TextView) headerView.findViewById(R.id.exercise_days); // 联系天数
        levael = (TextView) headerView.findViewById(R.id.level);
        smartTest = headerView.findViewById(R.id.smart_test);

        listView.setGroupIndicator(null);
        listView.addHeaderView(headerView);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getKind().getKind());

        if(isShow) {
            initData();
            Log.e("aaaaaaaaaaaaa", "initdata" + getKind().getKind());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getKind().getKind());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    abstract public void initData();
    abstract public FrenchKind getKind();

    public void setData(final StatusResponse statusResponse) {

        ExerciseManager.getInstance().setFrenchKind(getKind());

        mStatusResponse = statusResponse;

        if(!isAdded()) {
            return;
        }

        try {
            forcastScore.setText(String.valueOf(mStatusResponse.forcast_score));
            exerciseQuestionNumber.setText(getResources().getString(R.string.exercise_question_number, mStatusResponse.exercise_question_number));
            exerciseDays.setText(getResources().getString(R.string.exercise_days, mStatusResponse.exercise_days));
            levael.setText(mStatusResponse.level);

            smartTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = "";
                    String title = "";
                    switch (getKind()) {
                        case TCF:
                            path = "q/C/exercise";
                            title = "TCF 水平测试";
                            break;
                        case TEF:
                            path = "q/E/exercise";
                            title = "TEF 水平测试";
                            break;
                        case TEM4:
                            path = "q/S/exercise";
                            title = "专四水平测试";
                            break;
                        default:
                            break;
                    }
                    TestActivity.launch(getActivity(), path, title);
                }
            });

            if(mDetailAdapter == null) {
                mDetailAdapter = new DetailAdapter(getContext(), mStatusResponse);
                listView.setAdapter(mDetailAdapter);

                listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView expandableListView, View view,
                                                int groupPosition, int childPosition, long id) {

                        PItem pItem = statusResponse.pItemList.get(groupPosition);
                        Status status = pItem.statusList.get(childPosition);
                        String path = getKind().getKind() + "/" + pItem.name +
                                "/" + status.type;

                        TestActivity.launch(getActivity(), buildPath(path, status.subType), NameConstants.getName(pItem.name));
                        return false;
                    }
                });
            } else {
                mDetailAdapter.notifyDataSetChanged();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    private static class GroupViewHolder {
        TextView title;
        RatingBar ratingBar;
        ProgressBar progressBar;
        TextView count;
        View doTest;
    }

    private static class ChildViewHolder {
        TextView title;
        RatingBar ratingBar;
        ProgressBar progressBar;
        TextView count;
    }

    class DetailAdapter extends BaseExpandableListAdapter {

        private StatusResponse statusResponses;
        private LayoutInflater inflater;

        public DetailAdapter(Context context, StatusResponse statusResponse) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.statusResponses = statusResponse;
        }

        @Override
        public Object getChild( int groupPosition, int childPosition ) {
            return statusResponses.pItemList.get(groupPosition).statusList.get(childPosition);
        }

        @Override
        public long getChildId( int groupPosition, int childPosition ) {
            return childPosition;
        }

        @Override
        public View getChildView( int groupPosition, int childPosition, boolean isLastChild,
                                  View convertView, ViewGroup parent ) {
            ChildViewHolder childViewHolder = null;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_child, parent, false);

                childViewHolder = new ChildViewHolder();

                childViewHolder.title= (TextView) convertView.findViewById(R.id.child_title);
                childViewHolder.ratingBar= (RatingBar) convertView.findViewById(R.id.child_rating);
                childViewHolder.progressBar= (ProgressBar) convertView.findViewById(R.id.child_progress);
                childViewHolder.count = (TextView) convertView.findViewById(R.id.child_count);

                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

            final Status status = (Status) getChild(groupPosition, childPosition);
            String title = NameConstants.getName(StringUtils.isEmpty(status.subType) ? status.type : status.subType);
            childViewHolder.title.setText(title);
            childViewHolder.ratingBar.setRating(countRating(status.correct_num, status.total_quesstion_num));
            childViewHolder.progressBar.setProgress((int) (100 * status.exercise_question_number / status.total_quesstion_num));
            childViewHolder.count.setText(status.exercise_question_number + "/" + status.total_quesstion_num);
            return convertView;
        }

        @Override
        public int getChildrenCount( int groupPosition ) {

            // 专四 阅读 作文 不展开
            if(getKind() == FrenchKind.TEM4) {
                String pName = statusResponses.pItemList.get(groupPosition).name;
                if("R".equals(pName) || "C".equals(pName)) {
                    return 0;
                }
            }

            return statusResponses.pItemList.get(groupPosition).statusList.size();
        }



        @Override
        public Object getGroup( int groupPosition ) {
            return statusResponses.pItemList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return statusResponses.pItemList.size();
        }

        @Override
        public long getGroupId( int groupPosition ) {
            return groupPosition;
        }

        @Override
        public View getGroupView( int groupPosition, boolean isExpanded, View convertView,
                                  ViewGroup parent )  {
            GroupViewHolder groupViewHolder = null;

            if(convertView == null) {
                groupViewHolder = new GroupViewHolder();

                convertView = inflater.inflate(R.layout.list_item_parent, parent, false);

                groupViewHolder.title = (TextView) convertView.findViewById(R.id.parent_title);
                groupViewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.parent_rating);
                groupViewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.parent_progress);
                groupViewHolder.count = (TextView) convertView.findViewById(R.id.parent_count);

                groupViewHolder.doTest = convertView.findViewById(R.id.parent_test);


                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }

            final PItem item = (PItem) getGroup(groupPosition);
            groupViewHolder.title.setText(NameConstants.getName(item.name));

            groupViewHolder.ratingBar.setRating(countRating(item.correct_num , item.total_quesstion_num));
            groupViewHolder.progressBar.setProgress((int) (100 * item.exercise_question_number / item.total_quesstion_num));
            groupViewHolder.count.setText(item.exercise_question_number + "/" + item.total_quesstion_num);

            groupViewHolder.doTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = getKind().getKind() + "/" + item.name;
                    TestActivity.launch(getActivity(), buildPath(path, null), NameConstants.getName(item.name));
                }
            });

            return convertView;
        }

        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable( int groupPosition, int childPosition ) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    private int countRating(int number, int total) {

        int rating = 0;
        rating = number * 5 / total;
        if(rating == 0 && number > 0) {
            rating = 1;
        }

        return rating;
    }

    private String buildPath(String path, String level) {
        String action;
        if(!StringUtils.isEmpty(level)) {
            action = "q/" + path + "/exercise?q_tcf_level=" + level;
        } else {
            action = "q/" + path + "/exercise";
        }

        return action;
    }
}
