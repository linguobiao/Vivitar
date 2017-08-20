package com.gzgamut.vivitar.main;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.gzgamut.vivitar.R;
import com.gzgamut.vivitar.adapter.HomeAdapter;
import com.gzgamut.vivitar.adapter.UserAdapter;
import com.gzgamut.vivitar.been.Event;
import com.gzgamut.vivitar.been.Info;
import com.gzgamut.vivitar.been.User;
import com.gzgamut.vivitar.been.Value;
import com.gzgamut.vivitar.database.DatabaseProvider;
import com.gzgamut.vivitar.global.Global;
import com.gzgamut.vivitar.helper.CalculateHelper;
import com.gzgamut.vivitar.helper.CalendarHelper;
import com.gzgamut.vivitar.helper.ParserHelper;
import com.gzgamut.vivitar.helper.ShowValueHelper;
import com.gzgamut.vivitar.helper.SyncHelper;
import com.gzgamut.vivitar.logger.Logger;
import com.gzgamut.vivitar.service.InnofitService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends Activity {

	private String TAG = "MainActivity";
	private InnofitService mService = null;
	private BluetoothAdapter mBtAdapter = null;
	private PopupWindow popupWindow;
	private Calendar date_query;
	private double height = Global.DEFAULT_HEIGHT;
	private int profileID = -1;
	private List<User> listUser;
	private SharedPreferences sharedPreferences = null;
	private List<User> userList;
	Handler handler = null;
	private Boolean Judge = true;

	// Timer timer = new Timer();
	private static double[] Value = new double[2];

	private Handler myHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			Calendar cal_now = Calendar.getInstance();
			if (msg.what == Global.HANDLE_NUMBER1) {

				if (info != null) {
					saveInfo(msg.arg1, info, cal_now);
					// 断开连接
					// mService.disconnect();
					Judge = true;
					// beginScanDevice();
				}
				if (dialogNoUser != null) {
					dialogNoUser.dismiss();
					isShowDialog = false;
				}
				if (dialog != null) {
					dialog.dismiss();
					isShowDialog = false;
				}

			}
			// dialog.isShowing()
			dialog = null;
			text_weight.setText("00.0");
			text_BMI.setText("00.0");
			state = Global.TYPE_RECEIVE_DATA_NO;
			initList(false);

		}

	};
	private Context context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		EventBus.getDefault().register(this);
		initUI();
		Value[0] = 0;
		Value[1] = 0;
		initServiceConnection();
		initBroadcastReceiver();
	}

	private void initBroadcastReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(InnofitService.ACTION_DEVICE_FOUND);
		intentFilter.addAction(InnofitService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(InnofitService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(InnofitService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(InnofitService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
		this.registerReceiver(myBLEBroadcastReceiver, intentFilter);
	}

	/**
	 * my BLE BroadcastReceiver
	 */
	private BroadcastReceiver myBLEBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 找到设备
			if (action.equals(InnofitService.ACTION_DEVICE_FOUND)) {

				Bundle data = intent.getExtras();
				BluetoothDevice device = data.getParcelable(BluetoothDevice.EXTRA_DEVICE);

				if (device != null) {
					String deviceName = device.getName();
					String deviceAddress = device.getAddress();
					Log.i(TAG, "device name = " + deviceName + "-------------deviceAddress=" + deviceAddress);
					if ((deviceName != null) && (deviceAddress != null)) {

						if (deviceName.contains(Global.DEVICE_NAME_ELECSCALESBH)) {
							if (mService != null) {
								mService.scan(false);
								mService.connect(deviceAddress, false);
							}
						}
					}
				}
			}
			// 连接上设备
			// /////////////////////////////////////////////////////////
			else if (action.equals(InnofitService.ACTION_GATT_CONNECTED)) {
				// DialogHelper.hideDialog(dialog_connecting); if
				// (timer_stop_scan != null) { timer_stop_scan.cancel(); }
				image_bluetoothconnect.setImageResource(R.drawable.first_blue_tooth_true);
			}
			// 连接失败
			// /////////////////////////////////////////////////////////
			/*
			 * else if
			 * (action.equals(MyFitnessService.ACTION_GATT_CONNECTED_FAIL)) {
			 * myHandler.sendEmptyMessage(Global.HANDLER_SCAN_TIME_OUT); }
			 */
			// 断开设备
			else if (action.equals(InnofitService.ACTION_GATT_DISCONNECTED)) {
				text_weight.setText("00.0");
				text_BMI.setText("00.0");
				connectBluetooth();
				// if (timer != null) {
				// timer.schedule(task, 2000);
				// }
				// try {
				// Thread.sleep(3000);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// beginScanDevice();
				image_bluetoothconnect.setImageResource(R.drawable.btn_blue_selected);
				state = Global.TYPE_RECEIVE_DATA_NO;
			}

			// 找到服务
			// ////////////////////////////////////////////////////////
			else if (action.equals(InnofitService.ACTION_GATT_SERVICES_DISCOVERED)) {
				Log.i("test", "ACTION_GATT_SERVICES_DISCOVERED");
				mService.setScaleNotifyTrue();
			}
			// 收到广播数据
			else if (action.equals(InnofitService.ACTION_DATA_AVAILABLE)) {

				byte[] value = intent.getByteArrayExtra(InnofitService.KEY_NOTIFY_DATA);
				receiveScales(value, date_query);
			}

			// }
			// 手机蓝牙状态改变
			// ///////////////////////////////////////////////////////////
			else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
				Log.i(TAG, "bluetooth state change");
				int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
				if (state == BluetoothAdapter.STATE_ON) {
					connectBluetooth();
				} else if (state == BluetoothAdapter.STATE_OFF) {
					Log.i(TAG, "bluetooth disable");
					// myHandler.sendEmptyMessage(Global.HANDLER_DISCONNECTED);
				}
			}
			// else if (action.equals(Global.ACTION_BLUETOOTH_ENABLE_CONFORM)) {
			// Log.i(TAG, "bluetooth enable");
			// int sdk = Build.VERSION.SDK_INT;
			// Log.i(TAG, "sdk int : " + sdk);
			// if (sdk >= 18) {
			// beginScanDevice();
			// } else {
			// Toast.makeText(getActivity(),
			// getString(R.string.can_not_use_BLE),
			// Toast.LENGTH_SHORT).show();
			// }
			// }
		}
	};

	/**
	 * 初始化 serviceConnection
	 */
	private void initServiceConnection() {
		Log.i(TAG, "初始化 serviceConnection");
		Intent bindIntent = new Intent(MainActivity.this, InnofitService.class);
		// startService(bindIntent);
		bindService(bindIntent, myServiceConnection, Context.BIND_AUTO_CREATE);
	}

	/**
	 * my ServiceConnection
	 */
	private ServiceConnection myServiceConnection = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.i(TAG, "onServiceDisconnected--");
			mService = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.i(TAG, "onServiceConnected==");
			mService = ((InnofitService.LocalBinder) service).getService();
			/**
			 * 刚进入app连接进行判断，连接蓝牙
			 */
			connectBluetooth();

		}
	};

	private void connectBluetooth() {
		int sdk = Build.VERSION.SDK_INT;
		if (sdk >= 18) {
			if (mService != null && mService.getConnectionState() != InnofitService.STATE_CONNECTED) {
				beginScanDevice();
			} else {
				Log.i(TAG, "had connected");
			}
		} else {
			Toast.makeText(this, getString(R.string.can_not_use_BLE), Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 开始扫描设备
	 */
	private void beginScanDevice() {

		mBtAdapter = SyncHelper.initBluetooth_manual(this);

		if (mBtAdapter != null) {
			// 蓝牙是否打开
			if (mBtAdapter.isEnabled()) {
				Log.i(TAG, "connnectionState:" + mService.getConnectionState());

				if (mService.getConnectionState() != InnofitService.STATE_CONNECTED) {

					Log.i(TAG, "no connected device, begin to scan");
					// showConnectingDialog();
					mService.scan(true);
					// 开启定时
					// mechanismScanTimeOut();
				} else {
					// mService.disconnect();
					Log.i(TAG, "no connected device, begin to scan");
					// mService.scan(true);
					// showConnectingDialog();
				}
			} else {

			}

		} else {
			// Toast.makeText(this,
			// getString(R.string.No_bluetooth_in_device),
			// Toast.LENGTH_SHORT).show();
		}
	}

	private OnClickListener myOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View view) {

			switch (view.getId()) {
			case R.id.image_list:
				showListDialog(MainActivity.this);
				break;
			case R.id.layout_home:
				popupWindow.dismiss();
				break;
			case R.id.layout_user:
				Intent intent_user = new Intent(MainActivity.this, UserActivity.class);
				intent_user.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_user);
				finish();
				break;
			case R.id.layout_setting:
				Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
				intent_setting.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent_setting);
				finish();
				break;
			case R.id.but_detele:
				Judge = true;
				dialog.dismiss();
				dialog = null;
				isShowDialog = false;
				text_weight.setText("00.0");
				text_BMI.setText("00.0");
				state = Global.TYPE_RECEIVE_DATA_NO;
				break;
			case R.id.iv_close_dialog:
				dialogNoUser.dismiss();
				dialogNoUser = null;
				isShowDialog = false;
				text_weight.setText("00.0");
				text_BMI.setText("00.0");
				state = Global.TYPE_RECEIVE_DATA_NO;
			default:
				break;
			}

		}
	};

	/**
	 * 显示list对话框
	 */
	private void showListDialog(Activity context) {
		if (popupWindow == null) {
			int width = ShowValueHelper.getScreenWidth(context);
			View popupView = getLayoutInflater().inflate(R.layout.innofit_left, null);
			popupWindow = new PopupWindow(popupView, (int) (width * 0.9), LayoutParams.MATCH_PARENT, true);
			popupWindow.setTouchable(true);
			popupWindow.setOutsideTouchable(true);
			popupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
			popupWindow.setAnimationStyle(R.anim.pull_in_left);

			// ///////////////////////////////////
			View layout_home = popupView.findViewById(R.id.layout_home);
			layout_home.setOnClickListener(myOnClickListener);

			View layout_user = popupView.findViewById(R.id.layout_user);
			layout_user.setOnClickListener(myOnClickListener);

			View layout_setting = popupView.findViewById(R.id.layout_setting);
			layout_setting.setOnClickListener(myOnClickListener);
			// ///////////////////////////////////

			popupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.LEFT, 0, 0);

		} else {
			popupWindow.showAtLocation(findViewById(R.id.layout_main), Gravity.LEFT, 0, 0);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (mService != null) {
			mService.scan(false);
			mService.disconnect();
		}

		this.unbindService(myServiceConnection);
		this.unregisterReceiver(myBLEBroadcastReceiver);
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		updateDate();
	}

	private void updateDate() {
		date_query = CalendarHelper.getToday();
	}

	private byte state = Global.TYPE_RECEIVE_DATA_NO;

	private Info info;

//	/**
//	 * 处理收到的数据
//	 *
//	 * @param value
//	 */
//	private void receiveScales(byte[] value, Calendar data) {
//
//		if (value[0] != Global.TYPE_RECEIVE_DATA_OK) {
//			Value[0] = 0;
//			Value[1] = 0;
//		}
//
//		if (value != null && date_query != null) {
//			info = ParserHelper.parseScales(value, data);
//
//			if (info.getWeight() == 0) {
//				text_weight.setText("00.0");
//				text_BMI.setText("00.0");
//			}
//			if (info != null) {
//				// 获取传过来的是那个用户
//				profileID = info.getpUser();
//				Logger.e("profileID:" + profileID);
//				if (profileID == -1) {
//					// 体重秤
//					if (dialogNoUser == null) {
//						Logger.e("Judge:" + Judge);
//						if (Judge == true) {
//							Judge = false;
//							dialogNoUser = new AlertDialog.Builder(MainActivity.this).show();
//							dialogNoUser.setContentView(R.layout.choose_user);
//							ListView lv_choose_user = (ListView) dialogNoUser.findViewById(R.id.lv_choose_user);
//							userList = new ArrayList<User>();
//							// 查询数据库
//							userList = queryUser(context);
//
//							UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
//
//							lv_choose_user.setAdapter(userAdapter);
//							userAdapter.notifyDataSetChanged();
//							ImageView iv_close_dialog = (ImageView) dialogNoUser.findViewById(R.id.iv_close_dialog);
//
//							iv_close_dialog.setOnClickListener(myOnClickListener);
//
//							lv_choose_user.setOnItemClickListener(new OnItemClickListener() {
//								@Override
//								public void onItemClick(AdapterView<?> arg0, View arg1, int positiong, long arg3) {
//									int i = userList.get(positiong).getUserId();
//									Judge = true;
//									dialogNoUser.dismiss();
//									dialogNoUser = null;
//									showUserDiao(i, info, dialog);
//								}
//							});
//
//						}
//					}
//				} else {
//					// 脂肪秤
//					String bmiStr = Global.df_double_2.format(info.getWeight() / ((height * height) / 10000));
//					bmiStr = bmiStr.replaceAll(",", ".");
//					double bmi = Double.parseDouble(bmiStr);
//					info.setBmi(bmi);
//
//					Log.i("vivitar", "Value[0] =" + Value[0] + "----" + Value[1] + "===");
//					if (Value[0] != Value[1] || value[0] != Global.TYPE_RECEIVE_DATA_OK) {
//						if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
//							double f1 = CalculateHelper.decimal(CalculateHelper.kgToLbs(info.getWeight()));
//							text_weight.setText(String.valueOf(f1));
//
//						} else {
//							text_weight.setText(String.valueOf(info.getWeight()));
//						}
//						text_BMI.setText(String.valueOf(bmi));
//					}
//					// 判斷是否穩定
//					if (value[0] == Global.TYPE_RECEIVE_DATA_OK && state != Global.TYPE_RECEIVE_DATA_OK) {
//						if (Value[0] == 0) {
//							Value[0] = info.getWeight();
//						} else {
//							Value[1] = info.getWeight();
//						}
//
//						if (Value[0] != Value[1]) {
//							state = Global.TYPE_RECEIVE_DATA_OK;
//							mService.disconnect();
//							if (dialogNoUser == null) {
//								if (Judge == true) {
//									Judge = false;
//									dialogNoUser = new AlertDialog.Builder(MainActivity.this).show();
//
//									dialogNoUser.setContentView(R.layout.choose_user);
//									ListView lv_choose_user = (ListView) dialogNoUser.findViewById(R.id.lv_choose_user);
//									userList = new ArrayList<User>();
//									// 查询数据库
//									userList = queryUser(context);
//
//									UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);
//									lv_choose_user.setAdapter(userAdapter);
//									userAdapter.notifyDataSetChanged();
//
//									ImageView iv_close_dialog = (ImageView) dialogNoUser.findViewById(R.id.iv_close_dialog);
//									iv_close_dialog.setOnClickListener(myOnClickListener);
//									lv_choose_user.setOnItemClickListener(new OnItemClickListener() {
//
//										@Override
//										public void onItemClick(AdapterView<?> arg0, View arg1, int positiong, long arg3) {
//											int i = userList.get(positiong).getUserId();
//											dialogNoUser.dismiss();
//											dialogNoUser = null;
//											showUserDiao(i, info, dialog);
//										}
//									});
//
//								}
//							}
//						}
//
//					}
//
//				}
//
//			}
//		}
//	}

	protected void showUserDiao(final int i, Info info2, AlertDialog dialog2) {

		if (dialog == null) {
			dialog = new AlertDialog.Builder(MainActivity.this).show();
			dialog.setContentView(R.layout.info_dialog);
			User user = DatabaseProvider.queryUser(getApplicationContext(), i);
			TextView tv_user = (TextView) dialog.findViewById(R.id.tv_user__dialog);
			tv_user.setText(user.getUsername());

			TextView tv_gender = (TextView) dialog.findViewById(R.id.tv_gender__dialog);
			if (user.getSex() == 0) {
				tv_gender.setText(getResources().getString(R.string.male_info_dialog));
			} else if (user.getSex() == 1) {
				tv_gender.setText(getResources().getString(R.string.female_user_details));
			}
			//
			TextView tv_unit_weight = (TextView) dialog.findViewById(R.id.tv_unit_weight);

			TextView tv_age = (TextView) dialog.findViewById(R.id.tv_age__dialog);
			tv_age.setText(user.getAge() + "s");
			TextView tv_height = (TextView) dialog.findViewById(R.id.tv_height__dialog);
			if (sharedPreferences.getString(Global.UNIT_CMIN, "").equals(Global.UNIT_CM)) {
				tv_height.setText(CalculateHelper.decimal(user.getHeight()) + "cm");
			} else {
				double height = CalculateHelper.cmToFeet(user.getHeight());
				int ia = (int) height;
				int ib = (int) ((height * 10) % 10);
				String apped = ia + "'" + ib + "''";
				// holder.tv_main_cm.setText("ft");
				tv_height.setText(apped + "ft");
				// tv_height.setText(user.getHeight() + "");
			}

			//
			String bmiStr = Global.df_double_2.format(info2.getWeight() / ((height * height) / 10000));
			bmiStr = bmiStr.replaceAll(",", ".");
			double bmi = Double.parseDouble(bmiStr);
			info2.setBmi(bmi);
			TextView tv_weight_dialog = (TextView) dialog.findViewById(R.id.tv_weight_dialog);
			if (sharedPreferences.getString(Global.UNIT, "").equals("lb")) {
				double f = CalculateHelper.kgToLbs(info.getWeight());
				tv_weight_dialog.setText(String.valueOf(CalculateHelper.decimal(f)));
				tv_unit_weight.setText("LB");
			} else {
				tv_weight_dialog.setText(String.valueOf(info.getWeight()));
				tv_unit_weight.setText("Kg");
			}

			TextView tv_bmi_dialog = (TextView) dialog.findViewById(R.id.tv_bmi_dialog);
			//
			tv_bmi_dialog.setText(info2.getBmi() + "");
			Button but_save = (Button) dialog.findViewById(R.id.but_save);

			but_save.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Judge = true;
					Message msg = new Message();
					msg.arg1 = i;
					msg.what = Global.HANDLE_NUMBER1;
					myHandler.sendMessage(msg);
					// mService.disconnect();
				}
			});
			ImageView but_detele = (ImageView) dialog.findViewById(R.id.but_detele);
			but_detele.setOnClickListener(myOnClickListener);
		}
	}

	/**
	 * 保存电子称数据
	 */
	public void saveInfo(int profileID, Info info, Calendar calendar) {
		if (info != null) {
			Info temp = DatabaseProvider.queryScale(MainActivity.this, profileID, calendar);
			if (temp == null) {
				DatabaseProvider.insertScale(MainActivity.this, profileID, info, calendar);
			} else {
				DatabaseProvider.updateScale(MainActivity.this, profileID, info, calendar);
			}

		}
	}

	private void initList(boolean isFirst) {
		// 查询所有用户数据
		listUser = new ArrayList<User>();
		listUser = queryAllUser(MainActivity.this);
		List<Value> value_list = new ArrayList<Value>();

		Map<Integer, List<Info>> infoChartMap = new HashMap<Integer, List<Info>>();

		for (int i = 0; i < listUser.size(); i++) {

			int test = listUser.get(i).getUserId();

			List<Info> infoFrist = DatabaseProvider.queryScale(MainActivity.this, test);
			Value value = new Value();
			value.setId(i);

			if (infoFrist != null && infoFrist.size() > 0) {
				// 单位判断
				if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
					double f = CalculateHelper.kgToLbs(infoFrist.get(infoFrist.size() - 1).getWeight());
					value.setWeight(CalculateHelper.decimal(f));

					value.setJudgeUnit(Global.UNIT_LB);
				} else {
					value.setWeight(infoFrist.get(infoFrist.size() - 1).getWeight());
					value.setJudgeUnit(Global.UNIT_KG);
				}
				value.setFat(infoFrist.get(infoFrist.size() - 1).getFat());
				value.setBmi(infoFrist.get(infoFrist.size() - 1).getBmi());
				if (infoFrist.get(infoFrist.size() - 1).getWeight() > 0 && infoFrist.get(infoFrist.size() - 1).getWeight() <= 100) {

					value.setSeekWeight((int) infoFrist.get(infoFrist.size() - 1).getWeight());
				} else {
					value.setSeekWeight(100);
				}
				if (infoFrist.get(infoFrist.size() - 1).getBmi() > 0 && infoFrist.get(infoFrist.size() - 1).getBmi() <= 100) {
					value.setSeekBmi((int) infoFrist.get(infoFrist.size() - 1).getBmi());
				} else {
					value.setSeekBmi(100);
				}
				if (infoFrist.get(infoFrist.size() - 1).getFat() > 0 && infoFrist.get(infoFrist.size() - 1).getFat() <= 100) {
					value.setSeekFat((int) infoFrist.get(infoFrist.size() - 1).getFat());
				} else {
					value.setSeekFat(100);
				}
				Calendar cal_last = infoFrist.get(infoFrist.size() - 1).getDate();
				if (cal_last != null) {
					int days = CalendarHelper.getXDays(cal_last);
					if (days == 0) {
						value.setDate(getString(R.string.today_) + Global.sdf_0.format(cal_last.getTime()));
					} else if (days == 1) {
						value.setDate(getString(R.string.yesterday_) + Global.sdf_0.format(cal_last.getTime()));
					} else {
						value.setDate(Global.sdf_2.format(cal_last.getTime()));
					}

					Calendar[] calArray = null;
					calArray = CalendarHelper.getLastMonthToTomorrow(cal_last);
					//
					List<Info> infoChartList = DatabaseProvider.queryScale(MainActivity.this, listUser.get(i).getUserId(), calArray[0], calArray[1]);

					infoChartMap.put(i, infoChartList);
				}
				//
			} else {

				if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
					value.setJudgeUnit(Global.UNIT_LB);
				} else {
					value.setJudgeUnit(Global.UNIT_KG);
				}
			}
			value_list.add(value);
		}

		homeAdapter = new HomeAdapter(MainActivity.this, value_list, listUser, infoChartMap);
		list_home.setAdapter(homeAdapter);
		// homeAdapter.notifyDataSetChanged();
	}

	private List<User> queryAllUser(Context context) {

		List<User> list = DatabaseProvider.queryUserAll(context);
		return list;
	}

	/**
	 * 初始化UI
	 */
	private void initUI() {
		context = MainActivity.this;
		tv_danwei_kg = (TextView) findViewById(R.id.tv_danwei_kg);
		sharedPreferences = getSharedPreferences(SettingActivity.SAVE_UNIT_NAME, MODE_PRIVATE);
		if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
			tv_danwei_kg.setText("Lb");
		} else {
			tv_danwei_kg.setText("Kg");
		}
		//
		text_BMI = (TextView) findViewById(R.id.text_BMI);
		text_weight = (TextView) findViewById(R.id.text_weight);
		// text_weight
		ImageView image_list = (ImageView) findViewById(R.id.image_list);
		image_list.setOnClickListener(myOnClickListener);

		image_bluetoothconnect = (ImageView) findViewById(R.id.image_bluetoothconnect);

		list_home = (ListView) findViewById(R.id.list_home);
		initList(true);
	}

	// 监听返回键，退出
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			new AlertDialog.Builder(this).setTitle(R.string.exit_tip).setMessage(R.string.whether_to_exit_).setPositiveButton(R.string.exit_ok, new Dialog.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					finish();
					System.exit(0);
				}
			}).setNegativeButton(R.string.cancel, null).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private List<User> queryUser(Context context) {
		List<User> list = DatabaseProvider.queryUserAll(context);
		return list;
	}

	// TimerTask task = new TimerTask() {
	// public void run() {
	// connectBluetooth();
	// timer = null;
	// }
	// };
	//
	private TextView text_BMI;
	private TextView text_weight;
	private ImageView image_bluetoothconnect;
	private ListView list_home;
	HomeAdapter homeAdapter;
	private AlertDialog dialog, dialogNoUser;
	private TextView tv_danwei_kg;

	@Subscribe
	public void onEventMainThread(Event event) {
		if (Event.EVENT_SYNC_DATA == event.getEventType() ||Event.EVENT_SYNC_DATA_LIVE == event.getEventType()) {

		}
	}

	private boolean isShowDialog = false;
	private boolean isRetry = false;

	/**
	 * 处理收到的数据
	 *
	 * @param value
	 */
	private void receiveScales(byte[] value, Calendar data) {

		if (value == null || date_query == null) return;
		if (value[0] == Global.TYPE_RECEIVE_DATA_NO) isRetry = true;
		info = ParserHelper.parseScales(value, data);
		if (info == null) return;
		if (info.getWeight() == 0) {
			text_weight.setText("00.0");
			text_BMI.setText("00.0");
		}
		// 获取传过来的是那个用户
		profileID = info.getpUser();
		if (value[0] == Global.TYPE_RECEIVE_DATA_OK && isRetry) {

			if (sharedPreferences.getString(Global.UNIT, "").equals(Global.UNIT_LB)) {
				double f1 = CalculateHelper.decimal(CalculateHelper.kgToLbs(info.getWeight()));
				text_weight.setText(String.valueOf(f1));
			} else {
				text_weight.setText(String.valueOf(info.getWeight()));
			}
			// 脂肪秤
			String bmiStr = Global.df_double_2.format(info.getWeight() / ((height * height) / 10000));
			bmiStr = bmiStr.replaceAll(",", ".");
			double bmi = Double.parseDouble(bmiStr);
			info.setBmi(bmi);
			text_BMI.setText(String.valueOf(bmi));

			// 体重秤
			if (dialogNoUser == null || !dialogNoUser.isShowing()) {
				if (dialog != null) {
					dialog.dismiss();
					dialog = null;
				}
				isRetry = false;
				isShowDialog = true;
				dialogNoUser = new AlertDialog.Builder(MainActivity.this).show();
				dialogNoUser.setContentView(R.layout.choose_user);
				ListView lv_choose_user = (ListView) dialogNoUser.findViewById(R.id.lv_choose_user);
				userList = new ArrayList<User>();
				// 查询数据库
				userList = queryUser(context);

				UserAdapter userAdapter = new UserAdapter(getApplicationContext(), userList);

				lv_choose_user.setAdapter(userAdapter);
				userAdapter.notifyDataSetChanged();
				ImageView iv_close_dialog = (ImageView) dialogNoUser.findViewById(R.id.iv_close_dialog);

				iv_close_dialog.setOnClickListener(myOnClickListener);

				lv_choose_user.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int positiong, long arg3) {
						int i = userList.get(positiong).getUserId();
						Judge = true;
						dialogNoUser.dismiss();
						dialogNoUser = null;
						isShowDialog = false;
						showUserDiao(i, info, dialog);
					}
				});
			}
		}
	}
}
