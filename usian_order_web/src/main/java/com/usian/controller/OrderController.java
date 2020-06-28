package com.usian.controller;

import com.usian.feign.CartServiceFeign;
import com.usian.feign.OrderServiceFeign;
import com.usian.pojo.OrderInfo;
import com.usian.pojo.TbItem;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderShipping;
import com.usian.utils.Result;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequestMapping("/frontend/order")
@RestController
public class OrderController {

    @Autowired
    private CartServiceFeign cartServiceFeign;

    @Autowired
    private OrderServiceFeign orderServiceFeign;

    @RequestMapping("goSettlement")
    public Result goSettlement(String[] ids,String userId){
        List<TbItem> tbItems = new ArrayList<>();
        Map<String, TbItem> cart = cartServiceFeign.selectCartByUserId(userId);
        for (int i = 0; i < ids.length; i++) {
            String itemId = ids[i];
            tbItems.add(cart.get(itemId));
        }
        if (tbItems.size()>0){
            return Result.ok(tbItems);
        }
        return Result.error("查询失败");
    }

    //一个request只能包含一个requestbody，所以feign不能包括多个requestbody
    @RequestMapping("/insertOrder")
    public Result insertOrder(TbOrder tbOrder, TbOrderShipping tbOrderShipping,String orderItem){
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setTbOrder(tbOrder);
        orderInfo.setTbOrderShipping(tbOrderShipping);
        orderInfo.setOrderItem(orderItem);
        String orderId = orderServiceFeign.insertOrder(orderInfo);
        if (StringUtils.isNotBlank(orderId)){
            return Result.ok(orderId);
        }
        return Result.error("订单保存失败");
    }
}
