package org.unimelb.itime.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.unimelb.itime.R;
import org.unimelb.itime.ui.fragment.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.FragmentEventEndRepeat;
import org.unimelb.itime.ui.fragment.FragmentEventRepeat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentEventEndRepeat fragment = new FragmentEventEndRepeat();
//        FragmentEventRepeat fragment = new FragmentEventRepeat();
        getSupportFragmentManager().beginTransaction().add(R.id.frag_container, fragment).commit();
//        FragmentEventCreate fragmentEventCreate = new FragmentEventCreate();
//        getSupportFragmentManager().beginTransaction().add(R.id.frag_container,fragmentEventCreate).commit();
    }
}
