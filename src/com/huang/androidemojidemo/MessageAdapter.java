package com.huang.androidemojidemo;

import java.util.ArrayList;
import java.util.List;

import com.example.androidemojidemo.R;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private static final String TAG = MessageAdapter.class.getSimpleName();

	private static final int MSG_TYPE_MY = 0;
	private static final int MSG_TYPE_OTHER = 1;

	private LayoutInflater mLayoutInflater;
	private List<Message> mData = new ArrayList<Message>();
	private Context mContext;

	public MessageAdapter(Context mContext, List<Message> data) {
		this.mContext = mContext;
		this.mLayoutInflater = LayoutInflater.from(mContext);
		this.mData = data;
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		if (position < 0 || position > getCount() - 1) {
			return null;
		}
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		Message msg = mData.get(position);
		int msgType;

		if (msg.isSend()) {
			msgType = MSG_TYPE_MY;
		} else {
			msgType = MSG_TYPE_OTHER;
		}

		if (convertView == null) {
			holder = new ViewHolder();
			if (msgType == MSG_TYPE_MY) {
				convertView = mLayoutInflater.inflate(R.layout.chatting_item_to, null);
				findViewById(convertView, holder);
			} else {
				convertView = mLayoutInflater.inflate(R.layout.chatting_item_from, null);
				findViewById(convertView, holder);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		initMessageView(position, holder);
		return convertView;
	}

	private void initMessageView(int position, ViewHolder holder) {
		final Message msg = mData.get(position);
		holder.contentTv.setVisibility(View.VISIBLE);
		holder.contentTv.setTag(msg.getContent());
		String unicode = EmojiParser.getInstance(mContext).parseEmoji(msg.getContent());
		SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(mContext, unicode);
		holder.contentTv.setText(spannableString);
		holder.timeTv.setText(mData.get(position).getTime());
	}

	private void findViewById(View convertView, ViewHolder holder) {
		holder.contentTv = (TextView) convertView.findViewById(R.id.content);
		holder.timeTv = (TextView) convertView.findViewById(R.id.chatting_time_tv);
	}

	public void setDataSet(List<Message> data) {
		this.mData=data;
	}

	class ViewHolder {

		TextView contentTv;
		TextView timeTv;

	}
}
