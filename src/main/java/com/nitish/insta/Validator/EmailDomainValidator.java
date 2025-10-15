package com.nitish.insta.Validator;

import java.net.InetAddress;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailDomainValidator implements ConstraintValidator<ValidateEmail, String> {
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        System.out.println("VALIDATION IS TRIGERED");
        if (email == null || email.isEmpty())
            return false;
        String regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if (!email.matches(regex))
            return false;
        try {
            String domain = email.substring(email.indexOf("@") + 1);
            if( matchedProvider(domain)){
                InetAddress.getByName(domain);
                System.out.println("Domain exists: "+domain);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    boolean matchedProvider(String provider) {
        List<String> emailDomains = List.of(
            "gmail.com",
            "yahoo.com",
            "outlook.com",
            "hotmail.com",
            "icloud.com",
            "aol.com",
            "protonmail.com",
            "zoho.com",
            "gmx.com",
            "yandex.com",
            "mail.com",
            "fastmail.com",
            "tutanota.com",
            "hushmail.com",
            "ntc.com",
            "wlink.com",
            "ntcnet.com"
    );
        for(String emailProvider : emailDomains){
            if(provider.equals(emailProvider)) return true;
        }
        return false;
    }
}
