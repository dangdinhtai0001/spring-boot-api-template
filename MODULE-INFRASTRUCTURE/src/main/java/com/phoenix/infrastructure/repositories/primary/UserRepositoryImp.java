package com.phoenix.infrastructure.repositories.primary;

import com.phoenix.domain.entity.DomainUser;
import org.springframework.stereotype.Repository;

@Repository(value = "UserRepositoryImp")
public class UserRepositoryImp {
    public  DomainUser findUserByEmailOrUsername(String name){
        return null;
    }
}
