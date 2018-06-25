package com.armhansa.app.grandwarp.for_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.ui.Main2Activity;
import com.armhansa.app.grandwarp.ui.TestMessageActivity;

public class MenuForTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_for_test);
    }

    public void toMessageTest(View view) {
        startActivity(new Intent(MenuForTest.this, TestMessageActivity.class));
    }

    public void toNavigationTest(View view) {
        startActivity(new Intent(MenuForTest.this, Main2Activity.class));
    }

}
