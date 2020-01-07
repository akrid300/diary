package com.example.agenda.ui.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.agenda.R;

public class ContextMenuManager implements View.OnAttachStateChangeListener {

    @SuppressLint("StaticFieldLeak")
    private static ContextMenuManager instance;

    public boolean isContextMenuShowing() {
        return isContextMenuShowing;
    }

    private boolean isContextMenuShowing;
    private Context context;
    private ContextMenu contextMenuView;
    private boolean isContextMenuDismissing;

    private ContextMenuManager(Context context) {
        this.context = context;
    }

    public static ContextMenuManager getInstance(Context context) {
        if (instance == null) {
            instance = new ContextMenuManager(context);
        }
        return instance;
    }

    /**
     * Shows the context menu from a View position
     *
     * @param openingView The View it was opened from
     * @param position    The feed item position
     * @param listener    The onclick listener
     * @param below       Show the menu be rendered below the View?
     */
    public void showContextMenuFromView(final View openingView, int position, ContextMenu.OnContextMenuItemClickListener listener, final boolean below) {
        if (!isContextMenuShowing) {
            isContextMenuShowing = true;
            contextMenuView = new ContextMenu(openingView.getContext());
            contextMenuView.bindToItem(position);
            contextMenuView.addOnAttachStateChangeListener(this);
            contextMenuView.addOnMenuItemClickListener(listener);

            ((ViewGroup) openingView.getRootView().findViewById(android.R.id.content)).addView(contextMenuView);

            contextMenuView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contextMenuView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setupContextMenuInitialPosition(openingView);
                    performShowAnimation(below);
                    return false;
                }
            });
        }
    }

    private void setupContextMenuInitialPosition(View openingView) {
        final int[] openingViewLocation = new int[2];
        openingView.getLocationOnScreen(openingViewLocation);
        int additionalBottomMargin = 0;
        contextMenuView.setTranslationX(openingViewLocation[0]);
        contextMenuView.setTranslationY(openingViewLocation[1] - contextMenuView.getHeight() - additionalBottomMargin);
    }

    private void performShowAnimation(boolean below) {
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(below ? 0 : contextMenuView.getHeight());
        contextMenuView.setScaleX(0.1f);
        contextMenuView.setScaleY(0.1f);
        contextMenuView.animate()
                .scaleX(1f).scaleY(1f)
                .setDuration(150)
                .setInterpolator(new OvershootInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        isContextMenuShowing = true;
                    }
                });
    }

    /**
     * Hides the menu
     *
     * @param below If the animation should be inverted for menus "below" the View
     */
    public void hideContextMenu(boolean below) {
        if (!isContextMenuDismissing) {
            isContextMenuDismissing = true;
            performDismissAnimation(below);
        }
    }

    private void performDismissAnimation(boolean below) {
        if (contextMenuView == null) {
            isContextMenuDismissing = false;
            isContextMenuShowing = false;
            return;
        }
        contextMenuView.setPivotX(contextMenuView.getWidth() / 2);
        contextMenuView.setPivotY(below ? 0 : contextMenuView.getHeight());
        contextMenuView.animate()
                .scaleX(0.1f).scaleY(0.1f)
                .setDuration(100)
                .setInterpolator(new AccelerateInterpolator())
                .setStartDelay(0)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (contextMenuView != null) {
                            contextMenuView.dismiss();
                        }
                        isContextMenuDismissing = false;
                        isContextMenuShowing = false;
                    }
                });
    }

    public void onScrolled(float dy, boolean below) {
        if (contextMenuView != null) {
            //make menu move with feed
            contextMenuView.setTranslationY(contextMenuView.getTranslationY() - dy);
            float menuY = contextMenuView.getY();
            float padding = Utils.dpToPx(16);
            float yMax = Utils.getScreenHeight(context) - getStatusBarHeight() - contextMenuView.getHeight() - padding;
            if (menuY < padding || menuY > yMax)
                hideContextMenu(below);
//            if (Utils.pxToDp((int)contextMenuView.getY(), context) <= 78 && dy > 0)
//                hideContextMenu(below);
//            if (Utils.pxToDp((int)contextMenuView.getY() + contextMenuView.getHeight(), context) >=
//                    Utils.pxToDp(Utils.getScreenHeight(context), context))
//                hideContextMenu(below);
        }
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    protected int getFullTopBarsHeight() {
        return getStatusBarHeight() + getActionBarSize();
    }

    @Override
    public void onViewAttachedToWindow(View v) {

    }

    @Override
    public void onViewDetachedFromWindow(View v) {
        contextMenuView = null;
    }
}
