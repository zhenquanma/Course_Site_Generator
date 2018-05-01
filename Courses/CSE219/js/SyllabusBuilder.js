//CONSTANTS
var IMAGE_FOLDER_PATH;

// DATA TO LOAD
var startHour;
var endHour;
var daysOfWeek;
var officeHours;
var undergradTAs;
var graduateTAs;
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
    var instructorName = data.course_details.course_info.instructor_name;
    var instructorHome = data.course_details.course_info.instructor_home;
    
    var courseSubjectNumber = subject + " " + number;
    var courseSemesterYear = semester + " " + year;
    var courseTitle = title;
    var banner = courseSubjectNumber + " - " + courseSemesterYear + "<br />" + courseTitle;
    
    $("title").text(courseSubjectNumber + " Syllabus");
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

function buildOfficeHoursGrid() {
    var dataFile = "./js/SiteData.json";
    loadData(dataFile, loadOfficeHours);
}

function initPage() {
    var dataFile = "./js/SiteData.json";
    loadData(dataFile, loadPage);
}

function loadPage(json) {
    loadStylesheet(json);
    loadSitePages(json);
    loadCourse(json);
    loadImage(json);
}


function loadData(jsonFile, callback) {
    $.getJSON(jsonFile, function(json) {
        callback(json);
    });
}

function loadOfficeHours(json) {
    initDays(json);
    loadInstructor(json);
    addGraduateTAs(json);
    addUndergradTAs(json);
    addOfficeHours(json);
}

function loadInstructor(data) {
    var instructor = data.course_details.course_info.instructor_name;
    var instructorHome = data.course_details.course_info.instructor_home;
    $("#instructor").text(instructor);
    $("#instructor").attr("href",instructorHome);
    $("#instructorImage").hide();
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

function initDays(data) {
    // GET THE START AND END HOURS
    startHour = parseInt(data.ta_data.startHour);
    endHour = parseInt(data.ta_data.endHour);

    // THEN MAKE THE TIMES
    daysOfWeek = new Array();
    daysOfWeek[0] = "MONDAY";
    daysOfWeek[1] = "TUESDAY";
    daysOfWeek[2] = "WEDNESDAY";
    daysOfWeek[3] = "THURSDAY";
    daysOfWeek[4] = "FRIDAY";    
}

function addGraduateTAs(data) {
    var tas = $("#graduate_tas");
    var tasPerRow = 4;
    var numGradTAs = 0;
    var gradTAs = new Array();
    for (var i = 0; i < data.ta_data.tas.length; i++) {
        var isUndergrad = data.ta_data.tas[i].is_undergrad;
        if (isUndergrad != "true") {   
            gradTAs[numGradTAs] = data.ta_data.tas[i];
            numGradTAs++;
        }
    }
    for (var i = 0; i < numGradTAs; ) {
        var text = "";
        text = "<tr>";
        for (var j = 0; j < tasPerRow; j++) {
            text += buildTACell(i, numGradTAs, gradTAs[i]);
            i++;
        }
        text += "</tr>";
        tas.append(text);
    }
}

function addUndergradTAs(data) {
    var tas = $("#undergrad_tas");
    var tasPerRow = 4;
    var numUndergradTAs = 0;
    var undergradTAs = new Array();
    for (var i = 0; i < data.ta_data.tas.length; i++) {
        var isUndergrad = data.ta_data.tas[i].is_undergrad;
        if (isUndergrad == "true") {
            undergradTAs[numUndergradTAs] = data.ta_data.tas[i];
            numUndergradTAs++;
        }
    }
    for (var i = 0; i < numUndergradTAs; ) {
        var text = "";
        text = "<tr>";
        for (var j = 0; j < tasPerRow; j++) {
            text += buildTACell(i, numUndergradTAs, undergradTAs[i]);
            i++;
        }
        text += "</tr>";
        tas.append(text);
    }
}
function buildTACell(counter, numTAs, ta) {
    if (counter >= numTAs)
        return "<td></td>";

    var name = ta.name;
    var abbrName = name.replace(/\s/g, '');
    var email = ta.email;
    var text = "<td class='tas'><img width='100' height='100'"
                + " src='./images/tas/" + abbrName + ".JPG' "
                + " alt='" + name + "' /><br />"
                + "<strong>" + name + "</strong><br />"
                + "<span class='email'>" + email + "</span><br />"
                + "<br /><br /></td>";
    return text;
}
function addOfficeHours(data) {
    for (var i = startHour; i < endHour; i++) {
        // ON THE HOUR
        var textToAppend = "<tr>";
        var amPm = getAMorPM(i);
        var displayNum = i;
        if (i > 12)
            displayNum = displayNum-12;
        textToAppend += "<td>" + displayNum + ":00" + amPm + "</td>"
                    + "<td>" + displayNum + ":30" + amPm + "</td>";
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_00" + amPm
                                + "\" class=\"open\"></td>";
        }
        textToAppend += "</tr>"; 
        
        // ON THE HALF HOUR
        var altAmPm = amPm;
        if (displayNum === 11)
            altAmPm = "pm";
        var altDisplayNum = displayNum + 1;
        if (altDisplayNum > 12)
            altDisplayNum = 1;
                    
        textToAppend += "<tr>";
        textToAppend += "<td>" + displayNum + ":30" + amPm + "</td>"
                    + "<td>" + altDisplayNum + ":00" + altAmPm + "</td>";
            
        for (var j = 0; j < 5; j++) {
            textToAppend += "<td id=\"" + daysOfWeek[j]
                                + "_" + displayNum
                                + "_30" + amPm
                                + "\" class=\"open\"></td>";
        }
        
        textToAppend += "</tr>";
        var cell = $("#office_hours_table");
        cell.append(textToAppend);
    }
    
    // NOW SET THE OFFICE HOURS
    for (var i = 0; i < data.ta_data.officeHours.length; i++) {
	var id = data.ta_data.officeHours[i].day + "_" + data.ta_data.officeHours[i].time;
	var name = data.ta_data.officeHours[i].name;
	var cell = $("#" + id);
	if (name === "Lecture") {
	    cell.removeClass("open");
	    cell.addClass("lecture");
	    cell.html("Lecture");
	}
	else {
	    cell.removeClass("open");
	    cell.addClass("time");
            if (cell.html().toString().length == 0)
                cell.append(name);
            else
        	cell.append("<br />" + name);
	}
    }
}
function getAMorPM(testTime) {
    if (testTime >= 12)
        return "pm";
    else
        return "am";
}



function buildRecitations() {   
    var dataFile = "./js/SiteData.json";
    loadData(dataFile, addRecitations);
}

function addRecitations(data) {
    var recTable = $("#rec_table");
    var rowParity = 0;
    for (var i = 0; i < data.recitation_data.recitations.length; i+=2) {
        var text = "<tr>";
        var rec = data.recitation_data.recitations[i];
        var cellParity = rowParity;
        text += buildRecCell(cellParity, rec);
        cellParity++;
        cellParity %= 2;
        if ((i+1) < data.recitation_data.recitations.length) {
            rec = data.recitation_data.recitations[i+1];
            text += buildRecCell(cellParity, rec);
        }
        else
            text += "<td></td>";
        text += "</tr>";
        recTable.append(text);
        rowParity++;
        rowParity %= 2;
    }
}
function buildRecCell(recClassNum, recData) {
    var text = "<td class='rec_" + recClassNum + "'>"
                + "<table><tr><td valign='top' class='rec_cell'>" 
                + recData.section + "<br />"
                + recData.day_time + "<br />"
                + recData.location + "<br /></td></tr>"
                + "<tr>";
    
    // RECITATION TA #1
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (recData.ta_1 != "none")
        text += "<img src='./images/tas/" 
            + recData.ta_1.replace(/\s/g, '')
            + ".JPG' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + recData.ta_1 + ")<br />";
    else
        text += "TBA";
    text += "</td>";
    
    // RECITATION TA #2
    text += "<td class='ta_cell'><strong>Supervising TA</strong><br />";
    if (recData.ta_2 != "none")
        text += "<img src='./images/tas/" 
            + recData.ta_2.replace(/\s/g, '')
            + ".JPG' width='100' height='100' />"
            + "<br clear='both' />"
            + "(" + recData.ta_2 + ")<br />";            
    else
        text += "TBA";
    text += "</table></td>";
    return text;
}

