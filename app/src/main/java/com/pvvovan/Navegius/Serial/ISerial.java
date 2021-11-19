package com.pvvovan.Navegius.Serial;

/**
 * Created by Vovan on 14.05.2016.
 */
public interface ISerial {
    boolean IsOpen();
    void Open();
    void Close();
    int BytesAvailable();
    int Read(byte[] buffer, int offset, int size);
    int Write(byte[] buffer, int offset, int size);
}
