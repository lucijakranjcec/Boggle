package hr.tvz.boggle.jndi;

import hr.tvz.boggle.model.ConfigurationKey;

import javax.naming.Context;
import javax.naming.NamingException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Properties;

public class ConfigurationReader {

    private static final String PROVIDER_URL = "file:C:/java-config/";

    private static Hashtable<?, ?> configureEnvironment() {
        return new Hashtable<>() {
            {
                put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.fscontext.RefFSContextFactory");
                put(Context.PROVIDER_URL, PROVIDER_URL);
            }
        };
    }

    public static String getValue(ConfigurationKey key) {
        try (InitialDirContextCloseable context = new InitialDirContextCloseable(configureEnvironment())){

            String fileName = "conf.properties";
            Object object = context.lookup(fileName);

            Properties props = new Properties();
            props.load(new FileReader(object.toString()));

            return props.getProperty(key.getKey());
        } catch (NamingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
