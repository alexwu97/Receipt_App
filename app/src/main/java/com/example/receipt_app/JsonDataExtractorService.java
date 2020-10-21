package com.example.receipt_app;

import com.example.receipt_app.model.ReceiptItems;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonDataExtractorService {

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
        }else {
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
                value = cleanCost(r.get("text").toString());

            }catch(Exception e){
                e.printStackTrace();
            }
        }else {
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

    public static List<ReceiptItems> getReceiptItems(JSONObject object, String searchedKey) {
        List<ReceiptItems> value = new ArrayList<>();
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
                    int itemQuantity = 1;
                    if(valueObject.has("Quantity")){
                        JSONObject quantity = (JSONObject) valueObject.get("Quantity");
                        itemQuantity = cleanQuantity(quantity.get("text").toString());
                    }
                    String itemName = name.get("valueString").toString();
                    double itemPrice = cleanCost(price.get("text").toString()); //eliminate "$" sign
                    ReceiptItems item = new ReceiptItems();
                    item.setQuantity(itemQuantity);
                    item.setItemName(itemName);
                    item.setPrice(itemPrice);
                    value.add(item);
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }else {
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
        StringBuffer priceSB = new StringBuffer();
        for (int i = 0; i < price.length(); i++){
            char character = price.charAt(i);
            if(Character.isDigit(character) || character == '.'){
                priceSB.append(character);
            }
        }
        return Double.parseDouble(priceSB.toString());

    }

    public static int cleanQuantity(String quantity){
        quantity = quantity.trim();
        StringBuffer quantitySB = new StringBuffer();

        boolean containNonDigitAfterDigits = false;

        for (int i = 0; i < quantity.length(); i++){
            char character = quantity.charAt(i);
            if(!Character.isDigit(character) && character != ',' && containNonDigitAfterDigits == true) break;
            if(Character.isDigit(character)){
                quantitySB.append(character);
                containNonDigitAfterDigits = true;
            }
        }

        if(quantitySB.toString().isEmpty()){
            return 1;
        }
        if(Integer.parseInt(quantitySB.toString()) <= 0){
            return 1;
        }
        return Integer.parseInt(quantitySB.toString());
    }
}
