package com.brvsk.sender;



public interface MessageSender {

    void sendMessage(String to, String title, String body) throws  Exception;
}
