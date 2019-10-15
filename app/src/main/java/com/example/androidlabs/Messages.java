package com.example.androidlabs;

public class Messages {
    
    //Android Studio hint: to create getter and setter, put mouse on variable and click "alt+insert"
    protected String messages;
    protected boolean isSend;

    /**Constructor*/
    public Messages(String m, boolean i) {
        this.setMessages(m);
        isSend = i;

    }

    /**Chaining constructor*/
    public Messages(String n) {this(n, true);}


    public String getMessages() {
        return messages;
    }

    public void setMessages(String m) {
        this.messages = m;
    }

    public boolean isSend() {
        return this.isSend;
    }
}
