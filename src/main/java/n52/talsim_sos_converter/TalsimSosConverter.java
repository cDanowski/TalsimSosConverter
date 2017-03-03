package n52.talsim_sos_converter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import n52.talsim_sos_converter.helper.Constants;
import n52.talsim_sos_converter.helper.SosRequestConstructor;
import n52.talsim_sos_converter.helper.SosRequestSender;
import n52.talsim_sos_converter.helper.ResourceLoader;

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
		String insertSensorRequest = SosRequestConstructor.createInsertSensorRequest(talsimDocument,
				insertSensorRequestTemplate);

		System.out.println(insertSensorRequest);

		int responseCode_insertSensor = SosRequestSender.sendInsertSensorRequestToSOS(SosURL, insertSensorRequest);

		/*
		 * for each event in TalsimResult: create InsertObservation and send it
		 * to SOS-T
		 */
		NodeList talsim_eventNodes = talsimDocument.getElementsByTagName(Constants.TALSIM_RESULT_EVENT_NODE);
		int numberOfEventNodes = talsim_eventNodes.getLength();

		for (int index = 0; index < numberOfEventNodes; index++) {
			Node currentTalsimEventNode = talsim_eventNodes.item(index);

			String insertObservationTemplate_copy = insertObservationRequestTemplate;

			String insertObservationRequest = SosRequestConstructor.createInsertObservationRequest(talsimDocument,
					currentTalsimEventNode, insertObservationTemplate_copy);

			// if(index == 1){
			// System.out.println(insertObservationRequest);
			// }

			int responseCode_insertObservation = SosRequestSender.sendInsertObservationRequestToSOS(SosURL,
					insertObservationRequest);
		}

		/*
		 * optional: implement check, whether insertion was successful
		 */

		return true;
	}

	private Document parseTalsimDocument(InputStream talsimOutput)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		Document talsimDocument = docBuilder.parse(talsimOutput);
		return talsimDocument;
	}

}
