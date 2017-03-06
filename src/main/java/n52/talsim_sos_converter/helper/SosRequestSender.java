package n52.talsim_sos_converter.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Central component that provides methods to <i>send</i> <b>HTTP requests</b>
 * to a SOS instance.
 * 
 * @author Christian Danowski-Buhren (contact: c.danowski@52north.org)
 *
 */
public class SosRequestSender {

	/**
	 * Sends a HTTP POST request containing the SOS InsertObservation request as
	 * POX request body (Content-Type "application/xml").
	 * 
	 * @param sosURL
	 *            the URL of the SOS instance, to which the request is sent
	 * @param insertObservationRequest
	 *            full SOS InsertObservation request body as POX (Content-Type
	 *            "application/xml")
	 * @return the response (body) of the SOS instance as String
	 * @throws ProtocolException
	 * @throws IOException
	 */
	public static String sendInsertObservationRequestToSOS(URL sosURL, String insertObservationRequest)
			throws ProtocolException, IOException {
		String response_insertObservation = send_http_post(sosURL, insertObservationRequest);

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
	 * @return the response (body) of the SOS instance as String
	 * @throws IOException
	 */
	public static String sendInsertSensorRequestToSOS(URL sosURL, String insertSensorRequest) throws IOException {
		String response_insertSensor = send_http_post(sosURL, insertSensorRequest);

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
	 * @return the response (body) of the SOS instance as String
	 * @throws IOException
	 * @throws ProtocolException
	 */
	private static String send_http_post(URL sosURL, String post_body) throws IOException, ProtocolException {
		HttpURLConnection connection = (HttpURLConnection) sosURL.openConnection();
		
		String token = ResourceLoader.fetchAuthorizationToken();

		// reuqest header
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		connection.setRequestProperty("Content-Type", "application/xml");
		connection.setRequestProperty("Authorization", token);

		// String post_body = URLEncoder.encode(post_body, "UTF-8");

		// Send post request
		connection.setDoOutput(true);
		DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
		outStream.writeBytes(post_body);
		outStream.flush();
		outStream.close();

		int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + sosURL);
		System.out.println("Post parameters : " + post_body);
		System.out.println("Response Code : " + responseCode);

		// fetch response
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());
		
		// disconnect
		connection.disconnect();

		return response.toString();
	}

}
