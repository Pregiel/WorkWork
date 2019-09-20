package pl.pregiel.workwork;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import pl.pregiel.workwork.fragments.AddWorkFragment;
import pl.pregiel.workwork.fragments.AddWorkTimeFragment;
import pl.pregiel.workwork.fragments.CalculatorInputFragment;
import pl.pregiel.workwork.fragments.CalculatorResultFragment;
import pl.pregiel.workwork.fragments.TaggedFragment;
import pl.pregiel.workwork.fragments.SummaryFragment;
import pl.pregiel.workwork.fragments.UpdateWorkFragment;
import pl.pregiel.workwork.fragments.UpdateWorkTimeFragment;
import pl.pregiel.workwork.fragments.WorkDetailsFragment;
import pl.pregiel.workwork.fragments.WorkListFragment;
import pl.pregiel.workwork.utils.FragmentOpener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_worklist);

        if (savedInstanceState == null) {
            TaggedFragment fragment = new WorkListFragment();
            FragmentOpener.openFragment(getSupportFragmentManager(), fragment, FragmentOpener.OpenMode.REPLACE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            android.support.v4.app.Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame);

            if (currentFragment instanceof WorkDetailsFragment
                    || currentFragment instanceof AddWorkFragment) {
                TaggedFragment fragment = new WorkListFragment();
                FragmentOpener.openFragment(getSupportFragmentManager(), fragment, FragmentOpener.OpenMode.REPLACE);
            } else if (currentFragment instanceof AddWorkTimeFragment
                    || currentFragment instanceof UpdateWorkFragment
                    || currentFragment instanceof UpdateWorkTimeFragment) {
                TaggedFragment fragment = (TaggedFragment) getSupportFragmentManager().findFragmentByTag(WorkDetailsFragment.FRAGMENT_TAG);
                if (fragment == null) {
                    fragment = new WorkListFragment();
                } else {
                    ((WorkDetailsFragment) fragment).setTitle();
                    ((WorkDetailsFragment) fragment).reload();
                }
                FragmentOpener.openFragment(getSupportFragmentManager(), fragment, FragmentOpener.OpenMode.REPLACE);
            } else if (currentFragment instanceof CalculatorResultFragment) {
                TaggedFragment fragment = (TaggedFragment) getSupportFragmentManager().findFragmentByTag(CalculatorInputFragment.FRAGMENT_TAG);
                if (fragment == null) {
                    fragment = new CalculatorInputFragment();
                }
                FragmentOpener.openFragment(getSupportFragmentManager(), fragment, FragmentOpener.OpenMode.REPLACE);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        TaggedFragment fragment = null;
        if (id == R.id.nav_worklist) {
            fragment = new WorkListFragment();
        } else if (id == R.id.nav_summary) {
            fragment = new SummaryFragment();
        } else if (id == R.id.nav_calculator) {
            fragment = new CalculatorInputFragment();
        }

        FragmentOpener.openFragment(getSupportFragmentManager(), fragment, FragmentOpener.OpenMode.REPLACE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
