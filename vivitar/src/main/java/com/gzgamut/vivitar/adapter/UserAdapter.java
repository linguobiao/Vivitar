package com.gzgamut.vivitar.adapter;

import java.math.BigDecimal;
import java.util.List;
import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalculateHelper;
import com.gzgamut.vivitar.main.SettingActivity;
import com.gzgamut.vivitar.view.MyCircleImageView;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter {
	private Context mContext;
	private List<User> userList;
	private SharedPreferences sharedPreferences;

	public UserAdapter(Context context, List<User> userList) {

		mContext = context;
		this.userList = userList;
		sharedPreferences = mContext.getSharedPreferences(SettingActivity.SAVE_UNIT_NAME, mContext.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		if (userList != null) {
			return userList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (userList != null) {
			return userList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			// 按当前所需的样式，确定new的布局
			convertView = inflater.inflate(R.layout.user_item, parent, false);
			//
			holder.image_head = (MyCircleImageView) convertView.findViewById(R.id.image_head);
			// holder.tv_user_p = (TextView) convertView
			// .findViewById(R.id.tv_user_p);
			holder.tv_user = (TextView) convertView.findViewById(R.id.tv_user);
			holder.tv_gender = (TextView) convertView.findViewById(R.id.tv_gender);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			holder.tv_height = (TextView) convertView.findViewById(R.id.tv_height);
			holder.tv_cm = (TextView) convertView.findViewById(R.id.tv_cm);
			//
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// holder.tv_user_p.setText(userP[position]);
		if (userList != null && userList.size() > position) {

			if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_IN)) {
				holder.tv_cm.setText("ft");
				double height = CalculateHelper.cmToFeet(userList.get(position).getHeight());
				Log.i("height", "height=" + height);
				int ia = (int) height;
				int ib = (int) ((height * 10) % 10);
				String apped = ia + "'" + ib + "''";
				holder.tv_height.setText(apped);
			} else {
				holder.tv_height.setText(CalculateHelper.decimal(userList.get(position).getHeight()) + "");
			}

			holder.tv_user.setText(userList.get(position).getUsername());
			if (userList.get(position).getSex() == 0) {
				holder.tv_gender.setText(mContext.getString(R.string.male_user_details));
			} else {
				holder.tv_gender.setText(mContext.getString(R.string.female_user_details));
			}

			holder.tv_age.setText(userList.get(position).getAge() + "");

			Bitmap head = userList.get(position).getHead();
			if (head != null) {
				holder.image_head.setImageBitmap(head);
			} else {
				holder.image_head.setImageResource(R.drawable.image_head_big);
			}
		}

		return convertView;
	}

	public class ViewHolder {
		MyCircleImageView image_head;
		TextView tv_cm;
		TextView tv_user;
		TextView tv_gender;
		TextView tv_age;
		TextView tv_height;
	}
}
