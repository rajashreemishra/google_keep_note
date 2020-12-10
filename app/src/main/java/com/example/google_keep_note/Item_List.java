package com.example.google_keep_note;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;

public class Item_List {
    public boolean checkbox;
    public String itemname;
    public int id;


    public static String KEY_ID="id";
    public static String KEY_NAME="itemname";
    public static String KEY_CHECKED="checkbox";

    public static String convertArrayToJSONString(ArrayList<Item_List> arrayString){
        JSONArray itemArray =new JSONArray();
        for (Item_List il:arrayString){
            try{
                JSONObject jobj =new JSONObject();
                jobj.put(KEY_ID,il.id);
                jobj.put(KEY_NAME,il.itemname);
                jobj.put(KEY_CHECKED,il.checkbox);
                itemArray.put(jobj);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return itemArray.toString();
    }

    public static ArrayList<Item_List> convertJSONStringToArrayList(String JSON_array_string){
        ArrayList<Item_List> items=new ArrayList<>();
        try{
            JSONArray jarray=new JSONArray(JSON_array_string);
            if (jarray.length()>0){
                for (int i=0;i<jarray.length();i++){
                    Item_List list_item=new Item_List();
                    JSONObject jsonobj=jarray.optJSONObject(i);
                    list_item.id=jsonobj.optInt(KEY_ID);
                    list_item.itemname=jsonobj.optString(KEY_NAME);
                    list_item.checkbox=jsonobj.optBoolean(KEY_CHECKED);
                    items.add(list_item);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}

