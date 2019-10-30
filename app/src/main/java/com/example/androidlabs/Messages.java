package com.example.androidlabs;

public class Messages {
    
    //Android Studio hint: to create getter and setter, put mouse on variable and click "alt+insert"
    protected String messages;
    protected boolean isSend;
    protected long id;


    /**Constructor*/
    public Messages(String m,long id, boolean i) {
        this.setMessages(m);
        this.setId(id);
        this.setOwnerOfMessage(i);
    }

    /**Chaining constructor*/

    //mainly use
    public Messages(String m, boolean checked) {
        this(m, 1, checked);
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String m) {
        this.messages = m;
    }

    public boolean isSend() {
        return this.isSend;
    }

    public void setOwnerOfMessage(boolean isSend) { this.isSend = isSend; }


    public void setId(long id) { this.id = id; }

    public long getId() {
        return this.id;
    }


}
