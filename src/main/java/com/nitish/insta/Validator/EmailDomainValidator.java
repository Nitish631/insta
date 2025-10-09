package com.nitish.insta.Validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.InetAddress;
import java.util.List;

public class EmailDomainValidator implements ConstraintValidator<ValidateEmail,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null || value.isEmpty()) return false;
        String regex="^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        if(!value.matches(regex))return false;
        try{
            String domain=value.substring(value.indexOf("@")+1);
            String provider=domain.substring(0,domain.lastIndexOf("."));
            if(matchedProvider(provider)){
                InetAddress.getByName(domain);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    boolean matchedProvider(String provider){
        List<String> emailProviders= List.of(
                "gmail",
                "yahoo",
                "outlook",
                "hotmail",
                "icloud",
                "aol",
                "protonmail",
                "zoho",
                "gmx",
                "yandex",
                "mail",
                "fastmail",
                "tutanota",
                "hushmail",
                "ntc",
                "wlink",
                "ntcnet"
        );
        for(String emailProvider:emailProviders){
            if(provider.equals(emailProvider)) return true;
        }
        return false;
    }
}
