package com.qlx8.model;

import lotus.mq.IMQBase;

public class SmsCode extends IMQBase{
    public String phone;
    public String codevalue; 
    
    public SmsCode(String phone, String codevalue) {
        this.phone = phone;
        this.codevalue = codevalue;
    }

    @Override
    public boolean equals(Object o) {
        return phone.equals(o);
    }

}
