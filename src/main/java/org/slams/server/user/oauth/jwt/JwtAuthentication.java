package org.slams.server.user.oauth.jwt;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.Assert;

public class JwtAuthentication {

	public final String token;
	public final Long userId;

	public JwtAuthentication(String token, Long userId) {
		Assert.notNull(token, "token must be provided.");
		Assert.notNull(userId, "userId must be provided.");

		this.token = token;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("token", token)
			.append("userId", userId)
			.toString();
	}

}
