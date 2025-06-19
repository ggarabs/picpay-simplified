package com.picpay_simplified.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.picpay_simplified.domain.transaction.Transaction;
import com.picpay_simplified.domain.user.User;
import com.picpay_simplified.dtos.TransactionDTO;
import com.picpay_simplified.repositories.TransactionRepository;

@SuppressWarnings("unused")
@Service
public class TransactionService {
    @Autowired
    private TransactionRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception{
        User payer = this.userService.findUserById(transaction.payerId());
        User payee = this.userService.findUserById(transaction.payeeId());

        userService.validateTransaction(payer, transaction.value());

        if(!this.authorizeTransaction(payer, transaction.value())) throw new Exception("Transação não autorizada");

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setPayee(payee);
        newTransaction.setPayer(payer);
        newTransaction.setTimestamp(LocalDateTime.now());

        payer.setBalance(payer.getBalance().subtract(transaction.value()));
        payee.setBalance(payee.getBalance().add(transaction.value()));

        this.repository.save(newTransaction);
        userService.saveUser(payer);
        userService.saveUser(payee);

        this.notificationService.sendNotification(payer, "Transação realizada com sucesso.");

        this.notificationService.sendNotification(payee, "Transação recebida com sucesso.");

        return newTransaction;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean authorizeTransaction(User payer, BigDecimal value){
        ResponseEntity<Map> response = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if(response.getStatusCode() == HttpStatus.OK){
            String message = (String)response.getBody().get("status");
            Map<String, Object> data = (Map<String, Object>)response.getBody().get("data");
            boolean authorizationMessage = (boolean)data.get("authorization");
            return "success".equals(message) && authorizationMessage;
        }
        return false;
    }
}
