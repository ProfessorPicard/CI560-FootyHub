package uk.phsh.footyhub;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.Objects;

import uk.phsh.footyhub.adapters.DrawNavAdapter;
import uk.phsh.footyhub.fragments.BaseFragment;
import uk.phsh.footyhub.fragments.FixtureFragment;
import uk.phsh.footyhub.fragments.HomeFragment;
import uk.phsh.footyhub.fragments.NewsFragment;
import uk.phsh.footyhub.fragments.SelectTeamFragment;
import uk.phsh.footyhub.fragments.SettingsFragment;
import uk.phsh.footyhub.fragments.StandingsFragment;
import uk.phsh.footyhub.interfaces.I_FragmentCallback;
import uk.phsh.footyhub.models.NavItem;

public class MainActivity extends AppCompatActivity implements I_FragmentCallback {

    //Fragment Variables
    private HomeFragment _homeFragment;
    private SelectTeamFragment _selectTeamFragment;
    private NewsFragment _newsFragment;
    private FixtureFragment _fixturesFragment;
    private StandingsFragment _standingsFragment;
    private SettingsFragment _settingsFragment;

    //Nav Drawer Variables
    private ListView _navDrawerList;
    private RelativeLayout _drawContainer;
    private ActionBarDrawerToggle _drawerToggle;
    private DrawerLayout _drawerLayout;

    //Nav Drawer Items
    ArrayList<NavItem> _navItems = new ArrayList<>();

    //Actionbar Variables
    private ActionBar actionBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _homeFragment = new HomeFragment(this, this);
        _selectTeamFragment = new SelectTeamFragment(this, this);
        _newsFragment = new NewsFragment(this, this);
        _fixturesFragment = new FixtureFragment(this, this);
        _standingsFragment = new StandingsFragment(this, this);
        _settingsFragment = new SettingsFragment(this, this);

        _navItems.add(new NavItem(getResources().getString(R.string.change_favourite_drawer_menu), R.drawable.change, _selectTeamFragment));
        _navItems.add(new NavItem(getResources().getString(R.string.home_drawer_menu), R.drawable.home, _homeFragment));
        _navItems.add(new NavItem(getResources().getString(R.string.team_news_drawer_menu), R.drawable.news, _newsFragment));
        _navItems.add(new NavItem(getResources().getString(R.string.fixtures_drawer_menu), R.drawable.event, _fixturesFragment));
        _navItems.add(new NavItem(getResources().getString(R.string.standings_drawer_menu), R.drawable.standings, _standingsFragment));
        _navItems.add(new NavItem(getResources().getString(R.string.settings_drawer_menu), R.drawable.settings, _settingsFragment));

        _drawerLayout = findViewById(R.id.drawerLayout);
        _drawContainer  = findViewById(R.id.drawerContainer);
        _navDrawerList = findViewById(R.id.navList);
        DrawNavAdapter adapter = new DrawNavAdapter(this, _navItems);
        _navDrawerList.setAdapter(adapter);

        _navDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });

        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.string.drawer_open, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("Main Class", "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };
        _drawerLayout.addDrawerListener(_drawerToggle);
        actionBar = Objects.requireNonNull(getSupportActionBar());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary)));


        actionBar.setTitle("Choose a favourite team");
    }

    private void selectItemFromDrawer(int position) {
        BaseFragment fragment = _navItems.get(position).getFragment();
        changeFragment(fragment);

        _navDrawerList.setItemChecked(position, true);
        setTitle(_navItems.get(position).getTitle());

        _drawerLayout.closeDrawer(_drawContainer);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (_drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }

    private void changeFragment(BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.mainContent, fragment)
                .commit();
    }

    @Override
    public void changeActionbarTitle(String title) {
        actionBar.setTitle(title);
    }
}
