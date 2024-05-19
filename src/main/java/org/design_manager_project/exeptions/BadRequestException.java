package org.design_manager_project.exeptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends RuntimeException{
    public BadRequestException(String message){
        super(message);
    }
}
