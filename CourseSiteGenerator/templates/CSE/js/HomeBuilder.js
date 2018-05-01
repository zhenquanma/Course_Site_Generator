//CONSTANTS
var IMG_PATH;
var LEAD_ROLE;
var PM_ROLE;
var DESIGN_ROLE;
var DATA_ROLE;
var MOBILE_ROLE;
var NONE;
var AVAILABLE_RED;
var AVAILABLE_GREEN;
var AVAILABLE_BLUE;
var IMAGE_FOLDER_PATH;


//DATA TO LOAD
var course;
var bannerImagePath;
var leftFooterImagePath;
var rightFooterImagePath;
var stylesheet;
var teams;
var teamNames;
var availableStudents;

function Course(initSubject, initNumber, initSemester, initYear, initTitle, initInstructorName, initInstructorHome) {
    this.subject = initSubject;
    this.number = initNumber;
    this.semester = initSemester;
    this.year = initYear;
    this.title = initTitle;
    this.instructorName = initInstructorName;
    this.instructorHome = initInstructorHome;
}

function Team(initName, initRed, initGreen, initBlue, initTextRed, initTextGreen, initTextBlue) {
    this.name = initName;
    this.red = initRed;
    this.green = initGreen;
    this.blue = initBlue;
    this.students = new Array();
    this.textRed = initTextRed;
    this.textGreen = initTextGreen;
    this.textBlue = initTextBlue;
}

function Student(initLastName, initFirstName, initTeam, initRole) {
    this.lastName = initLastName;
    this.firstName = initFirstName;
    this.team = initTeam;
    this.role = initRole;
}

function SitePage(initUse, initFileName) {
    this.use = initUse;
    this.fileName = initFileName;
}

function initTeamsAndStudents() {
    IMG_PATH = "./images/students/";
    LEAD_ROLE = "Lead Programmer";
    PM_ROLE = "Project Manager";
    DESIGN_ROLE = "Lead Designer";
    DATA_ROLE = "Data Designer";
    MOBILE_ROLE = "Mobile Developer";
    NONE = "none";
    HOME_PAGE = "index.html";
    SYLLABUS_PAGE = "syllabus.html";
    SCHEDULE_PAGE = "schedule.html";
    HWS_PAGE = "hws.html";
    PROJECTS_PAGE = "projects.html";
    AVAILABLE_RED = 120;
    AVAILABLE_GREEN = 200;
    AVAILABLE_BLUE = 70;
    teams = new Array();
    teamNames = new Array();
    availableStudents = new Array();
    var dataFile = "./js/SiteData.json";
    loadData(dataFile);
}

function loadSitePages(data) {
    for (var i = 0; i < data.course_details.site_pages.length; i++) {
        var initUse = data.course_details.site_pages[i].use;
        var initFileName = data.course_details.site_pages[i].file_name;
        var sitePage = new SitePage(initUse, initFileName);
        
        if (sitePage.use == "false") {
        
        	if (sitePage.fileName == HOME_PAGE) {
        		$("#home_link").hide();
        	}
        	else if (sitePage.fileName == SYLLABUS_PAGE) {
        		$("#syllabus_link").hide();
        	}
        	else if (sitePage.fileName == SCHEDULE_PAGE) {
        		$("#schedule_link").hide();
        	}
        	else if (sitePage.fileName == HWS_PAGE) {
        		$("#hws_link").hide();
        	}
        	else if (sitePage.fileName == PROJECTS_PAGE) {
        		$("#projects_link").hide();
        	}
        }
    }
}

function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadSitePages(json);
        loadCourse(json);
	loadTeams(json);
	loadStudents(json);
        loadImages(json);
        loadStylesheet(json);
	initPage();
    });
}



function loadTeams(data) {
    for (var i = 0; i < data.project_data.teams.length; i++) {
	var rawTeam = data.project_data.teams[i];
	var team = new Team(rawTeam.name, rawTeam.red, rawTeam.green, rawTeam.blue,
                                rawTeam.text_red, rawTeam.text_green, rawTeam.text_blue);
	teams[team.name] = team;
	teamNames[i] = team.name;
    }
}

function loadStudents(data) {
    var counter = 0;
    for (var i = 0; i < data.project_data.students.length; i++) {
	var rawStudent = data.project_data.students[i];
	var student = new Student(rawStudent.last_name, rawStudent.first_name, rawStudent.team, rawStudent.role);
	if (student.team == NONE)
	    availableStudents[counter++] = student;
	else {
	    var team = teams[student.team];
	    team.students[student.role] = student;
	}
    }
}

function loadCourse(data) {
    var subject = data.course_details.course_info.subject;
    var number = data.course_details.course_info.number;
    var semester = data.course_details.course_info.semester;
    var year = data.course_details.course_info.year;
    var title = data.course_details.course_info.title;
    var instructorName = data.course_details.course_info.instructor_name;
    var instructorHome = data.course_details.course_info.instructor_home;
    
    course = new Course(subject, number, semester, year, title, instructorName, instructorHome);
}

function loadImages(data) {
    var rawBannerImagePath = data.course_details.banner_dir;
    var rawLeftFooterImagePath = data.course_details.left_footer_image_dir;
    var rawRightFooterImagePath = data.course_details.right_footer_image_dir;
    IMAGE_FOLDER_PATH = "./images/";
    bannerImagePath = IMAGE_FOLDER_PATH + rawBannerImagePath.substring(rawBannerImagePath.lastIndexOf("/") + 1);
    leftFooterImagePath = IMAGE_FOLDER_PATH + rawLeftFooterImagePath.substring(rawLeftFooterImagePath.lastIndexOf("/") + 1);
    rightFooterImagePath = IMAGE_FOLDER_PATH + rawRightFooterImagePath.substring(rawRightFooterImagePath.lastIndexOf("/") + 1);
}

function loadStylesheet(data) {
    stylesheet = "./css/" + data.course_details.stylesheet;
}

function initPage() {
    var courseSubjectNumber = course.subject + " " + course.number;
    var courseSemesterYear = course.semester + " " + course.year;
    var courseTitle = course.title;
    var banner = courseSubjectNumber + " - " + courseSemesterYear + "<br />" + courseTitle;
    
    $('meta[name=description]').attr('content', course.subject);
    $("title").text(courseSubjectNumber + " Home");
    $("#banner").text("");
    $("#banner").append(banner);
    $("#course").text("Welcome to " + courseSubjectNumber + "!");
    $(".sbu_navbar").attr("src", bannerImagePath);
    $(".sunysb").attr("src", leftFooterImagePath);
    $(".sbcs").attr("src", rightFooterImagePath);
    $("#css").attr("href", stylesheet);

    //ADD THE TEAM TABLES
    for (var i = 0; i < teamNames.length; i++) {
	var team = teams[teamNames[i]];
	var teamText =
		"<table style='background:rgb("
		+ team.red + ","
		+ team.green + ","
		+ team.blue + ")'>\n"
		+ "<tr>\n"
		+ "<td colspan='4' style='color:rgb(" 
                + team.textRed + ","
                + team.textGreen + ","
                + team.textBlue + ")'><strong>"
                + team.name + "</strong><br /></td>\n"
		+ "</tr>\n"
		+ "<tr>\n";
	teamText += addStudentToTeam(team.students[LEAD_ROLE], team.text_color);
	teamText += addStudentToTeam(team.students[PM_ROLE], team.text_color);
	teamText += addStudentToTeam(team.students[DESIGN_ROLE], team.text_color);
	teamText += addStudentToTeam(team.students[DATA_ROLE], team.text_color);
        if (team.students[MOBILE_ROLE]) {
            teamText += addStudentToTeam(team.students[MOBILE_ROLE], team.text_color);
        }
	teamText += "</tr>\n"
		+ "</table><br />\n";
	$("#teams_tables").append(teamText);
    }
}

function addStudentToTeam(student, textRed, textGreen, textBlue) {
    var text = "<td style='color:rgb("
            + textRed + ","
            + textGreen + ","
            + textBlue + ")'>\n"
            + "<img src='" + IMG_PATH + student.lastName
	    + student.firstName + ".JPG' "
	    + "alt='" + student.firstName + " " + student.lastName + "' />\n"
	    + "<br clear='all' /><strong>" + student.firstName + " " + student.lastName;
    if (student.role != NONE) {
	text +=	"<br />" + student.role; 
    }
    text += "<br /><br /></strong></td>\n";
    return text;
}


