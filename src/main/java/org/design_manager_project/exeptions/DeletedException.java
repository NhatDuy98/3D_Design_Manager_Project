package org.design_manager_project.exeptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeletedException extends RuntimeException{
    public DeletedException(String message){
        super(message);
    }

}
