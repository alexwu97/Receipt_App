package com.example.receipt_app;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class JsonSearcher {

    public static String getReceiptString(JSONObject object, String searchedKey) {
        String value = "";
        boolean exists = object.has(searchedKey);
        if(exists) {
            try{
                JSONObject r = (JSONObject) object.get(searchedKey);
                value = r.get("text").toString();

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if(!exists) {
            Iterator<?> keys = object.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                try{
                    if ( object.get(key) instanceof JSONObject ) {
                        value = getReceiptString((JSONObject) object.get(key), searchedKey);
                    }else if ( object.get(key) instanceof JSONArray){
                        JSONArray obj = (JSONArray) object.get(key);
                        for(int i = 0; i < obj.length(); i++){
                            if (obj.get(i) instanceof JSONObject){
                                value = getReceiptString((JSONObject) obj.get(i), searchedKey);
                            }
                        }
                    }
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    public static double getReceiptNumber(JSONObject object, String searchedKey) {
        double value = 0;
        boolean exists = object.has(searchedKey);
        if(exists) {
            try{
                JSONObject r = (JSONObject) object.get(searchedKey);
                value = Double.parseDouble(r.get("text").toString().substring(1)); //eliminate the "$" sign

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if(!exists) {
            Iterator<?> keys = object.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                try{
                    if ( object.get(key) instanceof JSONObject ) {
                        value = getReceiptNumber((JSONObject) object.get(key), searchedKey);
                    }else if ( object.get(key) instanceof JSONArray){
                        JSONArray obj = (JSONArray) object.get(key);
                        for(int i = 0; i < obj.length(); i++){
                            if (obj.get(i) instanceof JSONObject){
                                value = getReceiptNumber((JSONObject) obj.get(i), searchedKey);
                            }
                        }
                    }
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    public static ArrayList<Item> getReceiptItems(JSONObject object, String searchedKey) {
        ArrayList<Item> value = new ArrayList<>();
        boolean exists = object.has(searchedKey);
        if(exists) {
            try{
                JSONObject r = (JSONObject) object.get(searchedKey);
                JSONArray arr = (JSONArray) r.get("valueArray");
                for (int i = 0; i < arr.length(); i++){
                    JSONObject objInsideArr = (JSONObject) arr.get(i);
                    JSONObject valueObject = (JSONObject) objInsideArr.get("valueObject");
                    JSONObject name = (JSONObject) valueObject.get("Name");
                    JSONObject price = (JSONObject) valueObject.get("TotalPrice");
                    String itemName = name.get("valueString").toString();
                    double itemPrice = cleanCost(price.get("text").toString().substring(1)); //eliminate "$" sign
                    Item item = new Item(itemName, itemPrice);
                    value.add(item);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }
        if(!exists) {
            Iterator<?> keys = object.keys();
            while( keys.hasNext() ) {
                String key = (String)keys.next();
                try{
                    if ( object.get(key) instanceof JSONObject ) {
                        value = getReceiptItems((JSONObject) object.get(key), searchedKey);
                    }else if ( object.get(key) instanceof JSONArray){
                        JSONArray obj = (JSONArray) object.get(key);
                        for(int i = 0; i < obj.length(); i++){
                            if (obj.get(i) instanceof JSONObject){
                                value = getReceiptItems((JSONObject) obj.get(i), searchedKey);
                            }
                        }
                    }
                }catch(Exception e ){
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    public static double cleanCost(String price){
        price = price.replace("," , ".");
        return Double.parseDouble(price);

    }


}
