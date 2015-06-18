package nl.northcreek.twitazia.network;

public class Authenticated {
	private String token_type;
	private String access_token;

	public String getAccess_token() {
		return access_token;
	}

	public String setAccess_token(String access_token) {
		return this.access_token = access_token;
	}

	public String getToken_type() {
		return token_type;
	}

	public String setToken_type(String token_type) {
		return this.token_type = token_type;
	}
}