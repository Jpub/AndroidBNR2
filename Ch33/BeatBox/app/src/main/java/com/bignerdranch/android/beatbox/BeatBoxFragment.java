package com.bignerdranch.android.beatbox;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

public class BeatBoxFragment extends Fragment {

    private BeatBox mBeatBox;
    private View mRedFill;

    public static BeatBoxFragment newInstance() {
        return new BeatBoxFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mBeatBox = new BeatBox(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beat_box, container, false);

        //------ 33장에서 추가된 코드(리스트 33.4) --------------------------*
        mRedFill = view.findViewById(R.id.red_fill);
        mRedFill.setVisibility(View.INVISIBLE);
        //-------------------------------------------------------------------*

        RecyclerView recyclerView = (RecyclerView)view
                .findViewById(R.id.fragment_beat_box_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBeatBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private Button mButton;
        private Sound mSound;

        public SoundHolder(LayoutInflater inflater, ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_sound, container, false));

            mButton = (Button)itemView.findViewById(R.id.list_item_sound_button);
            mButton.setOnClickListener(this);
        }

        public void bindSound(Sound sound) {
            mSound = sound;
            mButton.setText(mSound.getName());
        }

        @Override
        public void onClick(View v) {
            //------ 33장에서 추가된 코드(리스트 33.4) --------------------------*
            int[] clickCoords = new int[2];
            v.getLocationOnScreen(clickCoords);

            clickCoords[0] += (v.getWidth() / 2);
            clickCoords[1] += (v.getHeight() / 2);

            performRevealAnimation(mRedFill, clickCoords[0], clickCoords[1]);
            //-------------------------------------------------------------------*
            mBeatBox.play(mSound);
        }
    }

    //------ 33장에서 추가된 코드(리스트 33.5) ----------------------------------------------*
    private void performRevealAnimation(final View view, int screenCenterX, int screenCenterY) {
        int[] animatingViewCoords = new int[2];
        view.getLocationOnScreen(animatingViewCoords);
        int centerX = screenCenterX - animatingViewCoords[0];
        int centerY = screenCenterY - animatingViewCoords[1];

        Point size = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getSize(size);
        int maxRadius = size.y;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setVisibility(View.VISIBLE);
            Animator animator = ViewAnimationUtils.createCircularReveal(view, centerX, centerY, 0, maxRadius);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });
            animator.start();
        }
    }
    //---------------------------------------------------------------------------------------*

    private class SoundAdapter extends RecyclerView.Adapter<SoundHolder> {

        private List<Sound> mSounds;

        public SoundAdapter(List<Sound> sounds) {
            mSounds = sounds;
        }

        @Override
        public SoundHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new SoundHolder(inflater, parent);
        }

        @Override
        public void onBindViewHolder(SoundHolder soundHolder, int position) {
            Sound sound = mSounds.get(position);
            soundHolder.bindSound(sound);
        }

        @Override
        public int getItemCount() {
            return mSounds.size();
        }
    }

}

