package com.pvvovan.Navegius.Serial;

/**
 * Created by Vovan on 14.05.2016.
 */
public class SerialPort implements ISerial {
    public SerialPort(){}

    int m_serial_fd;
    boolean m_IsOpen = false;

    @Override
    public boolean IsOpen() {
        return m_IsOpen;
    }

    @Override
    public void Open() {
        if (m_IsOpen)
            return;

        m_serial_fd = OpenSerial();
        if (m_serial_fd > 0)
        {
            m_IsOpen = true;
        }
    }



    @Override
    public int BytesAvailable()
    {
        if (!m_IsOpen)
            return 0;

        return BytesAvailableSerial(m_serial_fd);
    }

    @Override
    public void Close() {
        if (!m_IsOpen)
            return;

        CloseSerial(m_serial_fd);
        m_IsOpen = false;
    }

    @Override
    public int Read(byte[] buffer, int offset, int size) {
        if (!m_IsOpen)
            return 0;

        int nb = ReadSerial(m_serial_fd, buffer, offset, size);
        return nb;
    }

    @Override
    public int Write(byte[] buffer, int offset, int size) {
        if (!m_IsOpen)
            return 0;

        return WriteSerial(m_serial_fd, buffer, offset, size);
    }

    static {
        System.loadLibrary("serial_c-jni");
    }

    public native int OpenSerial();
    public native int CloseSerial(int fd_Serial);
    public native int ReadSerial(int fd_Serial, byte[] data, int offset, int size);
    public native int BytesAvailableSerial(int fd_Serial);
    public native int WriteSerial(int fd_Serial, byte[] data, int offset, int size);
}
