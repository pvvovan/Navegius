//package com.example.hellojni;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pvvovan.Navegius.Geometry.GeoPoint;
//import com.pvvovan.Navegius.NMEA.IGeoPointReceived;
//import com.pvvovan.Navegius.NMEA.NmeaReader;
//import com.pvvovan.Navegius.Serial.ISerial;
//import com.pvvovan.Navegius.Serial.SerialPort;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class MainActivity extends Activity {
//
////    int m_fdSerial;
//    TextView textViewReceived;
//    EditText editTextToSend;
//    ISerial serial = new SerialPort();
//    Button btnTimer;
//
//    private Timer mTimer;
//    private MyTimerTask mMyTimerTask;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button btnOpen = (Button) findViewById(R.id.buttonOpen);
//        btnOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                serial.Open();
//                textViewReceived.setText(String.valueOf(serial.IsOpen()));
//            }
//        });
//
//        Button btnClose = (Button)findViewById(R.id.buttonClose);
//        btnClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                serial.Close();
//                textViewReceived.setText(String.valueOf(serial.IsOpen()));
//            }
//        });
//
//        Button btnRead = (Button)findViewById(R.id.buttonRead);
//        btnRead.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (serial.BytesAvailable() == 0)
//                {
//                    textViewReceived.setText("0");
//                    return;
//                }
//
//                byte[] bs = new byte[1024];
//                int bytesRead = serial.Read(bs, 0, bs.length);
//                textViewReceived.setText(Integer.toString(bs.length));
//                String aString = new String(bs, 0, bytesRead);
//                textViewReceived.setText(aString);
//            }
//        });
//
//        Button btnWrite = (Button)findViewById(R.id.buttonWrite);
//        btnWrite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String data = editTextToSend.getText().toString();
//                byte[] bs = data.getBytes();
//                serial.Write(bs, 0, bs.length);
//            }
//        });
//
//        textViewReceived = (TextView)findViewById(R.id.textViewReceived);
//        editTextToSend = (EditText)findViewById(R.id.editTextToSend);
//
//        btnTimer = (Button)findViewById(R.id.buttonTimer);
//        btnTimer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (mTimer != null) {
////                    mTimer.cancel();
////                }
////                mTimer = new Timer();
////                mMyTimerTask = new MyTimerTask();
////                mTimer.schedule(mMyTimerTask, 100, 100);
//                NmeaReader.Instance().Start(serial, new IGeoPointReceived() {
//                    @Override
//                    public void GeoPointReceived(final GeoPoint geoPoint) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                btnTimer.setText(String.valueOf(geoPoint.NumberOfSatellites));
//                            }
//                        });
//
//                    }
//                });
//            }
//        });
//    }
//
//    class MyTimerTask extends TimerTask {
//
//        int i = 0;
//
//        @Override
//        public void run() {
//            runOnUiThread(new Runnable() {
//
//                @Override
//                public void run() {
//                    btnTimer.setText(String.valueOf(i++));
//                    if (serial.BytesAvailable() > 0)
//                    {
//                        byte[] bs = new byte[serial.BytesAvailable()];
//                        int bytesRead = serial.Read(bs, 0, bs.length);
//                        String aString = new String(bs, 0, bytesRead);
//                        textViewReceived.setText(aString);
//                    }
//                }
//            });
//        }
//    }
//}
