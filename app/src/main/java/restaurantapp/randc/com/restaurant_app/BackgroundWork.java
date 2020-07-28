package restaurantapp.randc.com.restaurant_app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class BackgroundWork extends Worker {


    int notificationId = 1001;
    private String uid;
    private DatabaseReference mDatabaseReference;
    private Context context;

    public BackgroundWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        uid = FirebaseAuth.getInstance().getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @NonNull
    @Override
    public Result doWork() {

        notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if (uid != null && uid.length() > 0) {
            mDatabaseReference.child(Constants.notifications).child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.exists()) {
                        boolean notify = (Boolean) snapshot.child(Constants.notify_fire).getValue();
                        if (notify) {
                            showNotification(true);
                        } else {
                            showNotification(false);
                        }
                    } else {
                        showNotification(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    showNotification(false);

                }
            });

        }


        return Result.success();


    }

    private void showNotification(boolean success) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());

        mBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        mBuilder.setContentTitle("Donation App");

        if (success) {

            mDatabaseReference.child(Constants.notifications).child(uid).child(Constants.notifyText_fire).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Log.d("TAG", "doInBackground: getting requests");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            mBuilder.setContentText(snapshot.getValue().toString());
                            NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(snapshot.getValue().toString()));
                            PendingIntent contentIntent =
                                    PendingIntent.getActivity(context, 0, new Intent(context, Main_Activity.class), PendingIntent.FLAG_UPDATE_CURRENT);
                            mBuilder.setAutoCancel(true);
                            mBuilder.setContentIntent(contentIntent);
                            if (manager != null) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    String channelId = "Restaurant App";
                                    NotificationChannel channel = new NotificationChannel(
                                            channelId,
                                            "Background Task",
                                            NotificationManager.IMPORTANCE_HIGH);
                                    manager.createNotificationChannel(channel);
                                    mBuilder.setChannelId(channelId);
                                }


                                manager.notify(notificationId, mBuilder.build());
                                notificationId++;
                                Log.d("TAG", "doWork: notification sent");
                            } else {
                                Log.d("TAG", "doWork: manager is null");

                            }
                        }
                    }
                    mDatabaseReference.child(Constants.notifications).child(uid).child(Constants.notify_fire).setValue(false);
                    mDatabaseReference.child(Constants.notifications).child(uid).child(Constants.notifyText_fire).removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {
            Log.d("tag", "No notifications");
        }
    }


}
