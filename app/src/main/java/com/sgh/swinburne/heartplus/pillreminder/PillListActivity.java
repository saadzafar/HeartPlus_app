package com.sgh.swinburne.heartplus.pillreminder;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.sgh.swinburne.heartplus.R;

/**
 * Created by VivekShah.
 */

/*Tap a class to edit it or long press to delete it*/

public class PillListActivity extends ListActivity {
    private static final int ACTIVITIES_CREATE = 0;
    private static final int ACTIVITIES_EDIT = 1;

    private PillDbAdapter DatabaseHelper;  //class level pillDbAdapter instance variable

    @Override
    // Save the values to the database
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pill_list);
        DatabaseHelper = new PillDbAdapter(this); // PillDbAdapter in instantiated here
        DatabaseHelper.open(); // open database
        filltheData(); //loads data from db to listview
        registerForContextMenu(getListView());
    }

    private void filltheData() {
        Cursor pillCursor = DatabaseHelper.fetchAllPills();

        startManagingCursor(pillCursor);

        // Create an array to specify the fields we want to display in the list (only TITLE)
        String[] frm = new String[]{PillDbAdapter.KEY_NAME}; //selection criteria for the query i.e. return task title

        // and an array of the fields we want to bind those fields to (in this case just text1)
        int[] _to = new int[]{R.id.text1};

        // Now create a simple cursor adapter and set it to display
        SimpleCursorAdapter reminders =
                new SimpleCursorAdapter(this, R.layout.pill_row, pillCursor, frm, _to); //map columns from cursor to textview like layout file
        setListAdapter(reminders);//inform list of data
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.item_menu_list, menu);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int feature_Id, MenuItem itm) {
        switch (itm.getItemId()) {
            case R.id.m_insert:
                create_Reminder();
                return true;
            case R.id.m_settings:
                Intent intnt = new Intent(this, TaskPref.class);
                startActivity(intnt);
                return true;
        }

        return super.onMenuItemSelected(feature_Id, itm);
    }

    @Override
    public void onCreateContextMenu(ContextMenu _menu, View view, ContextMenu.ContextMenuInfo _menuInfo) {
        super.onCreateContextMenu(_menu, view, _menuInfo);
        MenuInflater minf = getMenuInflater();
        minf.inflate(R.menu.item_logpressed, _menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem _item) //defines method handling menu events such as longpress on list view
    {
        switch (_item.getItemId()) {
            case R.id.menu_delete:
                AdapterView.AdapterContextMenuInfo inform = (AdapterView.AdapterContextMenuInfo) _item.getMenuInfo(); //method getmenuinfo() obtain instance of adaptercontextmenuinfo on the clicked item.
                DatabaseHelper.deletePill(inform.id);//delete task of id..which came from adaptercontextmenu
                filltheData();//refresh list view
                return true;
        }
        return super.onContextItemSelected(_item);
    }

    private void create_Reminder() {
        Intent intent = new Intent(this, PillEditActivity.class);
        startActivityForResult(intent, ACTIVITIES_CREATE);
    }

    @Override
    protected void onListItemClick(ListView lv, View view, int posi, long _id) {
        super.onListItemClick(lv, view, posi, _id);
        Intent intent = new Intent(this, PillEditActivity.class);
        intent.putExtra(PillDbAdapter.KEY_RID, _id); //gives id of reminder being edited
        startActivityForResult(intent, ACTIVITIES_EDIT);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent _intent) //result_ok inspected here
    {
        super.onActivityResult(reqCode, resCode, _intent);
        filltheData();//method called when returning from another activity in the case of update or edit in the reminder.
    }
}
