package com.gzgamut.vivitar.main;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.database.DatabaseProvider;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalculateHelper;
import com.inqbarna.tablefixheaders.TableFixHeaders;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

public class UserDetailsTableActivity extends Activity {
	private ImageView iv_button_chart;
	List<Info> infos;
	private int profileID = 0;
	private Spinner spinner_table;
	private SharedPreferences sharedPreferences = null;
	private String weightUnit = "Weight/Kg";
	private List<User> userList ;
	private class NexusTypes {
		// private final String name;
		private final List<Nexus> list;

		NexusTypes() {
			// this.name = name;
			list = new ArrayList<Nexus>();
		}

		public int size() {
			return list.size();
		}

		public Nexus get(int i) {
			return list.get(i);
		}
	}

	private class Nexus {
		private final String[] data;

		private Nexus(String name, String company, String version, String api,
				String storage, String inches, String ram, String last) {
			data = new String[] { name, company, version, api, storage, inches,
					ram, last };
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_details_table);
		initUI();
		Intent intent = this.getIntent();
		if (intent.getExtras() != null) {
			profileID = intent.getExtras().getInt("chartid",-1);
				}
		// if (intent.getExtras() != null) {
		// testID = intent.getExtras().getInt(Global.KEY_USER_TYPE, 0) + 1;
		// }
		Log.i("profileID", "profileID="+profileID);
		
		spinner_table.setSelection(profileID);
		
		infos = DatabaseProvider.queryScale(UserDetailsTableActivity.this,
				 userList.get(profileID).getUserId() );
		for (int i = 0; i < infos.size(); i++) {
			Log.i("spinner_table", 
					"date="
							+ Global.sdf_0.format(infos.get(i).getDate()
									.getTime()));
		}
		TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
		BaseTableAdapter baseTableAdapter = new FamilyNexusAdapter(this, infos);
		tableFixHeaders.setAdapter(baseTableAdapter);

	}

	private void initUI() {

		sharedPreferences = getSharedPreferences(
				SettingActivity.SAVE_UNIT_NAME, MODE_PRIVATE);
		if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
			weightUnit = getString(R.string.Weight_LB);
		} else {
			weightUnit = getString(R.string.Weight_KG);
		}
		iv_button_chart = (ImageView) findViewById(R.id.iv_button_chart);
		iv_button_chart.setOnClickListener(myOnClickListener);
		Button iv_button_back = (Button) findViewById(R.id.iv_button_back);
		iv_button_back.setOnClickListener(myOnClickListener);
		//
		userList= DatabaseProvider
				.queryUserAll(UserDetailsTableActivity.this);
		
		String[] strUser = new String[userList.size()];
		for (int i = 0; i < userList.size(); i++) {
			strUser[i] = userList.get(i).getUsername();
		}

		spinner_table = (Spinner) findViewById(R.id.spinner_table);
		List<User> users = DatabaseProvider
				.queryUserAll(UserDetailsTableActivity.this);
		final String[] str = new String[users.size()];
		for (int i = 0; i < users.size(); i++) {
			str[i] = users.get(i).getUsername();
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				UserDetailsTableActivity.this, R.layout.view_checked, str) {

			@Override
			public View getDropDownView(int position, View convertView,
					ViewGroup parent) {
				LayoutInflater inflater = getLayoutInflater();
				View view = inflater.inflate(R.layout.view_spinner_item, null);
				TextView text_label = (TextView) view
						.findViewById(R.id.text_label);
				text_label.setText(str[position]);
				return view;
			}
		};

		spinner_table.setAdapter(adapter);
		spinner_table.setSelection(profileID);
		//
		spinner_table.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				profileID = arg2;

				infos = DatabaseProvider.queryScale(
						UserDetailsTableActivity.this, userList.get(arg2).getUserId());
				TableFixHeaders tableFixHeaders = (TableFixHeaders) findViewById(R.id.table);
				BaseTableAdapter baseTableAdapter = new FamilyNexusAdapter(
						UserDetailsTableActivity.this, infos);
				tableFixHeaders.setAdapter(baseTableAdapter);
				baseTableAdapter.notifyDataSetChanged();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	public class FamilyNexusAdapter extends BaseTableAdapter {

		private final NexusTypes familys[];

		private final String headers[] = { getString(R.string.Time), weightUnit, getString(R.string.bmi), "Fat/%",
				"Water/%", "Muscle/%", "Bone/Kg", "BMR/Kcal", };

		private final int[] widths = { 120, 100, 100, 0, 0, 0, 0, 0, };
		private final float density;

		public FamilyNexusAdapter(Context context, List<Info> infos) {

			familys = new NexusTypes[] { new NexusTypes() };

			density = context.getResources().getDisplayMetrics().density;
			double weight = 0;
			infos = DatabaseProvider.queryScale(UserDetailsTableActivity.this,
					 userList.get(profileID).getUserId());
			for (int i = 0; i < infos.size(); i++) {

				if (sharedPreferences.getString(Global.UNIT, "").equals(
						Global.UNIT_LB)) {
					double f = CalculateHelper
							.kgToLbs(infos.get(i).getWeight());
					BigDecimal b = new BigDecimal(f);
					double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP)
							.doubleValue();
					weight = f1;
				} else {
					weight = infos.get(i).getWeight();
				}

				familys[0].list.add(new Nexus(Global.sdf_1.format(infos.get(i)
						.getDate().getTime())
						+ "", weight + "", infos.get(i).getBmi() + "", infos
						.get(i).getFat() + "", infos.get(i).getWater() + "",
						infos.get(i).getMuscle() + "", infos.get(i).getBone()
								+ "", infos.get(i).getKcal() + ""));
			}

		}

		@Override
		public int getRowCount() {
			return infos.size() + 1;
		}

		@Override
		public int getColumnCount() {
			return 7;
		}

		@Override
		public View getView(int row, int column, View convertView,
				ViewGroup parent) {
			final View view;
			switch (getItemViewType(row, column)) {
			case 0:
				view = getFirstHeader(row, column, convertView, parent);
				break;
			case 1:
				view = getHeader(row, column, convertView, parent);
				break;
			case 2:
				view = getFirstBody(row, column, convertView, parent);
				break;
			case 3:
				view = getBody(row, column, convertView, parent);
				break;
			case 4:
				view = getFamilyView(row, column, convertView, parent);
				break;
			default:
				throw new RuntimeException("wtf?");
			}
			return view;
		}

		private View getFirstHeader(int row, int column, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.item_table_header_first, parent, false);
			}
			((TextView) convertView.findViewById(android.R.id.text1))
					.setText(headers[0]);
			return convertView;
		}

		private View getHeader(int row, int column, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.item_table_header, parent, false);
			}
			((TextView) convertView.findViewById(android.R.id.text1))
					.setText(headers[column + 1]);
			return convertView;
		}

		private View getFirstBody(final int row, int column, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.item_table_first, parent, false);
			}
			// convertView
			// .setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1
			// : R.drawable.bg_table_color2);
			((TextView) convertView.findViewById(android.R.id.text1))
					.setText(getDevice(row).data[column + 1]);

			RelativeLayout layout_click = (RelativeLayout) convertView
					.findViewById(R.id.layout_click);

			layout_click.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View arg0) {

					dialog = new AlertDialog.Builder(
							UserDetailsTableActivity.this).show();
					dialog.setContentView(R.layout.goal_weight_dialog);
					
					Log.i("myid", "	getpUser()="+infos.get(row - 1)
									.getpUser());
					
					User user = DatabaseProvider.queryUser(
							UserDetailsTableActivity.this, infos.get(row - 1)
									.getpUser());
					
					TextView tv_date_show = (TextView) dialog
							.findViewById(R.id.tv_date_show);
					String date = Global.sdf_1.format(infos.get(row - 1)
							.getDate().getTime());
					tv_date_show.setText(date);

					TextView tv_dialog_weight = (TextView) dialog
							.findViewById(R.id.tv_dialog_weight);

					TextView tv_dialog_bmi = (TextView) dialog
							.findViewById(R.id.tv_dialog_bmi);
					tv_dialog_bmi.setText("" + infos.get(row - 1).getBmi());
					TextView tv_current_weight = (TextView) dialog
							.findViewById(R.id.tv_current_weight);
					TextView tv_gold = (TextView) dialog
							.findViewById(R.id.tv_gold);
					//
					tv_goal_weightunit = (TextView) dialog
							.findViewById(R.id.tv_goal_weightunit);
					tv_currentunit = (TextView) dialog
							.findViewById(R.id.tv_currentunit);
					tv_goalweightunit = (TextView) dialog
							.findViewById(R.id.tv_goalweightunit);
					tv_balanceunit = (TextView) dialog
							.findViewById(R.id.tv_balanceunit);
					TextView tv_banlance_goal = (TextView) dialog
							.findViewById(R.id.tv_banlance_goal);

					if (sharedPreferences.getString(Global.UNIT, "").equals(
							Global.UNIT_LB)) {
						double demo = CalculateHelper.kgToLbs(infos
								.get(row - 1).getWeight());
						BigDecimal bdemo = new BigDecimal(demo);
						double f0 = bdemo.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						tv_dialog_weight.setText(f0 + "");
						tv_goal_weightunit.setText("Lb");

						Log.i("data", "f0=" + f0 + "---");

						double kgtolb = CalculateHelper.kgToLbs(infos.get(
								row - 1).getWeight());
						BigDecimal ba = new BigDecimal(kgtolb);
						double f2 = ba.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						tv_current_weight.setText(f2 + "");
						tv_currentunit.setText("Lb");

						double kgtolbs = CalculateHelper.kgToLbs(user
								.getGoalWeight());
						BigDecimal baa = new BigDecimal(kgtolbs);
						double f3 = baa.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();
						tv_gold.setText(f3 + "");

						tv_goalweightunit.setText("Lb");
						tv_balanceunit.setText("Lb");

						double banlGoal = infos.get(row - 1).getWeight()
								- user.getGoalWeight();
						BigDecimal baaa = new BigDecimal(CalculateHelper
								.kgToLbs(banlGoal));

						double f4 = baaa.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();

						tv_banlance_goal.setText(f4 + "");

					} else {
						tv_dialog_weight.setText(infos.get(row - 1).getWeight()
								+ "");
						tv_current_weight.setText(infos.get(row - 1)
								.getWeight() + "");

						tv_gold.setText(user.getGoalWeight() + "");

						double banlGoal = infos.get(row - 1).getWeight()
								- user.getGoalWeight();
						BigDecimal baaa = new BigDecimal(banlGoal);

						double f5 = baaa.setScale(2, BigDecimal.ROUND_HALF_UP)
								.doubleValue();

						tv_banlance_goal.setText(f5 + "");

					}

					Log.i("user",
							infos.get(row - 1).getWeight() + "----"
									+ infos.get(row - 1).getBmi() + "----"
									+ user.getGoalWeight() + "--");

					// 删除
					Button but_delete = (Button) dialog
							.findViewById(R.id.but_delete);
					but_delete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							DatabaseProvider.deleteScale(
									UserDetailsTableActivity.this,
									infos.get(row - 1).getDate());
							infos.remove(row - 1);
							notifyDataSetChanged();
							dialog.dismiss();
						}
					});

					return false;
				}
			});
			return convertView;
		}

		private View getBody(int row, int column, View convertView,
				ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(R.layout.item_table,
						parent, false);
			}
			// convertView
			// .setBackgroundResource(row % 2 == 0 ? R.drawable.bg_table_color1
			// : R.drawable.bg_table_color2);
			((TextView) convertView.findViewById(android.R.id.text1))
					.setText(getDevice(row).data[column + 1]);
			return convertView;
		}

		private View getFamilyView(int row, int column, View convertView,
				ViewGroup parent) {
			if (convertView == null) {

				convertView = getLayoutInflater().inflate(
						R.layout.item_table_family, parent, false);
			}
			// final String string;
			// if (column == -1) {
			// string = getFamily(row).name;
			// } else {
			// string = "";
			// }
			// ((TextView) convertView.findViewById(android.R.id.text1))
			// .setText(string);
			return convertView;
		}

		@Override
		public int getWidth(int column) {
			return Math.round(widths[column + 1] * density);
		}

		@Override
		public int getHeight(int row) {
			final int height;
			if (row == -1) {
				height = 35;
			} else if (isFamily(row)) {
				height = 25;
			} else {
				height = 45;
			}
			return Math.round(height * density);
		}

		@Override
		public int getItemViewType(int row, int column) {
			final int itemViewType;
			if (row == -1 && column == -1) {
				itemViewType = 0;
			} else if (row == -1) {
				itemViewType = 1;
			} else if (isFamily(row)) {
				itemViewType = 4;
			}

			else if (column == -1) {
				itemViewType = 2;
			} else {
				itemViewType = 3;
			}
			return itemViewType;
		}

		private boolean isFamily(int row) {
			int family = 0;
			while (row > 0) {
				row -= familys[family].size() + 1;
				family++;
			}
			return row == 0;
		}

		private NexusTypes getFamily(int row) {
			int family = 0;
			while (row >= 0) {
				row -= familys[family].size() + 1;
				family++;
			}
			return familys[family - 1];
		}

		private Nexus getDevice(int row) {
			int family = 0;
			while (row >= 0) {
				row -= familys[family].size() + 1;
				family++;
			}
			family--;
			return familys[family].get(row + familys[family].size());
		}

		@Override
		public int getViewTypeCount() {
			return 5;
		}
	}

	// ---------------------------------------------------------------
	public OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_button_chart:
				Intent i = new Intent(UserDetailsTableActivity.this,
						ChartActivity.class);
				i.putExtra(Global.KEY_USER_TYPE, profileID);
				startActivity(i);
				finish();
				break;
			case R.id.iv_button_back:
				finish();
				break;

			default:
				break;
			}
		}
	};
	//
	private AlertDialog dialog;
	private TextView tv_goal_weightunit, tv_currentunit, tv_goalweightunit,
			tv_balanceunit;
}
