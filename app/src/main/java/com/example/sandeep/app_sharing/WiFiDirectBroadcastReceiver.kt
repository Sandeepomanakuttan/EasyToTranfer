package com.example.sandeep.app_sharing

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.sandeep.app_sharing.WifiActivity.Companion.connectionStatus

class WiFiDirectBroadcastReceiver: BroadcastReceiver {
        lateinit var mManager: WifiP2pManager
        lateinit var mChannel:WifiP2pManager.Channel
        lateinit var mActivity:WifiActivity

    constructor(
        mManager: WifiP2pManager,
        mChannel: WifiP2pManager.Channel,
        mActivity: WifiActivity
    ){
        this.mManager = mManager
        this.mChannel = mChannel
        this.mActivity = mActivity
    }



    override fun onReceive(context: Context, intent: Intent) {
        var action = intent.action

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION == action){
            var state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1)
            
            if (state==WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                Toast.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show()
            }

        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION == action){
            //do something
            if (mManager!=null){
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),101)
                    return
                }
                mManager.requestPeers(mChannel,mActivity.peerListListener)
            }
        }else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION==action){

            if(mManager!=null){
                var networkInfo=intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)

                if (networkInfo!!.isConnected){
                    mManager.requestConnectionInfo(mChannel,mActivity.connectionInfoListener)
                }else{
                    connectionStatus.text="Device disconnected"
                }
            }


        }else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION==action){

        }

    }
}