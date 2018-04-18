package io.pivotal.scs.demo.etl.geode.sink;

import java.util.Properties;

import org.apache.geode.LogWriter;
import org.apache.geode.distributed.DistributedMember;
import org.apache.geode.security.AuthInitialize;
import org.apache.geode.security.AuthenticationFailedException;

public class UserAuthInitialize implements AuthInitialize {

	public static final String USER_NAME = "security-username";

	public static final String PASSWORD = "security-password";

	protected LogWriter securitylog;

	protected LogWriter systemlog;

	public static AuthInitialize create() {
		return new UserAuthInitialize();
	}

	public void init(LogWriter systemLogger, LogWriter securityLogger) throws AuthenticationFailedException {
		this.systemlog = systemLogger;
		this.securitylog = securityLogger;
	}

	public UserAuthInitialize() {
	}

	public Properties getCredentials(Properties props, DistributedMember server, boolean isPeer) throws AuthenticationFailedException {

		Properties newProps = new Properties();
		String userName = props.getProperty(USER_NAME);
		if (userName == null) {
			throw new AuthenticationFailedException("UserPasswordAuthInit: user name property [" + USER_NAME + "] not set.");
		}
		newProps.setProperty(USER_NAME, userName);
		String passwd = props.getProperty(PASSWORD);
		// If password is not provided then use empty string as the password.
		if (passwd == null) {
			passwd = "";
		}
		newProps.setProperty(PASSWORD, passwd);
		return newProps;
	}

	public void close() {
	}

}