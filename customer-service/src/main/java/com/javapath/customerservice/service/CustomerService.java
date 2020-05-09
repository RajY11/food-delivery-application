package com.javapath.customerservice.service;

import com.javapath.customerservice.models.Customer;
import com.javapath.customerservice.models.Order;
import com.javapath.customerservice.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;
    private KafkaTemplate<String,Order> kafkaTemplate;
    private static final String PAYMENT_ERROR_TOPIC = "paymentErrorTopic";
    private static final String PAYMENT_COMPLETE_TOPIC = "paymentCompleteTopic";
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    public CustomerService(CustomerRepository customerRepository,KafkaTemplate<String,Order> kafkaTemplate){
        this.customerRepository = customerRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public Optional<Customer> findCustomer(int customerId){
        return  customerRepository.findById(customerId);
    }

    public Customer addCustomer(Customer customer) {
        customerRepository.save(customer);
        return customer;
    }

    @KafkaListener(topics = "orderTopic", groupId = "group_json")
    public void deductAmountForOrder(Order order){

           Customer customer = null;
        if (findCustomer(order.getCustomerId()).isPresent()) {
            customer = findCustomer(order.getCustomerId()).get();
        }

        if(customer == null){
               LOG.info("Payment failed for order # {} ",order.getOrderId());
               kafkaTemplate.send(PAYMENT_ERROR_TOPIC,order);
           }
           else if(isSufficientFunds(customer.getBalance() , order.getTotalAmount()) && customer !=null){
               long updatedBalance = customer.getBalance() - order.getTotalAmount();
               customer.setBalance(updatedBalance);
               updateCustomer(customer);
               LOG.info("Amount {} deducted form account {} for order {}",order.getTotalAmount(),customer.getName(),order.getOrderId());
               LOG.info("Order# {} sending to paymentCompleteTopic",order.getOrderId());
               kafkaTemplate.send(PAYMENT_COMPLETE_TOPIC,order);
               LOG.info("Order# {} sent to paymentComplete topic",order.getOrderId());
           } else {
               LOG.info("Payment failed for order # {} ",order.getOrderId());
                kafkaTemplate.send(PAYMENT_ERROR_TOPIC,order);
              }
    }

    private void updateCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    private boolean isSufficientFunds(long balance,long price) {
        return balance>=price ? true : false ;
    }

    @Bean
    public StringJsonMessageConverter jsonConverter() {
        return new StringJsonMessageConverter();
    }
}
