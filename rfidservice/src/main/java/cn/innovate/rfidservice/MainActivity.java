package cn.innovate.rfidservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import cn.innovate.rfidservice.entity.LogItem;

public class MainActivity extends Activity {

    public static final int MSG_LOG_CHANGED = 999;

    private ListView logview;
    private LogViewAdapter adapter = new LogViewAdapter();
    private  ServiceApp app;
    private TextView readerid;

    private Handler logChangeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOG_CHANGED:
                    adapter.notifyDataSetChanged();
                    readerid.setText("读卡器: " + RfidService.getDeviceId());
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private ServiceApp.LogChangedListener listener = new ServiceApp.LogChangedListener() {
        @Override
        public void logChanged() {
            logChangeHandler.sendEmptyMessage(MSG_LOG_CHANGED);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app =  ServiceApp.getInstance();
        setContentView(R.layout.activity_main);
        logview = (ListView) findViewById(R.id.log_view);
        logview.setAdapter(adapter);

        readerid = (TextView) findViewById(R.id.label_readerid);
        app.setLogChangedListener(listener);
        app.d("初始化服务设置界面" );
    }

    public void clearLogs(View view) {
        app.clearLogs();
    }

    class LogItemWrapper {
        TextView messageView;
        TextView dateView;
    }

    class LogViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return app.getLogs().size();
        }

        @Override
        public Object getItem(int position) {
            return app.getLogs().get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LogItemWrapper itemView;
            if (convertView == null) {
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.log_item_view, null);
                itemView = new LogItemWrapper();
                itemView.messageView = (TextView) convertView.findViewById(R.id.log_item_message);
                itemView.dateView = (TextView) convertView.findViewById(R.id.log_item_date);
                convertView.setTag(itemView);
            } else {
                itemView = (LogItemWrapper) convertView.getTag();
            }

            LogItem logItem = (LogItem) getItem(position);
            itemView.messageView.setText(logItem.getMessage());
            itemView.dateView.setText(
                    new SimpleDateFormat("MM-dd hh:mm:ss.SSS").format(logItem.getWhen())
            );

            return convertView;
        }
    }

    public void initDevice(View view) {
        Intent intent = new Intent(this, DeviceAttachedActivity.class);
        startActivity(intent);
    }

    public void closeDevice(View view) {
        RfidService.closeController();
    }

    @Override
    protected void onStop() {
        super.onStop();
        app.d("服务器设置界面关闭");
    }
}
