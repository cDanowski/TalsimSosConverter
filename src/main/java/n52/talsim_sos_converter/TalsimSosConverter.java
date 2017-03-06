package n52.talsim_sos_converter;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import n52.talsim_sos_converter.helper.Constants;
import n52.talsim_sos_converter.helper.ResourceLoader;
import n52.talsim_sos_converter.helper.SosRequestConstructor;
import n52.talsim_sos_converter.helper.SosRequestSender;

public class TalsimSosConverter {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String INSERT_OBSERVATION_RESPONSE_STRING = "InsertObservationResponse";
	private static final String INSERT_SENSOR_RESPONSE_STRING = "InsertSensorResponse";

	/**
	 * Parses the TALSIM output/result and uses the transactional SOS methods to
	 * insert both the sensor and all included measurements to the SOS instance
	 * with the specified URL
	 * 
	 * @param talsimOutput
	 *            an {@link InputStream} of the TASLIM XML output including
	 *            sensor definition and measurements. However, it does not
	 *            include a spatial reference.
	 * @param sosURL
	 *            URL to the SOS-T (transactional SOS instance), to which the
	 *            data from {@code talsimOutput} should be transferred to
	 * @return <b>true</b> if insertion was successful, <b>false</b> otherwise
	 * @throws Exception
	 */
	public boolean insertOutputToSOS(InputStream talsimOutput, URL sosURL) throws Exception {

		if (logger.isInfoEnabled())
			logger.info("Begin Insertion of TalsimResult into SOS instance with URL '{}'.", sosURL);

		/*
		 * parse TalsimResult.xml
		 */

		if (logger.isInfoEnabled())
			logger.info("Parsing TalsimResult InputStream.");

		// create a new DocumentBuilderFactory
		Document talsimDocument = parseTalsimDocument(talsimOutput);

		// load template files

		if (logger.isInfoEnabled())
			logger.info(
					"Loading template reource files for InsertSensorRequest and InsertObservationRequest and fetching authorization token.");

		String insertSensorRequestTemplate = ResourceLoader.loadInsertSensorRequestTemplate();
		String insertObservationRequestTemplate = ResourceLoader.loadInsertObservationRequestTemplate();
		String authorization_token = ResourceLoader.fetchAuthorizationToken();

		/*
		 * create InsertSensor Request and send it to SOS-T
		 */

		if (logger.isInfoEnabled())
			logger.info("Starting to build and send InsertSensorRequest.");

		processInsertSensorRequest(sosURL, talsimDocument, insertSensorRequestTemplate, authorization_token);

		/*
		 * process InsertObsrvation
		 */

		if (logger.isInfoEnabled())
			logger.info("Starting to build and send InsertObservationRequests.");

		processInsertObservationRequests(sosURL, talsimDocument, insertObservationRequestTemplate, authorization_token);

		if (logger.isInfoEnabled())
			logger.info("Insertion of Sensor and Observations from TalsimResult to SOS instance succeded.");

		return true;
	}

	private void processInsertObservationRequests(URL SosURL, Document talsimDocument,
			String insertObservationRequestTemplate, String authorization_token)
			throws Exception, ProtocolException, IOException {
		/*
		 * each series node contains information for one observableProperty,
		 * hence different request have to be set-up for different series nodes
		 */

		if (logger.isInfoEnabled())
			logger.info("Extracting all 'series' nodes from parsed TalsimResult.");

		NodeList seriesNodes = talsimDocument.getElementsByTagName(Constants.TALSIM_SERIES_NODE);
		int numberOfSeriesNodes = seriesNodes.getLength();

		if (logger.isInfoEnabled())
			logger.info("Number of extracted 'series' nodes is '{}'.", numberOfSeriesNodes);

		/*
		 * for each event in seriesNode: create InsertObservation requests and
		 * send them to SOS-T
		 */
		for (int i = 0; i < numberOfSeriesNodes; i++) {
			Node seriesNode = seriesNodes.item(i);

			if (logger.isInfoEnabled())
				logger.info("Start processing of next 'series' node.");

			if (logger.isInfoEnabled())
				logger.info("Begin building of InsertObservationRequests for current 'series' node.");

			List<String> insertObservationRequests = SosRequestConstructor.createInsertObservationRequestsForSeriesNode(
					talsimDocument, seriesNode, insertObservationRequestTemplate);

			if (logger.isInfoEnabled())
				logger.info("Number of constructued InsertObservationRequests is '{}'.",
						insertObservationRequests.size());

			if (logger.isInfoEnabled())
				logger.info("Start to send InsertObservationRequests to SOS instance.");

			for (String insertObservationRequest : insertObservationRequests) {

				if (logger.isInfoEnabled())
					logger.info("Sending next InsertObservationRequest.");

				String sosResponse_insertObservation = SosRequestSender.sendInsertObservationRequestToSOS(SosURL,
						insertObservationRequest, authorization_token);

				if (logger.isInfoEnabled())
					logger.info("Inspecting response of InsertObservation operation.");

				// throw exception if insertion was not successful
				checkResponse_insertObservation(sosResponse_insertObservation);

				if (logger.isInfoEnabled())
					logger.info("InsertObservationRequest succeeded.");
			}
		}
	}

	private void processInsertSensorRequest(URL SosURL, Document talsimDocument, String insertSensorRequestTemplate,
			String authorization_token) throws Exception, IOException {

		if (logger.isInfoEnabled())
			logger.info("Building InsertSensorRequest.");

		String insertSensorRequest = SosRequestConstructor.createInsertSensorRequest(talsimDocument,
				insertSensorRequestTemplate);

		if (logger.isInfoEnabled())
			logger.info("The following InsertSensorRequest was constructed: {}", insertSensorRequest);

		if (logger.isInfoEnabled())
			logger.info("Sending InsertSensorRequest.");

		String response_insertSensor = SosRequestSender.sendInsertSensorRequestToSOS(SosURL, insertSensorRequest,
				authorization_token);

		if (logger.isInfoEnabled())
			logger.info("The SOS instance sent the following response to the InsertSensorRequest: {}",
					response_insertSensor);

		// throw exception if insertion was not successful

		if (logger.isInfoEnabled())
			logger.info("Inspecting response of InsertSensor operation.");

		checkResponse_insertSensor(response_insertSensor);

		if (logger.isInfoEnabled())
			logger.info("InsertSensorRequest succeeded.");
	}

	private void checkResponse_insertSensor(String response_insertSensor) throws Exception {
		/*
		 * check if response contains the String "InsertSensorResponse"
		 * 
		 * If yes, then assume that request was accepted and insertion was
		 * successful
		 * 
		 * If no, assume that something went wrong and throw exception
		 */

		if (logger.isDebugEnabled())
			logger.debug(
					"Check if response of InsertSensor operation includes String '{}'. The response message is: {}",
					INSERT_SENSOR_RESPONSE_STRING, response_insertSensor);

		if (response_insertSensor.contains(INSERT_SENSOR_RESPONSE_STRING))
			return;
		else {
			if (logger.isErrorEnabled())
				logger.error("InsertSensorRequest failed! SOS instance returned the following response: {}",
						response_insertSensor);

			throw new Exception("InsertSensorRequest failed! SOS instance returned the following response: "
					+ response_insertSensor);
		}
	}

	private void checkResponse_insertObservation(String response_insertObservation) throws Exception {
		/*
		 * check if response contains th String "InsertObservationResponse"
		 * 
		 * If yes, then assume that request was accepted and insertion was
		 * successful
		 * 
		 * If no, assume that something went wrong and throw exception
		 */

		if (logger.isDebugEnabled())
			logger.debug(
					"Check if response of InsertObservation operation includes String '{}'. The response message is: {}",
					INSERT_OBSERVATION_RESPONSE_STRING, response_insertObservation);

		if (response_insertObservation.contains(INSERT_OBSERVATION_RESPONSE_STRING))
			return;
		else {
			if (logger.isErrorEnabled())
				logger.error("InsertObservationRequest failed! SOS instance returned the following response: {}",
						response_insertObservation);

			throw new Exception("InsertObservationRequest failed! SOS instance returned the following response: "
					+ response_insertObservation);
		}
	}

	private Document parseTalsimDocument(InputStream talsimOutput)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		Document talsimDocument = docBuilder.parse(talsimOutput);
		return talsimDocument;
	}

}
