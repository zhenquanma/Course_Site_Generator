//CONSTANTS
var IMAGE_FOLDER_PATH;

// DATA TO LOAD
var holidays;
var lectures;
var recitations;
var references;
var hws;
var startMondayDate;
var endFridayDate;
var daysInMonth;
var isLeapYear;
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

function loadStylesheet(data) {
    stylesheet = "./css/" + data.course_details.stylesheet;
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

function ScheduleItem(sDate, sTitle, sTopic, sLink) {
    this.date = sDate;
    this.title = sTitle;
    this.topic = sTopic;
    this.link = sLink;
}

function ScheduleDate(sMonth, sDay) {
    this.month = sMonth;
    this.day = sDay;
}

function initSchedule() {
    initDateData();
    var dataFile = "./js/SiteData.json";
    loadData(dataFile);
}


function loadData(jsonFile) {
    $.getJSON(jsonFile, function (json) {
        loadStylesheet(json);
        loadSitePages(json);
	loadJSONData(json);
        loadCourse(json);
        loadImage(json);
        buildScheduleTable();
        addHolidays();
        addLectures();
        addReferences();
        addRecitations();
        addHWs();
    });
}
function initDateData() {
    var currentYear = new Date().getFullYear();
    daysInMonth = new Array();
    if ((currentYear %4) == 0) {
        isLeapYear = true;
        daysInMonth[2] = 29;
    }
    else {
        isLeapYear = false;
        daysInMonth[2] = 28;
    }
    daysInMonth[1] = 31;
    daysInMonth[3] = 31;
    daysInMonth[4] = 30;
    daysInMonth[5] = 31;
    daysInMonth[6] = 30;
    daysInMonth[7] = 31;
    daysInMonth[8] = 31;
    daysInMonth[9] = 30;
    daysInMonth[10] = 31;
    daysInMonth[11] = 30;
    daysInMonth[12] = 31;
}
function loadJSONData(data) {
    // FIRST GET THE STARTING AND ENDING DATES
    var rawStartingMonday = data.schedule_data.starting_monday;
    var rawStartingMondayMonth = rawStartingMonday.substring(5, 7);
    var rawStartingMondayDay = rawStartingMonday.substring(8);
    var startingMondayMonth = parseInt(rawStartingMondayMonth, 10);
    var startingMondayDay = parseInt(rawStartingMondayDay, 10);
    startingMondayDate = new ScheduleDate(startingMondayMonth, startingMondayDay);
    var rawEndingFriday = data.schedule_data.ending_friday;
    var rawEndingFridayMonth = rawEndingFriday.substring(5, 7);
    var rawEndingFridayDay = rawEndingFriday.substring(8);
    var endingFridayMonth = parseInt(rawEndingFridayMonth, 10);
    var endingFridayDay = parseInt(rawEndingFridayDay, 10);
    endingFridayDate = new ScheduleDate(endingFridayMonth, endingFridayDay);
    
    // THEN GET THE HOLIDAYS
    holidays = new Array();
    var numHoliday = 0;
    for (var i = 0; i < data.schedule_data.schedule_items.length; i++) {
        if (data.schedule_data.schedule_items[i].type === "Holiday") {
            var holidayData = data.schedule_data.schedule_items[i];
            var rawMonth = holidayData.date.substring(5, 7);
            var rawDay = holidayData.date.substring(8);
            var month = numberConvert(rawMonth);
            var day = numberConvert(rawDay);
            var holidayDate = new ScheduleDate(month, day);
            var holiday = new ScheduleItem(holidayDate, holidayData.title, "none", holidayData.link);
            holidays[numHoliday] = holiday;
            numHoliday++;
        }
    }
    
    // AND THEN THE LECTURES
    lectures = new Array();
    var numLecture = 0;
    for (var i = 0; i < data.schedule_data.schedule_items.length; i++) {
        if (data.schedule_data.schedule_items[i].type === "Lecture") {
            var lectureData = data.schedule_data.schedule_items[i];
            var rawMonth = lectureData.date.substring(5, 7);
            var rawDay = lectureData.date.substring(8);
            var month = numberConvert(rawMonth);
            var day = numberConvert(rawDay);
            var lectureDate = new ScheduleDate(month, day);
            var lecture = new ScheduleItem(lectureDate, lectureData.title, lectureData.topic, lectureData.link);
            lectures[numLecture] = lecture;
            numLecture++;
        }
    }
    
    // AND THEN THE REFERENCES
    references = new Array();
    var numReference = 0;
    for (var i = 0; i < data.schedule_data.schedule_items.length; i++) {
        if (data.schedule_data.schedule_items[i].type === "Reference") {
            var refData = data.schedule_data.schedule_items[i];
            var rawMonth = refData.date.substring(5, 7);
            var rawDay = refData.date.substring(8);
            var month = numberConvert(rawMonth);
            var day = numberConvert(rawDay);
            var refDate = new ScheduleDate(month, day);
            var ref = new ScheduleItem(refDate, refData.title, refData.topic, refData.link);
            references[numReference] = ref;
            numReference++;
        }
    }
    
    // AND THEN THE RECITATIONS
    recitations = new Array();
    var numRec = 0;
    for (var i = 0; i < data.schedule_data.schedule_items.length; i++) {
        if (data.schedule_data.schedule_items[i].type === "Recitation") {
            var recData = data.schedule_data.schedule_items[i];
            var rawMonth = recData.date.substring(5, 7);
            var rawDay = recData.date.substring(8);
            var month = numberConvert(rawMonth);
            var day = numberConvert(rawDay);
            var recDate = new ScheduleDate(month, day);
            var rec = new ScheduleItem(recDate, recData.title, recData.topic, "none");
            recitations[numRec] = rec;
            numRec++;
        }
    }
    
    // AND THEN THE HWs
    hws = new Array();
    var numHW = 0;
    for (var i = 0; i < data.schedule_data.schedule_items.length; i++) {
        if (data.schedule_data.schedule_items[i].type === "HWs") {
            var hwData = data.schedule_data.schedule_items[i];
            var rawMonth = hwData.date.substring(5, 7);
            var rawDay = hwData.date.substring(8);
            var month = numberConvert(rawMonth);
            var day = numberConvert(rawDay);
            var hwDate = new ScheduleDate(month, day);
            var hw = new ScheduleItem(hwDate, hwData.title, hwData.topic, hwData.link);
            hws[numHW] = hw;
            numHW++;
        }
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

function buildScheduleTable() {
    var countMonth = startingMondayDate.month;
    var countDay = startingMondayDate.day;
    var countDate = new ScheduleDate(countMonth, countDay);
    var table = $("#schedule_table");
    while (firstDateIsBeforeSecond(countDate, endingFridayDate)) {
        table.append(
                  "<tr>"
                + "<th class=\"sch\">MONDAY</th>"
                + "<th class=\"sch\">TUESDAY</th>"
                + "<th class=\"sch\">WEDNESDAY</th>"
                + "<th class=\"sch\">THURSDAY</th>"
                + "<th class=\"sch\">FRIDAY</th>"
                + "</tr>");
        table.append("<tr>");
        for (var i = 0; i < 5; i++) {
            table.append(
                    "<td class=\"sch\" id=\"" + countDate.month + "_" + countDate.day + "\"><strong>" + countDate.month + "/" + countDate.day + "</strong><br /></td>");
            incDate(countDate);
        }
        table.append("</tr>");
        incDate(countDate);
        incDate(countDate);
    }
}

function incDate(dateToInc) {
    dateToInc.day++;
    var maxDays = daysInMonth[dateToInc.month];
    if (dateToInc.day > maxDays) {
        dateToInc.day = 1;
        dateToInc.month++;
    }
}

function firstDateIsBeforeSecond(firstDate, secondDate) {
    if (firstDate.month < secondDate.month)
        return true;
    if ((firstDate.month === secondDate.month)
        && (firstDate.day < secondDate.day))
        return true;
    return false;
}

function addHolidays() {
    for (var i = 0; i < holidays.length; i++) {
        var holiday = holidays[i];
        var cell = $("#" + holiday.date.month + "_" + holiday.date.day);
        cell.addClass("holiday");
        cell.append(
                "<a href=\"" + holiday.link + "\">" + holiday.title + "</a>"
                );
    }
}

function addLectures() {
    for (var i = 0; i < lectures.length; i++) {
        var lecture = lectures[i];
        var textToAppend = "" + lecture.topic;
        if (lecture.link.valueOf() != "none".valueOf()) {
            textToAppend = "<a href=\"" + lecture.link + "\">"
                          + textToAppend
                          + "</a>";
        }
        textToAppend = "<span class=\"lecture\">"
                       + lecture.title + "<br />"
                       + "</span>"
                       + textToAppend;
        var cell = $("#" + lecture.date.month + "_" + lecture.date.day);
        cell.append(textToAppend);
    }    
}
function addReferences() {
    for (var i = 0; i < references.length; i++) {
        var ref = references[i];
        var textToAppend = "<span class=\"tutorial\">" + ref.title + "</span><br />";

        if (ref.link.valueOf() != "none".valueOf()) {
            textToAppend += "<a href=\"" + ref.link + "\">"
                            + ref.topic + "</a>";
        }
        else
            textToAppend += ref.topic;

        textToAppend += "<br /><br />";
        var cell = $("#" + ref.date.month + "_" + ref.date.day);
        cell.append(textToAppend);
    }       
}
function addRecitations() {
    for (var i = 0; i < recitations.length; i++) {
        var rec = recitations[i];
        var textToAppend = "<span class=\"tutorial\">"
                + rec.title
                + "</span><br />"
                + rec.topic;
        var cell = $("#" + rec.date.month + "_" + rec.date.day);
        cell.append(textToAppend);
    }     
}

function addHWs() {
    for (var i = 0; i < hws.length; i++) {
        var hw = hws[i];
        var textToAppend = hw.title;
        if (hw.link.valueOf() == "none".valueOf()) {
            textToAppend = 
                "<span class=\"hw\">"
                + textToAppend
                + "</span><br />";
        }
        else {
            textToAppend =
                "<a href=\"" + hw.link + "\">"
                + textToAppend
                + "</a><br />";
        }
        textToAppend += hw.topic + "<br /><br />";
        var cell = $("#" + hw.date.month + "_" + hw.date.day);
        cell.append(textToAppend);
    }
}