package csg;

/**
 *
 * @author Zhenquan Ma <zhenquan.ma@stonybrook.edu>
 */
public enum CSGeneratorProp {
    // FOR SIMPLE OK/CANCEL DIALOG BOXES
    OK_PROMPT,
    CANCEL_PROMPT,
    
    // THESE ARE FOR TEXT PARTICULAR TO THE APP'S WORKSPACE CONTROLS
    TAS_HEADER_TEXT,
    NAME_COLUMN_TEXT,
    EMAIL_COLUMN_TEXT,
    UNDERGRAD_COLUMN_TEXT,
    NAME_PROMPT_TEXT,
    EMAIL_PROMPT_TEXT,
    ADD_BUTTON_TEXT,
    UPDATE_BUTTON_TEXT,
    CLEAR_BUTTON_TEXT,
    OFFICE_HOURS_SUBHEADER,
    OFFICE_HOURS_TABLE_HEADERS,
    DAYS_OF_WEEK,
    START_TIME_TEXT,
    END_TIME_TEXT,
    SUBJECTS,
    SEMESTERS,
    TYPES,

    
    // THESE ARE FOR ERROR MESSAGES PARTICULAR TO THE APP
    MISSING_TA_NAME_TITLE,
    MISSING_TA_NAME_MESSAGE,
    MISSING_TA_EMAIL_TITLE,
    MISSING_TA_EMAIL_MESSAGE,
    TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE,
    TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE,
    INVALID_EMAIL_TITLE,
    INVALID_EMAIL,
    
    MISSING_SECTION_TITLE,
    MISSING_SECTION_MESSAGE,
    MISSING_INSTRUCTOR_TITLE,
    MISSING_INSTRUCTOR_MESSAGE,
    MISSING_DAY_TIME_TITLE,
    MISSING_DAY_TIME_MESSAGE,
    MISSING_LOCATION_TITLE,
    MISSING_LOCATION_MESSAGE,
    MISSING_TA_TITLE,
    MISSING_TA_MESSAGE,
    
    RECITATION_NOT_UNIQUE_TITLE,
    RECITATION_NOT_UNIQUE_MESSAGE,
    
    // PAGE NAME TEXT
    COURSE_DETAILS_TXT,
    TA_DATA_TXT,
    RECITATION_DATA_TXT,
    SCHEDULE_DATA_TXT,
    PROJECT_DATA_TXT,
    
    //TEXT FOR COURSE DETAIL PAGE
    COURSE_INFO_TEXT,
    SUBJECT_TEXT,
    NUMBER_TEXT,
    SEMESTER_TEXT,
    YEAR_TEXT,
    TITLE_TEXT,
    INSTRUCTOR_NAME_TEXT,
    INSTRUCTORS_HOME_TEXT,
    EXPORT_DIR_TEXT,
    CHANGE_BUTTON_TEXT,
    USE_COLUMN_TEXT,
    NAVBAR_COLUMN_TEXT,
    FILENAME_COLUMN_TEXT,
    SCRIPT_COLUMN_TEXT,
    
    //TEXT FOR SITE TEMPLATE
    SITE_TEMPLATE_TEXT,
    INSTRUCTION_TEXT,
    SELECT_TEMPLATE_DIR_BUTTON_TEXT,
    SITE_PAGES_TEXT,
    
    //TEXT FOR PAGE STYLE
    PAGE_STYLE_TEXT,
    BANNER_SCHOOL_IMAGE_TEXT,
    LEFT_FOOTER_IMAGE_TEXT,
    RIGHT_FOOTER_IMAGE_TEXT,
    STYLESHEET_TEXT,
    NOTE_MESSAGE,
    
    //RECITATION PAGE
    RECITATION_HEARDER_TEXT,
    ADD_EDIT_HEADER_TEXT,
    SECTION_TEXT,
    INSTRUCTOR_TEXT,
    DAY_TIME_TEXT,
    LOCATION_TEXT,
    SUPERVISING_TA_TEXT,
    ADD_UPDATE_BUTTON_TEXT,
    TA_TEXT,
    TA1_TEXT,
    TA2_TEXT,
    
    //SCHEDULE PAGE
    SCHEDULE_HEADER_TEXT,
    CALENDAR_BOUNDARIES_HEADER_TEXT,
    STARTING_MONDAY_TEXT,
    ENDING_FRIDAY_TEXT,
    SCHEDULE_ITEMS_HEADER_TEXT,
    TYPE_TEXT,
    DATE_TEXT,
    TIME_TEXT,
    TOPIC_TEXT,
    LINK_TEXT,
    CRITERIA_TEXT,
    SCHEDULE_TYPE_HOLIDAY,
    SCHEDULE_TYPE_LECTURE,
    SCHEDULE_TYPE_HW,
    SCHEDULE_TYPE_REFERENCE,
    SCHEDULE_TYPE_RECITATION,
    
    
    //PROJECCT PAGE
    PROJECT_HEADER_TEXT,
    TEAMS_HEADER_TEXT,
    COLOR_COLUMN_TEXT,
    TEXT_COLOR_COLUMN_TEXT,
    LINK_COLUMN_TEXT,
    NAME_LABEL_TEXT,
    COLOR_LABEL_TEXT,
    TEXT_COLOR_LABEL_TEXT,
    LINK_LABEL_TEXT,
    STUDENTS_HEADER_TEXT,
    FIRST_NAME_COLUMN_TEXT,
    LAST_NAME_COLUMN_TEXT,
    TEAM_COLUMN_TEXT,
    ROLE_COLUMN_TEXT,
    FIRST_NAME_LABEL_TEXT,
    LAST_NAME_LABEL_TEXT,
    TEAM_LABEL_TEXT,
    ROLE_LABEL_TEXT,
    
    //IMAGE LOADING
    CHOOSE_IMAGE_TITLE,
    CHOOSE_EXPORT_DIR_TITLE,
    CHOOSE_TEMPLATE_DIR_TITLE
    
}