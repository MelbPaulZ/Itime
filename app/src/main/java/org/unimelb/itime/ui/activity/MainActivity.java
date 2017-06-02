package org.unimelb.itime.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.FragmentEventCreate;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentEventCreate fragmentEventCreate = new FragmentEventCreate();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,fragmentEventCreate).commit();
    }
}
