package lzb.com.eventbusdemo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import de.greenrobot.event.EventBus;
import lzb.com.event.CountDownEvent;
import lzb.com.event.SecondActivityEvent;
import lzb.com.event.SetTextAEvent;
import lzb.com.event.SetTextBEvent;

public class MainActivity extends Activity implements View.OnClickListener {

    private TextView textView;
    private Button btnMain2MainA;
    private Button btnMain2MainB;
    private Button btnToSecondActivity;
    private Button btnAsync;
    private Button btnNonUIThread2MainThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        // ע�᣺���������ֱ��ǣ���Ϣ�����ߣ������ߣ������շ��������¼���
        EventBus.getDefault().register(this, "setTextA", SetTextAEvent.class);
        EventBus.getDefault().register(this, "setTextB", SetTextBEvent.class);
        // EventBus.getDefault().register(this,"messageFromSecondActivity",SecondActivityEvent.class);
        EventBus.getDefault().registerSticky(this, "messageFromSecondActivity",
                SecondActivityEvent.class);
        EventBus.getDefault().register(this, "countDown", CountDownEvent.class);

    }

    private void initView() {
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        btnMain2MainA = (Button) findViewById(R.id.btnMain2MainA);
        btnMain2MainB = (Button) findViewById(R.id.btnMain2MainB);
        btnToSecondActivity = (Button) findViewById(R.id.btnToSecondActivity);
        btnAsync = (Button) findViewById(R.id.btnAsync);
        btnNonUIThread2MainThread = (Button) findViewById(R.id.btnNonUIThread2MainThread);

        btnMain2MainA.setOnClickListener(this);
        btnMain2MainB.setOnClickListener(this);
        btnToSecondActivity.setOnClickListener(this);
        btnAsync.setOnClickListener(this);
        btnNonUIThread2MainThread.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this, SetTextAEvent.class,
                SetTextBEvent.class, SecondActivityEvent.class,
                CountDownEvent.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ��ע���Ӧ�ķ������Ͳ���,û�к�׺��Ĭ��ʹ��PostThreadģʽ�����������¼���ͬһ�߳�ִ��
     * 
     * @param event
     */
    public void setTextAMainThread(SetTextAEvent event) {
        if (event.text == null) {
            textView.setText("A");
        } else {
            textView.setText(event.text);
        }

    }

    public void setTextB(SetTextBEvent event) {
        textView.setText("B");
    }

    public void messageFromSecondActivity(SecondActivityEvent event) {
        textView.setText(event.getText());
    }

    /**
     * ��Async��׺���첽ִ�С�����MainThread��BackgroundThread���ֱ��������̣߳�UI��ִ�кͺ�̨�̣߳���һ��ִ��
     * 
     * @param event
     */
    public void countDownAsync(CountDownEvent event) {
        for (int i = event.getMax(); i > 0; i--) {
            Log.v("CountDown", String.valueOf(i));
            SystemClock.sleep(1000);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnMain2MainA:
            EventBus.getDefault().post(new SetTextAEvent());
            break;
        case R.id.btnMain2MainB:
            EventBus.getDefault().post(new SetTextBEvent());
            break;
        case R.id.btnToSecondActivity:
            // ����SecondActivity�з����¼���MainActivity����
            Intent intent = new Intent(getApplicationContext(),
                    SecondActivity.class);
            startActivity(intent);
            finish();
            break;
        case R.id.btnAsync:
            EventBus.getDefault().post(new CountDownEvent(99));
            break;
        case R.id.btnNonUIThread2MainThread:
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                    }
                    EventBus.getDefault().post(
                            new SetTextAEvent("from other thread"));
                }
            }).start();
            break;
        }
    }
}
