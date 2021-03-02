package com.phoenix.infrastructure;

import com.phoenix.domain.entity.DomainUser;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TestAutoMatchQuery {
    @Test
    public void testGetAllFields(){
        DomainUser domainUser = new DomainUser();
        Class domainUserClass = domainUser.getClass();
        List<Field> fields = Arrays.asList(domainUserClass.getDeclaredFields());

        for (Field field : fields) {
            System.out.println(field.getName());
        }
    }
}
