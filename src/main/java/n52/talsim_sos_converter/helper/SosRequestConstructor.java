package n52.talsim_sos_converter.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Used to parse the relevant contents from the {@code TalsimResult Document}
 * and fill the associated SOS request templates with its values. It thus
 * produces fully usable SOS requests.
 * 
 * @author Christian Danowski-Buhren (contact: c.danowski@52north.org)
 *
 */
public class SosRequestConstructor {

	private static Logger logger = LoggerFactory.getLogger(SosRequestConstructor.class);

	/**
	 * Extracts the relevant parameters for a {@code SOS InsertSensor request}
	 * from {@code talsimDocument} and replaces the <i>placeholders</i> within
	 * the {@code insertSensorTemplate}. As a result, the method returns a fully
	 * usable {@code SOS InsertSensor request} as String that can be send to a
	 * transactional SOS instance. If any parameter cannot be extracted from
	 * {@code talsimDocument} then it is either generated or assumed as constant
	 * value (constant definitions are included in {@link Constants}).
	 * 
	 * The relevant parameters are:
	 * 
	 * <style type="text/css"> .tg {border-collapse:collapse;border-spacing:0;}
	 * .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg th{font-family:Arial,
	 * sans-serif;font-size:14px;font-weight:normal;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg .tg-baqh{text-align:center;vertical-align:top} .tg
	 * .tg-amwm{font-weight:bold;text-align:center;vertical-align:top} .tg
	 * .tg-yw4l{vertical-align:top} </style>
	 * <table class="tg">
	 * <tr>
	 * <th class="tg-amwm">SOS InsertObservation Parameter</th>
	 * <th class="tg-amwm">Component within TalsimResult Document</th>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Offering Identifier</td>
	 * <td class="tg-baqh">none - constant value
	 * {@link Constants#OFFERING_IDENTIFIER_VALUE}</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Phenomenon Time</td>
	 * <td class="tg-baqh">value of attributes "date" and "time" of a single
	 * "event" node</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Procedure Identifier</td>
	 * <td class="tg-baqh">value of node "stationName"</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Observable Property</td>
	 * <td class="tg-baqh">as observable property for process inputs the
	 * constant value {@link Constants#OBSERVABLE_PROPERTY_INPUT_VALUE} is used.
	 * <br/>
	 * <br/>
	 * As for the output observable properties, each "series" node of
	 * TalsimResult.xml is mapped to an individual observable property depending
	 * on the value of node "parameterId". It indicates the type of output as
	 * follows:
	 * 
	 * <style type="text/css"> .tg {border-collapse:collapse;border-spacing:0;}
	 * .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg th{font-family:Arial,
	 * sans-serif;font-size:14px;font-weight:normal;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg .tg-baqh{text-align:center;vertical-align:top} .tg
	 * .tg-amwm{font-weight:bold;text-align:center;vertical-align:top} </style>
	 * <table class="tg">
	 * <tr>
	 * <th class="tg-amwm">parameterId</th>
	 * <th class="tg-amwm">observable property</th>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">1ZU</td>
	 * <td class="tg-baqh">Zufluss</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">VOL</td>
	 * <td class="tg-baqh">Volumen</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">WSP</td>
	 * <td class="tg-baqh">Wasserstand</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">QA1</td>
	 * <td class="tg-baqh">Abgabe</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">QH1</td>
	 * <td class="tg-baqh">Hochwasserentlastung</td>
	 * </tr>
	 * </table>
	 * 
	 * </td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Feature of Interest Identifier</td>
	 * <td class="tg-baqh">none - constant value - TBD</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Station Location Latitude</td>
	 * <td class="tg-baqh">TBD</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Station Location Longitude</td>
	 * <td class="tg-baqh">TBD</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Station Location Altitude</td>
	 * <td class="tg-baqh">TBD</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Unit of Measure (UOM)</td>
	 * <td class="tg-baqh">value of node "units"</td>
	 * </tr>
	 * </table>
	 * 
	 * @param talsimDocument
	 *            the parsed contents of a {@code TalsimResult document}
	 * @param insertSensorTemplate
	 *            a String representation of an
	 *            {@code InsertSensor request template} containing several
	 *            <i>placeholders</i> that will be replaced by the contents from
	 *            {@code talsimDocument}
	 * @return a fully usable {@code SOS InsertSensor request} as String that
	 *         can be send to a transactional SOS instance
	 * @throws Exception
	 */
	public static String createInsertSensorRequest(Document talsimDocument, String insertSensorTemplate)
			throws Exception {
		/*
		 * extract the required information from talsimDocument
		 */

		if (logger.isDebugEnabled())
			logger.debug("Extracting InsertSensor parameters from TalsimResult and other constant definitions.");

		Map<String, String> talsimInsertSensorParameters = createInsertSensorParametersMap(talsimDocument);

		if (logger.isDebugEnabled())
			logger.debug("Following parameters for InsertSensorRequest were extracted from TalsimResult: '{}'",
					talsimInsertSensorParameters);

		if (logger.isDebugEnabled())
			logger.debug(
					"Replace all placeholders within InsertSensor template with extracted parameters and other constant definitions.");

		String insertSensorRequest = replacePlaceholdersInTemplate(insertSensorTemplate, talsimInsertSensorParameters);

		return insertSensorRequest;
	}

	/**
	 * Extracts the relevant parameters for a
	 * {@code SOS InsertObservation request} and replaces the
	 * <i>placeholders</i> within the {@code insertObservationTemplate}. As a
	 * result, the method returns a fully usable
	 * {@code SOS InsertSensor request} as String that can be send to a
	 * transactional SOS instance. If any parameter cannot be extracted from the
	 * Talsim output then it is either generated or assumed as constant value
	 * (constant definitions are included in {@link Constants}).
	 * 
	 * The relevant parameters are:
	 * 
	 * 
	 * 
	 * <style type="text/css"> .tg {border-collapse:collapse;border-spacing:0;}
	 * .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg th{font-family:Arial,
	 * sans-serif;font-size:14px;font-weight:normal;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg .tg-baqh{text-align:center;vertical-align:top} .tg
	 * .tg-amwm{font-weight:bold;text-align:center;vertical-align:top} .tg
	 * .tg-yw4l{vertical-align:top} </style>
	 * <table class="tg">
	 * <tr>
	 * <th class="tg-amwm">SOS InsertObservation Parameter</th>
	 * <th class="tg-amwm">Component within TalsimResult Document</th>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Offering Identifier</td>
	 * <td class="tg-baqh">none - constant value
	 * {@link Constants#OFFERING_IDENTIFIER_VALUE}</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Observation Identifier</td>
	 * <td class="tg-baqh">none - generated from procedure, observable property
	 * and phenomenon time</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Phenomenon Time</td>
	 * <td class="tg-baqh">value of attributes "date" and "time" of a single
	 * "event" node</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Procedure Identifier</td>
	 * <td class="tg-baqh">value of node "stationName"</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Observable Property</td>
	 * <td class="tg-baqh">value depends on the value of node "parameterId"
	 * within the "header" node of the associated "series" node. It indicates
	 * the type of output as follows:
	 * 
	 * <style type="text/css"> .tg {border-collapse:collapse;border-spacing:0;}
	 * .tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg th{font-family:Arial,
	 * sans-serif;font-size:14px;font-weight:normal;padding:10px
	 * 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal
	 * ;} .tg .tg-baqh{text-align:center;vertical-align:top} .tg
	 * .tg-amwm{font-weight:bold;text-align:center;vertical-align:top} </style>
	 * <table class="tg">
	 * <tr>
	 * <th class="tg-amwm">parameterId</th>
	 * <th class="tg-amwm">observable property</th>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">1ZU</td>
	 * <td class="tg-baqh">Zufluss</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">VOL</td>
	 * <td class="tg-baqh">Volumen</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">WSP</td>
	 * <td class="tg-baqh">Wasserstand</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">QA1</td>
	 * <td class="tg-baqh">Abgabe</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">QH1</td>
	 * <td class="tg-baqh">Hochwasserentlastung</td>
	 * </tr>
	 * </table>
	 * 
	 * </td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Feature of Interest Identifier (sampled Feature)</td>
	 * <td class="tg-baqh">none - constant value - TBD</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Feature of Interest Identifier (sampling Feature)
	 * </td>
	 * <td class="tg-baqh">none - constant value - TBD</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Sampling Feature Latitude</td>
	 * <td class="tg-baqh">use station location</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Sampling Feature Longitude</td>
	 * <td class="tg-baqh">use station location</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Unit of Measure (UOM)</td>
	 * <td class="tg-baqh">value of node "units"</td>
	 * </tr>
	 * <tr>
	 * <td class="tg-baqh">Result Value</td>
	 * <td class="tg-baqh">value of attribute "value" of node "units"</td>
	 * </tr>
	 * </table>
	 * 
	 * @param currentTalsimEventNode
	 *            the {@code TALSIM Event node instance}, for which an
	 *            {@code InsertObservation request} is constructed. It comprises
	 *            relevant information such as <i>event/measurement/phenomenon
	 *            time</i> and <i>measurement value</i>
	 * @param headerNode
	 *            the "header" node associated to the
	 *            {@code currentTalsimEventNode}
	 * 
	 * @param timeZone
	 *            a String value representing the {@code timeZone} parameter
	 *            from TalsimResult.xml
	 * 
	 * @param insertObservationTemplate
	 *            a String representation of an
	 *            {@code InsertObservation request template} containing several
	 *            <i>placeholders</i> that will be replaced by the contents from
	 *            {@code talsimDocument}
	 * @return
	 * @throws Exception
	 */
	public static String createInsertObservationRequest(Node currentTalsimEventNode, Node headerNode, String timeZone,
			String insertObservationTemplate) throws Exception {

		/*
		 * extract the required information from talsimDocument
		 */

		if (logger.isDebugEnabled())
			logger.debug("Extracting InsertObservation parameters from TalsimResult and other constant definitions.");

		if (logger.isDebugEnabled())
			logger.debug("Current 'header' node contents: {}", headerNode);

		if (logger.isDebugEnabled())
			logger.debug("Current 'event' node contents: {}", currentTalsimEventNode);

		Map<String, String> talsimInsertObservationParameters = createInsertObservationParametersMap(headerNode,
				currentTalsimEventNode, timeZone);

		if (logger.isDebugEnabled())
			logger.debug("Following parameters for InsertObservationRequest were extracted from TalsimResult: '{}'",
					talsimInsertObservationParameters);

		if (logger.isDebugEnabled())
			logger.debug(
					"Replace all placeholders within InsertObservation template with extracted parameters and other constant definitions.");

		String insertObservationRequest = replacePlaceholdersInTemplate(insertObservationTemplate,
				talsimInsertObservationParameters);

		return insertObservationRequest;
	}

	/**
	 * Creates and Collects all InserObservation requests for each {@code event}
	 * node within the given {@code series} node.
	 * 
	 * @param talsimDocument
	 *            the whole Talsim output document
	 * @param seriesNode
	 *            the "series" node that contains all relevant "event" nodes
	 * @param insertObservationRequestTemplate
	 *            a String representation of an
	 *            {@code InsertObservation request template} containing several
	 *            <i>placeholders</i> that will be replaced by the contents from
	 *            {@code talsimDocument}
	 * @return a list of all InsertObservation requests associated to the given
	 *         "series" node
	 * @throws Exception
	 */
	public static List<String> createInsertObservationRequestsForSeriesNode(Document talsimDocument, Node seriesNode,
			String insertObservationRequestTemplate) throws Exception {

		List<String> insertObservationRequests = new ArrayList<>();

		/*
		 * extract header node
		 */
		Node headerNode = extractHeaderNodeFromSeriesNode(seriesNode);

		/*
		 * extract timeZone parameter
		 */
		String timeZone = extractSingleNodeValueFromDocument(talsimDocument, Constants.TALSIM_RESULT_TIME_ZONE_NODE);

		/*
		 * extract event nodes
		 */
		List<Node> eventNodes = extractEventNodesFromSeriesNode(seriesNode);

		if (logger.isInfoEnabled())
			logger.info("Number of 'event' nodes = Number of InsertObservationRequests for current 'series' node: {}",
					eventNodes.size());

		/*
		 * extract all event nodes
		 */

		int currentIndex = 0;

		for (Node currentTalsimEventNode : eventNodes) {

			String insertObservationTemplate_copy = insertObservationRequestTemplate;

			if (logger.isInfoEnabled())
				logger.info("Building InsertObservationRequest #{}", currentIndex);

			String insertObservationRequest = SosRequestConstructor.createInsertObservationRequest(
					currentTalsimEventNode, headerNode, timeZone, insertObservationTemplate_copy);

			if (logger.isDebugEnabled())
				logger.debug("Following InsertObservationRequest was constructed: {}", insertObservationRequest);

			insertObservationRequests.add(insertObservationRequest);

			currentIndex++;
		}

		return insertObservationRequests;
	}

	private static String replacePlaceholdersInTemplate(String requestTemplate, Map<String, String> talsimParameters) {

		String request = requestTemplate;

		Set<Entry<String, String>> talsimParameterEntries = talsimParameters.entrySet();

		for (Entry<String, String> entry : talsimParameterEntries) {
			String parameterName = entry.getKey();
			String parameterValue = entry.getValue();

			if (logger.isDebugEnabled())
				logger.debug("Replace all occurences of '{}' with value '{}'", parameterName, parameterValue);

			request = request.replaceAll(parameterName, parameterValue);
		}

		return request;
	}

	private static Map<String, String> createInsertSensorParametersMap(Document talsimDocument) throws Exception {

		/*
		 * create a map with all sensor parameters
		 */

		Map<String, String> insertSensorParameters = new HashMap<String, String>();

		/*
		 * each series node contains information for one observableProperty
		 */
		NodeList seriesNodes = talsimDocument.getElementsByTagName(Constants.TALSIM_SERIES_NODE);
		int numberOfSeriesNodes = seriesNodes.getLength();

		for (int i = 0; i < numberOfSeriesNodes; i++) {
			Node seriesNode = seriesNodes.item(i);

			addObservablePropertyParameters_insertSensor(seriesNode, insertSensorParameters);

		}

		// STATIC SINGLE INPUT OBSERVABLE PROPERTY
		insertSensorParameters.put(Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_INPUT_NAME_PLACEHOLDER,
				Constants.OBSERVABLE_PROPERTY_INPUT_NAME);
		insertSensorParameters.put(Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_INPUT_VALUE_PLACEHOLDER,
				Constants.OBSERVABLE_PROPERTY_INPUT_VALUE);

		Node exemplarHeaderNode = extractHeaderNodeFromSeriesNode(seriesNodes.item(0));

		// STATION NAME
		String stationName = extractSingleNodeValueFromHeaderSection(exemplarHeaderNode,
				Constants.TALSIM_RESULT_STATION_NAME_NODE);
		insertSensorParameters.put(Constants.INSERT_SENSOR_STATION_IDENTIFIER_PLACEHOLDER, stationName);

		// STATION POSITION
		// TODO FIXME replace with real position that is retrieved from
		// stationMetadata
		String station_lon_in_deg = Constants.STATION_LON_IN_DEG;
		String station_lat_in_deg = Constants.STATION_LAT_IN_DEG;
		String station_alt_in_meters = Constants.STATION_ALT_IN_METERS;
		insertSensorParameters.put(Constants.INSERT_SENSOR_STATION_POSITION_LON_IN_DEG_PLACEHOLDER, station_lon_in_deg);
		insertSensorParameters.put(Constants.INSERT_SENSOR_STATION_POSITION_LAT_IN_DEG_PLACEHOLDER, station_lat_in_deg);
		insertSensorParameters.put(Constants.INSERT_SENSOR_STATION_POSITION_ALT_IN_METERS_PLACEHOLDER,
				station_alt_in_meters);

		// FEATURE OF INTEREST
		// TODO FIXME replace FOI with the real value --> ask Benjamin/Christoph
		insertSensorParameters.put(Constants.INSERT_SENSOR_FEATURE_OF_INTEREST_IDENTIFIER_PLACEHOLDER,
				Constants.FEATURE_OF_INTEREST_SAMPLING_FEATURE);

		/*
		 * OFFERING
		 */
		insertSensorParameters.put(Constants.INSERT_SENSOR_OFFERING_IDENTIFIER_NAME_PLACEHOLDER,
				Constants.OFFERING_IDENTIFIER_NAME);
		insertSensorParameters.put(Constants.INSERT_SENSOR_OFFERING_IDENTIFIER_VALUE_PLACEHOLDER,
				Constants.OFFERING_IDENTIFIER_VALUE);

		return insertSensorParameters;
	}

	private static void addObservablePropertyParameters_insertSensor(Node seriesNode,
			Map<String, String> insertSensorParameters) throws Exception {

		/*
		 * in TalsimResult.xml file each "series" node consists of exactly one
		 * "header" node and multiple "event" nodes. With regard to InsertSensor
		 * requests, only the information from the "header" node is relevant.
		 * 
		 * "header" should be the first child node of "series"
		 */

		Node headerNode = extractHeaderNodeFromSeriesNode(seriesNode);

		String obsProp_name_placeholder = "";
		String obsProp_value_placeholder = "";
		String obsProp_uom_placeholder = "";

		String obsProp_name_value = "";
		String obsProp_value_value = "";
		String obsProp_uom_value = "";

		String parameterID = extractParameterIdFromHeader(headerNode);

		obsProp_uom_value = extractSingleNodeValueFromHeaderSection(headerNode, Constants.TALSIM_RESULT_UNITS_UOM_NODE);

		switch (parameterID) {
		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_1ZU:
			obsProp_name_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_1ZU;
			obsProp_value_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_1ZU;
			obsProp_uom_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_1ZU;

			obsProp_name_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_NAME_1ZU;
			obsProp_value_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_1ZU;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_VOL:
			obsProp_name_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_VOL;
			obsProp_value_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_VOL;
			obsProp_uom_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_VOL;

			obsProp_name_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_NAME_VOL;
			obsProp_value_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_VOL;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_WSP:
			obsProp_name_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_WSP;
			obsProp_value_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_WSP;
			obsProp_uom_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_WSP;

			obsProp_name_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_NAME_WSP;
			obsProp_value_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_WSP;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_QA1:
			obsProp_name_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_QA1;
			obsProp_value_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_QA1;
			obsProp_uom_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_QA1;

			obsProp_name_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_NAME_QA1;
			obsProp_value_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_QA1;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_QH1:
			obsProp_name_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_QH1;
			obsProp_value_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_QH1;
			obsProp_uom_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_QH1;

			obsProp_name_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_NAME_QH1;
			obsProp_value_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_QH1;
			break;

		default:
			obsProp_name_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_1ZU;
			obsProp_value_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_1ZU;
			obsProp_uom_placeholder = Constants.INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_1ZU;

			obsProp_name_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_NAME_1ZU;
			obsProp_value_value = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_1ZU;
			break;
		}

		/*
		 * OBSERVABLE PROPERTY
		 */

		insertSensorParameters.put(obsProp_name_placeholder, obsProp_name_value);
		insertSensorParameters.put(obsProp_value_placeholder, obsProp_value_value);

		// UOM
		insertSensorParameters.put(obsProp_uom_placeholder, obsProp_uom_value);

	}

	private static Map<String, String> createInsertObservationParametersMap(Node headerNode, Node talsimEventNode,
			String timeZone) throws Exception {

		/*
		 * create a map with all sensor parameters
		 */

		Map<String, String> insertObservationParameters = new HashMap<String, String>();

		// STATION NAME
		String stationName = extractSingleNodeValueFromHeaderSection(headerNode,
				Constants.TALSIM_RESULT_STATION_NAME_NODE);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_PROCEDURE_IDENTIFIER_PLACEHOLDER, stationName);

		// EVENT DATE AND TIME
		String eventDate_date = extractSingleAttributeValueFromEventNode(talsimEventNode,
				Constants.TALSIM_RESULT_EVENT_DATE_ATTRIBUTE);
		String eventDate_time = extractSingleAttributeValueFromEventNode(talsimEventNode,
				Constants.TALSIM_RESULT_EVENT_TIME_ATTRIBUTE);

		String startDateAndTimeForRequest = generateDateAndTimeString(eventDate_date, eventDate_time, timeZone);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_PHENOMENON_TIME_PLACEHOLDER,
				startDateAndTimeForRequest);

		// UOM
		String uom = extractSingleNodeValueFromHeaderSection(headerNode, Constants.TALSIM_RESULT_UNITS_UOM_NODE);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_UOM_NAME_PLACEHOLDER, uom);

		/*
		 * OBSERVABLE PROPERTY
		 * 
		 * depends on the value of the "parameterId" node within the "header"
		 * node
		 */
		String observableProperty = deriveObservablePropertyFromParameterId(headerNode);

		insertObservationParameters.put(Constants.INSERT_OBSERVATION_OBSERVABLE_PROPERTY_IDENTIFIER_PLACEHOLDER,
				observableProperty);

		// STATION POSITION
		// TODO FIXME replace with real position that is retrieved from
		// stationMetadata
		String station_lon_in_deg = Constants.STATION_LON_IN_DEG;
		String station_lat_in_deg = Constants.STATION_LAT_IN_DEG;
		String station_alt_in_meters = Constants.STATION_ALT_IN_METERS;
		insertObservationParameters.put(
				Constants.INSERT_OBSERVATION_FEATURE_OF_INTEREST_POSITION_LON_IN_DEG_PLACEHOLDER, station_lon_in_deg);
		insertObservationParameters.put(
				Constants.INSERT_OBSERVATION_FEATURE_OF_INTEREST_POSITION_LAT_IN_DEG_PLACEHOLDER, station_lat_in_deg);
		// insertObservationParameters.put(, station_alt_in_meters);

		// TODO FIXME replace FOI with the real value --> ask Benjamin/Christoph
		insertObservationParameters.put(
				Constants.INSERT_OBSERVATION_FEATURE_OF_INTEREST_IDENTIFIER_SAMPLING_FEATURE_PLACEHOLDER,
				Constants.FEATURE_OF_INTEREST_SAMPLING_FEATURE);
		insertObservationParameters.put(
				Constants.INSERT_OBSERVATION_FEATURE_OF_INTEREST_IDENTIFIER_SAMPLED_FEATURE_PLACEHOLDER,
				Constants.FEATURE_OF_INTEREST_SAMPLED_FEATURE);

		/*
		 * OFFERING
		 */
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_OFFERING_IDENTIFIER_PLACEHOLDER,
				Constants.OFFERING_IDENTIFIER_NAME);

		// OBSERVATION IDENTIFIER
		String observationId = generateObservationIdentifier(stationName, observableProperty,
				startDateAndTimeForRequest);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_OBSERVATION_IDENTIFIER_PLACEHOLDER, observationId);

		// RESULT VALUE
		String resultValue = extractSingleAttributeValueFromEventNode(talsimEventNode,
				Constants.TALSIM_RESULT_EVENT_VALUE_ATTRIBUTE);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_RESULT_VALUE_PLACEHOLDER, resultValue);

		return insertObservationParameters;
	}

	private static String deriveObservablePropertyFromParameterId(Node headerNode) throws Exception {
		String observableProperty = "";
		String parameterID = extractParameterIdFromHeader(headerNode);

		switch (parameterID) {
		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_1ZU:

			observableProperty = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_1ZU;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_VOL:
			observableProperty = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_VOL;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_WSP:
			observableProperty = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_WSP;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_QA1:
			observableProperty = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_QA1;
			break;

		case Constants.TALSIM_OUTPUT_PARAMETER_IDENTIFIER_QH1:
			observableProperty = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_QH1;
			break;

		default:
			observableProperty = Constants.OBSERVABLE_PROPERTY_OUTPUT_VALUE_1ZU;
			break;
		}
		return observableProperty;
	}

	private static String generateDateAndTimeString(String date, String time, String timeZone) {
		// TODO Auto-generated method stub

		/*
		 * date String looks like: 2014-02-10 (yyyy-mm-dd), where month goes
		 * from 01-12 and day from 01-31!
		 * 
		 * time String looks like: 00:15:00 (hh-mm-ss)
		 * 
		 */
		String[] dateComponents = date.split("-");
		int year = Integer.parseInt(dateComponents[0]);
		int month = Integer.parseInt(dateComponents[1]);
		int day = Integer.parseInt(dateComponents[2]);

		String[] timeComponents = time.split(":");
		int hours = Integer.parseInt(timeComponents[0]);
		int minutes = Integer.parseInt(timeComponents[1]);
		int seconds = Integer.parseInt(timeComponents[2]);

		// TIMEZONE
		// parse values from timeZone String!
		// int timeZoneHours =
		//
		// DateTimeZone dateTimeZone = DateTimeZone.forOffsetHoursMinutes(arg0,
		// arg1);

		// TODO set time zone correctly!!!!

		DateTime dateTime = new DateTime(year, month, day, hours, minutes, seconds, DateTimeZone.UTC);

		String dateTimeString = dateTime.toString();

		return dateTimeString;
	}

	private static String generateObservationIdentifier(String stationName, String observableProperty,
			String startDateAndTimeForRequest) {
		// TODO FIXME construct unique identifier!

		String observationId = stationName + "_" + observableProperty + "_" + startDateAndTimeForRequest;

		return observationId;
	}

	private static String extractParameterIdFromHeader(Node headerNode) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("Extracting value of node '{}' within current 'header' node '{}'",
					Constants.TALSIM_RESULT_PARAMETER_ID_NODE, headerNode);

		NodeList childNodes = headerNode.getChildNodes();
		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(Constants.TALSIM_RESULT_PARAMETER_ID_NODE)) {
				// return its value
				String parameterId = currentNode.getTextContent();

				if (logger.isDebugEnabled())
					logger.debug("Extracted ParameterIdentifier: '{}'", parameterId);

				return parameterId;
			}
		}

		if (logger.isErrorEnabled())
			logger.error(
					"No 'parameterId' node could be found within the 'header' section of TALSIM_Document! Current 'header' node: {}",
					headerNode);

		throw new Exception("No 'parameterId' node could be found within the 'header' section of TALSIM_Document!");

	}

	private static Node extractHeaderNodeFromSeriesNode(Node seriesNode) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("Extracting 'header' node from current 'series' node.");

		NodeList childNodes = seriesNode.getChildNodes();

		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(Constants.TALSIM_HEADER_NODE)) {

				if (logger.isDebugEnabled())
					logger.debug("Extracted 'header' node: '{}'", currentNode);

				// return its value
				return currentNode;
			}
		}

		if (logger.isErrorEnabled())
			logger.error(
					"No 'header' node could be found within the 'series' section of TALSIM_Document! Current 'series' node: {}",
					seriesNode);

		throw new Exception("No 'header' node could be found within the 'series' section of TALSIM_Document!");
	}

	private static List<Node> extractEventNodesFromSeriesNode(Node seriesNode) {

		if (logger.isDebugEnabled())
			logger.debug("Extracting all 'event' node from current 'series' node.");

		List<Node> eventNodes = new ArrayList<Node>();

		NodeList childNodes = seriesNode.getChildNodes();

		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(Constants.TALSIM_RESULT_EVENT_NODE)) {
				// return its value
				eventNodes.add(currentNode);
			}
		}

		return eventNodes;
	}

	private static String extractSingleNodeValueFromHeaderSection(Node headerNode, String tagName) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("Extracting value of node named '{}' from current 'header' node '{}'.", tagName, headerNode);

		NodeList childNodes = headerNode.getChildNodes();
		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(tagName)) {

				// return its value
				String nodeValue = currentNode.getTextContent();

				if (logger.isDebugEnabled())
					logger.debug("Extracted '{}' node value: '{}'", tagName, nodeValue);

				return nodeValue;
			}
		}

		if (logger.isErrorEnabled())
			logger.error(
					"No Node named '{}' could be found within the 'header' section of TALSIM_Document! Current 'header' node: {}",
					tagName, headerNode);

		throw new Exception(
				"No Node for '" + tagName + "' could be found within the 'header' section of TALSIM_Document!");
	}

	private static String extractSingleAttributeValueFromHeaderSection(Node headerNode, String tagName,
			String attributeName) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug(
					"Extracting value of attribute named '{}' of node named '{}' within current 'header' node '{}'.",
					tagName, attributeName, headerNode);

		NodeList childNodes = headerNode.getChildNodes();
		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(tagName)) {
				// find attribute and return its value
				NamedNodeMap attributes = currentNode.getAttributes();

				int numberOfAttributes = attributes.getLength();

				for (int k = 0; k < numberOfAttributes; k++) {
					Node currentAttribute = attributes.item(k);
					String attributeName_current = currentAttribute.getNodeName();
					if (attributeName_current.equals(attributeName)) {
						// return attribute value
						String attributeValue = currentAttribute.getTextContent();

						if (logger.isDebugEnabled())
							logger.debug("Extracted '{}' attribute value: '{}'", attributeName, attributeValue);

						return attributeValue;
					}
				}
			}
		}

		if (logger.isErrorEnabled())
			logger.error(
					"No Node named '{}' with attribute '{}' could be found within the 'header' section of TALSIM_Document! Current 'header' node: {}",
					tagName, attributeName, headerNode);

		throw new Exception("No Node '" + tagName + "' with attribute '" + attributeName
				+ "' could be found within the 'header' section of TALSIM_Document!");
	}

	private static String extractSingleAttributeValueFromEventNode(Node talsimEventNode, String attributeName)
			throws Exception {
		
		if (logger.isDebugEnabled())
			logger.debug(
					"Extracting value of attribute named '{}' of 'event' node '{}'.",
					attributeName, talsimEventNode);

		NamedNodeMap attributes = talsimEventNode.getAttributes();

		int numberOfAttributes = attributes.getLength();

		for (int k = 0; k < numberOfAttributes; k++) {
			Node currentAttribute = attributes.item(k);
			String attributeName_current = currentAttribute.getNodeName();
			if (attributeName_current.equals(attributeName)) {
				// return attribute value
				String attributeValue = currentAttribute.getTextContent();
				
				if (logger.isDebugEnabled())
					logger.debug("Extracted '{}' attribute value: '{}'", attributeName, attributeValue);
				
				return attributeValue;
			}
		}

		if (logger.isErrorEnabled())
			logger.error(
					"No attribute named '{}' could be found within the 'event' node of TALSIM_Document! Current 'event' node: {}",
					attributeName, talsimEventNode);

		throw new Exception(
				"No attribute '" + attributeName + "' could be found within the 'event' node of TALSIM_Document!");
	}

	private static String extractSingleNodeValueFromDocument(Document talsimDocument, String tagName) {
		NodeList elementsByTagName = talsimDocument.getElementsByTagName(tagName);
		Node item = elementsByTagName.item(0);
		String stationName = item.getTextContent();
		return stationName;
	}

}
