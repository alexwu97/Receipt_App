package com.example.receipt_app.service;

import com.example.receipt_app.model.ReceiptItem;
import com.example.receipt_app.model.Receipt;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonDataExtractorService {

    public Receipt getReceiptFromReceiptData(JSONObject object) throws JSONException {
        Receipt receipt = new Receipt();
        String merchantName = searchJSONObject(object, "MerchantName").getString("text");
        double total = cleanCost(searchJSONObject(object, "Total").getString("text"));
        List<ReceiptItem> items = getReceiptItems(searchJSONObject(object, "Items").getJSONArray("valueArray"));
        receipt.setMerchantName(merchantName);
        receipt.setTotal(total);
        receipt.setItems(items);
        return receipt;
    }

    private JSONObject searchJSONObject(JSONObject object, String targetKey) throws JSONException {
        if (object.has(targetKey)) {
            return object.getJSONObject(targetKey);
        }

        JSONObject value = null;
        Iterator<?> keys = object.keys();
        while (keys.hasNext() && value == null) {
            String key = (String) keys.next();

            if (object.get(key) instanceof JSONObject) {
                value = searchJSONObject(object.getJSONObject(key), targetKey);
            } else if (object.get(key) instanceof JSONArray) {
                value = searchJSONArray(object.getJSONArray(key), targetKey);
            }
        }
        return value;
    }

    private JSONObject searchJSONArray(JSONArray array, String targetKey) throws JSONException {
        JSONObject value = null;

        for (int i = 0; i < array.length(); i++) {
            if (value != null) {
                return value;
            }

            if (array.get(i) instanceof JSONObject) {
                value = searchJSONObject(array.getJSONObject(i), targetKey);
            } else if (array.get(i) instanceof JSONArray) {
                value = searchJSONArray(array.getJSONArray(i), targetKey);
            }
        }
        return value;
    }

    private List<ReceiptItem> getReceiptItems(JSONArray itemArray) throws JSONException {
        List<ReceiptItem> items = new ArrayList<>();
        for (int i = 0; i < itemArray.length(); i++) {
            JSONObject valueObject = itemArray.getJSONObject(i).getJSONObject("valueObject");
            ReceiptItem item = new ReceiptItem();

            String itemName = valueObject.getJSONObject("Name").getString("valueString");
            int itemQuantity = (valueObject.has("Quantity")) ? cleanQuantity(valueObject.getJSONObject("Quantity").getString("text")) : 1;
            double itemPrice = cleanCost(valueObject.getJSONObject("TotalPrice").getString("text")); //eliminate "$" sign

            item.setQuantity(itemQuantity);
            item.setItemName(itemName);
            item.setPrice(itemPrice);

            items.add(item);
        }
        return items;
    }

    private double cleanCost(String price) {
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

    private int cleanQuantity(String quantity) {
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
