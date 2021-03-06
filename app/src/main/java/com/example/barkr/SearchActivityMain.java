package com.example.barkr;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SearchActivityMain extends Fragment implements View.OnClickListener{

    RecyclerView recyclerView;
    ArrayList<User> userList;
    SearchMainActivityAdapter adapter;
    FloatingActionButton advancedSearch;
    FirebaseUser user;
    EditText searchBar;
    String searchName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_search_main, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        user = FirebaseAuth.getInstance().getCurrentUser();


        advancedSearch = getView().findViewById(R.id.AdvancedSearchButton);
        advancedSearch.setOnClickListener(this);

        searchBar = (EditText) getView().findViewById(R.id.SearchBar);
        searchBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER))
                {
                    searchName = searchBar.getText().toString();
                    searchByName();
                    return true;
                }
                return false;
            }
        });

        if(user == null)
        {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
        }

        else {
            //System.out.println(user.getUid());
            //HumanProfile hp = new HumanProfile("name", "f", "Tyler, Texas", "903372", "hello", "12/12/1222");
            //DogProfile dogProfile = new DogProfile("pet", "corgi", "f", false, false, "hello", "12/12/1212");
            //ArrayList<DogProfile> dp = new ArrayList<DogProfile>();
            //dp.add(dogProfile);
            //userList.add(new User(user.getEmail(), hp, dp, user.getUid()));
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference().child("users");
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList = new ArrayList<User>();
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        if (userSnapshot.getValue(User.class) != null) {
                            userList.add(userSnapshot.getValue(User.class));
                        }
                    }
                    for (int i = 0; i < userList.size(); i++) {
                        if (userList.get(i).getUserId().equals(user.getUid())) {
                            userList.remove(i);
                        }
                    }

                    if (userList != null) {
                        if (getView() != null) {
                            recyclerView = getView().findViewById(R.id.RecyclerViewSearchMain);
                            adapter = new SearchMainActivityAdapter(getActivity(), userList);
                            recyclerView.setAdapter(adapter);
                            //here the second attribute for GridLayoutManager is 2 because this is the amount of columns we will have in the recycler view
                            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.w("SearchActivityMain", "loadPost:onCancelled", error.toException());
                }
            });
        }

    }


    //this was code from when this was not a fragment
   /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_main);

        navView = findViewById(R.id.navigation);

        userList = new ArrayList<User>();
        //User u = new User("livelikedragons");
        //u.setDogProfiles();
        HumanProfile hp = new HumanProfile("Human", "f", "Texas", "", "kdperez", "hello", 12);
        //u.setHumanProfile(hp);
        //userList.add(u);
        //userList.add(u);
        //userList.add(u);

        resultsRecycler = (RecyclerView) findViewById(R.id.RecyclerViewSearchMain);
        adapter = new SearchResultsAdapter(SearchActivityMain.this, userList);
        resultsRecycler.setAdapter(adapter);
        //here the second attribute for GridLayoutManager is 2 because this is the amount of columns we will have in the recycler view
        resultsRecycler.setLayoutManager(new GridLayoutManager(this, 2));



    }
*/

    public void searchByName() {
        //String testName = "Cody";
        //user

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users");
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<User> allUsers = new ArrayList<User>();
                ArrayList<User> matchedUsers = new ArrayList<User>();
                for(DataSnapshot userSnapshot: snapshot.getChildren())
                {
                    allUsers.add(userSnapshot.getValue(User.class));
                }
                for(int i = 0; i<allUsers.size();i++)
                {
                    if (allUsers.get(i).getHumanProfile().getname().toLowerCase().equals(searchName.toLowerCase()))
                    {
                        matchedUsers.add(allUsers.get(i));
                    }
                }
                //send values to another activity or screen
                Intent intent = new Intent(getContext(), SearchResultsActivity.class);
                intent.putExtra("SORTED_RESULTS", matchedUsers);

                startActivity(intent);
                ref.removeEventListener(this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("SearchActivityMain nameSearch", "loadPost:onCancelled", error.toException());
            }
        };
        ref.addValueEventListener(listener);
    }

    @Override
    public void onClick(View v)
    {
        if(v == advancedSearch)
        {
            startActivity(new Intent(getActivity(), AdvancedSearchActivity.class));
        }
    }

}