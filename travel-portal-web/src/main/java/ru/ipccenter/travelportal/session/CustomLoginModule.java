package ru.ipccenter.travelportal.session;

import org.apache.log4j.Logger;
import org.jboss.security.SimpleGroup;
import org.jboss.security.SimplePrincipal;
import org.jboss.security.auth.spi.DatabaseServerLoginModule;
import org.jboss.security.auth.spi.Util;
import ru.ipccenter.travelportal.common.model.objects.Role;
import ru.ipccenter.travelportal.common.utils.BeanLookupHelper;
import ru.ipccenter.travelportal.security.UserService;

import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Random;
import java.util.Set;

public class CustomLoginModule extends DatabaseServerLoginModule {
    public static final Logger LOG = Logger.getLogger(CustomLoginModule.class);

    private UserService userService;

    public CustomLoginModule() {
        LOG.info("");
        try {
            userService = BeanLookupHelper.lookup(UserService.class);
        } catch (NamingException e) {
            LOG.error(e);
        }
    }

    public static String createHash(String password, String salt) {
        return Util.createPasswordHash("MD5", Util.BASE64_ENCODING, null, null, password + salt);
    }

    public static String createSalt() {
        BigInteger n = new BigInteger("99999999999999999999");
        Random rand = new Random();
        BigInteger result = new BigInteger(n.bitLength(), rand);
        while( result.compareTo(n) >= 0 ) {
            result = new BigInteger(n.bitLength(), rand);
        }
        return result.toString(16);
    }

    @Override
    protected String getUsersPassword() throws LoginException {
            return getPasswordHash();
    }

    @Override
    protected Group[] getRoleSets() throws LoginException {

        Role role = getRole();
        SimpleGroup roles = new SimpleGroup("Roles");

        if (role != null) {
            roles.addMember(new SimplePrincipal(role.getName()));
        }

        return new Group[] { roles };
    }

    @Override
    public boolean login() throws LoginException {
        return super.login();
    }

    @Override
    protected Principal getIdentity() {
        return super.getIdentity();
    }

    @Override
    protected Principal getUnauthenticatedIdentity() {
        return super.getUnauthenticatedIdentity();
    }

    @Override
    protected Object getCredentials() {
        return super.getCredentials();
    }

    @Override
    protected String getUsername() {
        return super.getUsername();
    }

    @Override
    protected String[] getUsernameAndPassword() throws LoginException {
        return super.getUsernameAndPassword();
    }

    @Override
    protected String createPasswordHash(String username, String password, String digestOption) throws LoginException {
        String salt = getSalt();
        LOG.info("saltedPassword: " + password + salt);
        return createHash(password, salt);
    }

    @Override
    protected Throwable getValidateError() {
        return super.getValidateError();
    }

    @Override
    protected void setValidateError(Throwable validateError) {
        super.setValidateError(validateError);
    }

    @Override
    protected boolean validatePassword(String inputPassword, String expectedPassword) {
        try {
            inputPassword = createPasswordHash(getUsername(), inputPassword, null);
        } catch (LoginException e) {
            return false;
        }

        boolean isPasswordValid = super.validatePassword(inputPassword, expectedPassword);
        log.info("password is valid: " + isPasswordValid);

        return isPasswordValid;
    }


    @Override
    public boolean commit() throws LoginException {
        LOG.info("commit");
        return super.commit();
    }

    @Override
    public boolean abort() throws LoginException {
        LOG.info("abort");
        return super.abort();
    }

    @Override
    public boolean logout() throws LoginException {
        return super.logout();
    }

    @Override
    protected boolean getUseFirstPass() {
        return super.getUseFirstPass();
    }

    @Override
    protected Group createGroup(String name, Set<Principal> principals) {
        return super.createGroup(name, principals);
    }

    @Override
    protected Principal createIdentity(String username) throws Exception {
        return super.createIdentity(username);
    }

    private String getPasswordHash() {
        return userService.getPasswordHash(getUsername());
    }

    private Role getRole() {
        return userService.getRole(getUsername());
    }

    private String getSalt() {
        return userService.getSalt(getUsername());
    }
}
