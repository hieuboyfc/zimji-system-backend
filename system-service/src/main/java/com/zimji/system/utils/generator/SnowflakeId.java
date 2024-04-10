package com.zimji.system.utils.generator;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class SnowflakeId implements IdentifierGenerator {

    public SnowflakeId() {
    }

    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return IdentityGenerator.ofSnowFlake();
    }

}