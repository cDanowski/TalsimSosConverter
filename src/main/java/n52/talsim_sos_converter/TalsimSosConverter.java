package n52.talsim_sos_converter;

import java.io.IOException;
import java.io.InputStream;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import n52.talsim_sos_converter.helper.Constants;
import n52.talsim_sos_converter.helper.ResourceLoader;
import n52.talsim_sos_converter.helper.SosRequestConstructor;
import n52.talsim_sos_converter.helper.SosRequestSender;

public class TalsimSosConverter {

	/**
	 * Parses the TALSIM output/result and uses the transactional SOS methods to
	 * insert both the sensor and all included measurements to the SOS instance
	 * with the specified URL
	 * 
	 * @param talsimOutput
	 *            an {@link InputStream} of the TASLIM XML output including
	 *            sensor definition and measurements. However, it does not
	 *            include a spatial reference.
	 * @param SosURL
	 *            URL to the SOS-T (transactional SOS instance), to which the
	 *            data from {@code talsimOutput} should be transferred to
	 * @return <b>true</b> if insertion was successful, <b>false</b> otherwise
	 * @throws Exception
	 */
	public boolean insertOutputToSOS(InputStream talsimOutput, URL SosURL) throws Exception {

		/*
		 * parse TalsimResult.xml
		 */

		// create a new DocumentBuilderFactory
		Document talsimDocument = parseTalsimDocument(talsimOutput);

		// load template files
		String insertSensorRequestTemplate = ResourceLoader.loadInsertSensorRequestTemplate();
		String insertObservationRequestTemplate = ResourceLoader.loadInsertObservationRequestTemplate();

		/*
		 * create InsertSensor Request and send it to SOS-T
		 */
		int responseCode_insertSensor = processInsertSensorRequest(SosURL, talsimDocument, insertSensorRequestTemplate);

		/*
		 * process InsertObsrvation
		 */
		processInsertObservationRequests(SosURL, talsimDocument, insertObservationRequestTemplate);

		/*
		 * optional: implement check, whether insertion was successful TODO
		 * check, if response object is != ExceptionReport, or ==
		 * InsertObservationResponse or == InsertSensorResponse
		 */

		return true;
	}

	private void processInsertObservationRequests(URL SosURL, Document talsimDocument,
			String insertObservationRequestTemplate) throws Exception, ProtocolException, IOException {
		/*
		 * each series node contains information for one observableProperty,
		 * hence different request have to be set-up for different series nodes
		 */
		NodeList seriesNodes = talsimDocument.getElementsByTagName(Constants.TALSIM_SERIES_NODE);
		int numberOfSeriesNodes = seriesNodes.getLength();

		
		/*
		 * for each event in seriesNode: create InsertObservation requests
		 * and send them to SOS-T
		 */
		for (int i = 0; i < numberOfSeriesNodes; i++) {
			Node seriesNode = seriesNodes.item(i);
			
			List<String> insertObservationRequests = SosRequestConstructor.createInsertObservationRequestsForSeriesNode(talsimDocument,
					seriesNode, insertObservationRequestTemplate);
			
			for (String insertObservationRequest : insertObservationRequests) {
				int responseCode_insertObservation = SosRequestSender.sendInsertObservationRequestToSOS(SosURL,
						insertObservationRequest);
				
			}		
		}
	}

	private int processInsertSensorRequest(URL SosURL, Document talsimDocument, String insertSensorRequestTemplate)
			throws Exception, IOException {
		String insertSensorRequest = SosRequestConstructor.createInsertSensorRequest(talsimDocument,
				insertSensorRequestTemplate);

		int responseCode_insertSensor = SosRequestSender.sendInsertSensorRequestToSOS(SosURL, insertSensorRequest);

		return responseCode_insertSensor;
	}

	private Document parseTalsimDocument(InputStream talsimOutput)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		Document talsimDocument = docBuilder.parse(talsimOutput);
		return talsimDocument;
	}

}
