package com.example.demo.Controller;

import com.example.demo.Dao.hibernate.passwordresetimpl;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RequestMapping("/job")
@RestController
public class PasswordTokenController {

    public static final Logger logger  = LoggerFactory.getLogger(PasswordTokenController.class);

    @Autowired
    passwordresetimpl passwordimpl;

    @PostMapping("/clearpasswordtoken")
//    @Scheduled(cron = "* */24 * * * * ")
    public ResponseEntity<String> deletepasswordtoken()
    {
        Date date  = new Date();
        passwordimpl.deleteAllByExpiredtoken();
        logger.info("success + {}"+ date.getTime());
        return new ResponseEntity<String>("Success",HttpStatus.OK);

    }

}