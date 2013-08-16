package org.issg.ibis.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.vaadin.ui.UI;

import static org.mockito.Mockito.*;

public class TestPersistModule extends AbstractModule {
  
  @Override
  protected void configure() {
    
    bind(UI.class).toInstance(mock(UI.class));
    bind(EntityManagerFactory.class).toInstance(Persistence.createEntityManagerFactory("ibis-domain"));
    /*
     * Bind constants
     */
    Names.bindProperties(binder(), getProperties());
  }

  private Properties getProperties() {
    Properties props = new Properties();
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream("runtime.properties");
    try {
      props.load(stream);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return props;
  }
  
}
