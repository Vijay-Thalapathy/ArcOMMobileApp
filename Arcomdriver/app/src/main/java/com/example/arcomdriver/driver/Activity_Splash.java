package com.example.arcomdriver.driver;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.arcomdriver.R;
import com.example.arcomdriver.database.SQLiteController;
import com.example.arcomdriver.order.Activity_OrdersHistory;

/**Company: Isc Global Solutions (P) Ltd.
 * Project : 'ArcOM Driver App'
 * @author : SivaramYogesh
 * Created on : 01 Oct 2022*/
public class Activity_Splash extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 5000;
    private SQLiteController sqLiteController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                sqLiteController = new SQLiteController(Activity_Splash.this);
                sqLiteController.open();
                try {
                    long count = sqLiteController.fetchCount();
                    if (count > 0) {
                        startActivity(new Intent(Activity_Splash.this, Activity_OrdersHistory.class));
                        overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);
                    }else {
                        startActivity(new Intent(Activity_Splash.this, ActivitySignIn.class));
                        overridePendingTransition(R.anim.layout_anim_in, R.anim.layout_anim_out);

                    }
                } finally {
                    sqLiteController.close();
                }


            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void recreate() {
        super.recreate();
    }
}
