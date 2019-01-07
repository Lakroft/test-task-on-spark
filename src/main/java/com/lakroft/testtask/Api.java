package com.lakroft.testtask;

import com.lakroft.testtask.model.HibernateManager;
import com.lakroft.testtask.model.User;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Api {

    public static void initApi() {
        get("/summ", (request, response) -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("summ", HibernateManager.summ());
            return jsonObject.toString();
        });

        get("/users", (request, response) -> {
            JSONArray array = new JSONArray();
            List<JSONObject> objects = HibernateManager.loadAll().stream().map(x -> x.toJsonObject()).collect(Collectors.toList());
            array.addAll(objects);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("users", array);
            return jsonObject.toString();
        });

        post("/create", (request, response) -> {
            String firstName = request.queryParams("first_name");
            String lastName = request.queryParams("last_name");
            String balanceStr = request.queryParams("balance");
            if (firstName != null && lastName != null && balanceStr != null) {
                try {
                    BigDecimal balance = BigDecimal.valueOf(Float.parseFloat(balanceStr));
                    User user = new User(firstName, lastName, balance);
                    HibernateManager.save(user);
                    return JsonManager.getSuccess("user", user.toJsonObject()).toString();
                } catch (Exception e) {
                    return JsonManager.getError("Wrong balance").toString();
                }
            } else {
                return JsonManager.getError("Wrong params. Right params: /create?first_name='first name'&last_name='last name'&balance='balance'").toString();
            }
        });

        put("/transfer", (request, response) -> {
            String fromStr = request.queryParams("from");
            String toStr = request.queryParams("to");
            String amountStr = request.queryParams("amount");
            if (fromStr != null && toStr != null && amountStr != null) {
                try {
                    Long fromId = Long.valueOf(fromStr);
                    Long toId = Long.valueOf(toStr);
                    BigDecimal amount = BigDecimal.valueOf(Float.parseFloat(amountStr));
                    return HibernateManager.transfer(fromId, toId, amount);
                } catch (Exception e) {
                    return JsonManager.getError("Error. Check params");
                }
            } else {
                return JsonManager.getError("Wrong params. Right params: /transfer?from='id of sender'&to='id of receiver'&amount='amount'");
            }
        });
    }
}
