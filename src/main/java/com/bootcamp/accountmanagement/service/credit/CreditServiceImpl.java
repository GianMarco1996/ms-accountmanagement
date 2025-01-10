package com.bootcamp.accountmanagement.service.credit;

import com.bootcamp.accountmanagement.repository.credit.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditServiceImpl implements CreditService {

    @Autowired
    private CreditRepository creditRepository;
}