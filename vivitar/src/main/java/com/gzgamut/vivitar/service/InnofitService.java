package com.gzgamut.vivitar.service;

import java.util.Arrays;
import java.util.UUID;

import com.gzgamut.vivitar.global.Global;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class InnofitService extends Service {
	public final static String ACTION_GATT_CONNECTED = Global.PACKAGE_NAME
			+ "ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_CONNECTED_FAIL = Global.PACKAGE_NAME
			+ "ACTION_GATT_CONNECTED_FAIL";
	public final static String ACTION_GATT_DISCONNECTED = Global.PACKAGE_NAME
			+ "ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = Global.PACKAGE_NAME
			+ "ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_GATT_SERVICES_DISCOVER_FAIL = Global.PACKAGE_NAME
			+ "ACTION_GATT_SERVICES_DISCOVER_FAIL";
	public final static String ACTION_WRITE_DESCRIPTOR_SUCCESS = Global.PACKAGE_NAME
			+ "ACTION_WRITE_DESCRIPTOR_SUCCESS";
	public final static String ACTION_WRITE_DESCRIPTOR_FAIL = Global.PACKAGE_NAME
			+ "ACTION_WRITE_DESCRIPTOR_FAIL";
	public final static String ACTION_WRITE_CHARACTERISTIC_SUCCESS = Global.PACKAGE_NAME
			+ "ACTION_WRITE_CHARACTERISTIC_SUCCESS";
	public final static String ACTION_WRITE_CHARACTERISTIC_FAIL = Global.PACKAGE_NAME
			+ "ACTION_WRITE_CHARACTERISTIC_FAIL";
	public final static String ACTION_DATA_AVAILABLE = Global.PACKAGE_NAME
			+ "ACTION_DATA_AVAILABLE";
	private final IBinder mBinder = new LocalBinder();
	private String TAG = "InnofitService";
	// 连接状态
	public static final int STATE_DISCONNECTED = 0;
	public static final int STATE_CONNECTING = 1;
	public static final int STATE_CONNECTED = 2;
	//
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothManager mBluetoothManager;
	private BluetoothGatt mBluetoothGatt;
	private String mBluetoothDeviceAddress;
	public final static String KEY_NOTIFY_DATA = "innofit.UPADTEDATA";
	//
	public final static String ACTION_RETURN_SPECIAL_KEY = Global.PACKAGE_NAME
			+ "ACTION_RETURN_SPECIAL_KEY";

	public String getDeviceAddress() {
		return mBluetoothDeviceAddress;
	}

	public final static String ACTION_DEVICE_FOUND = Global.PACKAGE_NAME
			+ "ACTION_DEVICE_FOUND";

	@Override
	public IBinder onBind(Intent arg0) {
		return mBinder;
	}

	public class LocalBinder extends Binder {
		public InnofitService getService() {
			return InnofitService.this;
		}
	}

	private int mConnectionState = STATE_DISCONNECTED;

	public int getConnectionState() {
		return mConnectionState;
	}

	/**
	 * 扫描设备
	 * 
	 * @param start
	 */
	public void scan(boolean start) {
		if (mBluetoothAdapter != null) {
			if (start) {
				mBluetoothAdapter.startLeScan(mLeScanCallback);
			} else {
				mBluetoothAdapter.stopLeScan(mLeScanCallback);
			}
		} else {
			Log.i(TAG, "bluetoothadapter is null");
		}
	}

	public boolean initialize() {
		// For API level 18 and above, get a reference to BluetoothAdapter
		// through
		// BluetoothManager.
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}

		mBluetoothAdapter = mBluetoothManager.getAdapter();
		if (mBluetoothAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}

		return true;
	}

	/**
	 * 扫描设备的回调方法
	 */
	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,
				byte[] scanRecord) {
			Bundle mBundle = new Bundle();
			mBundle.putParcelable(BluetoothDevice.EXTRA_DEVICE, device);
			mBundle.putInt(BluetoothDevice.EXTRA_RSSI, rssi);
			Intent intent = new Intent();
			intent.setAction(ACTION_DEVICE_FOUND);
			intent.putExtras(mBundle);
			sendBroadcast(intent);
		}
	};

	public void disconnect() {
		if (mBluetoothAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
	}

	public boolean connect(final String address, boolean is) {
		if (mBluetoothAdapter == null || address == null) {
			Log.w(TAG,
					"BluetoothAdapter not initialized or unspecified address.");
			return false;
		}
		final BluetoothDevice device = mBluetoothAdapter
				.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		Log.d(TAG, "Trying to create a new connection.");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}

	/**
	 * 蓝牙回调函数
	 */
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		public void onConnectionStateChange(BluetoothGatt gatt, int status,
				int newState) {
			if (newState == BluetoothProfile.STATE_CONNECTED) {

				if (status == BluetoothGatt.GATT_SUCCESS) {
					Log.i(TAG, "Connected to GATT server.");
					mConnectionState = STATE_CONNECTED;
					broadcastUpdate(ACTION_GATT_CONNECTED);

					// Attempts to discover services after successful
					// connection.
					if (mBluetoothGatt != null) {
						Log.i(TAG, "Attempting to start service discovery:"
								+ mBluetoothGatt.discoverServices());
					}
				} else {
					mConnectionState = STATE_DISCONNECTED;
					disconnect();

					broadcastUpdate(ACTION_GATT_CONNECTED_FAIL);
					close();
				}

			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				Log.i(TAG, "Disconnected from GATT server.");
				mConnectionState = STATE_DISCONNECTED;
				mBluetoothDeviceAddress = null;
				broadcastUpdate(ACTION_GATT_DISCONNECTED);
				close();
			}
		}

		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			Log.i(TAG, "onServicesDiscovered received: " + status);

			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);

			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVER_FAIL);
			}
		}

		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {
			UUID uuid = characteristic.getUuid();
			if (uuid.equals(Global.UUID_CHARACTERISTIC_COMMUNICATION)) {
				if (status == BluetoothGatt.GATT_SUCCESS) {
					broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_SUCCESS);

				} else {
					broadcastUpdate(ACTION_WRITE_CHARACTERISTIC_FAIL);
				}

			}

		};

		// 接收蓝牙发过来的数据
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic) {
			Log.i(TAG, "*****广播数据");
			Log.i(TAG, Arrays.toString(characteristic.getValue()));

			// boolean specialKey = ParserHelper.isSpecialKey(characteristic
			// .getValue());
			//
			// if (specialKey) {
			// Log.i("testaaa", "specialKeyaaaaaaaaaaaa		");
			// broadcastUpdate(ACTION_RETURN_SPECIAL_KEY);
			// } else {
			broadcastUpdate(ACTION_DATA_AVAILABLE, KEY_NOTIFY_DATA,
					characteristic.getValue());
			// }

		}

		@Override
		public void onCharacteristicRead(BluetoothGatt gatt,
				BluetoothGattCharacteristic characteristic, int status) {

		}

		@Override
		public void onDescriptorWrite(BluetoothGatt gatt,
				BluetoothGattDescriptor descriptor, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_WRITE_DESCRIPTOR_SUCCESS);
			} else {
				broadcastUpdate(ACTION_WRITE_DESCRIPTOR_FAIL);
			}
		}

		@Override
		public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
			Log.i(TAG, "rssi value: " + rssi);
			// broadcastRSSI(rssi);
		}
	};

	/**
	 * 发送广播
	 * 
	 * @param action
	 */
	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}

	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
	}

	@Override
	public void onCreate() {
		initialize();
	}

	/**
	 * 打开通知后，才能通信
	 */
	public void setScaleNotifyTrue() {
		setCharactoristicNotifyAndWriteDescriptor(Global.UUID_SERVICE,
				Global.UUID_CHARACTERISTIC_COMMUNICATION_SCALE,
				Global.UUID_DESCRIPTOR_CONFIGURATION);
	}

	/**
	 * 设置通知属性为true，并写上descriptor
	 * 
	 * @param uuid_service
	 * @param uuid_characteristic
	 * @param uuid_descriptor
	 */
	public void setCharactoristicNotifyAndWriteDescriptor(UUID uuid_service,
			UUID uuid_characteristic, UUID uuid_descriptor) {
		// service
		BluetoothGattService mBluetoothGattService = getBluetoothGattService(
				mBluetoothGatt, uuid_service);
		// characteristic
		BluetoothGattCharacteristic mBluetoothGattCharacteristic = getBluetoothGattCharacteristic(
				mBluetoothGattService, uuid_characteristic);

		if (mBluetoothGatt != null && mBluetoothGattCharacteristic != null) {
			// 设置
			mBluetoothGatt.setCharacteristicNotification(
					mBluetoothGattCharacteristic, true);

			BluetoothGattDescriptor bluetoothGattDescriptor = mBluetoothGattCharacteristic
					.getDescriptor(uuid_descriptor);
			if (bluetoothGattDescriptor != null) {
				bluetoothGattDescriptor
						.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
				mBluetoothGatt.writeDescriptor(bluetoothGattDescriptor);
			}

		} else if (mBluetoothGatt == null) {
			Log.i(TAG, "mBluetoothGatt is null");
		} else if (mBluetoothGattCharacteristic == null) {
			Log.i(TAG, "mBluetoothGattCharacteristic is null");
		}
	}

	/**
	 * 获取bluetoothGattService
	 * 
	 * @param mBluetoothGatt
	 * @param UUID_SERVICE
	 * @return
	 */
	private BluetoothGattService getBluetoothGattService(
			BluetoothGatt mBluetoothGatt, UUID UUID_SERVICE) {
		if (mBluetoothGatt != null) {
			BluetoothGattService mBluetoothGattServer = mBluetoothGatt
					.getService(UUID_SERVICE);
			if (mBluetoothGattServer != null) {
				return mBluetoothGattServer;
			} else {
				Log.i(TAG,
						"getBluetoothGattService, bluetoothgatt get service uuid:"
								+ UUID_SERVICE + " is null");
			}
		} else {
			Log.i(TAG, "mBluetoothGatt is null");
		}

		return null;
	}

	/**
	 * 获取bluetoothGattCharacteristic
	 * 
	 * @param mBluetoothGattService
	 * @param UUID_CHARACTERISTIC
	 * @return
	 */
	private BluetoothGattCharacteristic getBluetoothGattCharacteristic(
			BluetoothGattService mBluetoothGattService, UUID UUID_CHARACTERISTIC) {
		if (mBluetoothGattService != null) {
			BluetoothGattCharacteristic mBluetoothGattCharacteristic = mBluetoothGattService
					.getCharacteristic(UUID_CHARACTERISTIC);

			if (mBluetoothGattCharacteristic != null) {
				return mBluetoothGattCharacteristic;
			} else {
				Log.i(TAG,
						"getBluetoothGattCharacteristic, bluetoothGattServer get characteristic uuid:"
								+ UUID_CHARACTERISTIC + " is null");
			}
		} else {
		}
		Log.i(TAG, "mBluetoothGattServer is null");

		return null;
	}

	private void broadcastUpdate(String action, String key, byte[] value) {

		Intent intent = new Intent(action);
		intent.putExtra(key, value);
		sendBroadcast(intent);
	}

}
