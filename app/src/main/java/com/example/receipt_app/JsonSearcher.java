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
                value = r.get("valueString").toString();

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
                value = Double.parseDouble(r.get("valueNumber").toString());

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

    /*public static HashMap<String, Double> getReceiptItems(JSONObject object, String searchedKey) {
        HashMap<String, Double> value;
        boolean exists = object.has(searchedKey);
        if(exists) {
            try{
                JSONObject r = (JSONObject) object.get(searchedKey);


                value = Double.parseDouble(r.get("valueNumber").toString());

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
    }*/


}
