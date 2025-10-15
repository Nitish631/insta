package com.nitish.insta.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.nitish.insta.Entities.Users;
import com.nitish.insta.Exception.ResourceNotFoundException;
import com.nitish.insta.Payloads.UsersDto;
import com.nitish.insta.Repository.UsersRepo;
import com.nitish.insta.Utils.AppConstant;

@Service
public class OtpService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Scheduled(fixedRate = 3*60 * 1000)
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        Iterator<Map.Entry<String, TempOtpData>> iterator = otpStore.entrySet().iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Map.Entry<String, TempOtpData> entry = iterator.next();
            TempOtpData data = entry.getValue();
            if (now.isAfter(data.expiry())) {
                iterator.remove();
                count++;
            }
        }
        if (count > 0) {
            System.out.println("" + count + " OTP cleanup");
        }
    }

    private static final SecureRandom random = new SecureRandom();

    private static String generateRandomString(int length) {
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        // Base64 encoding makes it readable and longer per byte
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        return token.length() > length ? token.substring(0, length) : token;
    }

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsersService usersService;
    private final Map<String, TempOtpData> otpStore = new HashMap<>();

    private record TempOtpData(String otpToken, String otp, boolean verified, boolean isForReset, LocalDateTime expiry) {
    }

    public String sendOtp(String email, boolean isForReset) {
        Optional<Users> existing = usersRepo.findByEmail(email);
        if ((!isForReset) && existing.isPresent()) {
            throw new RuntimeException("User already registered. Try login or password reset");
        }
        if (isForReset && existing.isEmpty()) {
            throw new RuntimeException("No account found with this email");
        }
        String otp = String.format("%d", (100000 + new Random().nextInt(99999)));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(1).plusSeconds(30);
        String otpToken = generateRandomString(50);
        try{
            otpStore.remove(email);
            System.out.println("OLD OTP REQUEST REMOVED");
        }catch (Exception e){
            System.out.println("NO OLD OTP REQUEST. FIRST REQUEST");
        }
        otpStore.put(email, new TempOtpData(otpToken, otp, false, isForReset, expiry));
        emailService.sendMessageToEmail(email, otp,true);
        return otpToken;

    }

    public String verifyOtp(String otpToken, String email, String otp) {
        TempOtpData data = otpStore.get(email);
        if (data == null)
            throw new RuntimeException("No OTP found for this email.");
        if (data.otpToken == null || !data.otpToken.equals(otpToken))
            throw new RuntimeException("No OTP found for this token");
        if (LocalDateTime.now().isAfter(data.expiry)) {
            otpStore.remove(email);
            throw new RuntimeException("OTP expired . Please request again");
        }
        LocalDateTime expiry=LocalDateTime.now().plusMinutes(5);
        if (!data.otp().equals(otp))
            throw new RuntimeException("Invalid OTP");
        otpStore.remove(email);
        otpStore.put(email, new TempOtpData(data.otpToken, otp, true, data.isForReset, expiry));
        System.out.println("OTP VERIFIED");
        return otpToken;
    }

    public String setPassword(String otpToken, String email, String password, String fullName) {
        TempOtpData data = otpStore.get(email);
        if (data == null)
            throw new RuntimeException("Too late sesson expired. Please request again");
        if (data.otpToken == null || !data.otpToken.equals(otpToken))
            throw new RuntimeException("No OTP found for this otp token");

        if (!data.verified())
            throw new RuntimeException("OTP not verified");
        if (data.isForReset) {
            Users user = usersRepo.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
            user.setPassword(passwordEncoder.encode(password));
            usersRepo.save(user);
            otpStore.remove(otpToken);
            return "Password reset successfully";
        } else {
            UsersDto usersDto = new UsersDto();
            usersDto.setEmail(email);
            usersDto.setPassword(password);
            usersDto.setFullName(fullName);
            usersDto.setUserName(AppConstant.APP_MAME.concat(" user"));
            usersService.registerNewUser(usersDto);
            otpStore.remove(otpToken);
            return "User register successfully!";
        }

    }
}
