package org.jails.validation.constraint.data;

import org.jails.validation.constraint.ISOCountryCode;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Country {
	@NotNull
	@Size(max = 2)
	@ISOCountryCode
	private String isoCode;

	@NotNull
	@Size(max = 50)
	private String name;

	public Country(String isoCode, String name) {
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

	public static List<Country> getCountries() {
		return new ArrayList<Country>(COUNTRIES.values());
	}

	public static boolean isValidCountry(String isoCountryCode) {
		return COUNTRIES.containsKey(isoCountryCode);
	}

	public static final Map<String, Country> COUNTRIES = new LinkedHashMap<String, Country>();
	public static final Country US = new Country("US","United States");
	public static final Country AF = new Country("AF","Afghanistan");
	public static final Country AL = new Country("AL","Albania");
	public static final Country DZ = new Country("DZ","Algeria");
	public static final Country AS = new Country("AS","American Samoa (US)");
	public static final Country AD = new Country("AD","Andorra");
	public static final Country AO = new Country("AO","Angola");
	public static final Country AI = new Country("AI","Anguilla (UK)");
	public static final Country AG = new Country("AG","Antigua and Barbuda");
	public static final Country AR = new Country("AR","Argentina");
	public static final Country AM = new Country("AM","Armenia");
	public static final Country AW = new Country("AW","Aruba");
	public static final Country AU = new Country("AU","Australia");
	public static final Country AT = new Country("AT","Austria");
	public static final Country AZ = new Country("AZ","Azerbaijan");
	public static final Country BS = new Country("BS","Bahamas");
	public static final Country BH = new Country("BH","Bahrain");
	public static final Country BD = new Country("BD","Bangladesh");
	public static final Country BB = new Country("BB","Barbados");
	public static final Country BY = new Country("BY","Belarus");
	public static final Country BE = new Country("BE","Belgium");
	public static final Country BZ = new Country("BZ","Belize");
	public static final Country BJ = new Country("BJ","Benin");
	public static final Country BM = new Country("BM","Bermuda (UK)");
	public static final Country BT = new Country("BT","Bhutan");
	public static final Country BO = new Country("BO","Bolivia");
	public static final Country BA = new Country("BA","Bosnia and Herzegovina");
	public static final Country BW = new Country("BW","Botswana");
	public static final Country BR = new Country("BR","Brazil");
	public static final Country VG = new Country("VG","British Virgin Islands (UK)");
	public static final Country BN = new Country("BN","Brunei Darussalam");
	public static final Country BG = new Country("BG","Bulgaria");
	public static final Country BF = new Country("BF","Burkina Faso");
	public static final Country MM = new Country("MM","Burma");
	public static final Country BI = new Country("BI","Burundi");
	public static final Country KH = new Country("KH","Cambodia");
	public static final Country CM = new Country("CM","Cameroon");
	public static final Country CA = new Country("CA","Canada");
	public static final Country CV = new Country("CV","Cape Verde");
	public static final Country KY = new Country("KY","Cayman Islands (UK)");
	public static final Country CF = new Country("CF","Central African Republic");
	public static final Country TD = new Country("TD","Chad");
	public static final Country CL = new Country("CL","Chile");
	public static final Country CN = new Country("CN","China");
	public static final Country CX = new Country("CX","Christmas Island (AU)");
	public static final Country CC = new Country("CC","Cocos (Keeling) Islands (AU)");
	public static final Country CO = new Country("CO","Colombia");
	public static final Country KM = new Country("KM","Comoros Islands");
	public static final Country CD = new Country("CD","Congo, Democratic Republic of the");
	public static final Country CG = new Country("CG","Congo, Republic of the");
	public static final Country CK = new Country("CK","Cook Islands (NZ)");
	public static final Country CR = new Country("CR","Costa Rica");
	public static final Country HR = new Country("HR","Croatia");
	public static final Country CU = new Country("CU","Cuba");
	public static final Country CY = new Country("CY","Cyprus");
	public static final Country CZ = new Country("CZ","Czech Republic");
	public static final Country DK = new Country("DK","Denmark");
	public static final Country DJ = new Country("DJ","Djibouti");
	public static final Country DM = new Country("DM","Dominica");
	public static final Country DO = new Country("DO","Dominican Republic");
	public static final Country TP = new Country("TP","East Timor");
	public static final Country EC = new Country("EC","Ecuador");
	public static final Country EG = new Country("EG","Egypt");
	public static final Country SV = new Country("SV","El Salvador");
	public static final Country GQ = new Country("GQ","Equatorial Guinea");
	public static final Country ER = new Country("ER","Eritrea");
	public static final Country EE = new Country("EE","Estonia");
	public static final Country ET = new Country("ET","Ethiopia");
	public static final Country FK = new Country("FK","Falkland Islands (UK)");
	public static final Country FO = new Country("FO","Faroe Islands (DK)");
	public static final Country FJ = new Country("FJ","Fiji");
	public static final Country FI = new Country("FI","Finland");
	public static final Country FR = new Country("FR","France");
	public static final Country GF = new Country("GF","French Guiana (FR)");
	public static final Country PF = new Country("PF","French Polynesia (FR)");
	public static final Country GA = new Country("GA","Gabon");
	public static final Country GM = new Country("GM","Gambia");
	public static final Country GE = new Country("GE","Georgia");
	public static final Country DE = new Country("DE","Germany");
	public static final Country GH = new Country("GH","Ghana");
	public static final Country GI = new Country("GI","Gibraltar (UK)");
	public static final Country GR = new Country("GR","Greece");
	public static final Country GL = new Country("GL","Greenland (DK)");
	public static final Country GD = new Country("GD","Grenada");
	public static final Country GP = new Country("GP","Guadeloupe (FR)");
	public static final Country GU = new Country("GU","Guam (US)");
	public static final Country GT = new Country("GT","Guatemala");
	public static final Country GN = new Country("GN","Guinea");
	public static final Country GW = new Country("GW","Guinea-Bissau");
	public static final Country GY = new Country("GY","Guyana");
	public static final Country HT = new Country("HT","Haiti");
	public static final Country VA = new Country("VA","Holy See (Vatican City)");
	public static final Country HN = new Country("HN","Honduras");
	public static final Country HK = new Country("HK","Hong Kong (CN)");
	public static final Country HU = new Country("HU","Hungary");
	public static final Country IS = new Country("IS","Iceland");
	public static final Country IN = new Country("IN","India");
	public static final Country ID = new Country("ID","Indonesia");
	public static final Country IR = new Country("IR","Iran");
	public static final Country IQ = new Country("IQ","Iraq");
	public static final Country IE = new Country("IE","Ireland");
	public static final Country IL = new Country("IL","Israel");
	public static final Country IT = new Country("IT","Italy");
	public static final Country CI = new Country("CI","Ivory Coast");
	public static final Country JM = new Country("JM","Jamaica");
	public static final Country JP = new Country("JP","Japan");
	public static final Country JO = new Country("JO","Jordan");
	public static final Country KZ = new Country("KZ","Kazakhstan");
	public static final Country KE = new Country("KE","Kenya");
	public static final Country KI = new Country("KI","Kiribati");
	public static final Country KP = new Country("KP","Korea, North");
	public static final Country KR = new Country("KR","Korea, South");
	public static final Country KW = new Country("KW","Kuwait");
	public static final Country KG = new Country("KG","Kyrgyzstan");
	public static final Country LA = new Country("LA","Laos");
	public static final Country LV = new Country("LV","Latvia");
	public static final Country LB = new Country("LB","Lebanon");
	public static final Country LS = new Country("LS","Lesotho");
	public static final Country LR = new Country("LR","Liberia");
	public static final Country LY = new Country("LY","Libya");
	public static final Country LI = new Country("LI","Liechtenstein");
	public static final Country LT = new Country("LT","Lithuania");
	public static final Country LU = new Country("LU","Luxembourg");
	public static final Country MO = new Country("MO","Macau (CN)");
	public static final Country MK = new Country("MK","Macedonia");
	public static final Country MG = new Country("MG","Madagascar");
	public static final Country MW = new Country("MW","Malawi");
	public static final Country MY = new Country("MY","Malaysia");
	public static final Country MV = new Country("MV","Maldives");
	public static final Country ML = new Country("ML","Mali");
	public static final Country MT = new Country("MT","Malta");
	public static final Country MH = new Country("MH","Marshall Islands");
	public static final Country MQ = new Country("MQ","Martinique (FR)");
	public static final Country MR = new Country("MR","Mauritania");
	public static final Country MU = new Country("MU","Mauritius");
	public static final Country YT = new Country("YT","Mayotte (FR)");
	public static final Country MX = new Country("MX","Mexico");
	public static final Country FM = new Country("FM","Micronesia, Federated States of");
	public static final Country MD = new Country("MD","Moldova");
	public static final Country MC = new Country("MC","Monaco");
	public static final Country MN = new Country("MN","Mongolia");
	public static final Country MS = new Country("MS","Montserrat (UK)");
	public static final Country MA = new Country("MA","Morocco");
	public static final Country MZ = new Country("MZ","Mozambique");
	public static final Country NA = new Country("NA","Namibia");
	public static final Country NR = new Country("NR","Nauru");
	public static final Country NP = new Country("NP","Nepal");
	public static final Country NL = new Country("NL","Netherlands");
	public static final Country AN = new Country("AN","Netherlands Antilles (NL)");
	public static final Country NC = new Country("NC","New Caledonia (FR)");
	public static final Country NZ = new Country("NZ","New Zealand");
	public static final Country NI = new Country("NI","Nicaragua");
	public static final Country NE = new Country("NE","Niger");
	public static final Country NG = new Country("NG","Nigeria");
	public static final Country NU = new Country("NU","Niue");
	public static final Country NF = new Country("NF","Norfolk Island (AU)");
	public static final Country MP = new Country("MP","Northern Mariana Islands (US)");
	public static final Country NO = new Country("NO","Norway");
	public static final Country OM = new Country("OM","Oman");
	public static final Country PK = new Country("PK","Pakistan");
	public static final Country PW = new Country("PW","Palau");
	public static final Country PA = new Country("PA","Panama");
	public static final Country PG = new Country("PG","Papua New Guinea");
	public static final Country PY = new Country("PY","Paraguay");
	public static final Country PE = new Country("PE","Peru");
	public static final Country PH = new Country("PH","Philippines");
	public static final Country PN = new Country("PN","Pitcairn Islands (UK)");
	public static final Country PL = new Country("PL","Poland");
	public static final Country PT = new Country("PT","Portugal");
	public static final Country PR = new Country("PR","Puerto Rico (US)");
	public static final Country QA = new Country("QA","Qatar");
	public static final Country RE = new Country("RE","Reunion (FR)");
	public static final Country RO = new Country("RO","Romania");
	public static final Country RU = new Country("RU","Russia");
	public static final Country RW = new Country("RW","Rwanda");
	public static final Country SH = new Country("SH","Saint Helena (UK)");
	public static final Country KN = new Country("KN","Saint Kitts and Nevis");
	public static final Country LC = new Country("LC","Saint Lucia");
	public static final Country PM = new Country("PM","Saint Pierre & Miquelon (FR)");
	public static final Country VC = new Country("VC","Saint Vincent and the Grenadines");
	public static final Country WS = new Country("WS","Samoa");
	public static final Country SM = new Country("SM","San Marino");
	public static final Country ST = new Country("ST","Sao Tome and Principe");
	public static final Country SA = new Country("SA","Saudi Arabia");
	public static final Country SN = new Country("SN","Senegal");
	public static final Country CS = new Country("CS","Serbia and Montenegro");
	public static final Country SC = new Country("SC","Seychelles");
	public static final Country SL = new Country("SL","Sierra Leone");
	public static final Country SG = new Country("SG","Singapore");
	public static final Country SK = new Country("SK","Slovakia");
	public static final Country SI = new Country("SI","Slovenia");
	public static final Country SB = new Country("SB","Solomon Islands");
	public static final Country SO = new Country("SO","Somalia");
	public static final Country ZA = new Country("ZA","South Africa");
	public static final Country GS = new Country("GS","South Georgia & South Sandwich Islands (UK)");
	public static final Country ES = new Country("ES","Spain");
	public static final Country LK = new Country("LK","Sri Lanka");
	public static final Country SD = new Country("SD","Sudan");
	public static final Country SR = new Country("SR","Suriname");
	public static final Country SZ = new Country("SZ","Swaziland");
	public static final Country SE = new Country("SE","Sweden");
	public static final Country CH = new Country("CH","Switzerland");
	public static final Country SY = new Country("SY","Syria");
	public static final Country TW = new Country("TW","Taiwan");
	public static final Country TJ = new Country("TJ","Tajikistan");
	public static final Country TZ = new Country("TZ","Tanzania");
	public static final Country TH = new Country("TH","Thailand");
	public static final Country TG = new Country("TG","Togo");
	public static final Country TK = new Country("TK","Tokelau");
	public static final Country TO = new Country("TO","Tonga");
	public static final Country TT = new Country("TT","Trinidad and Tobago");
	public static final Country TN = new Country("TN","Tunisia");
	public static final Country TR = new Country("TR","Turkey");
	public static final Country TM = new Country("TM","Turkmenistan");
	public static final Country TC = new Country("TC","Turks and Caicos Islands (UK)");
	public static final Country TV = new Country("TV","Tuvalu");
	public static final Country UG = new Country("UG","Uganda");
	public static final Country UA = new Country("UA","Ukraine");
	public static final Country AE = new Country("AE","United Arab Emirates");
	public static final Country GB = new Country("GB","United Kingdom");
	public static final Country UY = new Country("UY","Uruguay");
	public static final Country UZ = new Country("UZ","Uzbekistan");
	public static final Country VU = new Country("VU","Vanuatu");
	public static final Country VE = new Country("VE","Venezuela");
	public static final Country VN = new Country("VN","Vietnam");
	public static final Country VI = new Country("VI","Virgin Islands (US)");
	public static final Country WF = new Country("WF","Wallis and Futuna (DFR)");
	public static final Country EH = new Country("EH","Western Sahara");
	public static final Country YE = new Country("YE","Yemen");
	public static final Country ZM = new Country("ZM","Zambia");
	public static final Country ZW = new Country("ZW","Zimbabwe");

	static {
		COUNTRIES.put("US", US);
		COUNTRIES.put("AF", AF);
		COUNTRIES.put("AL", AL);
		COUNTRIES.put("DZ", DZ);
		COUNTRIES.put("AS", AS);
		COUNTRIES.put("AD", AD);
		COUNTRIES.put("AO", AO);
		COUNTRIES.put("AI", AI);
		COUNTRIES.put("AG", AG);
		COUNTRIES.put("AR", AR);
		COUNTRIES.put("AM", AM);
		COUNTRIES.put("AW", AW);
		COUNTRIES.put("AU", AU);
		COUNTRIES.put("AT", AT);
		COUNTRIES.put("AZ", AZ);
		COUNTRIES.put("BS", BS);
		COUNTRIES.put("BH", BH);
		COUNTRIES.put("BD", BD);
		COUNTRIES.put("BB", BB);
		COUNTRIES.put("BY", BY);
		COUNTRIES.put("BE", BE);
		COUNTRIES.put("BZ", BZ);
		COUNTRIES.put("BJ", BJ);
		COUNTRIES.put("BM", BM);
		COUNTRIES.put("BT", BT);
		COUNTRIES.put("BO", BO);
		COUNTRIES.put("BA", BA);
		COUNTRIES.put("BW", BW);
		COUNTRIES.put("BR", BR);
		COUNTRIES.put("VG", VG);
		COUNTRIES.put("BN", BN);
		COUNTRIES.put("BG", BG);
		COUNTRIES.put("BF", BF);
		COUNTRIES.put("MM", MM);
		COUNTRIES.put("BI", BI);
		COUNTRIES.put("KH", KH);
		COUNTRIES.put("CM", CM);
		COUNTRIES.put("CA", CA);
		COUNTRIES.put("CV", CV);
		COUNTRIES.put("KY", KY);
		COUNTRIES.put("CF", CF);
		COUNTRIES.put("TD", TD);
		COUNTRIES.put("CL", CL);
		COUNTRIES.put("CN", CN);
		COUNTRIES.put("CX", CX);
		COUNTRIES.put("CC", CC);
		COUNTRIES.put("CO", CO);
		COUNTRIES.put("KM", KM);
		COUNTRIES.put("CD", CD);
		COUNTRIES.put("CG", CG);
		COUNTRIES.put("CK", CK);
		COUNTRIES.put("CR", CR);
		COUNTRIES.put("HR", HR);
		COUNTRIES.put("CU", CU);
		COUNTRIES.put("CY", CY);
		COUNTRIES.put("CZ", CZ);
		COUNTRIES.put("DK", DK);
		COUNTRIES.put("DJ", DJ);
		COUNTRIES.put("DM", DM);
		COUNTRIES.put("DO", DO);
		COUNTRIES.put("TP", TP);
		COUNTRIES.put("EC", EC);
		COUNTRIES.put("EG", EG);
		COUNTRIES.put("SV", SV);
		COUNTRIES.put("GQ", GQ);
		COUNTRIES.put("ER", ER);
		COUNTRIES.put("EE", EE);
		COUNTRIES.put("ET", ET);
		COUNTRIES.put("FK", FK);
		COUNTRIES.put("FO", FO);
		COUNTRIES.put("FJ", FJ);
		COUNTRIES.put("FI", FI);
		COUNTRIES.put("FR", FR);
		COUNTRIES.put("GF", GF);
		COUNTRIES.put("PF", PF);
		COUNTRIES.put("GA", GA);
		COUNTRIES.put("GM", GM);
		COUNTRIES.put("GE", GE);
		COUNTRIES.put("DE", DE);
		COUNTRIES.put("GH", GH);
		COUNTRIES.put("GI", GI);
		COUNTRIES.put("GR", GR);
		COUNTRIES.put("GL", GL);
		COUNTRIES.put("GD", GD);
		COUNTRIES.put("GP", GP);
		COUNTRIES.put("GU", GU);
		COUNTRIES.put("GT", GT);
		COUNTRIES.put("GN", GN);
		COUNTRIES.put("GW", GW);
		COUNTRIES.put("GY", GY);
		COUNTRIES.put("HT", HT);
		COUNTRIES.put("VA", VA);
		COUNTRIES.put("HN", HN);
		COUNTRIES.put("HK", HK);
		COUNTRIES.put("HU", HU);
		COUNTRIES.put("IS", IS);
		COUNTRIES.put("IN", IN);
		COUNTRIES.put("ID", ID);
		COUNTRIES.put("IR", IR);
		COUNTRIES.put("IQ", IQ);
		COUNTRIES.put("IE", IE);
		COUNTRIES.put("IL", IL);
		COUNTRIES.put("IT", IT);
		COUNTRIES.put("CI", CI);
		COUNTRIES.put("JM", JM);
		COUNTRIES.put("JP", JP);
		COUNTRIES.put("JO", JO);
		COUNTRIES.put("KZ", KZ);
		COUNTRIES.put("KE", KE);
		COUNTRIES.put("KI", KI);
		COUNTRIES.put("KP", KP);
		COUNTRIES.put("KR", KR);
		COUNTRIES.put("KW", KW);
		COUNTRIES.put("KG", KG);
		COUNTRIES.put("LA", LA);
		COUNTRIES.put("LV", LV);
		COUNTRIES.put("LB", LB);
		COUNTRIES.put("LS", LS);
		COUNTRIES.put("LR", LR);
		COUNTRIES.put("LY", LY);
		COUNTRIES.put("LI", LI);
		COUNTRIES.put("LT", LT);
		COUNTRIES.put("LU", LU);
		COUNTRIES.put("MO", MO);
		COUNTRIES.put("MK", MK);
		COUNTRIES.put("MG", MG);
		COUNTRIES.put("MW", MW);
		COUNTRIES.put("MY", MY);
		COUNTRIES.put("MV", MV);
		COUNTRIES.put("ML", ML);
		COUNTRIES.put("MT", MT);
		COUNTRIES.put("MH", MH);
		COUNTRIES.put("MQ", MQ);
		COUNTRIES.put("MR", MR);
		COUNTRIES.put("MU", MU);
		COUNTRIES.put("YT", YT);
		COUNTRIES.put("MX", MX);
		COUNTRIES.put("FM", FM);
		COUNTRIES.put("MD", MD);
		COUNTRIES.put("MC", MC);
		COUNTRIES.put("MN", MN);
		COUNTRIES.put("MS", MS);
		COUNTRIES.put("MA", MA);
		COUNTRIES.put("MZ", MZ);
		COUNTRIES.put("NA", NA);
		COUNTRIES.put("NR", NR);
		COUNTRIES.put("NP", NP);
		COUNTRIES.put("NL", NL);
		COUNTRIES.put("AN", AN);
		COUNTRIES.put("NC", NC);
		COUNTRIES.put("NZ", NZ);
		COUNTRIES.put("NI", NI);
		COUNTRIES.put("NE", NE);
		COUNTRIES.put("NG", NG);
		COUNTRIES.put("NU", NU);
		COUNTRIES.put("NF", NF);
		COUNTRIES.put("MP", MP);
		COUNTRIES.put("NO", NO);
		COUNTRIES.put("OM", OM);
		COUNTRIES.put("PK", PK);
		COUNTRIES.put("PW", PW);
		COUNTRIES.put("PA", PA);
		COUNTRIES.put("PG", PG);
		COUNTRIES.put("PY", PY);
		COUNTRIES.put("PE", PE);
		COUNTRIES.put("PH", PH);
		COUNTRIES.put("PN", PN);
		COUNTRIES.put("PL", PL);
		COUNTRIES.put("PT", PT);
		COUNTRIES.put("PR", PR);
		COUNTRIES.put("QA", QA);
		COUNTRIES.put("RE", RE);
		COUNTRIES.put("RO", RO);
		COUNTRIES.put("RU", RU);
		COUNTRIES.put("RW", RW);
		COUNTRIES.put("SH", SH);
		COUNTRIES.put("KN", KN);
		COUNTRIES.put("LC", LC);
		COUNTRIES.put("PM", PM);
		COUNTRIES.put("VC", VC);
		COUNTRIES.put("WS", WS);
		COUNTRIES.put("SM", SM);
		COUNTRIES.put("ST", ST);
		COUNTRIES.put("SA", SA);
		COUNTRIES.put("SN", SN);
		COUNTRIES.put("CS", CS);
		COUNTRIES.put("SC", SC);
		COUNTRIES.put("SL", SL);
		COUNTRIES.put("SG", SG);
		COUNTRIES.put("SK", SK);
		COUNTRIES.put("SI", SI);
		COUNTRIES.put("SB", SB);
		COUNTRIES.put("SO", SO);
		COUNTRIES.put("ZA", ZA);
		COUNTRIES.put("GS", GS);
		COUNTRIES.put("ES", ES);
		COUNTRIES.put("LK", LK);
		COUNTRIES.put("SD", SD);
		COUNTRIES.put("SR", SR);
		COUNTRIES.put("SZ", SZ);
		COUNTRIES.put("SE", SE);
		COUNTRIES.put("CH", CH);
		COUNTRIES.put("SY", SY);
		COUNTRIES.put("TW", TW);
		COUNTRIES.put("TJ", TJ);
		COUNTRIES.put("TZ", TZ);
		COUNTRIES.put("TH", TH);
		COUNTRIES.put("TG", TG);
		COUNTRIES.put("TK", TK);
		COUNTRIES.put("TO", TO);
		COUNTRIES.put("TT", TT);
		COUNTRIES.put("TN", TN);
		COUNTRIES.put("TR", TR);
		COUNTRIES.put("TM", TM);
		COUNTRIES.put("TC", TC);
		COUNTRIES.put("TV", TV);
		COUNTRIES.put("UG", UG);
		COUNTRIES.put("UA", UA);
		COUNTRIES.put("AE", AE);
		COUNTRIES.put("GB", GB);
		COUNTRIES.put("UY", UY);
		COUNTRIES.put("UZ", UZ);
		COUNTRIES.put("VU", VU);
		COUNTRIES.put("VE", VE);
		COUNTRIES.put("VN", VN);
		COUNTRIES.put("VI", VI);
		COUNTRIES.put("WF", WF);
		COUNTRIES.put("EH", EH);
		COUNTRIES.put("YE", YE);
		COUNTRIES.put("ZM", ZM);
		COUNTRIES.put("ZW", ZW);

	}
}