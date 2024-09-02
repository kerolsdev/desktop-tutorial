package com.kerolsmm.incognitopro.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kerolsmm.incognitopro.Data.AppExecutors;
import com.kerolsmm.incognitopro.Data.BookMark;
import com.kerolsmm.incognitopro.Data.Data_name;
import com.kerolsmm.incognitopro.Data.RoomLiveData;
import com.kerolsmm.incognitopro.R;
import com.kerolsmm.incognitopro.Utilts.BookmarkAdapter;
import com.kerolsmm.incognitopro.databinding.ActivityBookmarkBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BookmarkActivity extends AppCompatActivity implements BookmarkAdapter.onMoreBookmark {

    private Data_name data_name;
    private ActivityBookmarkBinding binding;
    private ArrayList<BookMark> arrayList;
    private BookmarkAdapter bookmarkAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookmarkBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.ToolbarBookmark);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        bookmarkAdapter = new BookmarkAdapter(new ArrayList<>(),this,this);
        data_name = Data_name.getInstance(getApplicationContext());
        binding.listBookmark.setAdapter(bookmarkAdapter);
        RoomLiveData roomLiveData = new ViewModelProvider(this).get(RoomLiveData.class);
        roomLiveData.getTask().observe(this, new Observer<List<BookMark>>() {
            @Override
            public void onChanged(List<BookMark> bookMarks) {
                arrayList = new ArrayList<>(bookMarks);

                bookmarkAdapter.setArrayList(arrayList);
                if (arrayList.isEmpty()){
                    binding.theListIsEmpty.setVisibility(View.VISIBLE);
                }else {
                    binding.theListIsEmpty.setVisibility(View.GONE);

                }
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public void onMore(MenuItem Position, BookMark bookMark) {
        if (Position.getItemId() == R.id.delete_bookmark){
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    data_name.kerols().deleteBookMark(bookMark);
                }
            });
        }else {
            try {
                final Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_SUBJECT,bookMark.getTitle());
                intent.putExtra( Intent.EXTRA_TEXT,bookMark.getUrl());
                intent.setType("text/plain");
                startActivity(intent);
            }catch (ActivityNotFoundException a){

                Toast.makeText(this , "You don't have apps to run", Toast.LENGTH_SHORT).show();

            }
        }
        }



    @Override
    public void onClickBookmark(BookMark bookMark) {
        Intent intent = new Intent(this,Home.class);
        intent.putExtra("url_site",bookMark.getUrl());
        /*intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
        intent.setAction("addnewtab");
        startActivity(intent);
        finish();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.more_toolbar_history_bookmark,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filter(newText);

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      /*  if (item.getItemId() == R.id.Delete_All) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.AllDelete))
                    .setMessage(getString(R.string.Do_All_Data))
                    .setPositiveButton(getString(R.string.Ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                                @Override
                                public void run() {
                                    data_name.kerols().deleteAllBookmark();
                                }
                            });
                            dialogInterface.dismiss();
                        }
                    })
                    .setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })

                    .show();

        }
        else */if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return true;
    }
    private void filter(String text) {
        ArrayList<BookMark> filteredList = new ArrayList<>();

        for (BookMark item : arrayList) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        bookmarkAdapter.setArrayList(filteredList);
    }

}