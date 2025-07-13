package com.projects.Ecommerce.Service;

import com.projects.Ecommerce.Dto.OrderDTO;
import com.projects.Ecommerce.Dto.OrderItemDto;
import com.projects.Ecommerce.Model.OrderItem;
import com.projects.Ecommerce.Model.Orders;
import com.projects.Ecommerce.Model.Product;
import com.projects.Ecommerce.Model.User;
import com.projects.Ecommerce.Repository.OrderRepository;
import com.projects.Ecommerce.Repository.ProductRepository;
import com.projects.Ecommerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    public OrderDTO placeOrder(Long userId, Map<Long, Integer> productQuantities, double totalAmount) {
       User user= userRepository.findById(userId)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Orders order=new Orders();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setStatus("pending");
        order.setTotalAmount(totalAmount);

        List<OrderItem>orderItems=new ArrayList<>();
        List<OrderItemDto>orderItemDtos=new ArrayList<>();

        for(Map.Entry<Long,Integer>entry:productQuantities.entrySet()){
           Product product= productRepository.findById(entry.getKey())
                   .orElseThrow(()-> new RuntimeException("Product Not Found"));

           OrderItem orderItem=new OrderItem();
           orderItem.setOrders(order);
           orderItem.setProduct(product);
           orderItem.setQuantity(entry.getValue());
           orderItems.add(orderItem);

           orderItemDtos.add(new OrderItemDto(product.getName(),product.getPrice(),entry.getValue()));

        }
        order.setOrderItems(orderItems);
        Orders saveOrder=orderRepository.save(order);
        return new OrderDTO(saveOrder.getId(),saveOrder.getTotalAmount(),saveOrder.getStatus()
                ,saveOrder.getOrderDate(),orderItemDtos);
    }

    public List<OrderDTO> getAllOrders() {
        List<Orders> orders = orderRepository.findAllOrdersWithUsers();
        return orders.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private OrderDTO convertToDTO(Orders orders) {
        List<OrderItemDto> orderItems = orders.getOrderItems().stream()
                .map(item -> new OrderItemDto(
                        item.getProduct().getName(),
                        item.getProduct().getPrice(),
                        item.getQuantity())).collect(Collectors.toList());
        return new OrderDTO(
                orders.getId(),
                orders.getTotalAmount(),
                orders.getStatus(),
                orders.getOrderDate(),
                orders.getUser()!=null?orders.getUser().getName():"Unknown",
                orders.getUser()!=null ? orders.getUser().getEmail() :"Unknown",
                orderItems
        );
    }

    public List<OrderDTO> getOrderByUser(Long userId) {
        Optional<User> userOp = userRepository.findById(userId);
        if(userOp.isEmpty()){
            throw new RuntimeException("uses not found");
        }
        User user= userOp.get();
        List<Orders> orderList = orderRepository.findByUser(user);
        return orderList.stream().map(this::convertToDTO).collect(Collectors.toList());

    }
}
