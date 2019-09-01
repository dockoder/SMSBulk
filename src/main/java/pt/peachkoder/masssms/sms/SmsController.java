package pt.peachkoder.masssms.sms;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import pt.peachkoder.masssms.R;
import pt.peachkoder.masssms.listview.ListViewItemDTO;

// Class SmsController :
//
// Handle sms permissions on android.
// Sends sms
public class SmsController {

    private static final int SMS_PERMISSION_CODE = 0;

    private Activity activity;

    private SmsManager smsManager ;

    private List<Recipient> recipientsSMSNotSent = new ArrayList<>();

    private static  SmsController instance ;

    private static final long serialVersionUID = 123456789L;

    private int msgPerMinute = 1;

    private ReentrantLock lock = new ReentrantLock();

    private Handler customHandler = new Handler();

    private final AtomicBoolean running = new AtomicBoolean(false);

    // Bill Pugh singleton Pattern --------------------------------------------------------
    private SmsController() {

        // protect against instantiation via reflection
        if(instance != null) {

            throw new IllegalStateException("Singleton already initialized");

        }

        smsManager = SmsManager.getDefault();
    }

    // avoid multithreading overehead
    private static class SingletonHelper {

        private static final SmsController INSTANCE = new SmsController();

    }

    public static SmsController getInstance(){

        if (instance==null){
            instance = SingletonHelper.INSTANCE;
        }
        return instance;
    }

    // Protection against Serialization
    protected Object readResolve() {

        return getInstance();
    }
    // Bill Pugh singleton Pattern --------------------------------------------------------


    public boolean hasPermission() {

        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }


    public void showRequestPermissionsInfoAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity.getApplicationContext());

        builder.setTitle(R.string.permission_dialog_title);

        builder.setMessage(R.string.permission_dialog_message);

        builder.setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestSendSmsPermission();
            }
        });

        builder.show();
    }

    public void send(String number, String smsBody) {

        if (!isValidPhoneNumber(number)) return;

        lock.lock();
        try {

            smsManager.sendTextMessage( number,null,smsBody,null,null);

        } catch (Exception e) {

            recipientsSMSNotSent.add(new Recipient(number, e.getMessage()));

        } finally {

            lock.unlock();
        }

    }


    public void broadcast(List<ListViewItemDTO> list, String msg, int freq) {

        msgPerMinute = freq;

        recipientsSMSNotSent.clear();

        customHandler.postDelayed(senderThread, 0);

        Iterator<ListViewItemDTO> it = list.iterator();

        while (it.hasNext()) {

            send(it.next().getNumber(), msg);

        }


    }

    public void stop(){

        customHandler.removeCallbacks(senderThread);
    }



    private Runnable senderThread = new Runnable() {
        long startTime = SystemClock.elapsedRealtime();
        int interval = 60000 / msgPerMinute;

        @Override
        public void run() {

            running.set(true);

            while (running.get()){

                try {

                    long endTime = SystemClock.elapsedRealtime();

                    if(endTime-startTime >= interval){

                    }

                } catch (Exception ignored){


                } finally {

                    Thread.currentThread().interrupt();

                }

            }

        }
    };


    private static boolean isValidPhoneNumber(String phoneNumber) {

        return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
    }

    private void requestSendSmsPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_SMS)) {
            //TODO
        }
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS}, SMS_PERMISSION_CODE);
    }


    public List<Recipient> getRecipientsSMSNotSent() {

        return recipientsSMSNotSent;
    }

    // Helper class used to hold number & message of SMS's not sent
    public class Recipient {

        public final String number;

        public final String errorMessage;

        public Recipient(String number, String errorMessage) {

            this.number = number;

            this.errorMessage = errorMessage;
        }
    }




    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Activity getActivity() {
        return activity;
    }


    public AtomicBoolean getRunning() { return running; }
}


