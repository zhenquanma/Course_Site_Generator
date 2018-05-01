//CONSTANTS
var IMAGE_FOLDER_PATH;

// DATA TO LOAD
var work;
var daysOfWeek;
var redInc;
var greenInc;
var blueInc;
var bannerImagePath;
var leftFooterImagePath;
var rightFooterImagePath;
var stylesheet;

function loadCourse(data) {
    var subject = data.course_details.course_info.subject;
    var number = data.course_details.course_info.number;
    var semester = data.course_details.course_info.semester;
    var year = data.course_details.course_info.year;
    var title = data.course_details.course_info.title;
    
    var courseSubjectNumber = subject + " " + number;
    var courseSemesterYear = semester + " " + year;
    var courseTitle = title;
    var banner = courseSubjectNumber + " - " + courseSemesterYear + "<br />" + courseTitle;
    
    $("title").text(courseSubjectNumber + " Schedule");
    $("#banner").text("");
    $("#banner").append(banner);
}
function loadImage(data) {
    var rawBannerImagePath = data.course_details.banner_dir;
    var rawLeftFooterImagePath = data.course_details.left_footer_image_dir;
    var rawRightFooterImagePath = data.course_details.right_footer_image_dir;
    IMAGE_FOLDER_PATH = "./images/";
    bannerImagePath = IMAGE_FOLDER_PATH + rawBannerImagePath.substring(rawBannerImagePath.lastIndexOf("/") + 1);
    leftFooterImagePath = IMAGE_FOLDER_PATH + rawLeftFooterImagePath.substring(rawLeftFooterImagePath.lastIndexOf("/") + 1);
    rightFooterImagePath = IMAGE_FOLDER_PATH + rawRightFooterImagePath.substring(rawRightFooterImagePath.lastIndexOf("/") + 1);
    $(".sbu_navbar").attr("src", bannerImagePath);
    $(".sunysb").attr("src", leftFooterImagePath);
    $(".sbcs").attr("src", rightFooterImagePath);
    $("#css").attr("href", stylesheet);
}

function SitePage(initUse, initFileName) {
    this.use = initUse;
    this.fileName = initFileName;
}

function loadSitePages(data) {
    for (var i = 0; i < data.course_details.site_pages.length; i++) {
        var initUse = data.course_details.site_pages[i].use;
        var initFileName = data.course_details.site_pages[i].file_name;
        var sitePage = new SitePage(initUse, initFileName);
        
        if (sitePage.use == "false") {
        
        	if (sitePage.fileName == "index.html") {
        		$("#home_link").hide();
        	}
        	else if (sitePage.fileName == "syllabus.html") {
        		$("#syllabus_link").hide();
        	}
        	else if (sitePage.fileName == "schedule.html") {
        		$("#schedule_link").hide();
        	}
        	else if (sitePage.fileName == "hws.html") {
        		$("#hws_link").hide();
        	}
        	else if (sitePage.fileName == "projects.html") {
        		$("#projects_link").hide();
        	}
        }
    }
}

function loadStylesheet(data) {
    stylesheet = "./css/" + data.course_details.stylesheet;
}

function Work(hSemester, hProjects) {
    this.semester = hSemester;
    this.projects = hProjects;
}
function Project(hName, hStudents, hLink) {
    this.name = hName;
    this.students = hStudents;
    this.link = hLink;
}
function initProjects() {
    var dataFile = "./js/SiteData.json";
    loadData(dataFile);
}

function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadStylesheet(json);
        loadSitePages(json);
        loadCourse(json);
        loadImage(json); 
        loadJSONData(json);
        addProjects();
    });
}
function loadJSONData(data) {
    // LOAD Projects DATA
//    work = new Array();
    var wProjects = new Array();
    var projects = new Array();
    for (var i = 0; i < data.project_data.teams.length; i++) {
        projects[i] =  data.project_data.teams[i];
        var pStudents = new Array();
        var numPStudent = 0;
        for (var j = 0; j < data.project_data.students.length; j++) {
            var student = data.project_data.students[j];
            if (student.team === projects[i].name) {
                pStudents[numPStudent] = student;
                numPStudent++;
            }
        }
        var project = new Project(projects[i].name, pStudents, projects[i].link);
        wProjects[i] = project;
    }
    var semester = data.course_details.course_info.semester + " " + data.course_details.course_info.year;
    work = new Work(semester, wProjects);
}

function addProjects() {
    var div = $("#project_tables");

    var text = "<h3>" + work.semester + " Projects</h3>"
            + "<table><tbody>";
    var projects = work.projects;
    for (var i = 0; i < projects.length; i += 4) {
        var project = projects[i];
        text += "<tr>";
        var num = projects.length;
        if (num >= 4){
            num = 4;
        }
        for (var j = 0; j < num; j++) {
            text += getProjectCell(projects[i + j]);
        }
        text += "</tr>";
    }
    text += "</tbody></table><br /><br />";
    div.append(text);
    
}
function getProjectCell(project) {
     var text = "<td><a href=\""
            + project.link
            + "\"><img src=\"./images/projects/"
            + project.name.replace(/\s/g, '')
            + ".png\" /></a><br />"
            + "<a href=\""
            + project.link
            + "\">" + project.name + "</a><br />"
            + "by ";
    for (var k = 0; k < project.students.length; k++) {
        text += project.students[k].first_name + " " + project.students[k].last_name;
        if ((k + 1) < project.students.length)
            text += ", ";
    }
    text += "<br /><br /></td>";
    return text;
}