package org.jails.validation.constraint.data;

import org.jails.validation.constraint.ISOCountryCode;
import org.jails.validation.constraint.ISOStateCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class State {
	@NotNull
	@Size(max = 2)
	@ISOStateCode
	private String isoCode;

	@NotNull
	@Size(max = 35)
	private String name;

	@NotNull
	@Size(max = 50)
	@ISOCountryCode
	private String countryCode;

	public State(String isoCode, String name, String countryCode) {
		this.isoCode = isoCode;
		this.name = name;
		this.countryCode = countryCode;
	}

	public State(String isoCode, String name) {
		this.isoCode = isoCode;
		this.name = name;
	}

	public String getIsoCode() {
		return isoCode;
	}

	public void setIsoCode(String isoCode) {
		this.isoCode = isoCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public static List<State> getStates() {
		return new ArrayList<State>(STATES.values());
	}

	public static List<State> getUSStates() {
		List<State> states = new ArrayList<State>();
		for (State state : STATES.values()) {
			if ("US".equals(state.getCountryCode())) states.add(state);
		}
		return states;
	}

	public static boolean isValidState(String isoStateCode) {
		return STATES.containsKey(isoStateCode);
	}

	public static final State AL = new State("AL", "Alabama", "US");
	public static final State AK = new State("AK", "Alaska", "US");
	public static final State AS = new State("AS", "American Samoa");
	public static final State AZ = new State("AZ", "Arizona", "US");
	public static final State AR = new State("AR", "Arkansas", "US");
	public static final State CA = new State("CA", "California", "US");
	public static final State CO = new State("CO", "Colorado", "US");
	public static final State CT = new State("CT", "Connecticut", "US");
	public static final State DE = new State("DE", "Delaware", "US");
	public static final State DC = new State("DC", "District of Columbia", "US");
	public static final State FM = new State("FM", "Federated States of Micronesia");
	public static final State FL = new State("FL", "Florida", "US");
	public static final State GA = new State("GA", "Georgia", "US");
	public static final State GU = new State("GU", "Guam");
	public static final State HI = new State("HI", "Hawaii", "US");
	public static final State ID = new State("ID", "Idaho", "US");
	public static final State IL = new State("IL", "Illinois", "US");
	public static final State IN = new State("IN", "Indiana", "US");
	public static final State IA = new State("IA", "Iowa", "US");
	public static final State KS = new State("KS", "Kansas", "US");
	public static final State KY = new State("KY", "Kentucky", "US");
	public static final State LA = new State("LA", "Louisiana", "US");
	public static final State ME = new State("ME", "Maine", "US");
	public static final State MH = new State("MH", "Marshall Islands");
	public static final State MD = new State("MD", "Maryland", "US");
	public static final State MA = new State("MA", "Massachusetts", "US");
	public static final State MI = new State("MI", "Michigan", "US");
	public static final State MN = new State("MN", "Minnesota", "US");
	public static final State MS = new State("MS", "Mississippi", "US");
	public static final State MO = new State("MO", "Missouri", "US");
	public static final State MT = new State("MT", "Montana", "US");
	public static final State NE = new State("NE", "Nebraska", "US");
	public static final State NV = new State("NV", "Nevada", "US");
	public static final State NH = new State("NH", "New Hampshire", "US");
	public static final State NJ = new State("NJ", "New Jersey", "US");
	public static final State NM = new State("NM", "New Mexico", "US");
	public static final State NY = new State("NY", "New York", "US");
	public static final State NC = new State("NC", "North Carolina", "US");
	public static final State ND = new State("ND", "North Dakota", "US");
	public static final State MP = new State("MP", "Northern Mariana Islands");
	public static final State OH = new State("OH", "Ohio", "US");
	public static final State OK = new State("OK", "Oklahoma", "US");
	public static final State OR = new State("OR", "Oregon", "US");
	public static final State PW = new State("PW", "Palau");
	public static final State PA = new State("PA", "Pennsylvania", "US");
	public static final State PR = new State("PR", "Puerto Rico");
	public static final State RI = new State("RI", "Rhode Island", "US");
	public static final State SC = new State("SC", "South Carolina", "US");
	public static final State SD = new State("SD", "South Dakota", "US");
	public static final State TN = new State("TN", "Tennessee", "US");
	public static final State TX = new State("TX", "Texas", "US");
	public static final State UT = new State("UT", "Utah", "US");
	public static final State VT = new State("VT", "Vermont", "US");
	public static final State VI = new State("VI", "Virgin Islands");
	public static final State VA = new State("VA", "Virginia", "US");
	public static final State WA = new State("WA", "Washington", "US");
	public static final State WV = new State("WV", "West Virginia", "US");
	public static final State WI = new State("WI", "Wisconsin", "US");
	public static final State WY = new State("WY", "Wyoming", "US");
	public static final State AB = new State("AB", "Alberta", "CA");
	public static final State BC = new State("BC", "British Columbia", "CA");
	public static final State MB = new State("MB", "Manitoba", "CA");
	public static final State NB = new State("NB", "New Brunswick", "CA");
	public static final State NL = new State("NL", "Newfoundland and Labrador", "CA");
	public static final State NT = new State("NT", "Northwest Territories", "CA");
	public static final State NS = new State("NS", "Nova Scotia", "CA");
	public static final State NU = new State("NU", "Nunavut", "CA");
	public static final State ON = new State("ON", "Ontario", "CA");
	public static final State PE = new State("PE", "Prince Edward Island", "CA");
	public static final State QC = new State("QC", "Quebec", "CA");
	public static final State SK = new State("SK", "Saskatchewan", "CA");
	public static final State YT = new State("YT", "Yukon", "CA");
	public static final State AA = new State("AA", "AA");
	public static final State AE = new State("AE", "AE");
	public static final State AP = new State("AP", "AP");
	public static final State NA = new State("NA", "Not Applicable");

	public static final Map<String, State> STATES = new LinkedHashMap<String, State>();
	static {
		STATES.put("AL", AL);
		STATES.put("AK", AK);
		STATES.put("AS", AS);
		STATES.put("AZ", AZ);
		STATES.put("AR", AR);
		STATES.put("CA", CA);
		STATES.put("CO", CO);
		STATES.put("CT", CT);
		STATES.put("DE", DE);
		STATES.put("DC", DC);
		STATES.put("FM", FM);
		STATES.put("FL", FL);
		STATES.put("GA", GA);
		STATES.put("GU", GU);
		STATES.put("HI", HI);
		STATES.put("ID", ID);
		STATES.put("IL", IL);
		STATES.put("IN", IN);
		STATES.put("IA", IA);
		STATES.put("KS", KS);
		STATES.put("KY", KY);
		STATES.put("LA", LA);
		STATES.put("ME", ME);
		STATES.put("MH", MH);
		STATES.put("MD", MD);
		STATES.put("MA", MA);
		STATES.put("MI", MI);
		STATES.put("MN", MN);
		STATES.put("MS", MS);
		STATES.put("MO", MO);
		STATES.put("MT", MT);
		STATES.put("NE", NE);
		STATES.put("NV", NV);
		STATES.put("NH", NH);
		STATES.put("NJ", NJ);
		STATES.put("NM", NM);
		STATES.put("NY", NY);
		STATES.put("NC", NC);
		STATES.put("ND", ND);
		STATES.put("MP", MP);
		STATES.put("OH", OH);
		STATES.put("OK", OK);
		STATES.put("OR", OR);
		STATES.put("PW", PW);
		STATES.put("PA", PA);
		STATES.put("PR", PR);
		STATES.put("RI", RI);
		STATES.put("SC", SC);
		STATES.put("SD", SD);
		STATES.put("TN", TN);
		STATES.put("TX", TX);
		STATES.put("UT", UT);
		STATES.put("VT", VT);
		STATES.put("VI", VI);
		STATES.put("VA", VA);
		STATES.put("WA", WA);
		STATES.put("WV", WV);
		STATES.put("WI", WI);
		STATES.put("WY", WY);
		STATES.put("AB", AB);
		STATES.put("BC", BC);
		STATES.put("MB", MB);
		STATES.put("NB", NB);
		STATES.put("NL", NL);
		STATES.put("NT", NT);
		STATES.put("NS", NS);
		STATES.put("NU", NU);
		STATES.put("ON", ON);
		STATES.put("PE", PE);
		STATES.put("QC", QC);
		STATES.put("SK", SK);
		STATES.put("YT", YT);
		STATES.put("AA", AA);
		STATES.put("AE", AE);
		STATES.put("AP", AP);
		STATES.put("N/A", NA);
	}
}
