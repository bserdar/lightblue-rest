package com.redhat.lightblue.rest.auth.ldap;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.ldap.InitialLdapContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Holds configurations needed for ldap lookup and does the lookup on demand.
 *
 * @author mpatercz
 *
 */
public class InitialLdapContextProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialLdapContextProvider.class);

    private Hashtable<String, Object> env = new Hashtable<>();

    public InitialLdapContextProvider(String server, String bindDn, String bindDNPwd) {
        LOGGER.debug("init()");

        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        if (bindDn != null) {
            env.put(Context.SECURITY_PRINCIPAL, bindDn);
        }
        if (bindDNPwd != null) {
            env.put(Context.SECURITY_CREDENTIALS, bindDNPwd);
        }
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, server);
    }

    /**
     * This call is expensive. InitialLdapContext should be reused, but then we
     * have to deal with ldap closing the connections. A real pooling with
     * connection validation is needed.
     *
     * @throws NamingException
     */
    public InitialLdapContext lookupLdap() throws NamingException {
        LOGGER.debug("createInitialContext()");
        return new InitialLdapContext(env, null);
    }

}
