package com.mikepenz.materialdrawer;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.model.ContainerDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Badgeable;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Iconable;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.materialdrawer.util.KeyboardUtil;
import com.mikepenz.materialize.util.UIUtils;
import com.mikepenz.materialize.view.IScrimInsetsLayout;

import java.util.ArrayList;

/**
 * Created by mikepenz on 03.02.15.
 */
public class Drawer {
    /**
     * BUNDLE param to store the selection
     */
    protected static final String BUNDLE_SELECTION = "bundle_selection";
    protected static final String BUNDLE_FOOTER_SELECTION = "bundle_footer_selection";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    protected static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";


    private final DrawerBuilder mDrawerBuilder;
    private FrameLayout mContentView;
    private KeyboardUtil mKeyboardUtil = null;

    /**
     * the protected Constructor for the result
     *
     * @param drawerBuilder
     */
    protected Drawer(DrawerBuilder drawerBuilder) {
        this.mDrawerBuilder = drawerBuilder;
    }

    /**
     * Get the DrawerLayout of the current drawer
     *
     * @return
     */
    public DrawerLayout getDrawerLayout() {
        return this.mDrawerBuilder.mDrawerLayout;
    }

    /**
     * Open the drawer
     */
    public void openDrawer() {
        if (mDrawerBuilder.mDrawerLayout != null && mDrawerBuilder.mSliderLayout != null) {
            mDrawerBuilder.mDrawerLayout.openDrawer(mDrawerBuilder.mDrawerGravity);
        }
    }

    /**
     * close the drawer
     */
    public void closeDrawer() {
        if (mDrawerBuilder.mDrawerLayout != null) {
            mDrawerBuilder.mDrawerLayout.closeDrawer(mDrawerBuilder.mDrawerGravity);
        }
    }

    /**
     * Get the current state of the drawer.
     * True if the drawer is currently open.
     *
     * @return
     */
    public boolean isDrawerOpen() {
        if (mDrawerBuilder.mDrawerLayout != null && mDrawerBuilder.mSliderLayout != null) {
            return mDrawerBuilder.mDrawerLayout.isDrawerOpen(mDrawerBuilder.mDrawerGravity);
        }
        return false;
    }


    /**
     * set the insetsFrameLayout to display the content in fullscreen
     * under the statusBar and navigationBar
     *
     * @param fullscreen
     */
    public void setFullscreen(boolean fullscreen) {
        if (mDrawerBuilder.mDrawerContentRoot != null) {
            mDrawerBuilder.mDrawerContentRoot.getView().setEnabled(!fullscreen);
        }
    }

    /**
     * Set the color for the statusBar
     *
     * @param statusBarColor
     */
    public void setStatusBarColor(@ColorInt int statusBarColor) {
        if (mDrawerBuilder.mDrawerContentRoot != null) {
            mDrawerBuilder.mDrawerContentRoot.setInsetForeground(statusBarColor);
            mDrawerBuilder.mDrawerContentRoot.getView().invalidate();
        }
    }

    /**
     * get the drawerContentRoot Layout (ScrimInsetsFrameLayout)
     *
     * @return
     */
    public IScrimInsetsLayout getScrimInsetsFrameLayout() {
        return mDrawerBuilder.mDrawerContentRoot;
    }


    /**
     * a helper method to enable the keyboardUtil for a specific activity
     * or disable it. note this will cause some frame drops because of the
     * listener.
     *
     * @param activity
     * @param enable
     */
    public void keyboardSupportEnabled(@NonNull Activity activity, boolean enable) {
        if (getContent() != null && getContent().getChildCount() > 0) {
            if (mKeyboardUtil == null) {
                mKeyboardUtil = new KeyboardUtil(activity, getContent().getChildAt(0));
                mKeyboardUtil.disable();
            }

            if (enable) {
                mKeyboardUtil.enable();
            } else {
                mKeyboardUtil.disable();
            }
        }
    }


    /**
     * get the slider layout of the current drawer.
     * This is the layout containing the ListView
     *
     * @return
     */
    public RelativeLayout getSlider() {
        return mDrawerBuilder.mSliderLayout;
    }

    /**
     * get the container frameLayout of the current drawer
     *
     * @return
     */
    public FrameLayout getContent() {
        if (mContentView == null && this.mDrawerBuilder.mDrawerLayout != null) {
            mContentView = (FrameLayout) this.mDrawerBuilder.mDrawerLayout.findViewById(R.id.content_layout);
        }
        return mContentView;
    }

    /**
     * get the listView of the current drawer
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mDrawerBuilder.mRecyclerView;
    }

    /**
     * get the BaseDrawerAdapter of the current drawer
     *
     * @return
     */
    public BaseDrawerAdapter getAdapter() {
        return mDrawerBuilder.mAdapter;
    }

    /**
     * get all drawerItems of the current drawer
     *
     * @return
     */
    public ArrayList<IDrawerItem> getDrawerItems() {
        return mDrawerBuilder.getAdapter().getDrawerItems();
    }

    /**
     * get the Header View if set else NULL
     *
     * @return
     */
    public View getHeader() {
        return mDrawerBuilder.mHeaderView;
    }

    /**
     * get the StickyHeader View if set else NULL
     *
     * @return
     */
    public View getStickyHeader() {
        return mDrawerBuilder.mStickyHeaderView;
    }

    /**
     * method to replace a previous set header
     *
     * @param view
     */
    public void setHeader(@NonNull View view) {
        getAdapter().clearHeaderItems();
        getAdapter().addHeaderDrawerItems(new ContainerDrawerItem().withView(view).withViewPosition(ContainerDrawerItem.Position.TOP));
    }

    /**
     * method to remove the header of the list
     */
    public void removeHeader() {
        getAdapter().clearHeaderItems();
    }

    /**
     * get the Footer View if set else NULL
     *
     * @return
     */
    public View getFooter() {
        return mDrawerBuilder.mFooterView;
    }

    /**
     * get the StickyFooter View if set else NULL
     *
     * @return
     */
    public View getStickyFooter() {
        return mDrawerBuilder.mStickyFooterView;
    }

    /**
     * get the ActionBarDrawerToggle
     *
     * @return
     */
    public ActionBarDrawerToggle getActionBarDrawerToggle() {
        return mDrawerBuilder.mActionBarDrawerToggle;
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    public int getPosition(@NonNull IDrawerItem drawerItem) {
        return getPosition(drawerItem.getIdentifier());
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param identifier
     * @return
     */
    public int getPosition(int identifier) {
        return DrawerUtils.getPositionByIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * return sthe DrawerItem by the given identifier
     *
     * @param identifier
     * @return
     */
    public IDrawerItem getDrawerItem(int identifier) {
        return getAdapter().getItem(getPosition(identifier));
    }

    /**
     * calculates the position of an drawerItem. searching by it's identifier
     *
     * @param drawerItem
     * @return
     */
    public int getFooterPosition(@NonNull IDrawerItem drawerItem) {
        return getFooterPosition(drawerItem.getIdentifier());
    }

    /**
     * calculates the position of an drawerItem inside the footer. searching by it's identfier
     *
     * @param identifier
     * @return
     */
    public int getFooterPosition(int identifier) {
        return DrawerUtils.getFooterPositionByIdentifier(mDrawerBuilder, identifier);
    }

    /**
     * get the current selection
     *
     * @return
     */
    public int getCurrentSelection() {
        return mDrawerBuilder.mCurrentSelection;
    }

    /**
     * get the current footer selection
     *
     * @return
     */
    public int getCurrentFooterSelection() {
        return mDrawerBuilder.mCurrentFooterSelection;
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param identifier
     */
    public boolean setSelection(int identifier) {
        return setSelectionAtPosition(getPosition(identifier), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier
     * @param fireOnClick
     */
    public boolean setSelection(int identifier, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(identifier), fireOnClick);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param identifier
     * @param fireOnClick
     */
    public void setFooterSelection(int identifier, boolean fireOnClick) {
        setFooterSelectionAtPosition(getPosition(identifier), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param drawerItem
     */
    public boolean setSelection(@NonNull IDrawerItem drawerItem) {
        return setSelectionAtPosition(getPosition(drawerItem), true);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param drawerItem
     * @param fireOnClick
     */
    public boolean setSelection(@NonNull IDrawerItem drawerItem, boolean fireOnClick) {
        return setSelectionAtPosition(getPosition(drawerItem), fireOnClick);
    }

    /**
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param position the position to select
     */
    public boolean setSelectionAtPosition(int position) {
        return setSelectionAtPosition(position, true);
    }

    /*
     * set the current selection in the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     * @return true if the event was consumed
     */
    public boolean setSelectionAtPosition(int position, boolean fireOnClick) {
        if (mDrawerBuilder.mRecyclerView != null) {
            return DrawerUtils.setRecyclerViewSelection(mDrawerBuilder, position, fireOnClick, mDrawerBuilder.getDrawerItem(position));
        }
        return false;
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view!
     *
     * @param position the position to select
     */
    public void setFooterSelectionAtPosition(int position) {
        setFooterSelectionAtPosition(position, true);
    }

    /**
     * set the current selection in the footer of the drawer
     * NOTE: This will trigger onDrawerItemSelected without a view if you pass fireOnClick = true;
     *
     * @param position
     * @param fireOnClick
     */
    public void setFooterSelectionAtPosition(int position, boolean fireOnClick) {
        DrawerUtils.setFooterSelection(mDrawerBuilder, position, fireOnClick);
    }

    /**
     * update a specific drawer item :D
     * automatically identified by its id
     *
     * @param drawerItem
     */
    public void updateItem(@NonNull IDrawerItem drawerItem) {
        updateItemAtPosition(drawerItem, getPosition(drawerItem));
    }

    /**
     * Update a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void updateItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Add a drawerItem at the end
     *
     * @param drawerItem
     */
    public void addItem(@NonNull IDrawerItem drawerItem) {
        mDrawerBuilder.getAdapter().addDrawerItem(drawerItem);
    }

    /**
     * Add a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        mDrawerBuilder.getAdapter().addDrawerItem(position, drawerItem);
    }

    /**
     * Set a drawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        mDrawerBuilder.getAdapter().addDrawerItem(position, drawerItem);
    }

    /**
     * Remove a drawerItem at a specific position
     *
     * @param position
     */
    public void removeItemByPosition(int position) {
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getAdapter().removeDrawerItem(position);
        }
    }

    /**
     * Remove a drawerItem by the identifier
     *
     * @param identifier
     */
    public void removeItem(int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            mDrawerBuilder.getAdapter().removeDrawerItem(position);
        }
    }

    /**
     * Removes all items from drawer
     */
    public void removeAllItems() {
        mDrawerBuilder.getAdapter().clearDrawerItems();
    }

    /**
     * add new Items to the current DrawerItem List
     *
     * @param drawerItems
     */
    public void addItems(@NonNull IDrawerItem... drawerItems) {
        mDrawerBuilder.getAdapter().addDrawerItems(drawerItems);
    }

    /**
     * Replace the current DrawerItems with a new ArrayList of items
     *
     * @param drawerItems
     */
    public void setItems(@NonNull ArrayList<IDrawerItem> drawerItems) {
        setItems(drawerItems, false);
    }

    /**
     * replace the current DrawerItems with the new ArrayList.
     *
     * @param drawerItems
     * @param switchedItems
     */
    private void setItems(@NonNull ArrayList<IDrawerItem> drawerItems, boolean switchedItems) {
        //if we are currently at a switched list set the new reference
        if (originalDrawerItems != null && !switchedItems) {
            originalDrawerItems = drawerItems;
        } else {
            mDrawerBuilder.getAdapter().setDrawerItems(drawerItems);
        }

        mDrawerBuilder.mAdapter.notifyDataSetChanged();
    }

    /**
     * Update the name of a drawer item if its an instance of nameable
     *
     * @param nameRes
     * @param identifier
     */
    public void updateName(@StringRes int nameRes, int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(position);

            if (drawerItem instanceof Nameable) {
                ((Nameable) drawerItem).withName(nameRes);
            }

            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Update the name of a drawer item if its an instance of nameable
     *
     * @param name
     * @param identifier
     */
    public void updateName(String name, int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(position);

            if (drawerItem instanceof Nameable) {
                ((Nameable) drawerItem).withName(name);
            }

            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Update the badge of a drawer item if its an instance of badgeable
     *
     * @param badge
     * @param identifier
     */
    public void updateBadge(String badge, int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(position);

            if (drawerItem instanceof Badgeable) {
                ((Badgeable) drawerItem).withBadge(badge);
            }

            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Update the icon of a drawer item if its an instance of iconable
     *
     * @param icon
     * @param identifier
     */
    public void updateIcon(Drawable icon, int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(position);

            if (drawerItem instanceof Iconable) {
                ((Iconable) drawerItem).withIcon(icon);
            }

            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Update the icon of a drawer item from an iconRes
     *
     * @param iconRes
     * @param identifier
     */
    public void updateIcon(@DrawableRes int iconRes, int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.mRootView != null && mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(position);

            if (drawerItem instanceof Iconable) {
                ((Iconable) drawerItem).withIcon(UIUtils.getCompatDrawable(mDrawerBuilder.mRootView.getContext(), iconRes));
            }

            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * Update the icon of a drawer item if its an instance of iconable
     *
     * @param icon
     * @param identifier
     */
    public void updateIcon(@NonNull IIcon icon, int identifier) {
        int position = getPosition(identifier);
        if (mDrawerBuilder.checkDrawerItem(position, false)) {
            IDrawerItem drawerItem = mDrawerBuilder.getDrawerItem(position);

            if (drawerItem instanceof Iconable) {
                ((Iconable) drawerItem).withIcon(icon);
            }

            mDrawerBuilder.getAdapter().setDrawerItem(position, drawerItem);
        }
    }

    /**
     * update a specific footerDrawerItem :D
     * automatically identified by it's id
     *
     * @param drawerItem
     */
    public void updateFooterItem(@NonNull IDrawerItem drawerItem) {
        updateFooterItemAtPosition(drawerItem, getFooterPosition(drawerItem));
    }

    /**
     * update a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void updateFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }


    /**
     * Add a footerDrawerItem at the end
     *
     * @param drawerItem
     */
    public void addFooterItem(@NonNull IDrawerItem drawerItem) {
        if (mDrawerBuilder.mStickyDrawerItems == null) {
            mDrawerBuilder.mStickyDrawerItems = new ArrayList<>();
        }
        mDrawerBuilder.mStickyDrawerItems.add(drawerItem);

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }

    /**
     * Add a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void addFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems == null) {
            mDrawerBuilder.mStickyDrawerItems = new ArrayList<>();
        }
        mDrawerBuilder.mStickyDrawerItems.add(position, drawerItem);

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }

    /**
     * Set a footerDrawerItem at a specific position
     *
     * @param drawerItem
     * @param position
     */
    public void setFooterItemAtPosition(@NonNull IDrawerItem drawerItem, int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.set(position, drawerItem);
        }

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }


    /**
     * Remove a footerDrawerItem at a specific position
     *
     * @param position
     */
    public void removeFooterItemAtPosition(int position) {
        if (mDrawerBuilder.mStickyDrawerItems != null && mDrawerBuilder.mStickyDrawerItems.size() > position) {
            mDrawerBuilder.mStickyDrawerItems.remove(position);
        }

        DrawerUtils.rebuildFooterView(mDrawerBuilder);
    }

    /**
     * Removes all footerItems from drawer
     */
    public void removeAllFooterItems() {
        if (mDrawerBuilder.mStickyDrawerItems != null) {
            mDrawerBuilder.mStickyDrawerItems.clear();
        }
        if (mDrawerBuilder.mStickyFooterView != null) {
            mDrawerBuilder.mStickyFooterView.setVisibility(View.GONE);
        }
    }

    /**
     * setter for the OnDrawerItemClickListener
     *
     * @param onDrawerItemClickListener
     */
    public void setOnDrawerItemClickListener(@NonNull OnDrawerItemClickListener onDrawerItemClickListener) {
        mDrawerBuilder.mOnDrawerItemClickListener = onDrawerItemClickListener;
    }

    /**
     * method to get the OnDrawerItemClickListener
     *
     * @return
     */
    public OnDrawerItemClickListener getOnDrawerItemClickListener() {
        return mDrawerBuilder.mOnDrawerItemClickListener;
    }

    /**
     * setter for the OnDrawerItemLongClickListener
     *
     * @param onDrawerItemLongClickListener
     */
    public void setOnDrawerItemLongClickListener(@NonNull OnDrawerItemLongClickListener onDrawerItemLongClickListener) {
        mDrawerBuilder.mOnDrawerItemLongClickListener = onDrawerItemLongClickListener;
    }

    /**
     * method to get the OnDrawerItemLongClickListener
     *
     * @return
     */
    public OnDrawerItemLongClickListener getOnDrawerItemLongClickListener() {
        return mDrawerBuilder.mOnDrawerItemLongClickListener;
    }


    //variables to store and remember the original list of the drawer
    private Drawer.OnDrawerItemClickListener originalOnDrawerItemClickListener;
    private ArrayList<IDrawerItem> originalDrawerItems;
    private int originalDrawerSelection = -1;

    public boolean switchedDrawerContent() {
        return !(originalOnDrawerItemClickListener == null && originalDrawerItems == null && originalDrawerSelection == -1);
    }

    /**
     * method to switch the drawer content to new elements
     *
     * @param onDrawerItemClickListener
     * @param drawerItems
     * @param drawerSelection
     */
    public void switchDrawerContent(@NonNull OnDrawerItemClickListener onDrawerItemClickListener, @NonNull ArrayList<IDrawerItem> drawerItems, int drawerSelection) {
        //just allow a single switched drawer
        if (!switchedDrawerContent()) {
            //save out previous values
            originalOnDrawerItemClickListener = getOnDrawerItemClickListener();
            originalDrawerItems = getDrawerItems();
            originalDrawerSelection = getCurrentSelection();

            //set the new items
            setOnDrawerItemClickListener(onDrawerItemClickListener);
            setItems(drawerItems, true);
            setSelectionAtPosition(getAdapter().getHeaderItemCount() + drawerSelection, false);

            if (getStickyFooter() != null) {
                getStickyFooter().setVisibility(View.GONE);
            }
        }
    }

    /**
     * helper method to reset to the original drawerContent
     */
    public void resetDrawerContent() {
        if (switchedDrawerContent()) {
            //set the new items
            setOnDrawerItemClickListener(originalOnDrawerItemClickListener);
            setItems(originalDrawerItems, true);
            setSelectionAtPosition(originalDrawerSelection, false);
            //remove the references
            originalOnDrawerItemClickListener = null;
            originalDrawerItems = null;
            originalDrawerSelection = -1;

            //if we switch back scroll back to the top
            mDrawerBuilder.mRecyclerView.smoothScrollToPosition(0);

            if (getStickyFooter() != null) {
                getStickyFooter().setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * add the values to the bundle for saveInstanceState
     *
     * @param savedInstanceState
     * @return
     */
    public Bundle saveInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.putInt(BUNDLE_SELECTION, mDrawerBuilder.mCurrentSelection);
            savedInstanceState.putInt(BUNDLE_FOOTER_SELECTION, mDrawerBuilder.mCurrentFooterSelection);
        }
        return savedInstanceState;
    }


    public interface OnDrawerNavigationListener {
        /**
         * @param clickedView
         * @return true if the event was consumed
         */
        boolean onNavigationClickListener(View clickedView);
    }

    public interface OnDrawerItemClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @return true if the event was consumed
         */
        boolean onItemClick(View view, int position, IDrawerItem drawerItem);
    }

    public interface OnDrawerItemLongClickListener {
        /**
         * @param view
         * @param position
         * @param drawerItem
         * @return true if the event was consumed
         */
        boolean onItemLongClick(View view, int position, IDrawerItem drawerItem);
    }

    public interface OnDrawerListener {
        /**
         * @param drawerView
         */
        void onDrawerOpened(View drawerView);

        /**
         * @param drawerView
         */
        void onDrawerClosed(View drawerView);

        /**
         * @param drawerView
         * @param slideOffset
         */
        void onDrawerSlide(View drawerView, float slideOffset);
    }

    public interface OnDrawerItemSelectedListener {
        /**
         * @param parent
         * @param view
         * @param position
         * @param id
         * @param drawerItem
         */
        void onItemSelected(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem);

        /**
         * @param parent
         */
        void onNothingSelected(AdapterView<?> parent);
    }
}
