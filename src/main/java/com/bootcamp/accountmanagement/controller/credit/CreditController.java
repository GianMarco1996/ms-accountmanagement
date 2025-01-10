package com.bootcamp.accountmanagement.controller.credit;

import com.bootcamp.accountmanagement.service.credit.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreditController {

    @Autowired
    private CreditService creditService;
}