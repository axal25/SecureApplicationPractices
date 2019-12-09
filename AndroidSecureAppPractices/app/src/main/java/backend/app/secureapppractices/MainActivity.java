package backend.app.secureapppractices;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import backend.app.secureapppractices.ui.main.courses.CourseFragmentConstructor;
import backend.app.secureapppractices.ui.main.courses.HomeFragment;
import backend.app.secureapppractices.ui.main.courses.practical.CoursePractical1;
import backend.app.secureapppractices.ui.main.courses.practical.CoursePractical2;
import backend.app.secureapppractices.ui.main.courses.theoretical.CourseTheoretical1;
import backend.app.secureapppractices.ui.main.courses.theoretical.CourseTheoretical2;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;

    private static final CourseFragmentConstructor[] courseFragmentConstructors = new CourseFragmentConstructor[]{
            HomeFragment::new,
            CourseTheoretical1::new,
            CoursePractical1::new,
            CourseTheoretical2::new,
            CoursePractical2::new
    };
    private static final int[] courseFragmentNavElementIds = {
            R.id.nav_course_home,
            R.id.nav_course_theoretical,
            R.id.nav_course_practical,
            R.id.nav_course_1,
            R.id.nav_course_2
    };
    private static final int[] navViewGroupIds = {
            R.id.group_type,
            R.id.group_number
    };
    private int[] currentlySelectedNavViewItemIds = {-1,-1,-1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        ifNewlyOpenedApp(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if(this.drawerLayout.isDrawerOpen(GravityCompat.START)) this.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    private boolean isFullyChosen() {
        if(
                this.currentlySelectedNavViewItemIds[2]!=-1 ||
                (
                        this.currentlySelectedNavViewItemIds[0]!=-1 &&
                        this.currentlySelectedNavViewItemIds[1]!=-1
                )
        ) return true;
        else return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem pressedMenuItem) {
        setItemCheckedAndUncheckPreviousItemFromSameGroup(pressedMenuItem);
        changeCourseFragmentIfFullyChosen();
        closeDrawerIfFullyChosen();
        return false;
    }

    private void closeDrawerIfFullyChosen() {
        if(isFullyChosen()) this.drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setItemCheckedAndUncheckPreviousItemFromSameGroup(@NonNull MenuItem pressedMenuItem) {
        int groupNumber = getGroupNumber(pressedMenuItem);
        if( isCorrectGroupNumber(groupNumber) ) {
            if( isHomeGroupNumber(groupNumber) ) uncheckAllMenuItems();
            else uncheckPreviousItemFromSameGroup( groupNumber );
            this.currentlySelectedNavViewItemIds[groupNumber] = pressedMenuItem.getItemId();
            pressedMenuItem.setChecked(true);
        }
    }

    private boolean isCorrectGroupNumber(int groupNumber) {
        if( groupNumber != -1 && groupNumber >= 0 && groupNumber <= 2 ) return true;
        else return false;
    }

    private boolean isHomeGroupNumber(int groupNumber) {
        if(groupNumber==2) return true;
        else return false;
    }

    private void uncheckAllMenuItems() {
        for (int i = 0; i < this.currentlySelectedNavViewItemIds.length; i++) {
            safeUncheckItem(this.currentlySelectedNavViewItemIds[i]);
            this.currentlySelectedNavViewItemIds[i] = -1;
        }
    }

    private void uncheckPreviousItemFromSameGroup(int groupNumber) {
        safeUncheckItem(this.currentlySelectedNavViewItemIds[groupNumber]);
        this.currentlySelectedNavViewItemIds[groupNumber] = -1;
        uncheckHomeIfChecked();
    }

    private void uncheckHomeIfChecked() {
        safeUncheckItem(this.currentlySelectedNavViewItemIds[2]);
        this.currentlySelectedNavViewItemIds[2] = -1;
    }

    private void safeUncheckItem(int menuItemId) {
        if(menuItemId!=-1) {
            MenuItem prevSelectedMenuItem = this.navigationView.getMenu().findItem(menuItemId);
            prevSelectedMenuItem.setChecked(false);
        }
    }

    private int getGroupNumber(@NonNull MenuItem pressedMenuItem) {
        if(pressedMenuItem.getGroupId() == navViewGroupIds[0]) return 0;
        else if(pressedMenuItem.getGroupId() == navViewGroupIds[1]) return 1;
        else if(pressedMenuItem.getItemId() == courseFragmentNavElementIds[0]) return 2;
        else return -1;
    }

    private void changeCourseFragmentIfFullyChosen() {
        if(isFullyChosen()) {
            if(this.currentlySelectedNavViewItemIds[2]!=-1) openNewFragment(courseFragmentConstructors[0].getNewCourseFragment());
            else {
                if(this.currentlySelectedNavViewItemIds[0]==courseFragmentNavElementIds[1]) {
                    if(this.currentlySelectedNavViewItemIds[1]==courseFragmentNavElementIds[3])
                        openNewFragment(courseFragmentConstructors[1].getNewCourseFragment());
                    if(this.currentlySelectedNavViewItemIds[1]==courseFragmentNavElementIds[4])
                        openNewFragment(courseFragmentConstructors[3].getNewCourseFragment());
                }
                else if(this.currentlySelectedNavViewItemIds[0]==courseFragmentNavElementIds[2]) {
                    if(this.currentlySelectedNavViewItemIds[1]==courseFragmentNavElementIds[3])
                        openNewFragment(courseFragmentConstructors[2].getNewCourseFragment());
                    else if(this.currentlySelectedNavViewItemIds[1]==courseFragmentNavElementIds[4])
                        openNewFragment(courseFragmentConstructors[4].getNewCourseFragment());
                }
            }
        }
    }

    private void openNewFragment(Fragment fragmentInstance) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentInstance).commit();
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
            mockClickHomeFragment();
        }
    }

    private void mockClickHomeFragment() {
        openNewFragment(courseFragmentConstructors[0].getNewCourseFragment());
        this.navigationView.setCheckedItem(courseFragmentNavElementIds[0]);
        this.currentlySelectedNavViewItemIds[2] = courseFragmentNavElementIds[0];
    }
}
