package in.apps.maitreya.travelalarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static in.apps.maitreya.travelalarm.MainActivity.MY_PREFS_NAME;

public class FavoritesActivity extends AppCompatActivity {

    static final int ADD_REQ = 0;  // The request code for add
    RecyclerView recyclerView;
    private List<Route> routeList = new ArrayList<>();
    private RecyclerFavoritesAdapter mAdapter;
    private Context ctx=this;
    SharedPreferences appSharedPrefs;
    TextView no_list;
    Gson gson;
    MenuItem delete,ok_delete,cancel_delete;
    FloatingActionButton fab;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        toolbar = (Toolbar) findViewById(R.id.toolbar_favorites);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFavorites();
            }
        });
        //
        no_list =(TextView) findViewById(R.id.no_list_tv);
        //
        appSharedPrefs = getSharedPreferences(MY_PREFS_NAME,MODE_PRIVATE);
        gson = new Gson();
        String json = appSharedPrefs.getString("Route","");
        Type type = new TypeToken<List<Route>>(){}.getType();
        routeList =gson.fromJson(json, type);
        //
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                gson = new Gson();
                String json = gson.toJson(routeList);
                prefsEditor.putString("Route", json);
                prefsEditor.apply();
                //
                Intent intent=new Intent(ctx,AddRoute.class);
                startActivityForResult(intent,ADD_REQ);
            }
        });
        //
        recyclerView= (RecyclerView) findViewById(R.id.favorites_recycler_view);
        //

        mAdapter =new RecyclerFavoritesAdapter(routeList);
        RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //
        mAdapter.setCtx(this);
        //
        recyclerView.setAdapter(mAdapter);
        //
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(delete.isVisible()) {
                    Intent i=new Intent();
                    i.putExtra("pos",position);
                    setResult(RESULT_OK,i);
                    finish();
                }
            }

        }));
        //

        //
        if(routeList!=null) {
            if (routeList.size() == 0)
                no_list.setVisibility(View.VISIBLE);
            else
                no_list.setVisibility(View.GONE);
            //
        }
        else
            no_list.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorites_menu, menu);
        delete=menu.findItem(R.id.action_delete);
        ok_delete=menu.findItem(R.id.action_ok_delete);
        cancel_delete=menu.findItem(R.id.action_cancel_delete);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_delete:
                if(routeList.size()>0) {
                    toggleDelete(true, false);
                    fab.setVisibility(View.GONE);
                    toolbar.setNavigationIcon(null);
                }
                else
                    Toast.makeText(this,"There are no routes to delete!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_ok_delete:
                //
                for(int i=0;i<routeList.size();i++){
                    if(routeList.get(i).isDeleteYN()) {
                        routeList.remove(i);
                        mAdapter.notifyItemRemoved(i);
                        i--;
                    }
                }
                //
                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                gson = new Gson();
                String json = gson.toJson(routeList);
                prefsEditor.putString("Route", json);
                prefsEditor.apply();
                //
                toggleDelete(false,true);

                //
                gson = new Gson();
                json = appSharedPrefs.getString("Route", "");
                Type type = new TypeToken<List<Route>>() {
                }.getType();
                routeList = gson.fromJson(json, type);
                //
                mAdapter.notify(routeList);
                fab.setVisibility(View.VISIBLE);
                toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
                if(routeList.size()==0)
                    no_list.setVisibility(View.VISIBLE);

                break;
            case R.id.action_cancel_delete:
                toggleDelete(false,true);
                fab.setVisibility(View.VISIBLE);
                toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
                break;
            default:
                break;
        }
        return true;
    }
    public void toggleDelete(boolean checkBox_bool,boolean delete_bool){
        mAdapter.showCheckboxes(checkBox_bool);
        mAdapter.notifyDataSetChanged();
        delete.setVisible(delete_bool);
        //
        ok_delete.setVisible(checkBox_bool);
        cancel_delete.setVisible(checkBox_bool);

    }

    @Override
    public void onBackPressed(){
        exitFavorites();
    }
    public void exitFavorites(){
        setResult(RESULT_CANCELED);
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ADD_REQ:
            if (resultCode == RESULT_OK) {
                gson = new Gson();
                String json = appSharedPrefs.getString("Route", "");
                Type type = new TypeToken<List<Route>>() {
                }.getType();
                routeList = gson.fromJson(json, type);

                //
                mAdapter.notify(routeList);
                if (routeList.size()==0)
                    no_list.setVisibility(View.VISIBLE);
                else
                    no_list.setVisibility(View.GONE);
            }
        }
    }
}
