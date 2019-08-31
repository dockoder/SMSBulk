package pt.peachkoder.masssms.sms;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import android.telephony.SmsMessage;


public abstract class Message {

    private static String msg;


    // calculate the number of SMS's required to encode the message body.
    public static int calculateLength(String msg){

        int[] ret = SmsMessage.calculateLength(msg, false);
        return ret[1];
    }


    public String getMsg() {
        return msg;
    }

    public static void setMsg(String msg) {
        Message.msg = msg;
    }


}
