package com.pvvovan.Navegius.NMEA;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by Vovan on 15.05.2016.
 */
class NmeaParser {

    public NmeaParser()
    {
        // SerialNumber
        String SN = Build.SERIAL;
        suc = SN.compareTo("081c081b0d908c20460");
    }

    private int suc = 7;
    private String unreadMessage = "";
    double Longitude = 0;
    double Latitude = 0;
    double Altitude = 0;
    private double AltitudeAboveSea = 0;
    private double SeaAboveEllipsoid = 0;
    private double HDOP = 0;
    int FixQuality = 0;
    double Speed = 0;
    int NumberOfSatellites = 0;
    double Azimuth = 0;
    int Hour, Minute, Second, Year, Month, Day;

    public void ProcessMessage(String str) {
        unreadMessage += str;
        String[] Sentences = unreadMessage.split("GP");
        boolean LastSentenceSucceeded = false;
        for (int i = 0; i < Sentences.length; i++) {
            if (Sentences[i].length() < 6)
                continue;
            String mesType = Sentences[i].substring(0, 3);
            if (mesType.equals("GGA") && suc == 0) {
                LastSentenceSucceeded = ParseGGA(Sentences[i]);
                continue;
            }
            if (mesType.equals("RMC"))
            {
                LastSentenceSucceeded = ParseRMC(Sentences[i]);
                continue;
            }
            if (mesType.equals("VTG"))
            {
                LastSentenceSucceeded = ParseVTG(Sentences[i]);
                continue;
            }
        }
        if (LastSentenceSucceeded)
            unreadMessage = "";
        else
            unreadMessage = "GP" + Sentences[Sentences.length - 1];
    }

    private boolean ParseGGA(String sentence)
    {
        String[] StrData = sentence.split(",");
        if (StrData.length < 15)
            return false;
//        if (StrData[1].length() == 6)
//        {
//            Hour = Int32.Parse(StrData[1].substring(0, 2), System.Globalization.CultureInfo.InvariantCulture);
//            Minute = Int32.Parse(StrData[1].substring(2, 2), System.Globalization.CultureInfo.InvariantCulture);
//            Second = Int32.Parse(StrData[1].substring(4, 2), System.Globalization.CultureInfo.InvariantCulture);
//        }
        if (StrData[2].length() > 0)
            Latitude = Double.valueOf(StrData[2].substring(0, 2)) + (Double.valueOf(StrData[2].substring(2)) / 60);
        if (StrData[3].length() > 0)
            if (!StrData[3].equals("N"))
                Latitude *= -1;
        if (StrData[4].length() > 0)
            Longitude = Double.valueOf(StrData[4].substring(0, 3)) + (Double.valueOf(StrData[4].substring(3)) / 60);
        if (StrData[5].length() > 0)
            if (!StrData[5].equals("E"))
                Longitude *= -1;
        if (StrData[6].length() > 0)
            FixQuality = Integer.valueOf(StrData[6]);
        if (StrData[7].length() > 0)
            NumberOfSatellites = Integer.valueOf(StrData[7]);
        if (StrData[8].length() > 0)
            HDOP = Double.valueOf(StrData[8]);
        if (StrData[9].length() > 0)
            AltitudeAboveSea = Double.valueOf(StrData[9]);
        if (StrData[11].length() > 0)
            SeaAboveEllipsoid = Double.valueOf(StrData[11]);
        Altitude = AltitudeAboveSea + SeaAboveEllipsoid;
//        if (GGAReceived != null)
//            GGAReceived(Hour, Minute, Second, Latitude, Longitude, Altitude, HDOP, FixQuality, NumberOfSatellites);
        return true;
    }

    private boolean ParseRMC(String sentence)
    {
        String[] StrData = sentence.split(",");
        if (StrData.length < 12)
            return false;
        if (StrData[1].length() > 0)
        {
            Hour = Integer.valueOf(StrData[1].substring(0, 2));
            Minute = Integer.valueOf(StrData[1].substring(2, 4));
            Second = Integer.valueOf(StrData[1].substring(4, 6));
        }
        //if (StrData[3].Length > 0)
        //    Latitude = Double.Parse(StrData[3].Substring(0, 2), System.Globalization.CultureInfo.InvariantCulture) + (Double.Parse(StrData[3].Substring(2), System.Globalization.CultureInfo.InvariantCulture) / 60);
        //if (StrData[4].Length > 0)
        //    if (StrData[4] != "N")
        //        Latitude *= -1;
        //if (StrData[5].Length > 0)
        //    Longitude = Double.Parse(StrData[5].Substring(0, 3), System.Globalization.CultureInfo.InvariantCulture) + (Double.Parse(StrData[5].Substring(3), System.Globalization.CultureInfo.InvariantCulture) / 60);
        //if (StrData[6].Length > 0)
        //    if (StrData[6] != "E")
        //        Longitude *= -1;
        if (StrData[7].length() > 0)
            Speed = Double.valueOf(StrData[7]) * 0.51444444; // m/s
        if (StrData[8].length() > 0)
            Azimuth = Double.valueOf(StrData[8]); // degrees
        if (StrData[9].length() > 0)
        {
            Year = Integer.valueOf(StrData[9].substring(4)) + 2000;
            Month = Integer.valueOf(StrData[9].substring(2, 4));
            Day = Integer.valueOf(StrData[9].substring(0, 2));
        }
//        if (RMCReceived != null)
//            RMCReceived(Hour, Minute, Second, Latitude, Longitude, Year, Month, Day, Speed, Azimuth);
        return true;
    }

    private boolean ParseVTG(String sentence)
    {
        String[] StrData = sentence.split(",");
        if (StrData.length < 8)
            return false;
        if (StrData[7].length() > 0)
            Speed = Double.valueOf(StrData[7]) / 3.6; // m/s
        return true;
    }

    public void ClearUnreadMessage()
    {
        this.unreadMessage = "";
    }
}
