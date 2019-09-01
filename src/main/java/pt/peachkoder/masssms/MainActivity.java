package pt.peachkoder.masssms;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.peachkoder.masssms.listview.ListViewItemCheckboxBaseAdapter;
import pt.peachkoder.masssms.listview.ListViewItemDTO;
import pt.peachkoder.masssms.sms.SmsController;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private Map<String, String> preferences = new HashMap();

    private SmsController sms;

    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        sms = SmsController.getInstance();
        sms.setActivity(this);
        //Ask for permission -> Android version 6+
        if(!sms.hasPermission()) {
            sms.showRequestPermissionsInfoAlertDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        message = data.getStringExtra("message");
    }

    private void init(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Get listview checkbox.
        final ListView listViewWithCheckbox = (ListView)findViewById(R.id.list_view_with_checkbox);

        // Initiate listview data.
        final List<ListViewItemDTO> initItemList = this.getInitViewItemDtoList();

        // Create a custom list view adapter with checkbox control.
        final ListViewItemCheckboxBaseAdapter listViewDataAdapter =
                new ListViewItemCheckboxBaseAdapter(initItemList, getApplicationContext());

        listViewDataAdapter.notifyDataSetChanged();

        // Set data adapter to list view.
        listViewWithCheckbox.setAdapter(listViewDataAdapter);

        listViewWithCheckbox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get user selected item.
                Object itemObject = parent.getAdapter().getItem(position);

                // Translate the selected item to DTO object.
                ListViewItemDTO itemDto = (ListViewItemDTO)itemObject;

                // Get the checkbox.
                CheckBox itemCheckbox = (CheckBox) view.findViewById(R.id.list_view_item_checkbox);

                // Reverse the checkbox and clicked item check state.
                if(itemDto.isChecked())
                {
                    itemCheckbox.setChecked(false);
                    itemDto.setChecked(false);
                }else
                {
                    itemCheckbox.setChecked(true);
                    itemDto.setChecked(true);
                }

                //Toast.makeText(getApplicationContext(), "select item text : " + itemDto.getItemText(), Toast.LENGTH_SHORT).show();
            }
        });

        // Click checkbox to select all listview items with checkbox checked.
        final CheckBox chkAll = (CheckBox)findViewById(R.id.checkbox_select_all);
        chkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = initItemList.size();
                for(int i=0;i<size;i++)
                {
                    initItemList.get(i).setChecked(chkAll.isChecked());
                }

                listViewDataAdapter.notifyDataSetChanged();
            }
        });


    }

    private List<ListViewItemDTO> getInitViewItemDtoList() {

        Map<String, String> map = new HashMap<String, String>() ;
        map.put("Jose", "555-1234");
        map.put("Maria", "555-3245");
        map.put("Cesar", "555-7890");
        map.put("Joaquim", "555-1580");

        Set<Map.Entry<String, String>> set = map.entrySet();
        Iterator it = set.iterator();
        List<ListViewItemDTO> ret = new ArrayList<>();

        while (it.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry)it.next();

            ListViewItemDTO dto = new ListViewItemDTO();

            dto.setName(entry.getKey());
            dto.setNumber(entry.getValue());
            dto.setChecked(false);

            ret.add(dto);
        }

        return ret;
    }

    private void getPreferences(){

        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String max = getString(R.string.pref_max_sms);
        String enable = getString(R.string.pref_enable_schedule);
        String start = getString(R.string.pref_start_time);
        String end = getString(R.string.pref_end_time);
        String freq = getString(R.string.pref_freq_per_minute);

        preferences.put(max, sharedPreferences.getString(max, "0"));
        preferences.put(enable,  sharedPreferences.getString(enable, "0"));
        preferences.put(start,  sharedPreferences.getString(start, "21:00"));
        preferences.put(end,  sharedPreferences.getString(end, "7:00"));
        preferences.put(freq,  sharedPreferences.getString(freq, "1"));

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_message) {

            startActivity(new Intent(MainActivity.this, MessageActivity.class) );


        } else if (id == R.id.nav_groups) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_config) {

            startActivity(new Intent(MainActivity.this, SettingsActivity.class) );

        } else if (id == R.id.nav_view) {

        } else if (id == R.id.nav_send) { send(); }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void send(){

        if (message.isEmpty()){

            Toast.makeText(getBaseContext(),
                    getString(R.string.msg_empty),
                    Toast.LENGTH_LONG).show();
            return ;

        }

        if (sms.getRunning().get()){

            Toast.makeText(getBaseContext(),
                    getString(R.string.msg_sending_sms),
                    Toast.LENGTH_LONG).show();
            return ;

        }

        int freq = Integer.parseInt( preferences.get(getString(R.string.pref_freq_per_minute)));

        if (freq<=1){

            freq = 1;

        } else {

            Toast.makeText(getBaseContext(),
                    getString(R.string.msg_sending_sms_frequency_warning),
                    Toast.LENGTH_LONG).show();
        }

        sms.broadcast(null, message, freq);

    }
}
