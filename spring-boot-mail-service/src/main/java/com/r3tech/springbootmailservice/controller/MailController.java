package com.r3tech.springbootmailservice.controller;


import com.r3tech.springbootmailservice.model.MailContent;
import com.r3tech.springbootmailservice.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping
    public String sendMessage(@RequestBody MailContent message){
        try {
            mailService.sendMessage(message);
            return "success";
        }
        catch(Exception e){
            return "error in sending mail, " +e.getMessage() ;
        }
    }
}
