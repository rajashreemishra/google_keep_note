package com.example.google_keep_note;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Class_Recycler_View extends RecyclerView.Adapter<Class_Recycler_View.ItemHolder>{
    private Context context;
    private ArrayList<Task> arrayList;
    private TaskListListener listener;


    public Class_Recycler_View(Context context, ArrayList<Task> arrayList){
       this.context=context;
       this.arrayList=arrayList;
    }
    public void setListener(TaskListListener listener){
        this.listener=listener;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_info,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Task task_item=arrayList.get(position);
        holder. mtv_title_task.setText(task_item.task_title);
        ArrayList<Item_List> arrayitem=Item_List.convertJSONStringToArrayList(task_item.tasklist);
        holder.cardview_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.TaskClicked(task_item);
                }
            }
        });
        holder.lldynamicview.removeAllViews();
        for (Item_List il:arrayitem){
            View v=LayoutInflater.from(context).inflate(R.layout.checkbox_cell_info,null);
            TextView mtvdynamicview=v.findViewById(R.id.tv_dynamic_view_items);
            CheckBox mchkbox=v.findViewById(R.id.checkbox_items);

            mtvdynamicview.setText(il.itemname);
            mchkbox.setChecked(il.checkbox);
            if (il.checkbox){
                mtvdynamicview.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }
            mchkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (listener !=null){
                        listener.onUpdateItems(il,task_item,isChecked);
                    }

                }
            });
          holder.mdeletell.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (listener!=null){
                     listener.onDeleteItems(task_item);
                 }
             }
         });
            holder.lldynamicview.addView(v);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView mtv_title_task;
        private LinearLayout lldynamicview;
        private CardView cardview_root;
        private LinearLayout mdeletell;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mtv_title_task=itemView.findViewById(R.id.title_task);
            lldynamicview=itemView.findViewById(R.id.ll_card_view);
            cardview_root=itemView.findViewById(R.id.card_recycle_view);
            mdeletell=itemView.findViewById(R.id.image_view_delete);

        }
    }
    public interface TaskListListener{
        void onUpdateItems(Item_List updateitems,Task task,boolean checkedvalue);

        void TaskClicked(Task task);

        void onDeleteItems(Task task);
    }
}
