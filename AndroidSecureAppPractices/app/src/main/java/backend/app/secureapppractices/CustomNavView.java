package backend.app.secureapppractices;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import backend.app.secureapppractices.ui.main.courses.CourseFragmentConstructor;
import backend.app.secureapppractices.ui.main.courses.HomeFragment;
import backend.app.secureapppractices.ui.main.courses.comments.CommentsFragment;
import backend.app.secureapppractices.ui.main.courses.practical.CoursePractical1;
import backend.app.secureapppractices.ui.main.courses.practical.CoursePractical2;
import backend.app.secureapppractices.ui.main.courses.theoretical.CourseTheoretical1;
import backend.app.secureapppractices.ui.main.courses.theoretical.CourseTheoretical2;

interface CustomNavView extends NavigationView.OnNavigationItemSelectedListener  {

    public static final CourseFragmentConstructor[] courseFragmentConstructors = new CourseFragmentConstructor[]{
            HomeFragment::new,
            CourseTheoretical1::new,
            CoursePractical1::new,
            CourseTheoretical2::new,
            CoursePractical2::new,
            CommentsFragment::new
    };
    public static final int[] courseFragmentNavElementIds = {
            R.id.nav_course_home,
            R.id.nav_course_theoretical,
            R.id.nav_course_practical,
            R.id.nav_course_1,
            R.id.nav_course_2,
            R.id.nav_comments
    };
    public static final int[] navViewGroupIds = {
            R.id.group_type,
            R.id.group_number
    };

    public DrawerLayout getDrawerLayout() throws Exception;
    public NavigationView getNavigationView() throws Exception;
    public FragmentManager getSupportFragmentManager();
    public int[] getCurrentlySelectedNavViewItemIds() throws Exception;

    @Override
    default public boolean onNavigationItemSelected(@NonNull MenuItem pressedMenuItem) {
        try {
            setItemCheckedAndUncheckPreviousItemFromSameGroup(pressedMenuItem);
            changeCourseFragmentIfFullyChosen();
            closeDrawerIfFullyChosen();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    default public void setItemCheckedAndUncheckPreviousItemFromSameGroup(@NonNull MenuItem pressedMenuItem) throws Exception {
        int groupNumber = getGroupNumber(pressedMenuItem);
        if( isCorrectGroupNumber(groupNumber) ) {
            if( isHomeGroupNumber(groupNumber) ) uncheckAllMenuItems();
            else uncheckPreviousItemFromSameGroup( groupNumber );
            this.getCurrentlySelectedNavViewItemIds()[groupNumber] = pressedMenuItem.getItemId();
            pressedMenuItem.setChecked(true);
        } else {
            uncheckAllMenuItems();
            this.getCurrentlySelectedNavViewItemIds()[groupNumber] = pressedMenuItem.getItemId();
            pressedMenuItem.setChecked(true);
        }
    }

    default public void changeCourseFragmentIfFullyChosen() throws Exception {
        if(isFullyChosen()) {
            if(this.getCurrentlySelectedNavViewItemIds()[2]!=-1) openNewFragment(courseFragmentConstructors[0].getNewCourseFragment());
            else {
                if(this.getCurrentlySelectedNavViewItemIds()[0]==courseFragmentNavElementIds[1]) {
                    if(this.getCurrentlySelectedNavViewItemIds()[1]==courseFragmentNavElementIds[3])
                        openNewFragment(courseFragmentConstructors[1].getNewCourseFragment());
                    if(this.getCurrentlySelectedNavViewItemIds()[1]==courseFragmentNavElementIds[4])
                        openNewFragment(courseFragmentConstructors[3].getNewCourseFragment());
                }
                else if(this.getCurrentlySelectedNavViewItemIds()[0]==courseFragmentNavElementIds[2]) {
                    if(this.getCurrentlySelectedNavViewItemIds()[1]==courseFragmentNavElementIds[3])
                        openNewFragment(courseFragmentConstructors[2].getNewCourseFragment());
                    else if(this.getCurrentlySelectedNavViewItemIds()[1]==courseFragmentNavElementIds[4])
                        openNewFragment(courseFragmentConstructors[4].getNewCourseFragment());
                }
            }
        }
    }

    default public void closeDrawerIfFullyChosen() throws Exception {
        if(isFullyChosen()) this.getDrawerLayout().closeDrawer(GravityCompat.START);
    }

    default public boolean isFullyChosen() throws Exception {
        if(
                this.getCurrentlySelectedNavViewItemIds()[2]!=-1 ||
                        (
                                this.getCurrentlySelectedNavViewItemIds()[0]!=-1 &&
                                this.getCurrentlySelectedNavViewItemIds()[1]!=-1
                        )
        ) {
            return true;
        } else return false;
    }

    default public int getGroupNumber(@NonNull MenuItem pressedMenuItem) {
        if(pressedMenuItem.getGroupId() == navViewGroupIds[0]) return 0;
        else if(pressedMenuItem.getGroupId() == navViewGroupIds[1]) return 1;
        else if(pressedMenuItem.getItemId() == courseFragmentNavElementIds[0]) return 2;
        else return -1;
    }

    default public boolean isCorrectGroupNumber(int groupNumber) {
        if( groupNumber != -1 && groupNumber >= 0 && groupNumber <= 2 ) return true;
        else return false;
    }

    default public boolean isHomeGroupNumber(int groupNumber) {
        if(groupNumber==2) return true;
        else return false;
    }

    default public void uncheckAllMenuItems() throws Exception {
        for (int i = 0; i < this.getCurrentlySelectedNavViewItemIds().length; i++) {
            safeUncheckItem(this.getCurrentlySelectedNavViewItemIds()[i]);
            this.getCurrentlySelectedNavViewItemIds()[i] = -1;
        }
    }

    default public void uncheckPreviousItemFromSameGroup(int groupNumber) throws Exception {
        safeUncheckItem(this.getCurrentlySelectedNavViewItemIds()[groupNumber]);
        this.getCurrentlySelectedNavViewItemIds()[groupNumber] = -1;
        uncheckHomeIfChecked();
    }

    default public void safeUncheckItem(int menuItemId) throws Exception {
        if(menuItemId!=-1) {
            MenuItem prevSelectedMenuItem = this.getNavigationView().getMenu().findItem(menuItemId);
            prevSelectedMenuItem.setChecked(false);
        }
    }

    default public void uncheckHomeIfChecked() throws Exception {
        safeUncheckItem(this.getCurrentlySelectedNavViewItemIds()[2]);
        this.getCurrentlySelectedNavViewItemIds()[2] = -1;
    }

    default public void openNewFragment(Fragment fragmentInstance) {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentInstance).commit();
    }

    default public void mockClickHomeFragment() throws Exception {
        openNewFragment(courseFragmentConstructors[0].getNewCourseFragment());
        this.getNavigationView().setCheckedItem(courseFragmentNavElementIds[0]);
        this.getCurrentlySelectedNavViewItemIds()[2] = courseFragmentNavElementIds[0];
    }
}
