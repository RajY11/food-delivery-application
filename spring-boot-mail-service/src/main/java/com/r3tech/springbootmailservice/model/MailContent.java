package com.r3tech.springbootmailservice.model;


public class MailContent {

    private String emailId;
    private String subject;
    private String content;

    public MailContent() {
    }

    public MailContent(String emailId, String subject, String content) {
        this.emailId = emailId;
        this.subject = subject;
        this.content = content;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
