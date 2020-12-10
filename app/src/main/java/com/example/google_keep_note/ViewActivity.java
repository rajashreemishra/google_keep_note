package com.example.google_keep_note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity implements Class_Recycler_View.TaskListListener {
    public RecyclerView rcview;
    private DatabaseHelper dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        rcview=findViewById(R.id.recycler_view_info);
        rcview.setLayoutManager(new StaggeredGridLayoutManager(3,RecyclerView.VERTICAL));
        dbhelper=new DatabaseHelper(ViewActivity.this);
        setRecyclerAdapter();

    }
    public void floatingButtonClicked(View view){
        startActivityForResult(new Intent(ViewActivity.this,MainActivity.class),901);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==901||requestCode==408 )&& resultCode== Activity.RESULT_OK){
            setRecyclerAdapter();
        }
    }

    private void setRecyclerAdapter() {
        ArrayList<Task> arraytask=dbhelper.RetrieveDataFromDatabase(dbhelper.getReadableDatabase());
        Class_Recycler_View adapter=new Class_Recycler_View(ViewActivity.this,arraytask);
        adapter.setListener(ViewActivity.this);
        rcview.setAdapter(adapter);


    }

    @Override
    public void onUpdateItems(Item_List updateitems, Task task, boolean checkedvalue) {
        ArrayList<Item_List> oldItemValues=Item_List.convertJSONStringToArrayList(task.tasklist);
        if (oldItemValues != null && oldItemValues.size()>0){
            for (Item_List oldValues:oldItemValues){
                if (oldValues.id==updateitems.id){
                    oldValues.checkbox=checkedvalue;
                }
            }
        }
        Task newTaskforUpdate= new Task();
        newTaskforUpdate.id=task.id;
        newTaskforUpdate.task_title=task.task_title;
        newTaskforUpdate.tasklist=Item_List.convertArrayToJSONString(oldItemValues);
        dbhelper.UpdateItemsToDatabase(dbhelper.getWritableDatabase(),newTaskforUpdate);
        setRecyclerAdapter();

    }

    @Override
    public void TaskClicked(Task task) {
        startActivityForResult(new Intent(ViewActivity.this,MainActivity.class).
                putExtra("TASK",task).putExtra("ISUPDATE",true),408);
    }

    @Override
    public void onDeleteItems(Task task) {
        dbhelper.DeleteItemsFromDatabase(task,dbhelper.getWritableDatabase());
        setRecyclerAdapter();
    }
}
