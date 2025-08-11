package com.hipradeep.code.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    //We can add Extra properties which we want to manage...
    public ResourceNotFoundException(){
        super("Resource Not Found on Server !!");
    }

    public ResourceNotFoundException(String message){
        super(message);
    }

}
