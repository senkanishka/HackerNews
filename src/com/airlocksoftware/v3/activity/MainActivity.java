package com.airlocksoftware.v3.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.ViewStub;
import android.widget.FrameLayout;

import com.airlocksoftware.hackernews.BuildConfig;
import com.airlocksoftware.hackernews.R;
import com.airlocksoftware.hackernews.fragment.CommentsFragment;
import com.airlocksoftware.hackernews.fragment.StoryFragment;
import com.airlocksoftware.hackernews.interfaces.SharePopupInterface;
import com.airlocksoftware.hackernews.interfaces.TabletLayout;
import com.airlocksoftware.hackernews.model.Story;
import com.airlocksoftware.hackernews.view.SharePopup;
import com.airlocksoftware.v3.dagger.MainActivityModule;
import com.airlocksoftware.v3.fragment.MainFragment;
import com.airlocksoftware.v3.otto.ShowStoryEvent;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.airlocksoftware.hackernews.activity.MainActivity.CommentsTab;

/**
 * Created by matthewbbishop on 12/7/13.
 */
public class MainActivity extends BaseActivity implements StoryFragment.Callbacks, TabletLayout,
        SharePopupInterface, CommentsFragment.Callbacks {


  @InjectView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
  @InjectView(R.id.cnt_fragment) FrameLayout mFragmentContainer;
  @InjectView(R.id.debug_menu) ViewStub mDebugMenu;
  
  /* Navigation Drawer */
  private ActionBarDrawerToggle mDrawerToggle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /* Inflate views */
    setContentView(R.layout.act_main);
    /* Inject views */
    ButterKnife.inject(this);
    /* Setup the injected views */
    initViews();
    /* Setup the navigation drawer */
    initNavigationDrawer();
  }

  private void initNavigationDrawer() {
    int icon = R.drawable.ic_navigation_drawer;
    int open = R.string.drawer_open;
    int close = R.string.drawer_close;
    mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, icon, open, close);
//    {
//      public void onDrawerClosed(View view) { /* no op */ }
//      public void onDrawerOpened(View drawerView) { /* no op */ }
//    };

    /* Set the drawer toggle as the DrawerListener */
//    getActionBar().setDisplayHomeAsUpEnabled(true);
//    getActionBar().setHomeButtonEnabled(true);
//    getActionBar().setDisplayShowHomeEnabled(true);
//    mDrawerLayout.setDrawerListener(mDrawerToggle);
//    mDrawerToggle.setDrawerIndicatorEnabled(true);

    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setHomeButtonEnabled(true);
    mDrawerLayout.setDrawerListener(mDrawerToggle);
//    mDrawerToggle.setDrawerIndicatorEnabled(true);
  }

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    // Sync the toggle state after onRestoreInstanceState has occurred.
    mDrawerToggle.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }


  @Override public boolean onOptionsItemSelected(MenuItem item) {
    /* If the drawer indicator is enabled, give the drawer toggle the chance to consume the up action. Otherwise
    * call super which passes it down the chain to registered Fragments. */
    if (mDrawerToggle.isDrawerIndicatorEnabled() && mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }

  }

  private void initViews() {
    /* If we don't have any fragments, add the default (MainFragment) */
    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.add(R.id.cnt_fragment, new MainFragment(), "MainFragment");
    ft.commit();

    /* If we're in debug mode, display the debug menu. */
    if (BuildConfig.DEBUG) {
      setupDebugMenu();
    }
  }

  private void setupDebugMenu() {
    mDebugMenu.inflate();
  }

  @Override
  public void showCommentsForStory(Story story, CommentsTab initalTab) {
    getBus().post(new ShowStoryEvent(story));
  }

  @Override
  public Story getActiveStory() {
    return new Story();
  }

  @Override
  public boolean storyFragmentIsInLayout() {
    return true;
  }

  @Override
  public boolean isTabletLayout() {
    return false;
  }

  @Override
  public SharePopup getSharePopup() {
//        return new SharePopup(this, null, null);
    return null;
  }

  @Override public void receivedStory(Story story) {
    /* mb TODO */
  }

  @Override public boolean commentsFragmentIsInLayout() {
    return true;
  }

  /** Add our MainActivity-specific module to the Application & BaseActivity modules in the ObjectGraph **/
  @Override protected List<Object> getModules() {
    List<Object> modules = new ArrayList<Object>();
    modules.add(new MainActivityModule(this));
    modules.addAll(super.getModules());
    return modules;
  }

  public ActionBarDrawerToggle getDrawerToggle() {
    return mDrawerToggle;
  }
}
