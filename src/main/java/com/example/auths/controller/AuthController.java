package com.example.auths.controller;

import com.example.auths.payload.ErrorResponse;
import com.example.auths.payload.JwtResponse;
import com.example.auths.payload.LoginRequest;
import com.example.auths.security.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private com.example.auths.repository.UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Kiểm tra user có bị khóa không trước khi xác thực mật khẩu
            java.util.Optional<com.example.auths.model.User> userOptional = 
                userRepository.findByUsernameAndDeletedAtIsNull(loginRequest.getUsername());
            
            if (userOptional.isPresent()) {
                com.example.auths.model.User user = userOptional.get();
                if (user.getIsActive() != null && !user.getIsActive()) {
                    return ResponseEntity
                            .status(HttpStatus.FORBIDDEN)
                            .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                                    "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên."));
                }
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateToken(authentication);

            return ResponseEntity.ok(new JwtResponse(jwt));

        } catch (DisabledException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                            "Tài khoản của bạn đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên."));
        } catch (LockedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),
                            "Tài khoản của bạn đã bị khóa. Vui lòng liên hệ quản trị viên."));
        } catch (BadCredentialsException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
                            "Tên đăng nhập hoặc mật khẩu không đúng."));
        }
    }
}
