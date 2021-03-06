package com.leo.myactivityoptions;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.leo.myactivityoptions.utils.CircleProgressView;
import com.leo.myactivityoptions.utils.ProgressInterceptor;
import com.leo.myactivityoptions.utils.ProgressListener;

import static com.leo.myactivityoptions.Comment.urls;

/**
 * Created by Mr_Wrong on 15/10/6.
 */
public class LargePicFragment extends Fragment {
    private SecondActivity activity;
    private int index;
    private ImageView image;
    CircleProgressView progressView;//进度条

    public static Fragment newFragment(int index) {
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        LargePicFragment fragment = new LargePicFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (SecondActivity) context;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        index = getArguments().getInt("index");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewer, container, false);
        image = view.findViewById(R.id.image);
        progressView = view.findViewById(R.id.progressView);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.supportFinishAfterTransition();
            }
        });


        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                getActivity().supportStartPostponedEnterTransition();
                return true;
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();


        ProgressInterceptor.addListener(urls.get(index), new ProgressListener() {
            @Override
            public void onProgress(int progress) {
                progressView.setProgress(progress);
            }
        });

        Glide.with(activity)
                .load(urls.get(index))
                .dontAnimate()
                .into(new GlideDrawableImageViewTarget(image) {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        progressView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);

                        progressView.setVisibility(View.GONE);
                        ProgressInterceptor.removeListener(urls.get(index));

                    }
                });


    }

    public View getSharedElement() {
        return image;
    }


}
