package edu.bu.mad.jiayao.bumad2016_jiayao;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class HomeActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private String[] items = { "HOME", "Listings", "Reservations", "Notifications", "Bookmarks", "LOGOUT"};
    private CharSequence currentTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            username = extras.getString("username");
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        setTitle("GoParking");
        currentTitle = "GoParking";

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.nav_list);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, items));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, myToolbar, 0, 0) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Fragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(HomeFragment.ARG_USERNAME, username);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.addOnBackStackChangedListener(this);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    /** Inflate the app bar **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();
        if (id == R.id.action_search) {
            Intent i = new Intent(this, SearchActivity.class);
            i.putExtra("username", username);
            startActivityForResult(i, 1);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Bundle extras = getIntent().getExtras();
                if (extras != null){
                    username = extras.getString("username");
                }
            }
        }

        FragmentManager fm = getSupportFragmentManager();
        String name = fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();
        selectItem(Integer.parseInt(name));
    }

    @Override
    public void onBackStackChanged() {
        FragmentManager fm = getSupportFragmentManager();
        String name = fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName();
        mDrawerList.setItemChecked(Integer.parseInt(name),true);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view **/
    private void selectItem(int position) {
        if (position == 5){
            // logout
            Intent i = new Intent(this, LandingActivity.class);
            startActivity(i);
        } else {
            Fragment fragment = new HomeFragment();
            Bundle args = new Bundle();
            FragmentManager fragmentManager = getSupportFragmentManager();
            switch (position) {
                case 0:
                    fragment = new HomeFragment();
                    break;
                case 1:
                    fragment = new ListingFragment();
                    break;
                case 2:
                    fragment = new ReservationFragment();
                    break;
                case 3:
                    fragment = new NotificationFragment();
                    break;
                case 4:
                    fragment = new BookmarkFragment();
                    break;
                default:
                    break;
            }
            args.putString(HomeFragment.ARG_USERNAME, username);
            fragment.setArguments(args);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(Integer.toString(position)).commit();

            // Highlight the selected item, update the title, and close the drawer
            mDrawerList.setItemChecked(position, true);
            setTitle(items[position]);
            currentTitle = items[position];
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    /** Swaps fragments in the main content view **/
    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        Bundle args = new Bundle();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        args.putString(HomeFragment.ARG_USERNAME, username);
        fragment.setArguments(args);
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence("titleText", currentTitle);
        outState.putString("username", username);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        CharSequence titleText = savedInstanceState.getCharSequence("titleText");
        setTitle(titleText);
        username = savedInstanceState.getString("username");
    }
}

