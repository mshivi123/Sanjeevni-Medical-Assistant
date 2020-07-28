package com.finalproject.it.sanjeevni.activities.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.finalproject.it.sanjeevni.R;
import com.finalproject.it.sanjeevni.fragment.ProfileView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Approve_Requests extends AppCompatActivity {

    private ListView listView;
    private List<HashMap<String,String>> display_list=new ArrayList<>();
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve__requests);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Sanjeevni");
        getSupportActionBar().setSubtitle("Approve Requests");

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();

        final String[] from={"Name","City","Contact","Email","userID"};
        final int[] to={R.id.name,R.id.city,R.id.phone,R.id.email};
        listView=findViewById(R.id.list_view);

        if(fAuth.getCurrentUser()!=null) {
            fStore.collection("Temp_Doctor_Details").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    display_list.clear();
                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                        HashMap<String, String> temp = new HashMap<>();
                        temp.put("Name", snapshot.getString("name"));
                        temp.put("City", snapshot.getString("city"));
                        temp.put("Contact", snapshot.getString("phoneNo"));
                        temp.put("Email", snapshot.getString("emailID"));
                        temp.put("userID", snapshot.getId());
                        display_list.add(temp);
                    }
                    My_custom_adapter adapter = new My_custom_adapter(getBaseContext(), R.layout.list_view_items, display_list);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.logout_btn) {
            FirebaseAuth.getInstance().signOut();
            recreate();
        }
        else if(id==R.id.refresh){
            recreate();
        }
        else if(id==R.id.profile_btm){
            startActivity(new Intent(getBaseContext(), ProfileView.class));
        }
        return super.onOptionsItemSelected(item);
    }
}