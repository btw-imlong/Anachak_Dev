package AccomManage.system.Controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import AccomManage.system.Dto.Request.LoginRequest;
import AccomManage.system.Dto.Response.LoginResponse;
import AccomManage.system.Entity.User;
import AccomManage.system.Security.JwtUtil;
import AccomManage.system.Service.Impl.CustomUserDetailsService;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager,
                          CustomUserDetailsService userDetailsService,
                          JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userDetailsService.getUserByEmail(request.getEmail());
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());

        // 🍪 Set JWT as an HTTP-only, cross-site-capable cookie.
        // SameSite=None + Secure=true is required because the frontend
        // (Vercel) and backend (Render) live on different domains.
        ResponseCookie cookie = ResponseCookie.from("auth_token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(30 * 24 * 60 * 60) // 30 days
                .sameSite("None")
                .build();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUserId(user.getId());
        loginResponse.setName(user.getName());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setRole(user.getRole().name());
        loginResponse.setToken(token); // can remove this later

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(loginResponse);
    }

    // ✅ Check if user is still logged in
    @GetMapping("/me")
    public ResponseEntity<?> getMe(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.status(401).build();

        String token = null;
        for (Cookie cookie : cookies) {
            if ("auth_token".equals(cookie.getName())) {
                token = cookie.getValue();
                break;
            }
        }

        if (token == null) return ResponseEntity.status(401).build();

        try {
            String email = jwtUtil.extractEmail(token);
            String role  = jwtUtil.extractRole(token);
            return ResponseEntity.ok(Map.of("email", email, "role", role));
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    // ✅ Logout — must clear the cookie with the exact same attributes
    // (path, secure, sameSite) used when it was set, or the browser
    // won't recognize it as the same cookie and won't delete it.
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie cookie = ResponseCookie.from("auth_token", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // expires immediately
                .sameSite("None")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(Map.of("message", "Logged out"));
    }
}