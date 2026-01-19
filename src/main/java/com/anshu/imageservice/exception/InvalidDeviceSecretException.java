package com.anshu.imageservice.exception;

public class InvalidDeviceSecretException extends RuntimeException{

    public InvalidDeviceSecretException(String msg){
        super(msg);
    }
}
