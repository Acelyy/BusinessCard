package cn.innovate.rfidservice;

import android.app.Application;
import android.content.Intent;
import java.util.LinkedList;
import java.util.List;
import cn.innovate.rfidservice.entity.LogItem;

/**
 * Created by sofox on 16-7-12.
 */
public class ServiceApp extends Application {

    public interface LogChangedListener {
        void logChanged();
    }

    private static ServiceApp instance;
    private LogChangedListener logChangedListener = null;
    private LinkedList<LogItem> logs = new LinkedList<LogItem>();
    private int maxLogs = 1000;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this, this);
        Intent intent = new Intent(this, RfidService.class);
        d("读卡器服务程序启动...");
        startService(intent);

    }

    protected void logChanged() {
        if(logChangedListener != null) {
            logChangedListener.logChanged();
        }
    }

    protected void limitLogSize() {
        while (logs.size() > maxLogs)
            logs.removeLast();
    }


    public synchronized void d(String message) {
        logs.addFirst(new LogItem(message));
        limitLogSize();
        logChanged();
    }

    public List<LogItem> getLogs() {
        return logs;
    }

    public int getMaxLogs() {
        return maxLogs;
    }

    public void setMaxLogs(int maxLogs) {
        int newsize = Math.min(maxLogs, 100);
        this.maxLogs = newsize;
        if (logs.size() > newsize) {
            limitLogSize();
            logChanged();
        }
    }

    public void clearLogs() {
        logs.clear();
        d("清除日志");
    }

    public static ServiceApp getInstance() {
        return instance;
    }

    public void deviceInitialized() {
        d("设备初始化成功!");
    }

    public void setLogChangedListener(LogChangedListener logChangedListener) {
        this.logChangedListener = logChangedListener;
    }
}
