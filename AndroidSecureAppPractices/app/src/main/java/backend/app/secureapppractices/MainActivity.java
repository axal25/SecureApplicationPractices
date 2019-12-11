package backend.app.secureapppractices;

import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements CustomNavView {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public int[] currentlySelectedNavViewItemIds = {-1,-1,-1};

    public MainActivity() {
        System.out.println("MainActivity() Constructor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OnCreate");
        super.onCreate(savedInstanceState);
        init();
        ifNewlyOpenedApp(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)) this.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    private void init() {
        setContentView();
        setToolBar();
        this.drawerLayout = findViewById(R.id.drawer_layout);
        initActionBarDrawerToggle();
        setNavigationView();
    }

    private void setContentView() {
        System.out.println("Don't mind the error \\/\\/\\/\n");
        setContentView(R.layout.main_activity);
        System.out.println("Don't mind the error /\\/\\/\\\n");
        for(int i=0; i<25; i++) System.out.println("Space #"+i);
    }

    private void setToolBar() {
            this.toolbar = findViewById(R.id.custom_toolbar);
            setSupportActionBar(toolbar);
    }

    private void initActionBarDrawerToggle() {
        this.actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                this.drawerLayout,
                this.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        this.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();
    }

    private void setNavigationView() {
        this.navigationView = findViewById(R.id.nav_view);
        this.navigationView.setNavigationItemSelectedListener(this);
    }

    private void ifNewlyOpenedApp(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            try {
                mockClickHomeFragment();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public DrawerLayout getDrawerLayout() throws Exception {
        if(this.drawerLayout == null) throw new Exception("this.drawerLayout == null");
        return this.drawerLayout;
    }

    @Override
    public NavigationView getNavigationView() throws Exception {
        if(this.navigationView == null) throw new Exception("this.navigationView == null");
        return this.navigationView;
    }

    @Override
    public int[] getCurrentlySelectedNavViewItemIds() throws Exception {
        if(this.currentlySelectedNavViewItemIds == null) throw new Exception("this.navigationView == null");
        return this.currentlySelectedNavViewItemIds;
    }
}
