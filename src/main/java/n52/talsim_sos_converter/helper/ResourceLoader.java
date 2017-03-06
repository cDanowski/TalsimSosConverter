package n52.talsim_sos_converter.helper;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides methods that load relevant resources such as SOS request templates.
 * 
 * Since they are templates, the contents usually include so-called
 * <i>placeholders</i> that have to be replaced by real values, e.g. by using
 * component {@link SosRequestConstructor}.
 * 
 * @author Christian Danowski-Buhren (contact: c.danowski@52north.org)
 *
 */
public class ResourceLoader {

	private static Logger logger = LoggerFactory.getLogger(ResourceLoader.class);

	/**
	 * Loads the content of the <b>SOS InsertObservation request template
	 * file</b> located in src/main/resources and returns it as String.
	 * 
	 * @return a {@code String} representation of the file contents
	 * @throws IOException
	 */
	public static String loadInsertObservationRequestTemplate() throws IOException {

		if (logger.isDebugEnabled())
			logger.debug("Trying to load insertObservationRequest template as String from resource '{}'",
					Constants.PATH_TO_INSERT_OBSERVATION_REQUEST_TEMPLATE);

		return loadResourceAsString(Constants.PATH_TO_INSERT_OBSERVATION_REQUEST_TEMPLATE);
	}

	/**
	 * Loads the content of the <b>SOS InsertObservation request template
	 * file</b> located in src/main/resources and returns it as String.
	 * 
	 * @return a {@code String} representation of the file contents
	 * @throws IOException
	 */
	public static String loadInsertSensorRequestTemplate() throws IOException {

		if (logger.isDebugEnabled())
			logger.debug("Trying to load insertSensorRequest template as String from resource '{}'",
					Constants.PATH_TO_INSERT_SENSOR_REQUEST_TEMPLATE);

		return loadResourceAsString(Constants.PATH_TO_INSERT_SENSOR_REQUEST_TEMPLATE);
	}

	private static String loadResourceAsString(String pathToResource) throws IOException {
		InputStream input = Thread.currentThread().getContextClassLoader().getResourceAsStream(pathToResource);

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
		StringBuilder stringBuilder = new StringBuilder();
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			stringBuilder.append(line);
		}
		String outputAsString = stringBuilder.toString();

		bufferedReader.close();

		return outputAsString;
	}

	/**
	 * Fetch the token value that can be used to authenticate HTTP requests
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String fetchAuthorizationToken() throws IOException {

		if (logger.isInfoEnabled())
			logger.info("Starting to fetch authorization token for SOS-T.");

		if (logger.isDebugEnabled())
			logger.debug("Extract path to local properties file from property '{}' within properties-resource '{}'.",
					Constants.TOKEN_FILE_PATH_PROPERTY_NAME, Constants.PATH_TO_TOKEN_PROPERTIES_FILE);

		/*
		 * get path to local token.properties file outside of repository
		 */
		InputStream input_tokenPropertiesReferenceFile = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(Constants.PATH_TO_TOKEN_PROPERTIES_FILE);

		Properties properties_token_reference = new Properties();

		properties_token_reference.load(input_tokenPropertiesReferenceFile);

		String pathToTokenFile = properties_token_reference.getProperty(Constants.TOKEN_FILE_PATH_PROPERTY_NAME);

		if (logger.isDebugEnabled())
			logger.debug("The following local resource is declared to include the authorization token: '{}'.",
					pathToTokenFile);

		input_tokenPropertiesReferenceFile.close();

		if (logger.isDebugEnabled())
			logger.debug("Inspecting properties file '{}' for property '{}'.", pathToTokenFile,
					Constants.TOKEN_PROPERTY_NAME);

		/*
		 * extract token value from external properties file
		 */
		InputStream input_tokenPropertiesValueFile = new FileInputStream(pathToTokenFile);

		Properties properties_token_value = new Properties();

		properties_token_value.load(input_tokenPropertiesValueFile);

		String tokenValue = properties_token_value.getProperty(Constants.TOKEN_PROPERTY_NAME);

		input_tokenPropertiesValueFile.close();

		if (logger.isInfoEnabled())
			logger.info("Authorization token for SOS-T successfully retrieved.");

		return tokenValue;

	}

}
