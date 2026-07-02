package com.example.productsales.service;

import com.example.productsales.dto.CartItemResponse;
import com.example.productsales.dto.CartItemrequest;
import com.example.productsales.dto.CartResponse;
import com.example.productsales.entity.Cart;
import com.example.productsales.entity.CartItem;
import com.example.productsales.entity.Product;
import com.example.productsales.entity.User;
import com.example.productsales.exception.CartNotFoundException;
import com.example.productsales.exception.ProductNotFoundException;
import com.example.productsales.exception.UserNotFoundException;
import com.example.productsales.repository.CartItemRepository;
import com.example.productsales.repository.CartRepository;
import com.example.productsales.repository.ProductRepository;
import com.example.productsales.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    private User getCurrentUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public CartResponse getCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        return toResponse(cart);
    }


    public CartResponse addItem(CartItemrequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(request.getQuantity());
            cart.getItems().add(newItem);

        }
        cartRepository.save(cart);
        return toResponse(cart);
    }


    public CartResponse updateItem(Long itemId, CartItemrequest request) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("This item does not belong to you");
        }

        item.setQuantity(request.getQuantity());
        cartRepository.save(cart);
        return toResponse(cart);
    }

    public CartResponse removeItem(Long itemId) {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        if (!item.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("This item does not belong to you");
        }
        cart.getItems().remove(item);
        cartRepository.save(cart);
        return toResponse(cart);
    }

    public CartResponse clearCart() {
        User user = getCurrentUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CartNotFoundException("Cart not found"));
        cart.getItems().clear();
        cartRepository.save(cart);
        return toResponse(cart);
    }

    private CartResponse toResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(item -> {
                    CartItemResponse dto = new CartItemResponse();
                    dto.setId(item.getId());
                    dto.setProductId(item.getProduct().getId());
                    dto.setQuantity(item.getQuantity());
                    return dto;
                })
                .toList();
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setItems(itemResponses);
        return response;
    }
}
