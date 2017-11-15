package cn.innovate.rfidservice;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by sofox on 16-7-13.
 */
public class LocalNotifier {
    public static final void notify(String ticker, String title, String message) {
        Context context = ServiceApp.getInstance();
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.notify)
                .setWhen(System.currentTimeMillis())
                .setTicker(ticker)
                .setAutoCancel(true)
                .setOngoing(true)

                .setContentText(message)
                .setContentTitle(title);

        Notification notify = builder.build();

        notify.defaults = Notification.DEFAULT_LIGHTS;
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notify);
    }
}
