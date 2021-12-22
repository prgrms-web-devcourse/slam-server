package org.slams.server.user.oauth.jwt;

import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Jwt {

	private final String issuer;
	private final String clientSecret;
	private final int expirySeconds;
	private final Algorithm algorithm;
	private final JWTVerifier jwtVerifier;

	public Jwt(String issuer, String clientSecret, int expirySeconds) {
		this.issuer = issuer;
		this.clientSecret = clientSecret;
		this.expirySeconds = expirySeconds;
		this.algorithm = Algorithm.HMAC512(clientSecret);
		this.jwtVerifier = com.auth0.jwt.JWT.require(algorithm)
			.withIssuer(issuer)
			.build();
	}

	// token 생성
	public String sign(Claims claims) {
		Date now = new Date();
		JWTCreator.Builder builder = com.auth0.jwt.JWT.create();
		builder.withIssuer(issuer);
		builder.withIssuedAt(now);
		if (expirySeconds > 0) {
			builder.withExpiresAt(new Date(now.getTime() + expirySeconds * 1_000L));
		}
		builder.withClaim("userId", claims.userId);
		builder.withArrayClaim("roles", claims.roles);
		return builder.sign(algorithm);
	}

	// token 검증
	public Claims verify(String token) throws JWTVerificationException {
		return new Claims(jwtVerifier.verify(token));
	}


	// 토큰을 만들거나 검증할 때 필요한 데이터를 넘겨주는 inner class
	@Getter
	public static class Claims {
		Long userId;
		String email;
		String[] roles;
		Date iat; // token 발행일자
		Date exp; // token 만료일자

		private Claims() {/*no-op*/}

		public Claims(DecodedJWT decodedJWT) {
			Claim userId = decodedJWT.getClaim("userId");
			if(!userId.isNull()){
				this.userId = userId.asLong();
			}

			Claim email = decodedJWT.getClaim("email");
			if(!email.isNull()){
				this.email = email.asString();
			}

			Claim roles = decodedJWT.getClaim("roles");
			if (!roles.isNull()) {
				this.roles = roles.asArray(String.class);
			}

			this.iat = decodedJWT.getIssuedAt();
			this.exp = decodedJWT.getExpiresAt();
		}

		public static Claims from(Long userId, String email, String[] roles) {
			Claims claims = new Claims();
			claims.userId = userId;
			claims.email = email;
			claims.roles = roles;
			return claims;
		}

		public Map<String, Object> asMap() {
			Map<String, Object> map = new HashMap<>();
			map.put("userId", userId);
			map.put("email", email);
			map.put("roles", roles);
			map.put("iat", iat());
			map.put("exp", exp());
			return map;
		}

		public long iat() {
			return iat != null ? iat.getTime() : -1;
		}

		public long exp() {
			return exp != null ? exp.getTime() : -1;
		}

		@Override
		public String toString() {
			return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("userId", userId)
				.append("email", email)
				.append("roles", Arrays.toString(roles))
				.append("iat", iat)
				.append("exp", exp)
				.toString();
		}

	}

}
