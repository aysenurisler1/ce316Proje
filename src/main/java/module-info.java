module com.kaanege.iae {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires com.google.gson;

    exports com.kaanege.iae;
    exports com.kaanege.iae.ui;
    exports com.kaanege.iae.model;
    exports com.kaanege.iae.db;
    exports com.kaanege.iae.service;
}