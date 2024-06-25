package com.convozen;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public final class CommonConstants {
	public static final String CONVOZEN_USERNAME = "convozen.username";
	public static final String CONVOZEN_PASSWORD = "convozen.password";
	public static final String CONVOZEN_WEBURL = "convozen.weburl";
	public static final String BROWSER = "convozen.browser";
	public static final String RUNMODE_IS_HEADLESS = "convozen.headless";
	public static final String CONVOZEN_SLACK_CHANENEL = "convozen.slackChannel";
	public static final String CONVOZEN_SLACK_TOKEN = "convozen.slackToken";
	public static final String CURRENT_DIRECTORY = System.getProperty("user.dir");
	public static final String COMMON = "common";
	public static final String CONVOZEN = "convozen";

	public static String RANDOM_NAME() {
		return RandomStringUtils.randomAlphabetic(3).toLowerCase();
	}

	public static SEMANTIC_SENTENCE RANDOM_SEMANTIC() {
		Random random = new Random();
		return SEMANTIC_SENTENCE.values()[random.nextInt(SEMANTIC_SENTENCE.values().length)];
	}


	public enum STATUS {
		TO_DO("To Do"),ALL("All"),ONGOING("Ongoing"),COMPLETED("Completed"),OVERDUE("Overdue");

		String value;
		STATUS(String s) {
			value = s;
		}

		public String VALUE() {
			return value;
		}
	}



	public enum CALLZEN_VALUES {
		AUDIT("Audit"),SEMANTIC_MOMENT("Semantic Moment"), MOMENTS("Moments"), GLOBAL("Global"), GLOBL("global"), POSITIVE("Positive"),
		AAD_ANYWAY("Add anyway"), SUGGEST_SIMMILER_PHRASES("Suggest similar phrases"), DELET_PHRASES("Delete phrases"),
		FILTERED("Filtered"), TEAM("Team"), FILTER("Filter"), KEYWORD_MOMENT("Keyword Moment"),
		INSTRUCTIONAL_MOMENT("Instructional Moment"), CAMPAIGN_NAME("Campaign Name"),
		SELECT_CAMPAIGN_NAME("Select Campaign Name"), MODE_OF_CALLING("Mode of calling"),
		SELECT_CALLING_MODE("Select Mode of calling"), PROCESSNAME("Process Name"),
		SELECT_PROCESS_NAME("Select Process Name"), MOMENT_ACTIVATION_STATUS("Moment Activation Status"),
		ACTIVE("Active"), INACTIVE("Inactive"), DRAFTED("Drafted"), MOMENT_TYPE("Moment Type"),
		SELECT_MOMENT_TYPE("Select Moment Type"), VALIDATION_STATUS("Validation Status"),
		SELECT_VALIDATION_STATUS("Select Validation Status"), INSTRUCTIONAL("Instructional"), CHECKLIST("Checklist"),
		CALL_LEVEL("Call Level"), CUSTOMER_LEVEL("Customer Level"), SUCCESS("Success"), PROCESSING("Processing"),
		PROCESSING_FEEDBACK("Feedback processing"), FAILED("Failed"), SOURCE("Source"), SELECT_SOURCE("Select Source"),
		MOMENT_SCREEN("Moment Screen"), MOMENTS_SCREEN("Moments Screen"), SMARTCLUSTER_SCREEN("Smart Cluster Screen"),
		SHOW_ONLY_SMARTMOMENTS("Show Only Smart Moments"), CALLS("Calls"), YESTERDAY("Yesterday"),
		CALL_DURATION("Call Duration"), CUSTOMER_ID("Customer Id"), ENTER_CUSTOMER_ID("Enter Customer Id"),
		CUSTOMER_PHONE("Customer Phone"), ENTER_CUSTOMER_PHONE("Enter Customer Phone"), CALL_TIME("Call Time"),
		SMART_CLUSTER_SCREEN("Smart Cluster Screen"), SMARTMOMENTS("Smart Moments"), AGENT_NAME("Agent Name"),
		ENTER_AGENT_NAME("Enter Agent Name"), CUSTOMER_EMAIL("Customer Email"),
		ENTER_CUSTOMER_EMAIL("Enter Customer Email"), CALL_ID("Call Id"), TALKTIME("Talktime"),
		GRETER_THAN_OR_EQUAL_TO("Greater than or equal to"), LESS_THAN_OR_EQUAL_TO("Less than or equal to"),
		BETWEEN("Between"), OVERLAP_DURATION("Overlap Duration"), SILENCE_DURATION("Silence Duration"),
		TRANSCRIPTION_STATUS("Transcription Status"), CALL_PROGRESS("Call Progress"),
		SELECT_MOMENT_NAME("Select Moment Name"), POSITIVE_("Positive"), HEIGHLIGHTS("Highlights"),
		SELECT_HEIGHLIGHTS("Select Highlight"), LANGUAGE("Language"), SELECT_LANGUAGE("Select Language"),
		CHECKKISTS("Checklists"), SELECT_CHECKKIST_NAME("Select Checklist Name"), CALL_SCORE("Call Score"),
		CALLLER_TYPE("Calller Type"), SELECT_CALLER_TYPE("Select Caller Type"), DELET("Delete"),
		MARK_AS_OPTIONAL("Mark as Optional"), CREATE_GROUP("Create Group"), TAGGING_ATTRIBUTE("Tagging Attributes"),
		ACTIVATE("Activate"), DEACTIVATE("Deactivate"), BULK_TAG("Bulk tag"), BULK_UNTAG("Bulk untag"), TODAY("Today"),
		SUMMARY("Summary"), GENERATE_SUMMARY("Generate Summary"), INTRACTION_HISTORY("Interaction History"),
		AGENTS("Agents"), SMART_CLUSTERS("Smart Clusters"), CALL("Call"), CHAT("Chat"), SETTINGS("Settings"),
		SELECT_YOUR_TEAM("Select your team"), CHECKLIST_TYPE("Checklist Type"),
		SELECT_CHECKLIST_TYPE("Select Checklist Type"), CONDITIONAL("Conditional"),
		SELECT_CONDITIONAL("Select Conditional"), SEMANTIC("Semantic");

		String value;

		CALLZEN_VALUES(String s) {
			value = s;
		}

		public String VALUE() {
			return value;
		}
	}
	
	public enum INST_EX1 {
		CUSTOMER_GREET("Did the agent greet the customer?"),
		INTRODUCE("Did the agent introduce himself/herself to the customer?"), FOLLOWUP("Is it a follow-up call?"),
		SALE_AGREEMENT("Did the agent inquire about sale agreement package purchase planning?"),
		MAKE_PAYMENT("Did the agent discuss about making payment with the customer?"),
		PAYMENT_ISSUES("Did the customer face issues while making payment?"),
		TRANSFER_CALL("Did the agent transfer the call to the concerned department?"),
		DROP_MAIL("Did the customer agree to drop mail related to service?"),
		DELIVERY_LOCATION("Discussed about delivery location?"),
		INFORM_WORKING_DAYS("Did the agent inform customers about working days?"),
		CONNECT_AND_CONFIRM("Did the agent say,I will connect with you. Okay, if anything is required, yes, sir?");

		private final String keyword;

		INST_EX1(String keyword) {
			this.keyword = keyword;
		}

		public String VALUE() {
			return keyword;
		}
	}

	
	public enum INSTRUCTIONAL_SENTENCE {
		DISCUSS_MAIL_CHANGE("Did customer discuss with agent related to mail ID change or update?"),
		DISCUSS_SUBSCRIPTION("Did agent discuss with customer about subscription?"),
		INQUIRE_DISCOUNT("Did customer inquire about discount?"),
		DISCUSS_PAYMENT("Did agent discuss with customer about payment?"),
		AGREE_TO_PAYMENT("Customer agreed to make payment"),
		INQUIRE_PROPERTY_AVAILABILITY("Did agent ask if the property is available for rent or sale?"),
		CONFIRM_PAYMENT("Customer confirmed payment?"),
		INQUIRE_PAINTING_SERVICES("Did customer ask related to painting services?"),
		GREET_CUSTOMER("Did agent greet the customer?"), REQUEST_CALLBACK("Did customer request a callback?"),
		REQUEST_CANCELLATION("Did customer request for cancellation?"),
		INQUIRY_HOME_INSPECTION("Inquiry about home inspection services."),
		INQUIRE_DISCOUNTED_QUOTATION("Did customer inquire about discounted quotation?"),
		DISCUSS_FREE_ASSISTANCE("Agent discussed the free assistance available."),
		REQUEST_BETTER_DEALS("Customer requested guidance on better deals."),
		INQUIRE_SLOT_AVAILABILITY("Customer asked about slot availability for service."),
		REQUEST_TRIAL_OFFER("Customer requested a trial offer before commitment."),
		OFFER_SERVICE_DEMO("Did agent offer a demo of the service?"),
		INFORM_LIMITED_TIME_OFFER("Agent informed about a limited-time offer."),
		INQUIRE_EXCLUSIVE_DEAL("Customer enquired about exclusive deal?"),
		PROVIDE_PACKAGE_DETAILS("Agent provided details about packages"),
		REQUEST_UPGRADE_SUBSCRIPTION("Customer requested an upgrade plan or subscription"),
		OFFER_CONSULTATION("Agent offered a consultation for better understanding."),
		REQUEST_SERVICE_QUOTE("Customer asked for a quote for the service."),
		PROVIDE_SERVICE_ESTIMATE("Agent provided an estimate for the service."),
		REQUEST_FEATURE_COMPARISON("Customer requested a feature comparison."),
		SCHEDULE_INSPECTION_APPOINTMENT("Agent scheduled an inspection appointment."),
		REQUEST_PAINTING_QUOTATION("Customer requested painting service quotation?"),
		DISCUSS_RENOVATION_SERVICES("Agent discussed renovation services."),
		INQUIRE_MOVING_SERVICES("Customer inquired about moving services."),
		INQUIRE_PACKERS_MOVERS("Customer asked about packers and movers."),
		PROVIDE_SHIPMENT_INFO("Agent provided information about shipment process."),
		INQUIRE_DELIVERY_OPTIONS("Customer asked about delivery options.");

		private final String text;

		INSTRUCTIONAL_SENTENCE(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}
	}

	public static INST_EX1 RANDOM_INST() {
		Random random = new Random();
		return INST_EX1.values()[random.nextInt(INST_EX1.values().length)];
	}

	public static INST_TEST RANDOM_INS_TEST() {
		Random random = new Random();
		return INST_TEST.values()[random.nextInt(INST_TEST.values().length)];
	}

	public enum CDR {
		ID_URL("https://beta-new.callzen.ai/conversation-info?conversationId=8be8b399-3dca-4915-8bea-7fca8491e807&conversationType=call");

		private final String keyword;

		CDR(String keyword) {
			this.keyword = keyword;
		}

		public String VALUE() {
			return keyword;
		}
	}

	

	public enum INST_TEST {
		CUSTOMER_GREET("Did the agent greet the customer?");

		private final String keyword;

		INST_TEST(String keyword) {
			this.keyword = keyword;
		}

		public String VALUE() {
			return keyword;
		}
	}

	public enum SEMANTIC_SENTENCE {
		GOOD_MORNING("Good Morning"), HOW_R_YOU("How are you"), GOOD_EVENING("Good evening"),
		APPOLOGY("Sorry for interrupting"), SEND_MESSAGE("Hello"), UPDATE_PAYMENT_METHOD("update my information");

		private final String sentence;

		SEMANTIC_SENTENCE(String sentence) {
			this.sentence = sentence;
		}

		public String SENTENCE() {
			return sentence;
		}
	}

	public static INSTRUCTIONAL_SENTENCE getRandomInstructional() {
		Random random = new Random();
		return INSTRUCTIONAL_SENTENCE.values()[random.nextInt(INSTRUCTIONAL_SENTENCE.values().length)];
	}

	
	public static KEYWORD_TEXT RANDOM_KYWORD() {
		Random random = new Random();
		return KEYWORD_TEXT.values()[random.nextInt(KEYWORD_TEXT.values().length)];
	}

	public enum KEYWORD_TEXT {
		GOOD_MORNING("Good morning"), GOOD_NIGHT("Good night"), PAYMENT("Payment"), PROPERTY("Property"),
		WRONG_NUMBER("Wrong number"), NO_REQUIREMENT("No requirement"), CALL_BACK("Call back");

		private final String keyword;

		KEYWORD_TEXT(String keyword) {
			this.keyword = keyword;
		}

		public String VALUE() {
			return keyword;
		}
	}

	public enum MOMENT_KEYS {
		MOMENT_ID("momentId"), MOMENT_NAME("momentName"), INSPECTION("instructions"), EXAMPLE("example"),
		USER_ID("userId"), CHUNK_PHRASES("chunkPhrases"), MOMENT_DESCRIPTION("momentDescription"), SOURCE("source"),
		CREATED_BY("createdBy"), MOMENT_EMOTION("momentEmotion"), SEARCH_TYPE("searchType"),
		MUST_CONTAIN("mustContain"), MUST_NOT_CONTAIN("mustNotContain"), CONTAINS_ANY_ONE_OF("containsAnyOneOf"),
		GLOBAL_MOMENT("globalMoment"), CONVERSATION_TYPE("conversationType"), MOMENT_FILTER_SCOPE("momentFilterScope"),
		TEAM_NAME("teamName"), IS_FOLLOW_UP("isFollowUp"), PARENT_MOMENT("parentMoment"), ATTRIBUTES("attributes"),
		ENRICHED_DATA("enrichedData"), AUDIO_URL("audioUrl"), TRANSCRIPT("transcript"), HASH_IDS("hashIds"),
		CLUSTER_ID("clusterId"), SCORE_RANGE("scoreRange"), SUGGESTIONS("suggestions"), VERSION("version"),
		ACTIVATE("activate"), SERVICE_TYPES("serviceTypes"), CITY("city"), PHONE_COUNTRY_CODE("phoneCountryCode"),
		USER_TYPE("userType"), PHRASE("phrase"), START("start"), END("end"), GROUP_ID("groupId"), FORCE_ADD("forceAdd"),
		UPDATED_BY("updatedBy"), DELETED_BY("deletedBy"), IS_ACTIONABLE("isActionable"), SCRIPT("script"),
		ACTIVATED_BY("activatedBy"), ACTIVATION_DATE("activationDate"), ACTIVATION_STATUS("activationStatus"),
		BULK_JOB_STATUS("bulkJobStatus"), CREATED_DATE("createdDate"), IS_SYSTEM_GENERATED("isSystemGenerated"),
		MOMENT_THRESHOLD("momentThreshold"), STATUS("status"), UPDATED_DATE("updatedDate"),
		MOMENT_FEEDBACK_SCORE("momentFeedbackScore"), FEEDBACK_STATUS("feedbackStatus"),
		BULK_JOB_END_STAMP("bulkJobEndStamp"), BULK_JOB_COMPLETION_DATE("bulkJobCompletionDate"),
		INSTRUCTIONAL_FEEDBACK_SCORE("instructionalFeedbackScore"), UNTAG_STATUS("untagStatus"), IS_NEX("isNex"),
		ADDITIONAL_ATTRIBUTES("additionalAttributes"),
		CALLMETA_DISPOSITION_DATA_DISPOSE_NAME("callMeta_dispositionData_disposeName"),
		CALLMETA_DISPOSITION_DATA_FIRST_DISPOSE_NAME("callMeta_dispositionData_firstDisposeName"),
		CALLMETA_OTHERS_CALL_COUNT("callMeta_others_callCount"), SUGGEST_AGAIN("suggestAgain"),
		NSUGGESTIONS("nsuggestions"), ID("id"), CHUNK_ID("chunkId"), START_STAMP("startStamp"), CHANNEL("channel"),
		LANGUAGE("language"), SPEAKER("speaker"), HASH_ID("hashId"), SCORE("score"), ROUND("round"), RANGE("range"),
		FORCED_ADD("forceAdd");

		private final String key;

		MOMENT_KEYS(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	public enum MOMENT_VALUES {
		TRUE("true"), FALSE("false"), POSITIVE("positive"), NEGATIVE("negative"), NEUTRAL("neutral"), NULL("null"),
		AGENT("agent"), CUSTOMER("customer"), SEMANTIC("semantic"), KEYWORDS("keywords"),
		INSTRUCTIONAL("instructional"), FUZZY_MATCH("fuzzy_match"), FUZZY("fuzzy"), CALL("call"), INBOUND("inbound"),
		OUTBOUND("outbound"), MANUAL("manual"), AUTO("auto"), FEEDBACK_PENDING("FEEDBACK_PENDING"),

		KEYWORD("Keyword"), SEMANTIC_("Semantic");

		private final String key;

		MOMENT_VALUES(String key) {
			this.key = key;
		}

		public String getKey() {
			return key;
		}
	}

	public enum CONFIRMATION {
		OK, Ok, CANCEL, Cancel, YES, Yes, NO, No, EXPLORE, SUBSCRIBE, PREVIOUS, NEXT, Next, SUBMIT, Submit, Continue,
		CONTINUE, SEARCH, Proceed, Verify, Collect, Confirm, Done, Add, Update, Accept, ALLOW, Allow
	}

	public enum PAYMENT_OPTION {
		Total, Pay, Advance, Free, Discount, token, FINAL, full, Partial, TOKEN_PAYMENT("TOKEN"),
		FINAL_PAYMENT("final"), PAY_RENT("Pay Rent with Credit Card"), CASH("Cash"), DEBIT_CARD("Debit Card"),
		CREDIT_CARD("Credit Card"), InternetBanking("Internet Banking"), CORPORATE_CARD("Corporate Card"),
		BHIM_UPI("BHIM UPI"), NET_BANKING("Net Banking"), PAYTM("Paytm"), CHEQUE("Cheque"), NEFT("Neft"), PayZapp,
		Simpl, Mobikwik;

		String value;

		PAYMENT_OPTION() {
			value = this.name();
		}

		PAYMENT_OPTION(String s) {
			value = s;
		}

		public String toString() {
			return value;
		}
	}

	public enum AVAILABILITY {
		IMMEDIATE("Immediate"), WITHIN_15_DAYS("Within 15 Days"), WITHIN_30_DAYS("Within 30 Days"),
		AFTER_30_DAYS("After 30 Days");

		String value;

		AVAILABILITY(String s) {
			value = s;
		}

		public String getValue() {
			return value;
		}
	}

	public enum PROFESSION {
		DOESNT_MATTER("Doesn't Matter"), FAMILY("Family"), BACHELORS("Bachelor"), COMPANY("Company"), ANY("Any"),
		STUDENT("Student"), Working_PROFESSIONAL("Working Professional"), ANYONE("Anyone");

		String value;

		PROFESSION(String s) {
			value = s;
		}

		public String getValue() {
			return value;
		}
	}

	public enum CONFIRM {
		YES("Yes"), NO("No"), TRUE("True"), FALSE("False"), True("true"), False("false");

		String value;

		CONFIRM(String s) {
			value = s;
		}

		public String getValue() {
			return value;
		}
	}

	public enum AVAILABLE_DAYS {
		EVERYDAY("Everyday"), WEEKDAYS("Weekdays"), WEEKEND("Weekends");

		String value;

		AVAILABLE_DAYS(String s) {
			value = s;
		}

		public String getValue() {
			return value;
		}
	}

	public enum GENDER {
		MALE, FEMALE, Male, Female, male, female
	}

	public enum CHECKOUT_VALUES {
		AmexCardMsg("Amex cards are not supported currently"), PayNow("Pay Now"), SkipPay("Skip & Pay"),
		IntermilesCard("3671233333333333"), PayZappCorpCard("4329091207169785"), PzCorpCardPwd("1111");

		String value;

		CHECKOUT_VALUES(String s) {
			value = s;
		}

		public String toString() {
			return value;
		}
	}

	public enum CustomField {
		INTERESTED("Interested"), NOT_INTERESTED(" Not Interested"), YET_TO_CONFIRM(" Yet to Confirm");

		String value;

		CustomField(String s) {
			value = s;
		}

		public String getValue() {
			return value;
		}
	}

	public static String CURRENT_DATE() {
		SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyyHHmm");
		return currentDate.format(new Date());
	}

	public static String BEFORE_3_DATE() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyyHHmm");
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime newDateTime = now.minus(3, ChronoUnit.DAYS);
		return newDateTime.format(formatter);
	}

	public static String PLAYWRITE_CURRENT_DATE() {
		SimpleDateFormat currentDate = new SimpleDateFormat("ddMMyyyy HHmm");
		return currentDate.format(new Date());
	}

}
