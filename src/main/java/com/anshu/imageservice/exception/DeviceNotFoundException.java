package com.anshu.imageservice.exception;

public class DeviceNotFoundException extends RuntimeException{

    public DeviceNotFoundException(String msg){
        super(msg);
    }
}
