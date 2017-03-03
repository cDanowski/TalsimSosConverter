package n52.talsim_sos_converter.helper;

/**
 * Contains required constant defintions. They are necessary to identify paths,
 * node names or placeholders in several source files related to
 * Talsim-to-SOS-conversion.
 * 
 * @author Christian Danowski-Buhren (contact: c.danowski@52north.org)
 *
 */
public class Constants {

	/**
	 * CONSTANTS DEFINITION
	 */

	/*
	 * REFERENCE TO LOCAL REQUEST TEMPLATE FILES LOCATED IN SRC/MAIN/RESOURCES
	 */
	public static final String PATH_TO_INSERT_SENSOR_REQUEST_TEMPLATE = "InsertSensor_template.xml";
	public static final String PATH_TO_INSERT_OBSERVATION_REQUEST_TEMPLATE = "InsertObservation_template.xml";
	
	/*
	 * AUTHORIZATION TOKEN
	 */
	public static final String PATH_TO_TOKEN_PROPERTIES_FILE = "authorization.properties";
	public static final String TOKEN_FILE_PATH_PROPERTY_NAME = "pathToTokenFile";
	public static final String TOKEN_PROPERTY_NAME = "token";

	/*
	 * TalsimResult CONSTANTS - node and attribute names
	 */
	
	/*
	 * SERIES TAG
	 */
	public static final String TALSIM_SERIES_NODE = "series";
	
	// OUTPUT PARAMETER IDs
	public static final String TALSIM_OUTPUT_PARAMETER_IDENTIFIER_1ZU = "1ZU";
	public static final String TALSIM_OUTPUT_PARAMETER_IDENTIFIER_VOL = "VOL";
	public static final String TALSIM_OUTPUT_PARAMETER_IDENTIFIER_WSP = "WSP";
	public static final String TALSIM_OUTPUT_PARAMETER_IDENTIFIER_QA1 = "QA1";
	public static final String TALSIM_OUTPUT_PARAMETER_IDENTIFIER_QH1 = "QH1";

	// station information
	public static final String TALSIM_HEADER_NODE = "header";
	public static final String TALSIM_RESULT_PARAMETER_ID_NODE = "parameterId";
	public static final String TALSIM_RESULT_LOCATION_ID_NODE = "locationId";
	public static final String TALSIM_RESULT_TIME_ZONE_NODE = "timeZone";
	public static final String TALSIM_RESULT_START_DATE_NODE = "startDate";
	public static final String TALSIM_RESULT_START_DATE_DATE_ATTRIBUTE = "date";
	public static final String TALSIM_RESULT_START_DATE_TIME_ATTRIBUTE = "time";
	public static final String TALSIM_RESULT_END_DATE_NODE = "endDate";
	public static final String TALSIM_RESULT_END_DATE_DATE_ATTRIBUTE = "date";
	public static final String TALSIM_RESULT_END_DATE_TIME_ATTRIBUTE = "time";
	public static final String TALSIM_RESULT_STATION_NAME_NODE = "stationName";
	public static final String TALSIM_RESULT_UNITS_UOM_NODE = "units";
	public static final String TALSIM_RESULT_MISSING_VALUE_NODE = "missVal";

	// event/measurement information
	public static final String TALSIM_RESULT_EVENT_NODE = "event";
	public static final String TALSIM_RESULT_EVENT_DATE_ATTRIBUTE = "date";
	public static final String TALSIM_RESULT_EVENT_TIME_ATTRIBUTE = "time";
	public static final String TALSIM_RESULT_EVENT_VALUE_ATTRIBUTE = "value";

	// other node structures
	/*
	 * TEMPORARY STATION POSITION -- TODO should be removed in future and be
	 * taken from stationMetadata instead
	 */
	public static final String STATION_LON_IN_DEG = "7.369676";
	public static final String STATION_LAT_IN_DEG = "51.14431";
	public static final String STATION_ALT_IN_METERS = "0.0";

	/*
	 * InsertSensor PLACEHOLDER CONSTANTS
	 */
	public static final String INSERT_SENSOR_STATION_IDENTIFIER_PLACEHOLDER = "%STATION_IDENTIFIER%";
	public static final String INSERT_SENSOR_OFFERING_IDENTIFIER_NAME_PLACEHOLDER = "%OFFERING_IDENTIFIER_NAME%";
	public static final String INSERT_SENSOR_OFFERING_IDENTIFIER_VALUE_PLACEHOLDER = "%OFFERING_IDENTIFIER_VALUE%";

	public static final String INSERT_SENSOR_STATION_POSITION_LON_IN_DEG_PLACEHOLDER = "%POSITION_LON_IN_DEG%";
	public static final String INSERT_SENSOR_STATION_POSITION_LAT_IN_DEG_PLACEHOLDER = "%POSITION_LAT_IN_DEG%";
	public static final String INSERT_SENSOR_STATION_POSITION_ALT_IN_METERS_PLACEHOLDER = "%POSITION_ALT_IN_METERS%";

	// OBSERVABLE PROPERTIES
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_INPUT_NAME_PLACEHOLDER = "%OBSERVABLE_PROPERTY_INPUT_NAME%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_INPUT_VALUE_PLACEHOLDER = "%OBSERVABLE_PROPERTY_INPUT_VALUE%";

	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_1ZU = "%OBSERVABLE_PROPERTY_OUTPUT_NAME_1ZU%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_1ZU = "%OBSERVABLE_PROPERTY_OUTPUT_VALUE_1ZU%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_1ZU = "%UOM_DEFINITION_1ZU%";
	
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_VOL = "%OBSERVABLE_PROPERTY_OUTPUT_NAME_VOL%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_VOL = "%OBSERVABLE_PROPERTY_OUTPUT_VALUE_VOL%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_VOL = "%UOM_DEFINITION_VOL%";
	
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_WSP = "%OBSERVABLE_PROPERTY_OUTPUT_NAME_WSP%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_WSP = "%OBSERVABLE_PROPERTY_OUTPUT_VALUE_WSP%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_WSP = "%UOM_DEFINITION_WSP%";
	
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_QA1 = "%OBSERVABLE_PROPERTY_OUTPUT_NAME_QA1%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_QA1 = "%OBSERVABLE_PROPERTY_OUTPUT_VALUE_QA1%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_QA1 = "%UOM_DEFINITION_QA1%";

	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_NAME_PLACEHOLDER_QH1 = "%OBSERVABLE_PROPERTY_OUTPUT_NAME_QH1%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_VALUE_PLACEHOLDER_QH1 = "%OBSERVABLE_PROPERTY_OUTPUT_VALUE_QH1%";
	public static final String INSERT_SENSOR_OBSERVABLE_PROPERTY_OUTPUT_UOM_PLACEHOLDER_QH1 = "%UOM_DEFINITION_QH1%";

	
	// FEATUE OF INTEREST
	public static final String INSERT_SENSOR_FEATURE_OF_INTEREST_IDENTIFIER_PLACEHOLDER = "%FEATURE_OF_INTEREST_IDENTIFIER%";

	/*
	 * InsertObservation PLACEHOLDER CONSTANTS
	 */
	public static final String INSERT_OBSERVATION_OFFERING_IDENTIFIER_PLACEHOLDER = "%OFFERING_IDENTIFIER%";
	public static final String INSERT_OBSERVATION_OBSERVATION_IDENTIFIER_PLACEHOLDER = "%OBSERVATION_IDENTIFIER%";
	public static final String INSERT_OBSERVATION_PHENOMENON_TIME_PLACEHOLDER = "%PHENOMENON_TIME%";
	public static final String INSERT_OBSERVATION_PROCEDURE_IDENTIFIER_PLACEHOLDER = "%PROCEDURE_IDENTIFIER%";
	public static final String INSERT_OBSERVATION_OBSERVABLE_PROPERTY_IDENTIFIER_PLACEHOLDER = "%OBSERVABLE_PROPERTY%";

	// FEATURE OF INTEREST
	public static final String INSERT_OBSERVATION_FEATURE_OF_INTEREST_IDENTIFIER_SAMPLING_FEATURE_PLACEHOLDER = "%FEATURE_OF_INTEREST_IDENTIFIER_SAMPLING_FEATURE%";
	public static final String INSERT_OBSERVATION_FEATURE_OF_INTEREST_IDENTIFIER_SAMPLED_FEATURE_PLACEHOLDER = "%FEATURE_OF_INTEREST_IDENTIFIER_SAMPLED_FEATURE%";
	public static final String INSERT_OBSERVATION_FEATURE_OF_INTEREST_POSITION_LON_IN_DEG_PLACEHOLDER = "%SAMPLING_FEATURE_LON_IN_DEG%";
	public static final String INSERT_OBSERVATION_FEATURE_OF_INTEREST_POSITION_LAT_IN_DEG_PLACEHOLDER = "%SAMPLING_FEATURE_LAT_IN_DEG%";

	// UNIT OF MEASURE
	public static final String INSERT_OBSERVATION_UOM_NAME_PLACEHOLDER = "%UOM_NAME%";
	public static final String INSERT_OBSERVATION_RESULT_VALUE_PLACEHOLDER = "%RESULT_VALUE%";

	/*
	 * OBSERVABLE PROPERTY CONSTANTS
	 */
	public static final String OBSERVABLE_PROPERTY_INPUT_NAME = "Volumen";
	public static final String OBSERVABLE_PROPERTY_INPUT_VALUE = "Volumen";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_NAME_1ZU = "Zufluss";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_VALUE_1ZU = "Zufluss";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_NAME_VOL = "Volumen";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_VALUE_VOL = "Volumen";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_NAME_WSP = "Wasserstand";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_VALUE_WSP = "Wasserstand";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_NAME_QA1 = "Abgabe";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_VALUE_QA1 = "Abgabe";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_NAME_QH1 = "Hochwasserentlastung";
	public static final String OBSERVABLE_PROPERTY_OUTPUT_VALUE_QH1 = "Hochwasserentlastung";
	

	/*
	 * OFFERING
	 */
	public static final String OFFERING_IDENTIFIER_NAME = "TalsimResult";
	public static final String OFFERING_IDENTIFIER_VALUE = "TalsimResult";

	/*
	 * FEATURE OF INTEREST TODO FIXME replace with real value!
	 */
	public static final String FEATURE_OF_INTEREST_SAMPLING_FEATURE = "foi/test/sampling";
	public static final String FEATURE_OF_INTEREST_SAMPLED_FEATURE = "foi/test/sampled";

}
