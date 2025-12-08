/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.appsdeveloperblog.estore.UsersService.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.vadim.common.model.PaymentDetails;
import ru.vadim.common.model.User;
import ru.vadim.common.query.FetchUserPaymentDetailsQuery;


@Component
public class UserEventsHandler {

    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailsQuery query) {
        
        PaymentDetails paymentDetails = new PaymentDetails(
                "VADIM ZAKIROV",
                "123Card",
                12,
                2030,
                "123"
                );

        User user = new User(
                "Sergey",
                "Kargopolov",
                query.getUserId(),
                paymentDetails);

        return user;
    }
    
    
}
