package com.usian.quartz;

import com.usian.mq.MQSender;
import com.usian.pojo.LocalMessage;
import com.usian.pojo.TbOrder;
import com.usian.pojo.TbOrderItem;
import com.usian.redis.RedisClient;
import com.usian.service.LocalMessageService;
import com.usian.service.OrderService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

public class OrderQuartz implements Job{

    @Autowired
    private OrderService orderService;

    @Autowired
    private RedisClient redisClient;

    @Autowired
    private LocalMessageService localMessageServicel;

    @Autowired
    private MQSender mqSender;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        if (redisClient.setnx("SETNX_LOCK_ORDER_KEY",ip,30L)){//30L：避免死锁
            System.out.println("-------------执行关闭超时任务："+new Date());
            //1、查询超时订单
            List<TbOrder> tbOrderList = orderService.selectOverTimeTbOrder();

            //2、关闭超时订单
            for (int i = 0; i < tbOrderList.size(); i++) {
                TbOrder tbOrder = tbOrderList.get(i);
                orderService.updateTbOrder(tbOrder);

                //3、把超时订单中的商品库存数量加回去
                orderService.updateTbItemByOrderId(tbOrder.getOrderId());
            }

            System.out.println("执行本地消息表的任务...."+ new Date());
            List<LocalMessage> localMessageList = localMessageServicel.selectLocalMessageByStatus(0);
            for (int i = 0; i < localMessageList.size(); i++) {
                LocalMessage localMessage = localMessageList.get(i);
                mqSender.sendMsg(localMessage);
            }

            //释放锁
            redisClient.del("SETNX_LOCK_ORDER_KEY");
        }else {
            System.out.println(
                    "============机器："+ip+" 占用分布式锁，任务正在执行=======================");
        }
    }
}
