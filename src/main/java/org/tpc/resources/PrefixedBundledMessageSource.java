package org.tpc.resources;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.tpc.PrefixedResourceBundle;

/**
 * Provides a message source containing multiple resource bundles.
 * The resources bundles are added via the <code>addBasenames</code> method
 * or dependency injection. Each resource bundle has an assigned prefix
 * which prefixes each key of all messages. This makes it easier to provide
 * larger sets of resource bundles by splitting them into multiple files.
 * Due to the prefix each resource bundle can use shorted message names without
 * loosing the uniqueness of its key. 
 * 
 * Omitting the prefix and the '#' sign sets the bundle as fallback or default bundle
 * mapping the key of each message directly onto the keys of the resource bundle.
 * This is equal to an default namespace. The provided paths after the prefix defines
 * the path to search for the resource bundle.
 * 
 * <pre>
 * Dependency Injection via Spring:
 * {@code
 * <beans:bean id="messageSource" class="org.tpc.resources.PrefixedBundleMessageSource">
 *   <beans:property name="namePrefix" value="/WEB-INF/messages/"/>
 *   <beans:property name="basenames">
 *     <beans:list>
 *       <beans:value>
 *         global/global/global,
 *         lang#global/languages/lang
 *       </beans:value>
 *       <beans:value>
 *         login#login/global/global
 *       </beans:value>
 *     </beans:list>
 *   </beans:property>
 * </beans:bean>
 * }</pre>
 * 
 * @author Tobias Faller
 */
public class PrefixedBundledMessageSource extends ResourceBundleMessageSource {

	private String separator;
	private String namePrefix;
	private String nameSuffix;

	/**
	 * Creates a new instance with the separator set to the default value
	 * and no prefix / suffix.
	 */
	public PrefixedBundledMessageSource() {
		separator = PrefixedResourceBundle.getDefaultSeparator();
		namePrefix = null;
		nameSuffix = null;
	}

	/**
	 * Creates a new instance with a separator and a prefix / suffix.
	 * Pass <code>null</code> as pre-/suffix to leave them out.
	 */
	public PrefixedBundledMessageSource(String separator, String namePrefix, String nameSuffix) {
		checkAndAssignSeparator(separator);

		this.namePrefix = namePrefix;
		this.nameSuffix = nameSuffix;
	}
	
	@Override
	protected ResourceBundle doGetBundle(String basename, Locale locale)
			throws MissingResourceException {
		StringTokenizer tokenizer = new StringTokenizer(basename, ",");
		PrefixedResourceBundle bundle = new PrefixedResourceBundle(separator);

		String prefix = null;
		String name;

		while(tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().trim();
			int index = token.indexOf('#');
			if(index != -1) {
				prefix = token.substring(0, index).trim();
				name = token.substring(index + 1).trim();
			} else {
				prefix = null;
				name = token;
			}

			if(prefix != null && !prefix.isEmpty()) {
				bundle.addBundle(prefix,
					super.doGetBundle(getBundlePath(name), locale));
			} else {
				bundle.addDefaultBundle(
					super.doGetBundle(getBundlePath(name), locale));
			}
		}

		return bundle;
	}

	/**
	 * Appends pre- / postfix to the bundle-name.
	 * 
	 * @param bundle The bundle name
	 * @return The complete bundle-path
	 */
	private String getBundlePath(String bundle) {
		StringBuilder sb = new StringBuilder();

		if(namePrefix != null) {
			sb.append(namePrefix);
		}

		sb.append(bundle);

		if(nameSuffix != null) {
			sb.append(nameSuffix);
		}

		return sb.toString();
	}

	/**
	 * Sets the separator to use between the bundle-prefix and the key.
	 * 
	 * @param separator The separator to use
	 */
	public void setSeparator(String separator) {
		checkAndAssignSeparator(separator);
	}

	/**
	 * Checks and assigns the separator value.
	 * 
	 * @param separator The separator string to check
	 */
	private void checkAndAssignSeparator(String separator) {
		if(separator == null || (separator = separator.trim()).isEmpty())
			throw new IllegalArgumentException("Separator cannot be null or empty!");

		this.separator = separator;
	}

	/**
	 * Gets the separator which splits the resource-key from the bundle-prefix.
	 * 
	 * @return The resource-key
	 */
	public String getSeparator() {
		return separator;
	}

	/**
	 * Sets the prefix to append to each bundle-path.
	 * 
	 * @param namePrefix The prefix to add
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	/**
	 * Sets the suffix to append to each bundle-path.
	 * 
	 * @param nameSuffix The suffix to add
	 */
	public void setNameSuffix(String nameSuffix) {
		this.nameSuffix = nameSuffix;
	}

	/**
	 * Gets the prefix which is appended to the bundle-path.
	 * 
	 * @return The prefix to be used
	 */
	public String getNamePrefix() {
		return namePrefix;
	}

	/**
	 * Gets the suffix which is appended to the bundle-path.
	 * 
	 * @return The suffix to be used
	 */
	public String getNameSuffix() {
		return nameSuffix;
	}

}
