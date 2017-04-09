package com.serge.hillcipher;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by serge on 07.04.2017.
 */

public class MainActivity extends AppCompatActivity {
    Button processBtn;
    EditText userMsg;
    TextView decUserMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processBtn = (Button) findViewById(R.id.processBtn);
        processBtn.setOnClickListener(onClickListener);
        userMsg = (EditText) findViewById(R.id.userMsg);
        decUserMsg = (TextView) findViewById(R.id.decUserMsg);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.processBtn)
            {
                String msg = userMsg.getText().toString();
                HillCipher hillCipher = new HillCipher();
                Matrix[] encMsg = hillCipher.EncryptMessage(msg);
                String decMsg = hillCipher.DecryptMessage(encMsg);
                decUserMsg.setText(decMsg);
            }
        }
    };

}
