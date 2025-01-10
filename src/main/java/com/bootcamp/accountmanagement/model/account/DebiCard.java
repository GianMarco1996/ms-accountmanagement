package com.bootcamp.accountmanagement.model.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebiCard {
    private String id;

    private Boolean mainAccount;
}
