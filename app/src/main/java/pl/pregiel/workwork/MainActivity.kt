package pl.pregiel.workwork

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

import pl.pregiel.workwork.fragments.AddWorkFragment
import pl.pregiel.workwork.fragments.AddWorkTimeFragment
import pl.pregiel.workwork.fragments.CalculatorInputFragment
import pl.pregiel.workwork.fragments.CalculatorResultFragment
import pl.pregiel.workwork.fragments.TaggedFragment
import pl.pregiel.workwork.fragments.SummaryFragment
import pl.pregiel.workwork.fragments.UpdateWorkFragment
import pl.pregiel.workwork.fragments.UpdateWorkTimeFragment
import pl.pregiel.workwork.fragments.WorkDetailsFragment
import pl.pregiel.workwork.fragments.WorkListFragment
import pl.pregiel.workwork.utils.FragmentOpener

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        nav_view.setCheckedItem(R.id.nav_worklist)

        if (savedInstanceState == null) {
            val fragment = WorkListFragment()
            FragmentOpener.openFragment(supportFragmentManager, fragment, FragmentOpener.OpenMode.REPLACE)
        }
    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            when (supportFragmentManager.findFragmentById(R.id.frame)) {
                is WorkDetailsFragment, is AddWorkFragment -> {
                    val fragment = supportFragmentManager.findFragmentByTag(WorkListFragment.FRAGMENT_TAG) as TaggedFragment?
                            ?: WorkListFragment()
                    FragmentOpener.openFragment(supportFragmentManager, fragment, FragmentOpener.OpenMode.REPLACE)
                }
                is AddWorkTimeFragment, is UpdateWorkFragment, is UpdateWorkTimeFragment -> {
                    val fragment = supportFragmentManager.findFragmentByTag(WorkDetailsFragment.FRAGMENT_TAG) as TaggedFragment?
                            ?: WorkListFragment()
                    if (fragment is WorkDetailsFragment) {
                        fragment.setTitle()
                        fragment.reload()
                    }
                    FragmentOpener.openFragment(supportFragmentManager, fragment, FragmentOpener.OpenMode.REPLACE)
                }
                is CalculatorResultFragment -> {
                    val fragment = supportFragmentManager.findFragmentByTag(CalculatorInputFragment.FRAGMENT_TAG) as TaggedFragment?
                            ?: CalculatorInputFragment()
                    FragmentOpener.openFragment(supportFragmentManager, fragment, FragmentOpener.OpenMode.REPLACE)
                }
                else -> super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragment: TaggedFragment = when (item.itemId) {
            R.id.nav_summary -> SummaryFragment()
            R.id.nav_calculator -> CalculatorInputFragment()
            else -> WorkListFragment()
        }

        FragmentOpener.openFragment(supportFragmentManager, fragment, FragmentOpener.OpenMode.REPLACE)

        Handler().postDelayed(
                { drawer_layout.closeDrawer(GravityCompat.START) },
                100)

        return true
    }
}
