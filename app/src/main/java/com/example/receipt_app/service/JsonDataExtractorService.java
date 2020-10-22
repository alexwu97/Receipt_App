package com.example.receipt_app.service;

import com.example.receipt_app.model.ReceiptItem;
import com.example.receipt_app.model.ReceiptMain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonDataExtractorService {

    public static ReceiptMain getReceiptFromReceiptData(JSONObject object) throws JSONException {
        ReceiptMain receipt = new ReceiptMain();
        String merchantName = getStringFromJSONObject(object, "MerchantName").getString("text");
        double total = cleanCost(getStringFromJSONObject(object, "Total").getString("text"));
        receipt.setMerchantName(merchantName);
        receipt.setTotal(total);
        return receipt;
    }

    private static JSONObject getStringFromJSONObject(JSONObject object, String targetKey) throws JSONException {
        if (object.has(targetKey)) {
            return object.getJSONObject(targetKey);
        }

        JSONObject value = null;
        Iterator<?> keys = object.keys();
        while (keys.hasNext() && value == null) {
            String key = (String) keys.next();

            if (object.get(key) instanceof JSONObject) {
                value = getStringFromJSONObject(object.getJSONObject(key), targetKey);
            } else if (object.get(key) instanceof JSONArray) {
                value = getStringFromJSONArray(object.getJSONArray(key), targetKey);
            }
        }
        return value;
    }


    private static JSONObject getStringFromJSONArray(JSONArray array, String targetKey) throws JSONException {
        JSONObject value = null;

        for (int i = 0; i < array.length(); i++) {
            if (value != null) {
                return value;
            }

            if (array.get(i) instanceof JSONObject) {
                value = getStringFromJSONObject(array.getJSONObject(i), targetKey);
            } else if (array.get(i) instanceof JSONArray) {
                value = getStringFromJSONArray(array.getJSONArray(i), targetKey);
            }
        }
        return value;
    }

    public static List<ReceiptItem> getReceiptItems(JSONObject object, String searchedKey) {
        List<ReceiptItem> value = new ArrayList<>();
        if (object.has(searchedKey)) {
            try {
                JSONObject foundedObject = (JSONObject) object.get(searchedKey);
                JSONArray itemArray = (JSONArray) foundedObject.get("valueArray");
                for (int i = 0; i < itemArray.length(); i++) {
                    JSONObject objInsideArr = (JSONObject) itemArray.get(i);
                    JSONObject valueObject = (JSONObject) objInsideArr.get("valueObject");
                    JSONObject name = (JSONObject) valueObject.get("Name");
                    JSONObject price = (JSONObject) valueObject.get("TotalPrice");
                    int itemQuantity = 1;
                    if (valueObject.has("Quantity")) {
                        JSONObject quantity = (JSONObject) valueObject.get("Quantity");
                        itemQuantity = cleanQuantity(quantity.get("text").toString());
                    }
                    String itemName = name.get("valueString").toString();
                    double itemPrice = cleanCost(price.get("text").toString()); //eliminate "$" sign
                    ReceiptItem item = new ReceiptItem();
                    item.setQuantity(itemQuantity);
                    item.setItemName(itemName);
                    item.setPrice(itemPrice);
                    value.add(item);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Iterator<?> keys = object.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                try {
                    if (object.get(key) instanceof JSONObject) {
                        value = getReceiptItems((JSONObject) object.get(key), searchedKey);
                    } else if (object.get(key) instanceof JSONArray) {
                        JSONArray obj = (JSONArray) object.get(key);
                        for (int i = 0; i < obj.length(); i++) {
                            if (obj.get(i) instanceof JSONObject) {
                                value = getReceiptItems((JSONObject) obj.get(i), searchedKey);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return value;
    }

    private static double cleanCost(String price) {
        price = price.replace(",", ".");
        StringBuilder priceSB = new StringBuilder();
        for (int i = 0; i < price.length(); i++) {
            char character = price.charAt(i);
            if (Character.isDigit(character) || character == '.') {
                priceSB.append(character);
            }
        }
        return Double.parseDouble(priceSB.toString());

    }

    private static int cleanQuantity(String quantity) {
        quantity = quantity.trim();
        StringBuilder quantitySB = new StringBuilder();

        boolean containNonDigitAfterDigits = false;

        for (int i = 0; i < quantity.length(); i++) {
            char character = quantity.charAt(i);
            if (!Character.isDigit(character) && character != ',' && containNonDigitAfterDigits)
                break;
            if (Character.isDigit(character)) {
                quantitySB.append(character);
                containNonDigitAfterDigits = true;
            }
        }

        if (quantitySB.toString().isEmpty() || Integer.parseInt(quantitySB.toString()) <= 0) {
            return 1;
        }

        return Integer.parseInt(quantitySB.toString());
    }
}
