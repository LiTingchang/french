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
import com.snail.french.model.status.PItem;
import com.snail.french.model.status.Status;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.utils.JsonParseUtil;
import com.snail.french.utils.StringUtils;

import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-11.
 */
public abstract class BaseMainFragment extends Fragment {


    @Bind(R.id.list_view)
    ExpandableListView listView;

    private View headerView;
    private TextView forcastScore; // 预测分数
    private TextView exerciseQuestionNumber; //已做题数
    private TextView exerciseDays; // 联系天数
    private TextView levael; //预测等级
    private View smartTest;

    private String kind;


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

        initData();

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    abstract public void initData();
    abstract public FrenchKind getKind();

    public void setData(final String kind, final StatusResponse statusResponse) {

        this.kind = kind;

        forcastScore.setText(String.valueOf(statusResponse.forcast_score));
        exerciseQuestionNumber.setText(getResources().getString(R.string.exercise_question_number, statusResponse.exercise_question_number));
        exerciseDays.setText(getResources().getString(R.string.exercise_days, statusResponse.exercise_days));
        levael.setText(statusResponse.level);

        smartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String path = "";
                String title = "";
                switch (getKind()) {
                    case TCF:
                        path = "q/C/exercise";
                        title = "TCF 水水平测试";
                        break;
                    case TEF:
                        path = "q/E/exercise";
                        title = "TEF 水水平测试";
                        break;
                    case TEM4:
                        path = "q/S/exercise";
                        title = "专四水水平测试";
                        break;
                    default:
                        break;
                }
                TestActivity.launch(getActivity(), path, title);
            }
        });

        listView.setAdapter(new DetailAdapter(getContext(), statusResponse));

//        listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView expandableListView, View view,
//                                        int groupPosition, long id) {
//
//                Log.e("eeee", "g:" + groupPosition);
//                String path = kind + "/" + statusResponse.pItemList.get(groupPosition).name;
//                Log.e("eeee", "path:" + path);
//                return false;
//            }
//        });

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view,
                                        int groupPosition, int childPosition, long id) {

                Log.e("eeee", "g:" + groupPosition + "    child:" + childPosition);
                PItem pItem = statusResponse.pItemList.get(groupPosition);
                Status status = pItem.statusList.get(childPosition);
                String path = kind + "/" + pItem.name +
                        "/" + status.type;
                Log.e("eeee", "path:" + path);
                Log.e("eeee", "level:" + status.subType);

                TestActivity.launch(getActivity(), buildPath(path, status.subType), pItem.name + "水平测试");
                return false;
            }
        });
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
            // TODO 显示对应的title文案
            String title = StringUtils.isEmpty(status.subType) ? status.type : status.subType;
            childViewHolder.title.setText(title);
            childViewHolder.ratingBar.setRating(status.correct_num / status.total_quesstion_num * 5);
            childViewHolder.progressBar.setProgress((int) (100 * status.exercise_question_number / status.total_quesstion_num));
            childViewHolder.count.setText(status.exercise_question_number + "/" + status.total_quesstion_num);
            return convertView;
        }

        @Override
        public int getChildrenCount( int groupPosition ) {
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
            groupViewHolder.title.setText(item.name);
            groupViewHolder.ratingBar.setRating(item.correct_num / item.total_quesstion_num * 5);
            groupViewHolder.progressBar.setProgress((int) (100 * item.exercise_question_number / item.total_quesstion_num));
            groupViewHolder.count.setText(item.exercise_question_number + "/" + item.total_quesstion_num);

            groupViewHolder.doTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String path = kind + "/" + item.name;
                    Log.e("eeee", "path:" + path);
                    TestActivity.launch(getActivity(), buildPath(path, null), item.name + "水平测试");
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
