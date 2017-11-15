package com.yonggang.liyangyang.businesscard.app_landscape;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.NfcA;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Goods;
import com.yonggang.liyangyang.businesscard.app_landscape.Entry.Nfc;
import com.yonggang.liyangyang.businesscard.app_landscape.http.HttpUtil;
import com.yonggang.liyangyang.businesscard.app_landscape.request.GetCardBySn;
import com.yonggang.liyangyang.businesscard.app_landscape.response.GetCardByCodeResult;
import com.yonggang.liyangyang.businesscard.app_landscape.util.MD5;
import com.yonggang.liyangyang.businesscard.app_landscape.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardActivity extends BaseActivity {

    private NfcAdapter nfcAdapter;
    public List<Nfc> list;
    private Intent intents;
    private boolean isnews = true;
    private PendingIntent pendingIntent;
    private IntentFilter[] mFilters;
    private String[][] mTechLists;

    @BindView(R.id.edit_card_id)
    TextView editCardId;

    double sum;
    ArrayList<Goods> data;

    private String card_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        setTitle("永联旅游一卡通——收银系统");
        ButterKnife.bind(this);
        sum = getIntent().getExtras().getDouble("sum");
        data = (ArrayList<Goods>) getIntent().getExtras().getSerializable("data");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            Toast.makeText(this, "设备不支持NFC", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "请打开NFC功能", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        ndef.addCategory("*/*");
        mFilters = new IntentFilter[]{ndef};// 过滤器
        mTechLists = new String[][]{
                new String[]{MifareClassic.class.getName()},
                new String[]{NfcA.class.getName()}};// 允许扫描的标签类型
    }

    @OnClick(R.id.btn_complete)
    public void onClick() {
        if (card_id == null || "".equals(card_id)) {
            Toast.makeText(this, "卡号为空", Toast.LENGTH_SHORT).show();
            return;
        }
        card_id = card_id.replace(" ", "");
        Log.e("s", card_id);
        //Toast.makeText(this, card_id, Toast.LENGTH_SHORT).show();
        getCardBySn(Long.parseLong(card_id.trim()));
    }

    private void getCardBySn(long sn) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("获取中");
        dialog.show();
        final GetCardBySn gcb = HttpUtil.getService(GetCardBySn.class);
        gcb.doPost(Util.getDeviceNo(this), Util.getAuthCode(this), MD5.GetMD5Code(sn + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetCardByCodeResult>() {
                    @Override
                    public void onCompleted() {
                        dialog.dismiss();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("e", e.toString());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNext(GetCardByCodeResult gcbr) {
                        Log.i("s", gcbr.toString());
                        String flag = gcbr.getFlag();
                        if ("100".equals(flag)) {
                            Bundle bundle = new Bundle();
                            bundle.putDouble("sum", sum);
                            bundle.putSerializable("data", data);
                            bundle.putSerializable("card", gcbr.getCard());
                            bundle.putString("flag", "刷卡");
                            goActivity(bundle, PayActivity.class);
                        } else {
                            Toast.makeText(CardActivity.this, gcbr.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 得到是否检测到ACTION_TECH_DISCOVERED触发
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
                mTechLists);
        if (isnews) {
            if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(getIntent()
                    .getAction())) {
                // 处理该intent
                processIntent(getIntent());
                intents = getIntent();
                isnews = false;
            }
        }
    }

    // 字符序列转换为16进制字符串
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);

            stringBuilder.append(buffer);

        }
        return stringBuilder.toString();
    }

    private String bytesToHexString2(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);

            stringBuilder.append(buffer);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        // 得到是否检测到ACTION_TECH_DISCOVERED触发
        // nfcAdapter.enableForegroundDispatch(this, pendingIntent, mFilters,
        // mTechLists);
        if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
            // 处理该intent
            processIntent(intent);
            intents = intent;
        }
    }

    /**
     * 读取NFC信息数据
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        boolean auth = false;
        String cardStr = "";
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        cardStr = "ID：" + bytesToHexString(tag.getId());
        String[] techList = tag.getTechList();
        boolean haveMifareUltralight = false;
        cardStr += "\r\nTECH：";
        for (String tech : techList) {
            cardStr += tech + ",";
            if (tech.indexOf("MifareClassic") >= 0) {
                haveMifareUltralight = true;
                break;
            }
        }
        Log.i("str", cardStr);
        if (!haveMifareUltralight) {
            Toast.makeText(this, "this card type is not MifareClassic", Toast.LENGTH_LONG).show();
            return;
        }
        MifareClassic mfc = MifareClassic.get(tag);

        try {
            String metaInfo = "";
            // Enable I/O operations to the tag from this TagTechnology object.
            mfc.connect();
            int type = mfc.getType();// 获取TAG的类型
            int sectorCount = mfc.getSectorCount();// 获取TAG中包含的扇区数
            String typeS = "";
            switch (type) {
                case MifareClassic.TYPE_CLASSIC:
                    typeS = "TYPE_CLASSIC";
                    break;
                case MifareClassic.TYPE_PLUS:
                    typeS = "TYPE_PLUS";
                    break;
                case MifareClassic.TYPE_PRO:
                    typeS = "TYPE_PRO";
                    break;
                case MifareClassic.TYPE_UNKNOWN:
                    typeS = "TYPE_UNKNOWN";
                    break;
            }
            byte[] myNFCID = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            int before = (int) Long.parseLong(bytesToHexString(myNFCID), 16);
            int r24 = before >> 24 & 0x000000FF;
            int r8 = before >> 8 & 0x0000FF00;
            int l8 = before << 8 & 0x00FF0000;
            int l24 = before << 24 & 0xFF000000;
            metaInfo += "ID(dec):"
                    + Long.parseLong(
                    Integer.toHexString((r24 | r8 | l8 | l24)), 16)
                    + "\nID(hex):" + bytesToHexString2(myNFCID) + "\nType："
                    + typeS + "\nSector：" + sectorCount + "\n Block："
                    + mfc.getBlockCount() + "\nSize： " + mfc.getSize() + "B";
            Log.i("string", metaInfo);
            card_id = Long.parseLong(Integer.toHexString((r24 | r8 | l8 | l24)), 16) + "";
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < card_id.length(); i++) {
                sb.append("*");
            }
            editCardId.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (mfc != null) {
                    mfc.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
