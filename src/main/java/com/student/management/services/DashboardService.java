package com.student.management.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.student.management.dto.Dashboard;
import com.student.management.dto.OrderSummary;

@Service
public class DashboardService {

    @Autowired
    private JdbcTemplate jdbc;

    public Dashboard getDashboard() {

        Dashboard d = new Dashboard();
          Long users = jdbc.queryForObject(
            "SELECT COUNT(*) FROM users", Long.class
        );
          Long products = jdbc.queryForObject(
            "SELECT COUNT(*) FROM products", Long.class
        );
          Long totalOrders = jdbc.queryForObject(
            "SELECT COUNT(*) FROM orders", Long.class
        );
          Double revenue = jdbc.queryForObject(
            "SELECT IFNULL(SUM(total_price),0) FROM orders", Double.class
        );
          List<OrderSummary> recentOrders = jdbc.query(
            "SELECT id, user_id, total_price, delivery_status FROM orders ORDER BY order_date DESC LIMIT 5",
            (rs, rowNum) -> {
                OrderSummary o = new OrderSummary();
                o.setId(rs.getLong("id"));
                o.setUser_id(rs.getInt("user_id"));
                o.setTotal_price(rs.getDouble("total_price"));
                o.setDelivery_status(rs.getString("delivery_status"));
                return o;
            }
        );
          d.setUsers(users);
        d.setProducts(products);
        d.setTotalOrders(totalOrders);
        d.setTotalRevenue(revenue);
        d.setRecentOrders(recentOrders);

        return d;
    }
}