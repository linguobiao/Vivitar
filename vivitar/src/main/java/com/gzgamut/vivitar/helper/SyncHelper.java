package com.gzgamut.vivitar.helper;

import com.gzgamut.vivitar.global.Global;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;

public class SyncHelper {
	/**
	 * 初始化蓝牙
	 * 
	 * @param context
	 */
	public static BluetoothAdapter initBluetooth_manual(Activity context) {
		BluetoothManager mBluetoothManager = null;

		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) context
					.getSystemService(Context.BLUETOOTH_SERVICE);

			// 没有蓝牙模块
			if (mBluetoothManager == null) {
				return null;
			}
		}
		BluetoothAdapter mBtAdapter = mBluetoothManager.getAdapter();

		// 判断机器是否有蓝牙
		if (!mBtAdapter.isEnabled()) {
			// Intent enableIntent = new Intent(
			// BluetoothAdapter.ACTION_REQUEST_ENABLE);
			// context.startActivityForResult(enableIntent,
			// Global.REQUEST_ENABLE_BLUETOOTH);
			mBtAdapter.enable();
		}
		return mBtAdapter;
	}

}
