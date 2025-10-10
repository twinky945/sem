package com.napier.sem;

import java.sql.*;

public class App {

    /** Соединение с MySQL */
    private Connection con = null;

    /** Подключение к БД с ретраями */
    public void connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load SQL driver");
            System.exit(-1);
        }

        // 60 попыток с паузой 5 сек (~5 минут)
        for (int i = 1; i <= 60; i++) {
            System.out.println("Connecting to database...");
            try {
                con = DriverManager.getConnection(
                        // если оба сервиса в docker-compose:
                        "jdbc:mysql://db:3306/employees?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                        "root", "example"
                );
                System.out.println("Successfully connected");
                return;
            } catch (SQLException e) {
                System.out.printf("Failed to connect attempt %d: %s (SQLState=%s, code=%d)%n",
                        i, e.getMessage(), e.getSQLState(), e.getErrorCode());
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
            }
        }
        System.out.println("Giving up: database not reachable.");
    }

    /** Отключение от БД */
    public void disconnect() {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection to database");
            } finally {
                con = null;
            }
        }
    }

    public static void main(String[] args)
    {
        // Create new Application
        App a = new App();

        // Connect to database
        a.connect();

        // Disconnect from database
        a.disconnect();
    }
}
