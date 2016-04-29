package com.bignerdranch.android.draganddraw;

import android.support.v4.app.Fragment;
import android.os.Bundle;

public class DragAndDrawActivity extends SingleFragmentActivity {

       @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
