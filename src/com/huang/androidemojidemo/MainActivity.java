package com.huang.androidemojidemo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.androidemojidemo.R;
import com.huang.androidemojidemo.SelectFaceHelper.OnFaceOprateListener;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();

	private List<Message> mData = new ArrayList<Message>();
	private ListView mChattinglv;
	private MessageAdapter mAdapter;
	private SelectFaceHelper mFaceHelper;
	private Button mFaceBtn;
	private View addFaceToolView;
	private boolean isVisbilityFace;
	private EditText mEditMessageEt;
	private Button mSentBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mChattinglv = (ListView) findViewById(R.id.emoji_lv);

		initData();
		mAdapter = new MessageAdapter(MainActivity.this, mData);
		mChattinglv.setAdapter(mAdapter);
		addFaceToolView = (View) findViewById(R.id.add_tool);
		mEditMessageEt = (EditText) findViewById(R.id.txtMessage);
		mSentBtn = (Button) findViewById(R.id.btnSend);
		mFaceBtn = (Button) findViewById(R.id.btn_to_face);
		mFaceBtn.setOnClickListener(faceClick);
		mSentBtn.setOnClickListener(sendClick);
		mEditMessageEt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				isVisbilityFace = false;
				addFaceToolView.setVisibility(View.GONE);
				return false;
			}
		});
	}

	private void initData() {
		for (int i = 0; i < 5; i++) {
			Message msg = new Message();
			msg.setContent("测试emoji" + i);
			msg.setSend((i % 2 == 0) ? true : false);
			msg.setTime(Util.getDate());
			mData.add(msg);
		}
	}

	View.OnClickListener faceClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (null == mFaceHelper) {
				mFaceHelper = new SelectFaceHelper(MainActivity.this, addFaceToolView);
				mFaceHelper.setFaceOpreateListener(mOnFaceOprateListener);
			}
			if (isVisbilityFace) {
				isVisbilityFace = false;
				addFaceToolView.setVisibility(View.GONE);
			} else {
				isVisbilityFace = true;
				addFaceToolView.setVisibility(View.VISIBLE);
				hideInputManager(MainActivity.this);
			}
		}
	};
	View.OnClickListener sendClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String msg = mEditMessageEt.getText().toString();
			if (msg.equals("")) {
				return;
			}
			Message mMessage = new Message();
			String msgStr = ParseEmojiMsgUtil.convertToMsg(mEditMessageEt.getText(), MainActivity.this);// 这里不要直接用mEditMessageEt.getText().toString();
		    System.out.println(msgStr);
			mMessage.setContent(msgStr);
			mMessage.setSend(true);
			mMessage.setTime(Util.getDate());
			mData.add(mMessage);
			mAdapter.setDataSet(mData);
			mAdapter.notifyDataSetChanged();
			mEditMessageEt.setText("");
		}
	};
	OnFaceOprateListener mOnFaceOprateListener = new OnFaceOprateListener() {
		@Override
		public void onFaceSelected(SpannableString spanEmojiStr) {
			if (null != spanEmojiStr) {
				mEditMessageEt.append(spanEmojiStr);
			}
		}

		@Override
		public void onFaceDeleted() {
			int selection = mEditMessageEt.getSelectionStart();
			String text = mEditMessageEt.getText().toString();
			if (selection > 0) {
				String text2 = text.substring(selection - 1);
				if ("]".equals(text2)) {
					int start = text.lastIndexOf("[");
					int end = selection;
					mEditMessageEt.getText().delete(start, end);
					return;
				}
				mEditMessageEt.getText().delete(selection - 1, selection);
			}
		}

	};

	public void onBackPressed() {
		if (isVisbilityFace) {// 好吧,隐藏表情菜单再退出
			isVisbilityFace = false;
			addFaceToolView.setVisibility(View.GONE);
			return;
		}
		finish();

	};

	// 隐藏软键盘
	public void hideInputManager(Context ct) {
		try {
			((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(((Activity) ct)
					.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
			Log.e(TAG, "hideInputManager Catch error,skip it!", e);
		}
	}
}
