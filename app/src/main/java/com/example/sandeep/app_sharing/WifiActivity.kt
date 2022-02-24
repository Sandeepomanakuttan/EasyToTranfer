package com.example.sandeep.app_sharing

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.net.wifi.WifiManager
import android.net.wifi.p2p.*
import android.net.wifi.p2p.WifiP2pManager.ActionListener
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.io.*
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


open class WifiActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wifi)
        Companion.initialize(this)
        Companion.executeListener(this)
        readbox.setOnClickListener {

            val  i = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(i,100)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==100 && resultCode== RESULT_OK){
            val imgUri =data?.data
            var byte =getByteFromUri(this, imgUri!!)
            var encode = Base64.encodeToString(byte,Base64.DEFAULT)
            Log.e("encode",encode)
            dbyte = Base64.decode(encode,Base64.DEFAULT)

        }
    }


    var peerListListener: WifiP2pManager.PeerListListener =
        object : WifiP2pManager.PeerListListener {

            @SuppressLint("SetTextI18n")
            override fun onPeersAvailable(peerList: WifiP2pDeviceList) {
                if (peerList != peers) {
                    peers.clear()
                    peers.addAll(peerList.deviceList)

                    deviceArray = ArrayList(peerList.deviceList.size)
                    deviceNameArray = ArrayList(peerList.deviceList.size)

                    for (device in peerList.deviceList) {
                        deviceNameArray.add(device.deviceName)
                        deviceArray.add(device)

                    }

                    val adapter = ArrayAdapter(
                        applicationContext,
                        android.R.layout.simple_list_item_1,
                        deviceNameArray
                    )

                    list.adapter = adapter

                    if (peers.size == 0) {
                        connectionStatus.text="No Device Found"
                        return
                    }

                }

            }

        }

    @SuppressLint("SetTextI18n")
    var connectionInfoListener =
        WifiP2pManager.ConnectionInfoListener {
                wifiP2pInfo ->
                            val groupOwnerAddresss = wifiP2pInfo.groupOwnerAddress
                    if (wifiP2pInfo.groupFormed && wifiP2pInfo.isGroupOwner){

                        serverClass = ServerClass()
                        serverClass.start()
                        connectionStatus.text="Host"
                        isHost = true

                    }else if (wifiP2pInfo.groupFormed){

                        clientClass = ClientClass(groupOwnerAddresss)
                        clientClass.start()
                        connectionStatus.text="Client"
                        isHost = false

                    }
        }

//    override fun onResume() {
//        super.onResume()
//        registerReceiver(mReceiver, mIntentFilter)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        unregisterReceiver(mReceiver)
//    }

    class ServerClass : Thread() {

         var serversocket = ServerSocket()

        lateinit var inputStream : InputStream
         private lateinit var  outputStream: OutputStream




        override fun run() {

            Log.e("Server","run")
            serversocket=ServerSocket()
            serversocket.reuseAddress = true
            with(serversocket) { bind(InetSocketAddress(8888)) };
            socket=serversocket.accept()
            inputStream = socket.getInputStream()
            outputStream = socket.getOutputStream()


            val executor:ExecutorService = Executors.newSingleThreadExecutor()
            val handlers = Handler(Looper.getMainLooper())

            executor.execute {
                val buffer = ByteArray(1024)
                var bytes: Int

                while (socket != null) {
                    bytes = inputStream.read(buffer)

                    if (bytes > 0) {
                        val finalbytes = bytes

                        handlers.post {
                            //val tempMsg = String(buffer, 0, finalbytes)

                            val tempMsg = String(buffer, 0, finalbytes)
//                            Log.e("encode servar",tempMsg)
//                            var b = Base64.decode(tempMsg,Base64.DEFAULT)
//                            val bitmap = BitmapFactory.decodeByteArray(b,0,b.size)
//                            writeMsg.setImageBitmap(bitmap)
                           readbox.text = tempMsg
                        }
                    }
                }
            }

            serversocket.close()

        }
        fun write(byte: ByteArray){
            try {
                outputStream.write(byte)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }

    }


    class ClientClass(hostAddress: InetAddress) : Thread() {

          var hostAdd:String
        lateinit var inputStream: InputStream
        lateinit var outputStream: OutputStream

        init {
            hostAddress.hostAddress.also { hostAdd = it }
           socket = Socket()
        }



        override fun run() {

            Log.e("client","run")

            socket.connect(InetSocketAddress(hostAdd,8888),500)
            inputStream = socket.getInputStream()
            outputStream = socket.getOutputStream()


            val executor = Executors.newSingleThreadExecutor()

            val handlers = Handler(Looper.getMainLooper())
            executor.execute {
                val buffer = ByteArray(1024)


                while (socket != null) {
                    try {
                        var  bytes = inputStream.read()
                        if (bytes > 0) {
                            val finalbytes = bytes
                            handlers.post {
                               // var dbit = BitmapFactory.decodeByteArray(dbyte,0,dbyte.size)

                               val tempMsg = String(buffer, 0, finalbytes)
                                readbox.text=tempMsg
//                                var b = Base64.decode(tempMsg,Base64.DEFAULT)
//                                var bitmap = BitmapFactory.decodeByteArray(b,0,b.size)

                              //  writeMsg.setImageBitmap(bitmap)


                            }


                        }



                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }

            }
        }

        fun write(byte: ByteArray){
            try {
                outputStream.write(byte)
            }catch (e:IOException){
                e.printStackTrace()
            }
        }

    }

    companion object{

        @SuppressLint("StaticFieldLeak")

        lateinit var btnOnoff: Button
        lateinit var connectionStatus: TextView
        private lateinit var btnDiscover: Button
//        lateinit var imgbtn: ImageButton
        lateinit var readbox: TextView
        lateinit var writeMsg: EditText
        lateinit var list: ListView
        lateinit var wifiManager: WifiManager
        lateinit var mManager: WifiP2pManager
        lateinit var mChannel: WifiP2pManager.Channel
//        lateinit var mReceiver: BroadcastReceiver
        lateinit var mIntentFilter: IntentFilter
        private lateinit var socket:Socket
        lateinit var dbyte:ByteArray

        var peers: ArrayList<WifiP2pDevice> = ArrayList()
        lateinit var deviceNameArray: ArrayList<String>
        lateinit var deviceArray: ArrayList<WifiP2pDevice>

        lateinit var  serverClass:ServerClass
        lateinit var  clientClass:ClientClass

        var isHost:Boolean = false

        private fun executeListener(wifiActivity: WifiActivity) {
    
           btnOnoff.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    // if build version is less than Q try the old traditional method
                    wifiManager.isWifiEnabled = !wifiManager.isWifiEnabled
                } else {
                    // if it is Android Q and above go for the newer way    NOTE: You can also use this code for less than android Q also
                    val panelIntent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    wifiActivity.startActivityForResult(panelIntent, 1)
                }
            }
    
          btnDiscover.setOnClickListener {
    
                if (ActivityCompat.checkSelfPermission(
                        wifiActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        wifiActivity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        101
                    )
                }
               mManager.discoverPeers(mChannel, object : ActionListener {
                    @SuppressLint("SetTextI18n")
                    override fun onSuccess() {
                       connectionStatus.text = "Discover Started"
                    }
    
                    @SuppressLint("SetTextI18n")
                    override fun onFailure(p0: Int) {
                      connectionStatus.text = "Discover Starting Faild"
                    }
                })
            }
    
            list.setOnItemClickListener { parent, view, position, id ->
    
                val device = deviceArray[position]
                val config = WifiP2pConfig()
                config.deviceAddress = device.deviceAddress
    
                try {
                    mManager.connect(mChannel, config, object : ActionListener {
                        @SuppressLint("SetTextI18n")
                        override fun onSuccess() {

                            connectionStatus.text = "Connected " + device.deviceName
                        }
    
                        @SuppressLint("SetTextI18n")
                        override fun onFailure(p0: Int) {
                            connectionStatus.text = "Not Connected"
                        }
                    })
                } catch (e: IOException) {
    
    
                }
            }
    
    
//            imgbtn.setOnClickListener {
//                val executor = Executors.newSingleThreadExecutor()
//                val msg = writeMsg.text.toString()
//                executor.execute {
//                    if (isHost) {
//
//                        serverClass.write(msg.toByteArray())
//                    } else if (!isHost) {
//                        clientClass.write(msg.toByteArray())
//                    }
//                }
//            }



        }

        fun getByteFromUri(context: Context,uri : Uri): ByteArray {

           var  inStream = context.contentResolver.openInputStream(uri)

            var byteBuffer = ByteArrayOutputStream()

            var buffer = ByteArray(1024)

            var len =0
            while ((inStream?.read(buffer).also { len = it!! }) != -1){

                byteBuffer.write(buffer,0,len)

            }
            return byteBuffer.toByteArray()

        }

        private fun initialize(wifiActivity: WifiActivity) {
            btnOnoff = wifiActivity.findViewById(R.id.btnonoff)
            btnDiscover = wifiActivity.findViewById(R.id.btnDiscover)
            list = wifiActivity.findViewById(R.id.list)
            connectionStatus = wifiActivity.findViewById(R.id.connectionStatus)
//            imgbtn = wifiActivity.findViewById(R.id.imgbtn)
            readbox = wifiActivity.findViewById(R.id.readbox)
            writeMsg = wifiActivity.findViewById(R.id.writeMsg)

            wifiManager = wifiActivity.applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

            mManager = wifiActivity.getSystemService(WIFI_P2P_SERVICE) as WifiP2pManager

            mChannel = mManager.initialize(wifiActivity, wifiActivity.mainLooper, null)


            mIntentFilter = IntentFilter()
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)

        }


            // Used to load the 'app_sharing' library on application startup.
            init {
                System.loadLibrary("app_sharing")
            }





    }


}