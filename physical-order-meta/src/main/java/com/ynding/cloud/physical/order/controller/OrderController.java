package com.ynding.cloud.physical.order.controller;

import com.ynding.cloud.physical.order.model.OrderInfo;
import com.ynding.cloud.physical.order.model.PriceInfo;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.bind.annotation.*;

/**
 * <p> OrderController</p>
 *
 * @author dyn
 * @version 2020/9/27
 * @since JDK 1.8
 */
@Slf4j
@RestController
@RequestMapping("/order")
@Api(value = "Order", tags = {"Order-Controller"})
public class OrderController {

    /**
     * 会从请求的上下文里拿到令牌，放到请求头里，发出去。
     */
    @Autowired
    private OAuth2RestTemplate restTemplate;

    /**
     * 创建订单
     * 注解生效需在启动类配置@EnableGlobalMethodSecurity(prePostEnabled = true)
     * @PreAuthorize("#oauth2.hasScope('write')")
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/save")
    public OrderInfo create(@RequestBody OrderInfo info, @AuthenticationPrincipal String username) {
        log.info("获取到username = {}", username);
        //查询价格
        PriceInfo price = restTemplate.getForObject("http://localhost:9080/prices/" + info.getProductId(), PriceInfo.class);
        log.info("price is " + price.getPrice());
        return info;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public OrderInfo getInfo(@PathVariable Long id, @AuthenticationPrincipal String username) {
        log.info("getInfo: id is " + id + " , and username is " + username);
        OrderInfo info = new OrderInfo();
        info.setId(id);
        info.setProductId(id * 10);
        return info;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("get/{id}")
    public OrderInfo getOrder(@PathVariable Long id, Authentication authentication) {
        log.info("getInfo: id is " + id);
        authentication.getCredentials();
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails)authentication.getDetails();
        String token = details.getTokenValue();
        log.info("token:{}",token);
        OrderInfo info = new OrderInfo();
        info.setId(id);
        info.setProductId(id * 10);
        return info;
    }
}
