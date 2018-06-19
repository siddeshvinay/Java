/***
 **  @(#) TradeCard.com 1.0
 **
 **  Copyright (c) 1999 TradeCard, Inc. All Rights Reserved.
 **
 **
 **  THIS COMPUTER SOFTWARE IS THE PROPERTY OF TradeCard, Inc.
 **
 **  Permission is granted to use this software as specified by the TradeCard
 **  COMMERCIAL LICENSE AGREEMENT.  You may use this software only for
 **  commercial purposes, as specified in the details of the license.
 **  TRADECARD SHALL NOT BE LIABLE FOR ANY  DAMAGES SUFFERED BY
 **  THE LICENSEE AS A RESULT OF USING OR MODIFYING THIS SOFTWARE IN ANY WAY.
 **
 **  YOU MAY NOT DISTRIBUTE ANY SOURCE CODE OR OBJECT CODE FROM THE TradeCard.com
 **  TOOLKIT AT ANY TIME. VIOLATORS WILL BE PROSECUTED TO THE FULLEST EXTENT
 **  OF UNITED STATES LAW.
 **
 **  @version 1.0
 **  @author Copyright (c) 1999 TradeCard, Inc. All Rights Reserved.
 **
 **/

package com.gtnexus.selenium.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.*;
import com.google.common.collect.*;

/**
 * The properties reader wrappers a properties file with commonly used accessor commands
 */
public class PropertiesReader {
	
   private static Logger LOGGER = Logger.getLogger(PropertiesReader.class);

   protected Properties propertiesFile;
   private HashMap overrideMap = new HashMap();
   private String environmentSubstitution = null;
   private Set<String> keySet = null;
   //private Set<String> missed = new ConcurrentHashSet();
	private static final String EMPTY_STRING = "";

	public PropertiesReader(InputStream in) {
      try {
         propertiesFile = new Properties();
         propertiesFile.load(in);
         in.close();
         //environmentSubstitution = System.getProperty(Constants.ENVIRONMENT_PROPERTY);
      } catch (Exception e) {
         throw new IllegalArgumentException("Unable to load properties file from URL:" + e.getMessage());
      }
   }

   /**
	 * Add an override to the properties file
	 */
   public void addOverrideProperty(String property, Object value) {
      if (overrideMap.containsKey(property)) {
         overrideMap.remove(property);
      }
      overrideMap.put(property, value);
   }

   /**
	 * Convenience method to get the passed property
	 */
   public String getProperty(String property) throws MissingResourceException {
      return getProperty(property, null);
   }

   /**
	 * Get the passed property
	 */
   public String getProperty(String property, String defaultValue) throws MissingResourceException {
      String value = null;
      try {
         if (overrideMap.containsKey(property)) {
            value = (String)overrideMap.get(property);
         } else {
            value = propertiesFile.getProperty(property);
            if (value == null) {
               if (defaultValue == null) {
                  throw new MissingResourceException(property + " Property was not found or is null.", "MessagingServerStarter", property);
               } else {
				   /*
                  if (missed.addIfAbsent(property)) {
                     //only send this notice once
                     LOGGER.info("Property " + property + " was not found. Using default value = " + defaultValue);
                  }
                  */
                  value = defaultValue;
               }
            } else {
               value = processSubstitutions(value);
            }
         }
      } catch (MissingResourceException e) {
         LOGGER.info("Property " + property + " was not found.");
         throw new MissingResourceException("Property was not found or is null.", "MessagingServerStarter", property);
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("Unexpected exception encountered retrieving Property " + property + " was not found");
         throw new MissingResourceException("Unexpected exception encountered retrieving Property", "MessagingServerStarter", property);
      }
      return value;
   }

   /**
	 * Return a list of Starter.Entry objects
	 */
   public List getTableProperty(String property) {
      List properties = getListProperty(property);
      List result = new ArrayList();
      if (properties != null) {
         for (Iterator i = properties.iterator(); i.hasNext();) {
            String keyAndValue = (String)i.next();
            result.add(parseKeyAndValue(keyAndValue));
         }
      }
      return result;
   }

   /**
	 * Return a list based property, key=value,value2,value3
	 */
   public List getListProperty(String property) {
      String propertyStr = getProperty(property, EMPTY_STRING);
      if (propertyStr != null && !EMPTY_STRING.equals(propertyStr)) {
         List result = new ArrayList();
         StringTokenizer tokenizer = new StringTokenizer(propertyStr, ",");
         while (tokenizer.hasMoreElements()) {
            String value = ((String)tokenizer.nextElement()).trim();
            result.add(value);
         }
         return result;
      }
      return null;

   }

   /**
    * Return a table based property, key=value, key2=value, key3=value
    * @return a list of PropertiesReader.Entry objects
    */
   public List getInnerTableProperty(String property) {
      List properties = getListProperty(property);
      List result = new ArrayList();
      if (properties != null) {
         for (Iterator i = properties.iterator(); i.hasNext();) {
            String keyAndValue = (String)i.next();
            PropertiesReader.Entry keyEntry = parseKeyAndValue(keyAndValue);
            List innerEntries = parseInnerProperties(keyEntry.getStringValue());
            result.add(new PropertiesReader.Entry(keyEntry.getKey(), innerEntries));
         }
      }
      return result;
   }
   
   /**
    * Returns a {@link Map} representation of the specified Inner Table
    * Property, or an empty {@link Map} if no poperties entry exists for
    * {@code property}. */
   public Map<String, Map<String, String>> getInnerTablePropertyAsMap(String property) {
      List<String> properties = getListProperty(property);
      if (properties != null && !properties.isEmpty()) {
         Map<String, Map<String, String>> result = Maps.newHashMapWithExpectedSize(properties.size());
         for (String outerProp : properties) {
            Entry keyEntry = parseKeyAndValue(outerProp);
            List<Entry> innerEntries = parseInnerProperties(keyEntry.getStringValue());

            Map<String, String> innerMap = Maps.newHashMapWithExpectedSize(innerEntries.size());
            for (Entry innerEntry : innerEntries) {
               innerMap.put(innerEntry.getKey(), innerEntry.getStringValue());
            }

            result.put(keyEntry.getKey(), ImmutableMap.copyOf(innerMap));
         }
         return ImmutableMap.copyOf(result);
      } else {
         return Collections.EMPTY_MAP;
      }
   }
   
   public Set<String> getKeySet() {
      if(this.keySet == null) {
         Set<String> keySet = Sets.newHashSet();
         Enumeration keyEnumeration = propertiesFile.propertyNames();
         while(keyEnumeration.hasMoreElements()) {
            keySet.add((String)keyEnumeration.nextElement());
         }
         
         this.keySet = keySet;
      }
      
      return this.keySet;
   }

   /**
	 * Parse Key=Value
	 */
   protected PropertiesReader.Entry parseKeyAndValue(String keyAndValue) {
      int indexOfEquals = keyAndValue.indexOf("=");
      String key = keyAndValue.substring(0, indexOfEquals).trim();
      String value = keyAndValue.substring(indexOfEquals + 1).trim();
      return new PropertiesReader.Entry(key, processSubstitutions(value));
   }

   /**
	 * Parse innnerProprties outerKey={key1=value1}{key2=value2}
	 */
   protected List parseInnerProperties(String innerProperties) {
      List result = new ArrayList();
      StringTokenizer tokenizer = new StringTokenizer(innerProperties, ";");
      while (tokenizer.hasMoreElements()) {
         String keyAndValue = ((String)tokenizer.nextElement()).trim();
         //strip of optional {}, if present
         if (keyAndValue.startsWith("{") && keyAndValue.endsWith("}")) {
            keyAndValue = keyAndValue.substring(1, keyAndValue.length() - 1);
         }
         result.add(parseKeyAndValue(keyAndValue));
      }
      return result;
   }
   
   protected String processSubstitutions(String value) {
      if (environmentSubstitution != null) {
         value = StringUtils.replace(value, "${environment}", environmentSubstitution);
      }
      return value;
   }

   public static class Entry {

      private String key;
      private Object value;

      public Entry(String key, Object value) {
         this.key = key;
         this.value = value;
      }

      public String getKey() {
         return key;
      }

      public String getStringValue() {
         return (String)value;
      }

      public Object getValue() {
         return value;
      }

      public void setKey(String key) {
         this.key = key;
      }

      public void setValue(Object value) {
         this.value = value;
      }
   }

}