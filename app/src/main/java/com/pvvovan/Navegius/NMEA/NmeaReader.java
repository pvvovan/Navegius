package com.pvvovan.Navegius.NMEA;

import com.pvvovan.Navegius.Geometry.GeoPoint;
import com.pvvovan.Navegius.Serial.ISerial;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Vovan on 15.05.2016.
 */
public class NmeaReader {
    private NmeaReader(){}

    public static NmeaReader Instance() {
        return m_Instance;
    }

    private static NmeaReader m_Instance = new NmeaReader();
    private ISerial m_serial;
    //private IGeoPointReceived m_pointReceived;
    private Timer m_Timer;
    private MyTimerTask m_MyTimerTask;
    private NmeaParser m_NmeaParser = new NmeaParser();

    public void Start(ISerial serial)//, IGeoPointReceived pointReceived)
    {
        m_serial = serial;
        m_serial.Open();
        //m_pointReceived = pointReceived;
        m_Timer = new Timer();
        m_MyTimerTask = new MyTimerTask();
        m_Timer.schedule(m_MyTimerTask, 100, 100);
    }

    LinkedList<IGeoPointReceived> m_pointReceivedListeners = new LinkedList<IGeoPointReceived>();
    public void AddGeoPointListener(IGeoPointReceived pointReceived)
    {
        m_pointReceivedListeners.add(pointReceived);
    }

    public void RemoveGeoPointListener(IGeoPointReceived pointReceived)
    {
        m_pointReceivedListeners.remove(pointReceived);
    }

    public void Stop()
    {
        m_Timer.cancel();
        m_serial.Close();
    }

    class MyTimerTask extends TimerTask {

        byte[] buffer = new byte[10240];

        @Override
        public void run() {
            if (m_serial.BytesAvailable() > 0) {
                int bytesRead = m_serial.Read(buffer, 0, buffer.length);
                String aString = new String(buffer, 0, bytesRead);

                try {
                    m_NmeaParser.ProcessMessage(aString);
                }
                catch (Exception ex)
                {
                    m_NmeaParser.ClearUnreadMessage();
                }

                GeoPoint p = new GeoPoint();
                p.Azimuth = m_NmeaParser.Azimuth;
                p.NumberOfSatellites = m_NmeaParser.NumberOfSatellites;
                p.Elevation = m_NmeaParser.Altitude;
                p.FixQuality = m_NmeaParser.FixQuality;
                p.Latitude = m_NmeaParser.Latitude;
                p.Longitude = m_NmeaParser.Longitude;
                p.Speed = m_NmeaParser.Speed;
                for(IGeoPointReceived handler : m_pointReceivedListeners)
                {
                    handler.GeoPointReceived(p);
                }
            }
        }
    }
}
