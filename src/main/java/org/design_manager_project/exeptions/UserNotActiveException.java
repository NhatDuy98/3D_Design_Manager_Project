package org.design_manager_project.exeptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserNotActiveException extends RuntimeException{
    public UserNotActiveException(String message){
        super(message);
    }
}
