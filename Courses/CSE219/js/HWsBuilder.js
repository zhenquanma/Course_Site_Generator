//CONSTANTS
var IMAGE_FOLDER_PATH;

// DATA TO LOAD
var hws;
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

function HW(hDate, hTime, hTitle, hTopic, hLink, hCriteria) {
    this.date = hDate;
    this.time = hTime;
    this.title = hTitle;
    this.topic = hTopic;
    this.link = hLink;
    this.criteria = hCriteria;
}
function ScheduleDate(sMonth, sDay) {
    this.month = sMonth;
    this.day = sDay;
}
function initHWs() {
    redInc = 10;
    greenInc = 10;
    blueInc = 5;
    
    daysOfWeek = new Array(7);
    daysOfWeek[0]=  "Sunday";
    daysOfWeek[1] = "Monday";
    daysOfWeek[2] = "Tuesday";
    daysOfWeek[3] = "Wednesday";
    daysOfWeek[4] = "Thursday";
    daysOfWeek[5] = "Friday";
    daysOfWeek[6] = "Saturday";
    
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
        addHWs();
    });
}
function loadJSONData(data) {    
    // LOAD HWs DATA
    hws = new Array();
    var numHW = 0;
    var rawHWs = new Array();
    for (var i = 0; i < data.schedule_data.schedule_items.length; i++) {
        if (data.schedule_data.schedule_items[i].type === "HW") {
            rawHWs[numHW] = data.schedule_data.schedule_items[i];
            numHW++;
        }
    }
    for (var i = 0; i < numHW; i++) {
        var hwData = rawHWs[i];
        var rawMonth = hwData.date.substring(5, 7);
        var rawDay = hwData.date.substring(8);
        var month = numberConvert(rawMonth);
        var day = numberConvert(rawDay);
        var hwDate = new ScheduleDate(month, day);
        var hw = new HW(hwDate, hwData.time, hwData.title, hwData.topic, hwData.link, hwData.criteria);
        hws[i] = hw;
    }
}
//CUT THE FIRST 0 IN NUNBER FROM JSON
function numberConvert(rawNumber) {
    if (rawNumber.substring(0, 1) == "0") {
        return rawNumber.substring(1);
    }
    else
        return rawNumber;
}

function addHWs() {
    var tBody = $("#hws");
    var red = 240;
    var green = 240;
    var blue = 255;
    for (var i = 0; i < hws.length; i++) {
        var hw = hws[i];
        var day = hw.date.day;
        var month = hw.date.month;
        var dayOfWeek = getDayOfWeek(day,month);
        
        // THE FIRST CELL
        var textToAppend = "<tr class=\"hw\" style=\"background-color:rgb(" + red + "," + green + "," + blue + ")\">"
                            + "<td class=\"hw\" style=\"padding-right: 60px\">"
                                + "<br />";
        if (hw.link.valueOf() === "none".valueOf()) {
            textToAppend += hw.title;
        }
        else {
            textToAppend += "<a href=\"" + hw.link + "\">" + hw.title + "</a>";
        }
        textToAppend += " - " + hw.topic + "<br /><br /></td>";
        
        // THE SECOND CELL
        textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\">"
                        + "<br />" + dayOfWeek + ", " + month + "/" + day
                        + "<br /><br /><br />"
                        + "</td>";
        if (hw.criteria.valueOf() === "none".valueOf()) {
            textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\"><br />TBA<br /><br /><br /></td>";
        }
        else {
            textToAppend += "<td class=\"hw\" style=\"padding-right: 60px\">"
            + "<a href=\"" + hw.criteria + "\"><br />" + hw.title + " Grading Criteria</a><br /><br /><br /></td>";
        }
                        
        textToAppend += "</tr>";
        tBody.append(textToAppend);
        red -= redInc;
        green -= greenInc;
        blue -= blueInc;
    }
}
function getDayOfWeek(gDay, gMonth) {
    var date = new Date();
    date.setDate(1);
    date.setMonth(gMonth-1);
    date.setDate(gDay);
    return daysOfWeek[date.getDay()];
}