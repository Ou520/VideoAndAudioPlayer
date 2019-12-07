package com.example.textthread.Service;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;
import android.widget.Toast;


//借助中介（ResultReceiver是接收器）发消息和接收消息
public class OnReceiveResult extends ResultReceiver {
    Context context;
    TextView tvBR;
    /**
     * Create a new ResultReceive to receive results.  Your
     * {@link #onReceiveResult} method will be called from the thread running
     * <var>handler</var> if given, or from an arbitrary thread if null.
     *
     * @param handler
     */
    public OnReceiveResult(Handler handler, Context context, TextView textView) {
        super(handler);
        this.context=context;
        this.tvBR=textView;
    }

    @Override
    //接收别的地方发来的ResultReceiver消息
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode ==0x120 && resultData !=null)//通过结果码来判断发送过来的是什么消息
        {
            Toast.makeText(context, resultData.getString("MyServiceKey","获取消息失败"), Toast.LENGTH_SHORT).show();
            tvBR.setText(resultData.getString("MyServiceKey"));

        }
    }
}
