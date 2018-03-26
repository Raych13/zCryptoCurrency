package com.example.jingze.zcryptocurrency.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Jingze HUANG on Mar.25, 2018.
 */

public class SnackBarUtils {

    private static FindView findView;

    public static void setFindView(FindView findView) {
        SnackBarUtils.findView = findView;
    }

    public interface FindView {
        View findView();
    }

    public static Runnable getSnackBarRunnable(final String text, final int duration) {
        if (findView != null) {
            return new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findView.findView(), text, duration).show();
                }
            };
        }
        return null;
    }

    public static Runnable getSnackBarWithActionRunnable(final String text, final int duration, final String actionText, final View.OnClickListener onClickListener) {
        if (findView != null) {
            return new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(findView.findView(), text, duration).setAction(actionText, onClickListener).show();
                }
            };
        }
        return null;
    }

}
