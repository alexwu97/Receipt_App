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
    private static final String TEXT = "text";
    private static final String VALUE_ARRAY = "valueArray";
    private static final String VALUE_OBJECT = "valueObject";
    private static final String VALUE_STRING = "valueString";
    private static final String NAME = "Name";
    private static final String QUANTITY = "Quantity";
    private static final String TOTAL_PRICE = "TotalPrice";
    private static final String MERCHANT_NAME = "MerchantName";
    private static final String TOTAL = "Total";
    private static final String ITEMS = "Items";

    public Receipt getReceiptFromReceiptData(JSONObject object) throws JSONException {
        Receipt receipt = new Receipt();
        JSONObject merchantObj = searchJSONObject(object, MERCHANT_NAME);
        JSONObject totalObj = searchJSONObject(object, TOTAL);
        JSONObject itemObj = searchJSONObject(object, ITEMS);
        //if cannot find any, it is not a receipt
        if (merchantObj == null || totalObj == null || itemObj == null) {
            return null;
        }
        receipt.setMerchantName(merchantObj.getString(TEXT));
        receipt.setTotal(cleanCost(totalObj.getString(TEXT)));
        List<ReceiptItem> items = getReceiptItems(itemObj.getJSONArray(VALUE_ARRAY));
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
            JSONObject valueObject = itemArray.getJSONObject(i).getJSONObject(VALUE_OBJECT);
            ReceiptItem item = new ReceiptItem();

            String itemName = valueObject.getJSONObject(NAME).getString(VALUE_STRING);
            int itemQuantity = (valueObject.has(QUANTITY)) ? cleanQuantity(valueObject.getJSONObject(QUANTITY).getString(TEXT)) : 1;
            double itemPrice = cleanCost(valueObject.getJSONObject(TOTAL_PRICE).getString(TEXT));

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

        return (quantitySB.toString().isEmpty() || Integer.parseInt(quantitySB.toString()) <= 0)
                ? 1
                : Integer.parseInt(quantitySB.toString());
    }
}
