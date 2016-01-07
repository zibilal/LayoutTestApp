package com.example;

import java.io.File;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyClass {
    public static void main(String[] args) throws Exception {
        System.out.println("Generating Dao....");

        Schema schema = new Schema(1000, "com.zibilal.dao");

        addCompany(schema);
        addCustomerOrder(schema);

        File file = new File("..\\..\\..\\..\\..\\");
        System.out.println("The file: " + file.isDirectory());
        System.out.println("File name : " + file.getName());

        new DaoGenerator().generateAll(schema, "F:\\learning-curves\\android\\LayoutTestApp\\app\\src\\main\\java");
    }

    private static void addCompany(Schema schema) {
        Entity company = schema.addEntity("Company");
        company.addIdProperty();
        company.addStringProperty("name").notNull();
        company.addIntProperty("ranking").notNull();
        company.addDateProperty("create_date").notNull();
        company.addDateProperty("update_date").notNull();
    }

    private static void addCustomerOrder(Schema schema) {
        Entity client = schema.addEntity("Client");
        client.addIdProperty();
        client.addStringProperty("name").notNull();

        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS");
        order.addIdProperty();

        Property orderDate = order.addDateProperty("date").getProperty();
        Property clientId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(client, clientId);

        ToMany clientToOrders = client.addToMany(order, clientId);
        clientToOrders.setName("orders");
        clientToOrders.orderAsc(orderDate);
    }
}
