package n52.talsim_sos_converter.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central component that provides methods to <i>send</i> <b>HTTP requests</b>
 * to a SOS instance.
 * 
 * @author Christian Danowski-Buhren (contact: c.danowski@52north.org)
 *
 */
public class SosRequestSender {

	private static Logger logger = LoggerFactory.getLogger(SosRequestSender.class);

	/**
	 * Sends a HTTP POST request containing the SOS InsertObservation request as
	 * POX request body (Content-Type "application/xml").
	 * 
	 * @param sosURL
	 *            the URL of the SOS instance, to which the request is sent
	 * @param insertObservationRequest
	 *            full SOS InsertObservation request body as POX (Content-Type
	 *            "application/xml")
	 * @param authorization_token
	 *            the token for the request header 'Authorization'
	 * @return the response (body) of the SOS instance as String
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public static String sendInsertObservationRequestToSOS(URL sosURL, String insertObservationRequest,
			String authorization_token) throws ProtocolException, IOException {
		String response_insertObservation = send_http_post(sosURL, insertObservationRequest, authorization_token);

		return response_insertObservation;
	}

	/**
	 * Sends a HTTP POST request containing the SOS InsertSensor request as POX
	 * request body (Content-Type "application/xml").
	 * 
	 * @param sosURL
	 *            the URL of the SOS instance, to which the request is sent
	 * @param insertSensorRequest
	 *            full SOS InsertSensor request body as POX (Content-Type
	 *            "application/xml")
	 * @param authorization_token
	 *            the token for the request header 'Authorization'
	 * @return the response (body) of the SOS instance as String
	 * @throws IOException
	 */
	public static String sendInsertSensorRequestToSOS(URL sosURL, String insertSensorRequest,
			String authorization_token) throws IOException {
		String response_insertSensor = send_http_post(sosURL, insertSensorRequest, authorization_token);

		return response_insertSensor;
	}

	/**
	 * Opens the connection to the {@code sosURL} and sends an HTTP POST request
	 * containing the attached POX {@code post_body} using header
	 * {@code Content-Type: application/xml}
	 * 
	 * @param sosURL
	 *            the URL of the SOS instance, to which the request is sent
	 * @param post_body
	 *            HTTP POST request body as POX Content-Type "application/xml")
	 * @param authorization_token
	 *            the token for the request header 'Authorization'
	 * @return the response (body) of the SOS instance as String
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static String send_http_post(URL sosURL, String post_body, String authorization_token)
			throws IOException, ProtocolException {

		if (logger.isDebugEnabled())
			logger.debug("Constructing HTTP POST request against URL '{}' with request body {}", sosURL, post_body);

		HttpURLConnection connection = (HttpURLConnection) sosURL.openConnection();

		// request header
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Authorization", authorization_token);

		if (logger.isDebugEnabled())
			logger.debug("The following request properties/headers were set: '{}'", connection.getRequestProperties());

		if (logger.isDebugEnabled())
			logger.debug("Execute request.");

		// Send post request
		connection.setDoOutput(true);
		DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		outStream.writeBytes(post_body);
		outStream.flush();
		outStream.close();

		int responseCode = connection.getResponseCode();

		if (logger.isDebugEnabled())
			logger.debug("Response Code: '{}'", responseCode);

		if (logger.isDebugEnabled())
			logger.debug("Fetching response body.");

		// fetch response
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// disconnect
		connection.disconnect();
		
		// log result
		String responseBody = response.toString();
		if (logger.isDebugEnabled())
			logger.debug("Response body: {}", responseBody);

		return responseBody;
	}

}
