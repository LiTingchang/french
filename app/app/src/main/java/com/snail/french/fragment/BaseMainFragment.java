package com.snail.french.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.snail.french.R;
import com.snail.french.temp.DetailItem;
import com.snail.french.temp.DetailManager;
import com.snail.french.temp.OptionItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-11.
 */
public abstract class BaseMainFragment extends Fragment {


    @Bind(R.id.list_view)
    ExpandableListView listView;

    View headerView;

    List<DetailItem> detailItems;
    DetailAdapter adapter;


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
        listView.addHeaderView(headerView);


        listView.setGroupIndicator(null);

        detailItems = new ArrayList<DetailItem>();

//        for ( int i = 0; i < 4; i++ )
//        {
//            DetailItem detailItem = new DetailItem();
//            detailItem.date = new Date();
//            detailItem.optionItemList = DetailManager.getBodyOptionItem();
//
//            detailItems.add(detailItem);
//        }

        DetailItem detailItem = new DetailItem();
        detailItem.name = "听力";
        detailItem.total = 300;
        detailItem.test = 39;
        detailItem.level = 5;
        detailItem.progress = 0.5f;
        detailItem.optionItemList = DetailManager.getBodyOptionItem();
        detailItems.add(detailItem);

        DetailItem detailItem2 = new DetailItem();
        detailItem2.name = "语法";
        detailItem2.total = 200;
        detailItem2.test = 77;
        detailItem2.level = 3;
        detailItem2.progress = 0.5f;
        detailItem2.optionItemList = DetailManager.getBodyOptionItem();
        detailItems.add(detailItem2);

        DetailItem detailItem3 = new DetailItem();
        detailItem3.name = "阅读";
        detailItem3.total = 270;
        detailItem3.test = 30;
        detailItem3.level = 4;
        detailItem3.progress = 0.5f;
        detailItem3.optionItemList = DetailManager.getBodyOptionItem();
        detailItems.add(detailItem3);

        adapter = new DetailAdapter(getContext(), detailItems);

        listView.setAdapter(adapter);

//        for ( int i = 0; i < adapter.getGroupCount(); i++ )
//        {
//            listView.expandGroup(i);
//        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
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


    private static class GroupViewHolder {
        TextView title;
        RatingBar ratingBar;
        ProgressBar progressBar;
        TextView count;
    }

    private static class ChildViewHolder {
        TextView title;
        RatingBar ratingBar;
        ProgressBar progressBar;
    }

    class DetailAdapter extends BaseExpandableListAdapter {

        private List<DetailItem> detailItems;
        private LayoutInflater inflater;

        public DetailAdapter(Context context, List<DetailItem> detailTtems) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.detailItems = detailTtems;

        }

        @Override
        public Object getChild( int groupPosition, int childPosition )
        {
            return detailItems.get(groupPosition).optionItemList.get(childPosition);
        }

        @Override
        public long getChildId( int groupPosition, int childPosition )
        {
            return childPosition;
        }

        @Override
        public View getChildView( int groupPosition, int childPosition, boolean isLastChild,
                                  View convertView, ViewGroup parent )
        {
            ChildViewHolder childViewHolder = null;

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_child, parent, false);

                childViewHolder = new ChildViewHolder();

                childViewHolder.title= (TextView) convertView.findViewById(R.id.child_title);
                childViewHolder.ratingBar= (RatingBar) convertView.findViewById(R.id.child_rating);
                childViewHolder.progressBar= (ProgressBar) convertView.findViewById(R.id.child_progress);

                convertView.setTag(childViewHolder);
            } else {
                childViewHolder = (ChildViewHolder) convertView.getTag();
            }

            final OptionItem optionItem = (OptionItem) getChild(groupPosition, childPosition);
            childViewHolder.title.setText(optionItem.name);
            childViewHolder.ratingBar.setRating(optionItem.level);
            childViewHolder.progressBar.setProgress((int) (100 * optionItem.test / optionItem.total));

            return convertView;
        }

        @Override
        public int getChildrenCount( int groupPosition )
        {
            return detailItems.get(groupPosition).optionItemList.size();
        }

        @Override
        public Object getGroup( int groupPosition )
        {
            return detailItems.get(groupPosition);
        }

        @Override
        public int getGroupCount()
        {
            return detailItems.size();
        }

        @Override
        public long getGroupId( int groupPosition )
        {
            return groupPosition;
        }

        @Override
        public View getGroupView( int groupPosition, boolean isExpanded, View convertView,
                                  ViewGroup parent )
        {
            GroupViewHolder groupViewHolder = null;

            if(convertView == null) {
                groupViewHolder = new GroupViewHolder();

                convertView = inflater.inflate(R.layout.list_item_parent, parent, false);

                groupViewHolder.title = (TextView) convertView.findViewById(R.id.parent_title);
                groupViewHolder.ratingBar = (RatingBar) convertView.findViewById(R.id.parent_rating);
                groupViewHolder.progressBar = (ProgressBar) convertView.findViewById(R.id.parent_progress);
                groupViewHolder.count = (TextView) convertView.findViewById(R.id.parent_count);

                convertView.setTag(groupViewHolder);
            } else {
                groupViewHolder = (GroupViewHolder) convertView.getTag();
            }

            final DetailItem item = (DetailItem) getGroup(groupPosition);
            groupViewHolder.title.setText(item.name);
            groupViewHolder.ratingBar.setRating(item.level);
            groupViewHolder.progressBar.setProgress((int) (100 * item.test / item.total));
            groupViewHolder.count.setText(item.test + "/" + item.total);

            return convertView;
        }

        @Override
        public boolean hasStableIds()
        {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable( int groupPosition, int childPosition )
        {
            // TODO Auto-generated method stub
            return false;
        }

    }
}
