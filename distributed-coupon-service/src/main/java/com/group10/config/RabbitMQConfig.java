package com.group10.config;

import lombok.Data;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
@Data
public class RabbitMQConfig {


    /**
     * interchanger name
     */
    @Value("${mqconfig.coupon_event_exchange}")
    private String eventExchange;


    /**
     * the first queue, delay queue
     */
    @Value("${mqconfig.coupon_release_delay_queue}")
    private String couponReleaseDelayQueue;

    /**
     * the first queue's routing key
     * that is the key to enter the dead letter queue
     */
    @Value("${mqconfig.coupon_release_delay_routing_key}")
    private String couponReleaseDelayRoutingKey;


    /**
     * the second queue
     */
    @Value("${mqconfig.coupon_release_queue}")
    private String couponReleaseQueue;

    /**
     * the second queue's routing key
     *
     * that is the key to enter the dead letter queue
     */
    @Value("${mqconfig.coupon_release_routing_key}")
    private String couponReleaseRoutingKey;

    /**
     * the time to live
     */
    @Value("${mqconfig.ttl}")
    private Integer ttl;


    /**
     * message converter
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }


    /**
     * create a exchange
     * get the exchange from the configuration file
     * @return
     */
    @Bean
    public Exchange couponEventExchange(){
        return new TopicExchange(eventExchange,true,false);
    }


    /**
     * create a delay queue
     */
    @Bean
    public Queue couponReleaseDelayQueue(){

        Map<String,Object> args = new HashMap<>(3);
        args.put("x-message-ttl",ttl);
        args.put("x-dead-letter-routing-key",couponReleaseRoutingKey);
        args.put("x-dead-letter-exchange",eventExchange);

        return new Queue(couponReleaseDelayQueue,true,false,false,args);
    }


    /**
     * dead letter queue
     */
    @Bean
    public Queue couponReleaseQueue(){

        return new Queue(couponReleaseQueue,true,false,false);

    }


    /**
     * the first queue binding relationship
     * @return
     */
    @Bean
    public Binding couponReleaseDelayBinding(){

        return new Binding(couponReleaseDelayQueue,Binding.DestinationType.QUEUE,eventExchange,couponReleaseDelayRoutingKey,null);
    }

    /**
     * the second queue binding relationship
     * @return
     */
    @Bean
    public Binding couponReleaseBinding(){

        return new Binding(couponReleaseQueue,Binding.DestinationType.QUEUE,eventExchange,couponReleaseRoutingKey,null);
    }





}
