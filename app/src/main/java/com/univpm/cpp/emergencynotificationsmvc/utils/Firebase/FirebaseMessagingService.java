package com.univpm.cpp.emergencynotificationsmvc.utils.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.univpm.cpp.emergencynotificationsmvc.MainActivity;
import com.univpm.cpp.emergencynotificationsmvc.R;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.Beacon;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModel;
import com.univpm.cpp.emergencynotificationsmvc.models.beacon.BeaconModelImpl;

import org.json.JSONException;
import org.json.JSONObject;

import static com.univpm.cpp.emergencynotificationsmvc.utils.Firebase.FirebaseMessagingService.Message.NOTIFICATION_ACC;
import static com.univpm.cpp.emergencynotificationsmvc.utils.Firebase.FirebaseMessagingService.Message.NOTIFICATION_HUM;
import static com.univpm.cpp.emergencynotificationsmvc.utils.Firebase.FirebaseMessagingService.Message.NOTIFICATION_TEMP;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public class Message{

        static public final String NOTIFICATION_TEMP = "temperature";
        static public final String NOTIFICATION_HUM = "humidity";
        static public final String NOTIFICATION_ACC = "acceleration";

        private String idBeacon;
        private String notification;


        public Message(String jsonMessage){
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonMessage);
                this.idBeacon = jsonObject.getString("beacon");
                this.notification = jsonObject.getString("notification");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public String getIdBeacon() {
            return idBeacon;
        }

        public void setIdBeacon(String idBeacon) {
            this.idBeacon = idBeacon;
        }

        public String getNotification() {
            return notification;
        }

        public void setNotification(String notification) {
            this.notification = notification;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "idBeacon='" + idBeacon + '\'' +
                    ", notification='" + notification + '\'' +
                    '}';
        }
    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.w("message", remoteMessage.getData().get("message"));
        Message message = new Message(remoteMessage.getData().get("message"));
        if (message != null) Log.w("Message", message.toString());
        showNotification(message);


    }

    private void showNotification(Message message){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("prova", message.getIdBeacon());
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);



        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Emergenza!!")
                .setContentText(buildMessage(message))
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

    private String buildMessage(Message message){
        String mex = null;
        switch (message.notification){
            case NOTIFICATION_TEMP:
                mex = "Rilevato incendio in ";
                break;
            case NOTIFICATION_HUM:
                mex = "Rilevato allagamento in ";
                break;
            case NOTIFICATION_ACC:
                mex = "Rilevato evento sismico in ";
                break;
            default:
                mex = "Rilevata emergenza in ";
        }

        BeaconModel beaconModel = new BeaconModelImpl();
        Beacon beacon = beaconModel.getBeaconById(message.getIdBeacon());
        mex += beacon.getNode().getNodename();

        return mex;
    }

}
