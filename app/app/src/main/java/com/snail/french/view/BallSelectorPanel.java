package com.snail.french.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snail.french.R;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.model.exercise.Question;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class BallSelectorPanel extends LinearLayout {

	private Context mContext;
	private TextView mTitle;
	private TextView mResult;
	private int mIndex;
	private FullSizeGridView myGridView;
	private BallSelectorAdapter myAdapter;
	private OnItemClickedListener onItemClickedListener;

	public BallSelectorPanel(Context context) {
		this(context, null);
	}

	public BallSelectorPanel(Context context, AttributeSet attrs) {
		super(context, attrs);

		mContext = context;

		LayoutInflater.from(context).inflate(
				R.layout.sheet_panel_layout, this, true);
		mTitle = (TextView) findViewById(R.id.shell_panel_title);
		mResult = (TextView) findViewById(R.id.shell_panel_result);
		myGridView = (FullSizeGridView) findViewById(R.id.shell_panel_grid_view);
	}

	public void init(String titleText, String result, final int index, final ArrayList<Question> questions, boolean checkResult) {

		if(questions == null || questions.isEmpty()) {
			return;
		}

		mTitle.setText(titleText);
		mResult.setText(result);
		mIndex = index;
		myAdapter = new BallSelectorAdapter(mContext, questions, checkResult);
		myGridView.setAdapter(myAdapter);
		myGridView
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
											int position, long id) {
						if(onItemClickedListener != null) {
							onItemClickedListener.onItemClicked(questions.get(position), index, position);
						}
					}
				});
	}

	public int getIndex() {
		return mIndex;
	}


	public void setOnItemClickedListener(
			OnItemClickedListener onItemClickedListener) {
		this.onItemClickedListener = onItemClickedListener;
	}


	private static class ViewHolder {
		ImageView sheetImage;
		TextView sheetNumberView;
	}

	private class BallSelectorAdapter extends BaseAdapter {

		private LayoutInflater mLayoutInflater;

		private final boolean checkResult;
		ArrayList<Question> questions;
		Map<Integer, Integer> answerMap;

		public BallSelectorAdapter(Context context, ArrayList<Question> questions, boolean checkResult) {
			mLayoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			this.questions = questions;
			answerMap = ExerciseManager.getInstance().getAnswerMap();
			this.checkResult = checkResult;
		}

		@Override
		public int getCount() {
			return questions == null ? 0 : questions.size();
		}

		@Override
		public Object getItem(int position) {
			return questions == null ? null : questions.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			if (convertView == null) {
				convertView = mLayoutInflater.inflate(
						R.layout.sheet_panel_item, parent, false);

				holder = new ViewHolder();
				holder.sheetImage = (ImageView) convertView
						.findViewById(R.id.sheet_item_bg);
				holder.sheetNumberView = (TextView) convertView
						.findViewById(R.id.sheet_item_number);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final Question question = questions.get(position);
			holder.sheetNumberView.setText(String.valueOf(position + 1));
			if(answerMap.containsKey(question.id)) {
				if(checkResult) {
					if(question.content_data.answer_index - 1 == answerMap.get(question.id)) {
						holder.sheetImage.setImageResource(R.drawable.ico_sheet_fill_l);
						holder.sheetNumberView.setTextColor(Color.WHITE);
					} else {
						holder.sheetImage.setImageResource(R.drawable.ico_wrong_sheet_l);
						holder.sheetNumberView.setTextColor(Color.WHITE);
					}
				} else {
					holder.sheetImage.setImageResource(R.drawable.ico_sheet_fill_l);
					holder.sheetNumberView.setTextColor(Color.WHITE);
				}
			} else {
				holder.sheetImage.setImageResource(R.drawable.ico_sheet_empty_l);
				holder.sheetNumberView.setTextColor(getResources().getColor(R.color.sheet_number_color));
			}

			return convertView;
		}
	}

	public interface OnItemClickedListener {
		public void onItemClicked(Question question, int index ,int position);
	}
}
