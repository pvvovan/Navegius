/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
#include <string.h>
#include <jni.h>
#include <fcntl.h>
#include <termios.h>

/* This is a trivial JNI example where we use a native method
 * to return a new VM String. See the corresponding Java source
 * file located at:
 *
 *   apps/samples/hello-jni/project/src/com/example/hellojni/HelloJni.java
 */



jint Java_com_pvvovan_Navegius_Serial_SerialPort_OpenSerial( JNIEnv* env, jobject thiz )
{
    int fd_serial = open("/dev/ttyS3", O_RDWR | O_NONBLOCK);
    if (fd_serial < 0)
    {
        return -1;
    }

    struct termios newtio;
    memset (&newtio, 0, sizeof newtio);
    newtio.c_cflag = B9600 | CS8 | CLOCAL | CREAD;
    newtio.c_iflag = IGNPAR;
    newtio.c_cc[VTIME]    = 0;
    newtio.c_cc[VMIN]     = 0;

    tcsetattr(fd_serial, TCSANOW, &newtio);

    return fd_serial;
}

jint Java_com_pvvovan_Navegius_Serial_SerialPort_CloseSerial( JNIEnv* env, jobject thiz, jint fd_serial)
{
    return close(fd_serial);
}

jint Java_com_pvvovan_Navegius_Serial_SerialPort_BytesAvailableSerial( JNIEnv* env, jobject thiz, jint fd_serial) {
    int bytes_avail = 0;
    ioctl(fd_serial, FIONREAD, &bytes_avail);
    return bytes_avail;
}

jint Java_com_pvvovan_Navegius_Serial_SerialPort_ReadSerial( JNIEnv* env, jobject thiz, jint fd_serial, jbyteArray jData, jint offset, jint size)
{
    int bytes_avail = 0;
    ioctl(fd_serial, FIONREAD, &bytes_avail);
    if (bytes_avail > 0) {
        if (bytes_avail > size)
        {
            bytes_avail = size;
        }
        char buff[bytes_avail];
        int bytes_read = read(fd_serial, buff, sizeof(buff));
        if (bytes_read > 0) {
            //jbyteArray jdata = (*env)->NewByteArray(env, bytes_read);
            (*env)->SetByteArrayRegion(env, jData, offset, bytes_read, (const jbyte*)buff);
        }
    }
    return bytes_avail;
}

jint Java_com_pvvovan_Navegius_Serial_SerialPort_WriteSerial( JNIEnv* env, jobject thiz, jint fd_serial, jbyteArray data, jint offset, jint size)
{
    char buff[size];
    (*env)->GetByteArrayRegion(env, data, offset, size, (jbyte*)buff);
    return write(fd_serial, buff, size);
}