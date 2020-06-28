package com.usian.mq;

import com.usian.mapper.LocalMessageMapper;
import com.usian.pojo.LocalMessage;
import com.usian.utils.JsonUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.util.locale.LocaleMatcher;

import java.io.IOException;

/**
 * 任务：
 *   1、发送消息
 *   2、消息确认返回后修改local_messag(status:1)
 */
@Component
public class MQSender implements ReturnCallback,ConfirmCallback{

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private LocalMessageMapper localMessageMapper;

    /**
     * 消息发送失败时调用
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,String exchange, String routingKey) {
        System.out.println("return--message:" + new String(message.getBody())
                + ",exchange:" + exchange + ",routingKey:" + routingKey);
    }

    /**
     * 下游服务消息确认后调用
     * @param correlationData
     * @param ack
     * @param cause
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String id = correlationData.getId();
        if (ack){
            //修改本地消息表的状态
            String txNo = correlationData.getId();
            LocalMessage localMessage = new LocalMessage();
            localMessage.setTxNo(txNo);
            localMessage.setState(1);
            localMessageMapper.updateByPrimaryKeySelective(localMessage);
        }
    }

    /**
     * 发送消息
     * @param localMessage
     */
    public void sendMsg(LocalMessage localMessage) {
        RabbitTemplate rabbitTemplate = (RabbitTemplate) this.amqpTemplate;

        rabbitTemplate.setConfirmCallback(this);//确认回调
        rabbitTemplate.setReturnCallback(this);//失败回退

        //消息id：用于消息确认成功返回后修改本地消息表的状态
        CorrelationData correlationData = new CorrelationData(localMessage.getTxNo());
        //发送消息到mq
        //usain_order_service---->exchange(routing key)
        rabbitTemplate.convertAndSend("order_exchange","order.add", JsonUtils.objectToJson(localMessage),correlationData);

    }
}
