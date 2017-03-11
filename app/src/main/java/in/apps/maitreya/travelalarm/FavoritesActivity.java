package in.apps.maitreya.travelalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Route> routeList = new ArrayList<>();;
    private RecyclerFavoritesAdapter mAdapter;
    private Context ctx=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_favorites);
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_back);
        setSupportActionBar(toolbar);
        //
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitFavorites();
            }
        });
        //
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ctx,AddRoute.class);
                startActivity(intent);
            }
        });
        //
        recyclerView= (RecyclerView) findViewById(R.id.favorites_recycler_view);
        //

        //
        mAdapter =new RecyclerFavoritesAdapter(routeList);
        RecyclerView.LayoutManager mLayoutManager =new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        //
        prepareRouteData();
        //
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.favorites_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*
            case R.id.home:
                exitFavorites();
                return true;
                */
            default:
                break;
        }
        return true;
    }
    @Override
    public void onBackPressed(){
        exitFavorites();
    }
    public void exitFavorites(){
        setResult(RESULT_CANCELED);
        finish();
    }
    public void prepareRouteData(){
        Route route=new Route("Source");
        routeList.add(route);
        route=new Route("Destination");
        routeList.add(route);
        route=new Route("Destination1");
        routeList.add(route);
        route=new Route("Destination2");
        routeList.add(route);
        route=new Route("Destination 3");
        routeList.add(route);

        mAdapter.notifyDataSetChanged();
    }
}
