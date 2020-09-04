package com.example.helloworld2

import android.bluetooth.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import java.util.*

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)

class GattClientCallback(private val viewModel: BtViewModel): BluetoothGattCallback() {

    private val HEART_RATE_SERVICE_UUID = convertFromInteger(0x180D)
    private val HEART_RATE_MEASUREMENT_CHAR_UUID = convertFromInteger(0x2A37)
    private val CLIENT_CHARACTERISTIC_CONFIG_UUID = convertFromInteger(0x2902)

    private fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (status == BluetoothGatt.GATT_FAILURE) {
            Log.d("DBG", "GATT connection failure")
            return
        } else if (status != BluetoothGatt.GATT_SUCCESS) {
            Log.d("DBG", "GATT connection success")
            return
        }
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.d("DBG", "Connected GATT service")
            gatt.discoverServices();
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.d("DBG", "GATT disconnected")
        }
    }
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }

        Log.d("DBG", "onServicesDiscovered()")
        gatt.services?.forEach {

            Log.d("DBG", "Service ${it.uuid}")
            if (it.uuid == HEART_RATE_SERVICE_UUID) {
                Log.d("DBG", "BINGO!!!")
                for (gattCharacteristic in it.characteristics)
                    Log.d("DBG", "Characteristic ${gattCharacteristic.uuid}")

                gatt.setCharacteristicNotification(it.characteristics[2], true)
                val descriptor = it.characteristics[2].getDescriptor(CLIENT_CHARACTERISTIC_CONFIG_UUID).apply {
                    value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                }
                gatt.writeDescriptor(descriptor)
            }
        }
    }
    override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor, status: Int) {
        Log.d("DBG", "onDescriptorWrite")
    }
    override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
        val heartRate = characteristic.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8, 1)
        viewModel.changeHr(heartRate.toString())
        Log.d("DBG", String.format("Received heart rate: %d", heartRate))
    }
}

class BtViewModel: ViewModel() {
    private val heartRate = MutableLiveData<String>()

    fun changeHr(hr: String) {
        heartRate.postValue(hr)
    }

    val data = heartRate.switchMap {
        liveData(Dispatchers.Main) {
            emit(it)
        }
    }
}