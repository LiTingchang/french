package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.model.GuideItem;
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.view.CommonTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class GuideActivity extends BaseActivity {

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.guide_list_view)
    ListView guideListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(this))
                .get("guidance", null, new TypeReference<ArrayList<GuideItem>>() {
                }.getType(), new StickerHttpResponseHandler<ArrayList<GuideItem>>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ArrayList<GuideItem> response) {
                        guideListView.setAdapter(new GuideAdapter(GuideActivity.this, response));
                    }

                    @Override
                    public void onFailure(String message) {

                    }

                    @Override
                    public void onFinish() {

                    }
                });
    }

    private static class ViewHolder {
        TextView title;
    }


    class GuideAdapter extends BaseAdapter {

        private List<GuideItem> data;
        private Context cxt;

        public GuideAdapter(Context cxt, List<GuideItem> data) {
            this.data = data;
            this.cxt = cxt;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(cxt).inflate(R.layout.guide_list_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.guide_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            GuideItem item = data.get(position);
            holder.title.setText(item.title);

            return convertView;
        }
    }


    public static void launch(Context context) {
        Intent intent = new Intent(context, GuideActivity.class);
        context.startActivity(intent);
    }

}
