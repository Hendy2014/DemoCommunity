package com.lhy.comm.ui.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.lhy.comm.R;
import com.lhy.comm.utils.ImageUtils;

import static com.lhy.comm.utils.GlobalContext.getApplicationContext;


/**
 * A singleton cache that holds tinted drawable resources for displaying messages, such as
 * message bubbles, audio attachments etc.
 */
public class ConversationDrawables {
    private static ConversationDrawables sInstance;

    private Drawable mAudioPlayButtonDrawable;
    private Drawable mAudioPauseButtonDrawable;
    private Drawable mAudioPauseButtonDrawable2;
    private final Context mContext;
    private int mThemeColor;

    public static ConversationDrawables get() {
        if (sInstance == null) {
            sInstance = new ConversationDrawables(getApplicationContext());
        }
        return sInstance;
    }

    private ConversationDrawables(final Context context) {
        mContext = context;
        // Pre-create all the drawables.
        updateDrawables();
    }

    public int getConversationThemeColor() {
        return mThemeColor;
    }

    public void updateDrawables() {
        final Resources resources = mContext.getResources();
        mAudioPlayButtonDrawable = resources.getDrawable(R.drawable.hy_card_audio_3);
        mAudioPauseButtonDrawable = resources.getDrawable(R.drawable.hy_card_audio_3);
        mAudioPauseButtonDrawable2 = resources.getDrawable(R.drawable.hy_card_audio_1);
        mThemeColor = resources.getColor(R.color.hy_primary_color);
    }

    public Drawable getPlayButtonDrawable(final boolean incoming) {
        return ImageUtils.getTintedDrawable(
                mContext, mAudioPlayButtonDrawable);
    }

    public Drawable getPauseButtonDrawable(final boolean incoming) {
        return ImageUtils.getTintedDrawable(
                mContext, mAudioPauseButtonDrawable);
    }

    public Drawable getPauseButtonDrawable2(final boolean incoming) {
        return ImageUtils.getTintedDrawable(
                mContext, mAudioPauseButtonDrawable2);
    }
}
