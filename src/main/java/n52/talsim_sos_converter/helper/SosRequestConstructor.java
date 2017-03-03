package n52.talsim_sos_converter.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
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
	 * <td class="tg-baqh">none - constant value "Wasserdurchfluss"</td>
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
	 * <td class="tg-baqh">none - constant value "Wasserdurchfluss"</td>
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

		Map<String, String> talsimInsertSensorParameters = createInsertSensorParametersMap(talsimDocument);

		String insertSensorRequest = replacePlaceholdersInTemplate(insertSensorTemplate, talsimInsertSensorParameters);

		return insertSensorRequest;
	}

	/**
	 * Extracts the relevant parameters for a
	 * {@code SOS InsertObservation request} from {@code talsimDocument} and
	 * {@code currentTalsimEventNode} and replaces the <i>placeholders</i>
	 * within the {@code insertObservationTemplate}. As a result, the method
	 * returns a fully usable {@code SOS InsertSensor request} as String that
	 * can be send to a transactional SOS instance. If any parameter cannot be
	 * extracted from {@code talsimDocument} then it is either generated or
	 * assumed as constant value (constant definitions are included in
	 * {@link Constants}).
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
	 * <td class="tg-baqh">none - constant value "Wasserdurchfluss"</td>
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
	 * <td class="tg-baqh">none - constant value "Wasserdurchfluss"</td>
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
	 * @param talsimDocument
	 *            the parsed contents of a {@code TalsimResult document}
	 * @param currentTalsimEventNode
	 *            the {@code TALSIM Event node instance}, for which an
	 *            {@code InsertObservation request} is constructed. It comprises
	 *            relevant information such as <i>event/measurement/phenomenon
	 *            time</i> and <i>measurement value</i>
	 * 
	 * @param insertObservationTemplate
	 *            a String representation of an
	 *            {@code InsertObservation request template} containing several
	 *            <i>placeholders</i> that will be replaced by the contents from
	 *            {@code talsimDocument}
	 * @return
	 * @throws Exception
	 */
	public static String createInsertObservationRequest(Document talsimDocument, Node currentTalsimEventNode,
			String insertObservationTemplate) throws Exception {

		/*
		 * extract the required information from talsimDocument
		 */

		Map<String, String> talsimInsertObservationParameters = createInsertObservationParametersMap(talsimDocument,
				currentTalsimEventNode);

		String insertObservationRequest = replacePlaceholdersInTemplate(insertObservationTemplate,
				talsimInsertObservationParameters);

		return insertObservationRequest;
	}

	private static String replacePlaceholdersInTemplate(String requestTemplate, Map<String, String> talsimParameters) {

		String request = requestTemplate;

		Set<Entry<String, String>> talsimParameterEntries = talsimParameters.entrySet();

		for (Entry<String, String> entry : talsimParameterEntries) {
			String parameterName = entry.getKey();
			String parameterValue = entry.getValue();
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

			addObservablePropertyParameters(seriesNode, insertSensorParameters);

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

	private static void addObservablePropertyParameters(Node seriesNode, Map<String, String> insertSensorParameters)
			throws Exception {

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

	

	private static Map<String, String> createInsertObservationParametersMap(Document talsimDocument,
			Node talsimEventNode) throws Exception {

		/*
		 * create a map with all sensor parameters
		 */

		Map<String, String> insertObservationParameters = new HashMap<String, String>();

		Node headerNode = talsimDocument.getElementsByTagName(Constants.TALSIM_HEADER_NODE).item(0);

		// STATION NAME
		String stationName = extractSingleNodeValueFromHeaderSection(headerNode,
				Constants.TALSIM_RESULT_STATION_NAME_NODE);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_PROCEDURE_IDENTIFIER_PLACEHOLDER, stationName);

		String timeZone = extractSingleNodeValueFromDocument(talsimDocument, Constants.TALSIM_RESULT_TIME_ZONE_NODE);

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
		 * OBSERVABLE PROPERTY
		 */
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_OBSERVABLE_PROPERTY_IDENTIFIER_PLACEHOLDER,
				Constants.OBSERVABLE_PROPERTY_INPUT_VALUE);

		/*
		 * OFFERING
		 */
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_OFFERING_IDENTIFIER_PLACEHOLDER,
				Constants.OFFERING_IDENTIFIER_NAME);

		// OBSERVATION IDENTIFIER
		String observationId = generateObservationIdentifier(stationName, Constants.OBSERVABLE_PROPERTY_INPUT_VALUE,
				startDateAndTimeForRequest);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_OBSERVATION_IDENTIFIER_PLACEHOLDER, observationId);

		// RESULT VALUE
		String resultValue = extractSingleAttributeValueFromEventNode(talsimEventNode,
				Constants.TALSIM_RESULT_EVENT_VALUE_ATTRIBUTE);
		insertObservationParameters.put(Constants.INSERT_OBSERVATION_RESULT_VALUE_PLACEHOLDER, resultValue);

		return insertObservationParameters;
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

		NodeList childNodes = headerNode.getChildNodes();
		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(Constants.TALSIM_RESULT_PARAMETER_ID_NODE)) {
				// return its value
				return currentNode.getTextContent();
			}
		}

		throw new Exception("No `parameterId` node could be found within the 'header' section of TALSIM_Document!");

	}
	
	private static Node extractHeaderNodeFromSeriesNode(Node seriesNode) throws Exception {
		NodeList childNodes = seriesNode.getChildNodes();
		
		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(Constants.TALSIM_HEADER_NODE)) {
				// return its value
				return currentNode;
			}
		}

		throw new Exception("No `header` node could be found within the 'series' section of TALSIM_Document!");

	}

	private static String extractSingleNodeValueFromHeaderSection(Node headerNode, String tagName) throws Exception {
		NodeList childNodes = headerNode.getChildNodes();
		int numberOfChildNodes = childNodes.getLength();

		for (int i = 0; i < numberOfChildNodes; i++) {
			Node currentNode = childNodes.item(i);
			String nodeName = currentNode.getNodeName();

			if (nodeName.equals(tagName)) {
				// return its value
				return currentNode.getTextContent();
			}
		}

		throw new Exception(
				"No Node for '" + tagName + "' could be found within the 'header' section of TALSIM_Document!");
	}

	private static String extractSingleAttributeValueFromHeaderSection(Node headerNode, String tagName,
			String attributeName) throws Exception {
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
						return currentAttribute.getTextContent();
					}
				}
			}
		}

		throw new Exception("No Node '" + tagName + "' with attribute '" + attributeName
				+ "' could be found within the 'header' section of TALSIM_Document!");
	}

	private static String extractSingleAttributeValueFromEventNode(Node talsimEventNode, String attributeName)
			throws Exception {

		NamedNodeMap attributes = talsimEventNode.getAttributes();

		int numberOfAttributes = attributes.getLength();

		for (int k = 0; k < numberOfAttributes; k++) {
			Node currentAttribute = attributes.item(k);
			String attributeName_current = currentAttribute.getNodeName();
			if (attributeName_current.equals(attributeName)) {
				// return attribute value
				return currentAttribute.getTextContent();
			}
		}

		throw new Exception("No attribute '" + attributeName
				+ "' could be found within the 'eventNode' of TALSIM_Document! EventNode: " + talsimEventNode);
	}

	private static String extractSingleNodeValueFromDocument(Document talsimDocument, String tagName) {
		NodeList elementsByTagName = talsimDocument.getElementsByTagName(tagName);
		Node item = elementsByTagName.item(0);
		String stationName = item.getTextContent();
		return stationName;
	}

}
