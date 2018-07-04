package com.armhansa.app.grandwarp.for_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.ui.ChatsActivity;
import com.armhansa.app.grandwarp.ui.MainActivity;

public class MenuForTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_for_test);
    }

    public void toNavigationTest(View view) {
        startActivity(new Intent(MenuForTest.this, MainActivity.class));
    }

    public void toTest(View view) {
        startActivity(new Intent(MenuForTest.this, TestActivity.class));
    }

    public void toMessageTest(View view) {
        startActivity(new Intent(MenuForTest.this, TestMessageActivity.class));
    }

}
