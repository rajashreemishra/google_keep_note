package com.example.google_keep_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText mettitle;
    private LinearLayout lldynamicview;
    private LinearLayout lladd;
    private LinearLayout lllist_item;
    private Button btn_add_to_task;
    private int task_itemid=0;
    private ArrayList<Item_List> arraylist;
    private DatabaseHelper dbhelper;
    private Toolbar mtoolbar;
    private boolean isupdate=false;
    private Task updatedtask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mettitle=findViewById(R.id.title_edit_text);
        lldynamicview=findViewById(R.id.ll_dynamic_view);
        lladd=findViewById(R.id.ll_add);
        lllist_item=findViewById(R.id.ll_list_item);
        btn_add_to_task=findViewById(R.id.btn_add_to_task);

        arraylist=new ArrayList<>();

        mtoolbar=findViewById(R.id.toolbar_icon);
        setSupportActionBar(mtoolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        dbhelper=new DatabaseHelper(MainActivity.this);
        Bundle data=getIntent().getExtras();
        if (data!=null){
            isupdate=data.getBoolean("ISUPDATE");
           updatedtask=(Task) data.getSerializable("TASK");
           if (isupdate && updatedtask!=null){
               ArrayList<Item_List> value=Item_List.convertJSONStringToArrayList(updatedtask.tasklist);
               mettitle.setText(updatedtask.task_title);

               for (int i=0;i<value.size();i++){
                   View v=LayoutInflater.from(MainActivity.this).inflate(R.layout.keep_note_cell_info,null);
                   EditText metcell_info=v.findViewById(R.id.item_edit_text);
                   ImageView mivcheck=v.findViewById(R.id.cross_image_view);

                   metcell_info.setText(value.get(i).itemname);
                   mivcheck.setVisibility(View.INVISIBLE);

                   lldynamicview.addView(v);

                   task_itemid=i+1;
                   arraylist.add(value.get(i));

               }

           }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    public void AddOnClick(View view){
        task_itemid++;

        View addView= LayoutInflater.from(MainActivity.this).inflate(R.layout.keep_note_cell_info,null);
       EditText metcell_info=addView.findViewById(R.id.item_edit_text);
       ImageView mivcheck=addView.findViewById(R.id.cross_image_view);
       mivcheck.setVisibility(View.INVISIBLE);






       metcell_info.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
                mivcheck.setVisibility(s.length()>0?View.VISIBLE:View.INVISIBLE);

           }

       });
             mivcheck.setOnClickListener(new View.OnClickListener() {
//           @Override
           public void onClick(View v) {
               metcell_info.setEnabled(false);
//               Task t=new Task();
//           ArrayList<Item_List> al=Item_List.convertJSONStringToArrayList(t.tasklist);
//               for (Item_List il:al){
//                   if (R.id.cross_image_view==t.id){
//                       al.remove(il);
//                   }
//               }
               Item_List item=new Item_List();
               item.id=task_itemid;
               item.itemname=metcell_info.getText().toString();
               item.checkbox=false;
               arraylist.add(item);
           }
       });



       lldynamicview.addView(addView);
   }
   public void AddToTask(View view){
        if (mettitle.getText().toString().isEmpty()){
            return;
        }
        Task mtask=new Task();
        mtask.task_title=mettitle.getText().toString();
        mtask.tasklist=Item_List.convertArrayToJSONString(arraylist);
        if (!isupdate){
            dbhelper.InsertToDatabase(dbhelper.getWritableDatabase(),mtask);
        }
        else {
            mtask.id=updatedtask.id;
            dbhelper.UpdateItemsToDatabase(dbhelper.getWritableDatabase(),mtask);
        }
       setResult(Activity.RESULT_OK);
       finish();
   }


}