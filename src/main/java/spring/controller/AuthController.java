package spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.dto.auth.AuthResponse;
import spring.dto.auth.LoginRequest;
import spring.dto.auth.RegisterRequest;
import spring.security.JwtUtil;
import spring.security.User;
import spring.security.UserRepository;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthenticationManager authenticationManager;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;
	
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			return ResponseEntity.badRequest()
					.body(new AuthResponse(null, null, "Username already exists"));
		}
		
		if (userRepository.existsByEmail(request.getEmail())) {
			return ResponseEntity.badRequest()
					.body(new AuthResponse(null, null, "Email already exists"));
		}
		
		User user = User.builder()
				.username(request.getUsername())
				.password(passwordEncoder.encode(request.getPassword()))
				.email(request.getEmail())
				.build();
		
		userRepository.save(user);
		
		String token = jwtUtil.generateToken(user.getUsername());
		
		return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "User registered successfully"));
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
			);
			
			String token = jwtUtil.generateToken(request.getUsername());
			
			return ResponseEntity.ok(new AuthResponse(token, request.getUsername(), "Login successful"));
		} catch (Exception e) {
			return ResponseEntity.badRequest()
					.body(new AuthResponse(null, null, "Invalid credentials"));
		}
	}
}
