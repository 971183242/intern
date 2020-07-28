var currentMonth = new Date().getMonth() + 1;
var currentYear = new Date().getFullYear();
var currentDate = new Date().getDate();
let attendanceList = [];
let nowDateStr = '';
let currentIntern = '';
let checkInStr = '<span class="calendar-replenish" status="1">待审批</span>';
let approvedStr = '<span class="calendar-sign" status="0">已审批</span>';
let rejectedStr = '<span class="calendar-reject" status="2">拒批</span>';

var Calendar = function (element, options) {
    this.el = $(element);
    this.options = $.extend(true, {}, this.options, options);
    this.init();

};

Calendar.prototype = {
    options: {
        mode: "month",
        weekMode: ["一", "二", "三", "四", "五", "六", "日"],
        addholiday:true,//是否自定义节假日时间
        holiday:null,//放假安排
        work:null,//上班时间
        datecoding:[19416, 19168, 42352, 21717, 53856, 55632, 91476, 22176, 39632, 21970, 19168, 42422, 42192, 53840, 119381, 46400, 54944, 44450, 38320, 84343, 18800, 42160, 46261, 27216, 27968, 109396, 11104, 38256, 21234, 18800, 25958, 54432, 59984, 28309, 23248, 11104, 100067, 37600, 116951, 51536, 54432, 120998, 46416, 22176, 107956, 9680, 37584, 53938, 43344, 46423, 27808, 46416, 86869, 19872, 42448, 83315, 21200, 43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616, 46400, 46496, 103846, 38320, 18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984, 27480, 21952, 43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780, 44368, 21977, 19360, 42416, 86390, 21168, 43312, 31060, 27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200, 30371, 38608, 19415, 19152, 42192, 118966, 53840, 54560, 56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 111189, 27936, 44448],  
        signyear : "甲乙丙丁戊己庚辛壬癸",  
        signyears : "子丑寅卯辰巳午未申酉戌亥",  
        sign :"鼠牛虎兔龙蛇马羊猴鸡狗猪",  
        terms: ["小寒", "大寒", "立春", "雨水", "惊蛰", "春分", "清明", "谷雨", "立夏", "小满", "芒种", "夏至", "小暑", "大暑", "立秋", "处暑", "白露", "秋分", "寒露", "霜降", "立冬", "小雪", "大雪", "冬至"],  
        datenate: [0, 21208, 43467, 63836, 85337, 107014, 128867, 150921, 173149, 195551, 218072, 240693, 263343, 285989, 308563, 331033, 353350, 375494, 397447, 419210, 440795, 462224, 483532, 504758],  
        period:"日一二三四五六七八九十",
        lunar:["正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "腊"],  
        lunarperiod:"初十廿卅",  
        festival:{  
            "0101": "元旦节",
            "0214": "情人节",  
            "0305": "学雷锋纪念日",  
            "0308": "妇女节",  
            "0312": "植树节",  
            "0315": "消费者权益日",  
            "0401": "愚人节",  
            "0501": "劳动节",
            "0504": "青年节",  
            "0601": "国际儿童节",  
            "0701": "建党节",
            "0801": "建军节",  
            "0910": "中国教师节",  
            "1001": "国庆节",
            "1224": "平安夜",  
            "1225": "圣诞节"  
        },  
        festivals:{  
            "0101": "*2春节",  
            "0115": "元宵节",  
            "0505": "*1端午节",  
            "0815": "*1中秋节",  
            "0909": "重阳节",  
            "1208": "腊八节",  
            "0100": "除夕"  
        },  
        newDate: new Date(),
        width: null,
        shwoLunar: true,
        showModeBtn: false,
        showEvent: true,
        maxEvent: null
    },

    init: function () {
        var me = this,
            el = me.el,
            opts = me.options,
            cycleData=null;
        el.addClass("calendar");
       // opts.width = el.width();
        //opts.height = el.height();
        typeof (opts.newDate) == "string" ? opts.newDate = me._getDateByString(opts.newDate) : "";
        me._createCalendar();
        //绑定事件
        //changeMode
        el.on("click", ".calendar-mode-select .btn", function (e) {
            e.stopPropagation();
            var modeText = $(this).text();
            var mode = modeText == "月" ? "month" : "year";
            me.changeMode(mode);
        })
        //年份下拉
        el.on("click", ".calendar-year-select", function (e) {
            e.stopPropagation();
            $(".dropdown-month").removeClass("open");
            $(".dropdown-year").toggleClass("open");
            //创建下拉数据
            var yearText = opts.newDate.getFullYear();
            var s = '';
            for (var i = 0; i < 21; i++) {
                if (i == 10) {
                    s += '<li class="year-item active">'
                }
                else {
                    s += '<li class="year-item">'
                }
                s += '<span class="year-check">' + (yearText - 10 + i) + '</span>'
                s += '<span >年</span>'
                s += '</li>'
            }
            me.el.find(".dropdown-year").html(s);
        })
        //年份改变
        el.on("click", ".year-item", function (e) {
        	var cycleData=null;
            e.stopPropagation();
            $(".dropdown-year").removeClass("open");
            var yearText = $(this).text();
            var yearNum = yearText.split("年")[0];
            if (yearNum == opts.newDate.getFullYear()) return;
            opts.newDate.setFullYear(yearNum);
            opts.mode == "month" ? me._refreshCalendar(opts.newDate,cycleData) : me._refreshYearCalendar(opts.newDate);
            $(".calendar-year-text").text(yearText);


        })
        //触发选择月份
        el.on("click", ".calendar-month-select", function (e) {
            e.stopPropagation();
            $(".dropdown-year").removeClass("open");
            $(".dropdown-month").toggleClass("open");
        })
        //月份check事件
        $(document).on("click", ".last-month-item", function (e) {
            e.stopPropagation();
            var monthText = $(this).text();
            if (currentMonth === 1) {
                currentMonth = 13;
                opts.newDate.setFullYear(currentYear - 1);
                currentYear --;
            }
            var monthNum = --currentMonth;
            var beforeDate = opts.newDate.getDate();
            opts.newDate.setMonth(monthNum - 1);
            var afterDate = opts.newDate.getDate();
            if (opts.monthClick) opts.monthClick.call(monthText,e, parseInt(monthNum),opts,me);
            //处理日期30号，切换到2月不存在30号
            if (beforeDate != afterDate) {
                opts.newDate.setDate(opts.newDate.getDate() - 1);
            }
            if (currentDate > 20) {
                nowDateStr = dateFormat("YYYY-mm-dd", new Date(opts.newDate.setMonth(opts.newDate.getMonth()-1)));
            } else {
                nowDateStr = dateFormat("YYYY-mm-dd", opts.newDate);
            }
            getAttendances(currentIntern, nowDateStr);
            initAttendance();
            let lastMonth = currentMonth - 1;
            if (lastMonth === 0) {
                lastMonth = 12;
            }
            $('.date-text').text(currentYear + '年' + lastMonth + '月-' + currentMonth +'月');
        })
        $(document).on("click", ".next-month-item", function (e) {
            e.stopPropagation();
            if (currentDate > 20) {
                if ((currentMonth -1) ===  (new Date().getMonth() + 1) && currentYear === (new Date().getFullYear())) {
                    return
                }
            } else {
                if (currentMonth ===  (new Date().getMonth() + 1) && currentYear === (new Date().getFullYear())) {
                    return
                }
            }

            var monthText = $(this).text();
            if (currentMonth === 12) {
                currentMonth = 0;
                opts.newDate.setFullYear(currentYear + 1);
                currentYear ++;
            }
            var monthNum = ++currentMonth;
            var beforeDate = opts.newDate.getDate();
            opts.newDate.setMonth(monthNum - 1);
            var afterDate = opts.newDate.getDate();
            if (opts.monthClick) opts.monthClick.call(monthText,e, parseInt(monthNum),opts,me);
            //处理日期30号，切换到2月不存在30号
            if (beforeDate != afterDate) {
                opts.newDate.setDate(opts.newDate.getDate() - 1);
            }
            if (currentDate > 20) {
                let date = new Date();
                date.setFullYear(currentYear);
                date.setMonth(opts.newDate.getMonth()-1);
                nowDateStr = dateFormat("YYYY-mm-dd", date);
            } else {
                nowDateStr = dateFormat("YYYY-mm-dd", opts.newDate);
            }
            getAttendances(currentIntern, nowDateStr);
            initAttendance();
            let lastMonth = currentMonth - 1;
            if (lastMonth === 0) {
                lastMonth = 12;
            }
            $('.date-text').text(currentYear + '年' + lastMonth  + '月-' + currentMonth +'月');
        })
    },
    //公开方法
    changeMode: function (mode) {
        var me = this;
        if (mode == me.options.mode) return;
        me.options.mode = mode;
        me._createCalendar();
    },
    getOffsetTop: function(obj) {
		var tmp = obj.offsetTop;
		var val = obj.offsetParent;
		while(val != null) {
			tmp += val.offsetTop;
			val = val.offsetParent;
		}
		return tmp;
	},
	getOffsetLeft: function(obj) {
		var tmp = obj.offsetLeft;
		var val = obj.offsetParent;
		while(val != null) {
			tmp += val.offsetLeft;
			val = val.offsetParent;
		}
		return tmp;
	},
    getViewDate: function (viewDate,cycleData) {
        var me = this,
            opts = me.options,
            mode = opts.mode;
            if(cycleData!=null){
            	var data =cycleData;
            }else{
            	var data = opts.data;
            }
        if (!data || data.length == 0) return [];
        var viewData = {},
            modeYear = viewDate.getFullYear(),
            modeMonth = null;
        if (mode == "month") { modeMonth = viewDate.getMonth() };
        //筛选视图数据并转化未对象 要不要转化为属性
        for (var i = 0; i < data.length; i++) {
            var item = data[i];


            var start = me._getDateByString(item.startDate);

            var year = start.getFullYear();
            var month = start.getMonth();
            var date = start.getDate();
            if (modeMonth && year == modeYear && modeMonth == month) {
                if (!viewData[date]) viewData[date] = [];
                viewData[date].push(item);
            }
            else if (!modeMonth && year == modeYear) {
                if (!viewData[month]) viewData[month] = [];
                viewData[month].push(item);

            }

        }

        return viewData;

    },
    _getDateByString: function (stringDate) {
        var me = this;
        var timearr = stringDate.replace(" ", ":").replace(/\:/g, "-").split("-");
        var timestr = ""+timearr[0]+"-" + timearr[1] + "-" + timearr[2];
        var year = timestr.split("-")[0];
        var month = parseInt(stringDate.split("-")[1]) - 1;
        var date = timestr.split("-")[2];
        return new Date(year, month, date);
    },
    Width: function() {
		return self.innerWidth || document.documentElement.clientWidth || document.body.clientWidth;
	},
	Height: function() {
		return self.innerHeight || document.documentElement.clientHeight || document.body.clientHeight;
	},
    //私有方法
    _createCalendar: function () {
        var me = this;
        var dateMode = me.options.mode;
        me._createView()
        //dateMode == "year" ? me._createYearView() : me._createMonthView();
    },
    _createView: function () {
        var me = this,
            el = me.el,
            opts = me.options,
            mode = opts.mode,
            newDate = opts.newDate,
            w = opts.width,
            h = opts.height,
            cycleData=null,
            html = '';
        html += me._createToolbar();
        html += '<div class="calendar-body">';
        html += '<table class="calendar-table" cellspacing="0">'
        if (mode == "month") {
            html += me._createHeader();
        }
        html += me._createBody();
        html += '</table>'
        html += '</div>'
        el.html(html); 
        if (mode == "month") {
            me._refreshCalendar(newDate,cycleData);
        }
        else {
            me._refreshYearCalendar(newDate);
        }
        if(w!=null){
        	 el.css({width:w })
        }else{
        	 el.css({
	        	width:me.Width(),
	        	height:me.Height()-$(".calendar-header").height()
            })
        }
    },
    /**************时间转换格式方法*************/
	transferDate:function(date) {
		// 年
		var year = date.getFullYear();
		// 月
		var month = date.getMonth() + 1;
		// 日
		var day = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (day >= 0 && day <= 9) {
			day = "0" + day;
		}
		var dateString = year + '' + month + '' + day;
		return dateString;
	},
	calendardata:function(Y,weekDay){
		var newarr=[];
		var me=this,
		    holiday= me.options.holiday,
		    work=me.options.work,
		    P=me.options.datecoding,  
            K=me.options.signyear,
		    J=me.options.signyears,  
		    O=me.options.sign,  
		    L=me.options.terms,  
		    D=me.options.datenate,  
		    B=me.options.period,  
		    H=me.options.lunar,  
		    E=me.options.lunarperiod,  
		    V=me.options.festival,  
	        T=me.options.festivals;
        var c=function(j, i) {  
            var h = new Date((31556925974.7 * (j - 1900) + D[i] * 60000) + Date.UTC(1900, 0, 6, 2, 5));  
            return (h.getUTCDate())  
        }  
        var d=function(k) {  
            var h, j = 348;  
            for (h = 32768; h > 8; h >>= 1) {  
                j += (P[k - 1900] & h) ? 1 : 0  
            }  
            return (j + b(k))  
        }  
        var a=function(h) {  
            return (K.charAt(h % 10) + J.charAt(h % 12))  
        }  
        var b=function(h) {  
            if (g(h)) {  
                return ((P[h - 1900] & 65536) ? 30 : 29)  
            } else {  
                return (0)  
            }  
        }  
		var g=function(h) {  
                return (P[h - 1900] & 15)  
            }  
        var e=function(i, h) {  
                return ((P[i - 1900] & (65536 >> h)) ? 30 : 29)  
            }  
        var C=function(m){  
                var k, j = 0, arr=[], 
                h = 0;  
                var l = new Date(1900, 0, 31);  
                var n = (m - l) / 86400000;  
                arr.dayCyl = n + 40;  
                arr.monCyl = 14;  
                for (k = 1900; k < 2050 && n > 0; k++) {  
                    h = d(k);  
                    n -= h;  
                    arr.monCyl += 12  
                }  
                if (n < 0) {  
                    n += h;  
                    k--;  
                    arr.monCyl -= 12  
                }  
                arr.year = k;  
                arr.yearCyl = k - 1864;  
                j = g(k);  
                arr.isLeap = false;  
                for (k = 1; k < 13 && n > 0; k++) {  
                    if (j > 0 && k == (j + 1) && arr.isLeap == false) {--k;  
                        arr.isLeap = true;  
                        h = b(arr.year)  
                    } else {  
                        h = e(arr.year, k)  
                    }  
                    if (arr.isLeap == true && k == (j + 1)) {  
                        arr.isLeap = false  
                    }  
                    n -= h;  
                    if (arr.isLeap == false) {  
                        arr.monCyl++  
                    }  
                }  
                if (n == 0 && j > 0 && k == j + 1) {  
                    if (arr.isLeap) {  
                        arr.isLeap = false  
                    } else {  
                        arr.isLeap = true; --k; --arr.monCyl  
                    }  
                }  
                if (n < 0) {  
                    n += h; --k; --arr.monCyl  
                }  
                arr.month = k;  
                arr.day = n + 1;
                return arr;
         
            }
            var G=function(h) {  
                return h < 10 ? "0" + h: h  
            }  
            var f=function(i, j) {  
                var h = i;  
                return j.replace(/dd?d?d?|MM?M?M?|yy?y?y?/g,  
                function(k) {  
                    switch (k) {  
                    case "yyyy":  
                        var l = "000" + h.getFullYear();  
                        return l.substring(l.length - 4);  
                    case "dd":  
                        return G(h.getDate());  
                    case "d":  
                        return h.getDate().toString();  
                    case "MM": 
                        if(weekDay==1){
                        	return G((h.getMonth()));  
                        }else{
                        	return G((h.getMonth() + 1));  
                        }
                    case "M":  
                       if(weekDay==1){
                        	return G((h.getMonth()));  
                        }else{
                        	return G((h.getMonth() + 1));  
                        } 
                    }  
                })  
            }  
            var Z=function(i, h) {  
                var j;  
                switch (i, h) {  
                case 10:  
                    j = "初十";  
                    break;  
                case 20:  
                    j = "二十";  
                    break;  
                case 30:  
                    j = "三十";  
                    break;  
                default:  
                    j = E.charAt(Math.floor(h / 10));  
                    j += B.charAt(h % 10)  
                }  
                return (j)  
            }
            newarr.date = Y;  
            newarr.isToday = false;  
            newarr.isRestDay = false;  
            newarr.solarYear = f(Y, "yyyy");  
            newarr.solarMonth = f(Y, "M");  
            newarr.solarDate = f(Y, "d");  
            newarr.solarWeekDay = Y.getDay();  
            newarr.solarWeekDayInChinese = "星期" + B.charAt(newarr.solarWeekDay);  
            var X =C(Y);  
            newarr.lunarYear = X.year;  
            newarr.shengxiao = O.charAt((newarr.lunarYear - 4) % 12);  
            newarr.lunarMonth = X.month;  
            newarr.lunarIsLeapMonth = X.isLeap;  
            newarr.lunarMonthInChinese = newarr.lunarIsLeapMonth ? "闰" + H[X.month - 1] : H[X.month - 1];  
            newarr.lunarDate = X.day;  
            newarr.showInLunar = newarr.lunarDateInChinese = Z(newarr.lunarMonth, newarr.lunarDate);  
            if (newarr.lunarDate == 1) {  
                newarr.showInLunar = newarr.lunarMonthInChinese + "月"  
            }  
            newarr.ganzhiYear = a(X.yearCyl);  
            newarr.ganzhiMonth = a(X.monCyl);  
            newarr.ganzhiDate = a(X.dayCyl++); 
            newarr.holidayDate="";
            newarr.jieqi = "";  
            newarr.restDays = 0;  
            if (c(newarr.solarYear, (newarr.solarMonth - 1) * 2) == f(Y, "d")) {  
                newarr.showInLunar = newarr.jieqi = L[(newarr.solarMonth - 1) * 2]  
            }  
            if (c(newarr.solarYear, (newarr.solarMonth - 1) * 2 + 1) == f(Y, "d")) {  
                newarr.showInLunar = newarr.jieqi = L[(newarr.solarMonth - 1) * 2 + 1]  
            }  
            if (newarr.showInLunar == "清明") {  
                newarr.showInLunar = "清明节";  
                newarr.restDays = 1  
            }  
            newarr.solarFestival = V[f(Y, "MM") + f(Y, "dd")];  
            if (typeof newarr.solarFestival == "undefined") {  
                newarr.solarFestival = ""  
            } else {  
                if (/\*(\d)/.test(newarr.solarFestival)) {  
                    newarr.restDays = parseInt(RegExp.$1);  
                    newarr.solarFestival = newarr.solarFestival.replace(/\*\d/, "")  
                }  
            } 
           
            newarr.showInLunar = (newarr.solarFestival == "") ? newarr.showInLunar: newarr.solarFestival;  
            newarr.lunarFestival = T[newarr.lunarIsLeapMonth ? "00": G(newarr.lunarMonth) + G(newarr.lunarDate)];  
            if (typeof newarr.lunarFestival == "undefined") {  
                newarr.lunarFestival = ""  
            } else {  
                if (/\*(\d)/.test(newarr.lunarFestival)) {  
                    newarr.restDays = (newarr.restDays > parseInt(RegExp.$1)) ? newarr.restDays: parseInt(RegExp.$1);  
                    newarr.lunarFestival = newarr.lunarFestival.replace(/\*\d/, "")  
                }  
            }  
            if (newarr.lunarMonth == 12 && newarr.lunarDate == e(newarr.lunarYear, 12)) {  
                newarr.lunarFestival = T["0100"];  
                newarr.restDays = 1  
            }  
            newarr.showInLunar = (newarr.lunarFestival == "") ? newarr.showInLunar: newarr.lunarFestival;  
            newarr.showInLunar = (newarr.showInLunar.length > 4) ? newarr.showInLunar.substr(0, 4) + "...": newarr.showInLunar ;
            if(holiday!=null){
            	holiday.myMap(function(eve,i){
	            	var name=eve.holiday_name;
	            	var lt=eve.holiday_time;
	            	for(var t=0;t<lt.length;t++){
	            		var startDate = me._getDateByString(lt[t]); // 日期变换
	            		var year = startDate.getFullYear();
			            var month = startDate.getMonth()+1;
			            var day = startDate.getDate();
			            if(newarr.solarYear==year){
			            	if(newarr.solarMonth==month){
			            		if(newarr.solarDate ==day ){
			            			newarr.holidayDate="holiday";
			            		}
			            	}
			            }
	            	}
	            });
            }
            
	        for(var w=0;w<work.length;w++){
	        		var startDate = me._getDateByString(work[w]); // 日期变换
	        		var year = startDate.getFullYear();
		            var month = startDate.getMonth()+1;
		            var day = startDate.getDate();
		            if(newarr.solarYear==year){
		            	if(newarr.solarMonth==month){
		            		if(newarr.solarDate ==day ){
		            			newarr.holidayDate="work";
		            		}
		            	}
		            }
	        }
            return newarr
	},
	structure:function(date,X){
		 var me=this;
            X.lines = 0;  
            X.dateArray = new Array(82);
            var Y=function(a) {  
                return (((a % 4 === 0) && (a % 100 !== 0)) || (a % 400 === 0))  
            }  
            var G=function(a, b) {  
                return [31, (Y(a) ? 29 : 28), 31, 30, 31, 30, 31, 31, 30, 31, 30, 31][b]  
            }  
            var C=function(a, b) {  
                a.setDate(a.getDate() + b);  
                return a  
            }  
            var Z=function(a) {  
                var f = 0;
                var c = me.calendardata(new Date(a.solarYear, a.solarMonth, 1)); 
                var d = (c.solarWeekDay - 1 == -1) ? 6 : c.solarWeekDay-1;
                d = d +14;
                X.lines = Math.ceil((d + G(a.solarYear, a.solarMonth)) / 7);  
                for (var e = 0; e < X.dateArray.length; e++) {  
                    if (c.restDays != 0) {  
                        f = c.restDays  
                    }  
                    if (f > 0) {  
                        c.isRest = true  
                    }  
                    if (d-->0) {
                    	var days=parseInt(a.solarDate)+d;
                    	var o = me.calendardata(new Date(a.solarYear, a.solarMonth-1,days)); 
                        X.dateArray[e] =o;    
                    }else{  
	                    var b = me.calendardata(new Date());  
	                    if (c.solarYear == b.solarYear && c.solarMonth == b.solarMonth && c.solarDate == b.solarDate) {  
	                        c.isToday = true  
	                    }  
	                    X.dateArray[e] = c;  
	                    c = me.calendardata(C(c.date, 1));  
	                    f--  
                    }
                }  
            }  
            Z(date);  
	},
    _createToolbar: function () {
        var me = this,
            newDare = me.options.newDate,
            mode = me.options.mode,
            showModeBtn = me.options.showModeBtn,
            s = '';

        var year = newDare.getFullYear();
        var month = newDare.getMonth() + 1;

        s += '<div class="calendar-header" style="text-align: center !important;letter-spacing: 2px;font-size: 28px;font-weight: 600;padding: 8px 16px 5px 0">';
        // s += '<div class="calendar-header">'
        // s += '<div class="calendar-select calendar-year-select" >'
        // s += '<span class="calendar-year-text"> ' + year + '年</span >'
        // s += '<span class="calendar-icon"><i class="iconfont">∨</i></span>'
        // s += '<ul id="dropdown-year" class="dropdown-year">'
        // s += '</ul>'
        s += '<span class="date-text">2020年' + (currentMonth -1) + '月-' + currentMonth +'月</span>'
        s += '</div > '

        if (mode == "month") {
            // s += '<div class="calendar-select  calendar-month-select">'
            // s += '<span class="calendar-month-text"> ' + month + '月</span >'
            // s += '<span class="calendar-icon"><i class="iconfont">∨</i></span>'
            //
            // //创建月份下拉(写死)
            // s += '<ul class="dropdown-month">'
            // for (var i = 1; i <= 12; i++) {
            //     s += '<li class="month-item">'
            //     s += '<span class="month-check">' + i + '</span>'
            //     s += '<span >月</span>'
            //     s += '</li>'
            // }
            // s += '</ul>'
            // s += '</div > '
        }

        if (showModeBtn) {
            s += '<div class="calendar-select  calendar-mode-select">'
            s += '<div class="btn-group">'

            if (mode == "month") {
                s += '<span  class="btn calendar-select-active">月</span>'
                s += '<span class="btn">年</span>'
            }
            else {
                s += '<span  class="btn">月</span>'
                s += '<span class="btn calendar-select-active">年</span>'
            }
            s += '</div>'
            s += '</div>'
        }

        s += '</div >'
        return s;
        
    },
    _createHeader: function () {
        var me = this,
            opts = me.options,
            weekMode = opts.weekMode;
        var s = '<thead><tr>'
        weekMode.forEach(function (item) {
            s += ' <th class="calendar-column-header" title="周' + item + '"><span class="calendar-column-header-inner">' + item + '</span></th>'
        })
        s += '</thead></tr>'
        return s;
    },
    _createBody: function () {
        var me = this;
        var s = ' <tbody class="calendar-tbody">'
        s += '</tbody>'
        return s;
    },
    _refreshYearCalendar: function (newDate) {
        var me = this,
            showEvent = me.options.showEvent,
            maxEvent = me.options.maxEvent,
            s = '';

        //每次都重新获取会不会影响性能
        me.viewData = viewData = me.getViewDate(newDate);

        var year = newDate.getFullYear(), month = newDate.getMonth();
        //四行三列
        for (var i = 0; i < 4; i++) {
            s += '<tr>'
            for (var l = 0; l < 3; l++) {
                renderMonth = i * 3 + l;
                if (month == renderMonth) {
                    s += '<td title="' + year + '年' + (renderMonth + 1) + '月" class="calendar-cell calendar-thisMonth">';
                }
                else {
                    s += '<td title="' + year + '年' + (renderMonth + 1) + '月" class="calendar-cell">';
                }
                s += '<div class="calendar-date">';
                s += '<div class="calendar-value">' + (renderMonth + 1) + '月</div>';
                s += '<div class="calendar-content"><ul class="events">'

                if (showEvent && viewData[renderMonth]) {
                    if (maxEvent && viewData[renderMonth].length > maxEvent) {
                        s += viewData[renderMonth].length + "个事件";
                    }
                    else {
                        viewData[renderMonth].forEach(function (item) {
                            s += '<li><span>' + item.name + '</span></li>'
                        })
                    }
                }
                s += '</ul ></div > ';
                s += '</div></td>';
            }
            s += '</tr>'
        }

        me.el.find(".calendar-tbody").html(s);
    }, 
    _refreshCalendar: function (newDate,cycleData) {
        var me = this,
            showEvent = me.options.showEvent,
            maxEvent = me.options.maxEvent,
            holiday= me.options.holiday,
            h= me.options.height,
            el = me.el,
            s = '',
            X={};
        if(cycleData!=null){
        	me.viewData = viewData = me.getViewDate(newDate,cycleData);
        }else{
        	me.viewData = viewData = me.getViewDate(newDate,null);
        }
        var _newDate = me._cloneDate(newDate);
        //当前date
        var nowNum = _newDate.getDate();
        var week=[6,0];
        //第一天周几
        _newDate.setDate(1);
        
        var weekDay = _newDate.getDay() == 0 ? 7 : _newDate.getDay();
        //视图第一天
        var viewDate = me._cloneDate(_newDate);
        viewDate.setDate(viewDate.getDate() - weekDay + 1 - 14);
        //当前第几周/行 (暂不处理)
        var spileDate = (newDate.getTime() - viewDate.getTime()) / (1000 * 60 * 60 * 24);
        renderDate = me._cloneDate(viewDate);
        var A=me.calendardata(renderDate,weekDay);
        me.structure(A,X);
        var ins= X.dateArray;
        //固定五行
        for (var i = 0; i < 6; i++) {
            s += '<tr>'
            for (var l = 0; l < 7; l++) {
                var year = renderDate.getFullYear();
                var month = renderDate.getMonth() + 1;
                var date = renderDate.getDate();
                var day = renderDate.getDay();

                if ((renderDate.getMonth() < newDate.getMonth() && date < 21) || (newDate.getMonth()===0 && renderDate.getMonth() ===11 && date < 21)) {
                    s += '<td title="' + year + '年' + month + '月' + date + '日" class="calendar-cell calendar-last-month-cell">';
                    s += '<div class="calendar-date">';
                }
                else if (renderDate.getMonth() >= newDate.getMonth() && date > 20&&(renderDate.getMonth() !== 11)) {
                    s += '<td title="' + year + '年' + month + '月' + date + '日" class="calendar-cell calendar-next-month-cell">';
                    s += '<div class="calendar-date">';
                }
                else if (date == nowNum) {
                    s += '<td title="' + year + '年' + month + '月' + date + '日" class="calendar-cell calendar-today calendar-cell-active" attendanceId="" relTime="'+ renderDate +'" id="'+ month + date +'">';
                    s += '<div class="calendar-date-active calendar-date">';
                }
                // else if(day==week[0] || day==week[1]){
                // 	   s += '<td title="' + year + '年' + month + '月' + date + '日" class="calendar-cell calendar-weekend">';
                // }
                else {
                    s += '<td title="' + year + '年' + month + '月' + date + '日" class="calendar-cell calendar-cell-active" attendanceId="" relTime="'+ renderDate +'" id="'+ month + date + '">';
                    s += '<div class="calendar-date-active calendar-date">';
                }
                s += '<div class="calendar-value">' + date + '</div>';
                for (var a = 0; a < ins.length; a++) { 
	                if(ins[a]!=null){
		                if(ins[a].solarMonth==month){
		                	if(ins[a].solarDate==date){
		                		s += '<div class="value">' + ins[a].showInLunar + '</div>';
		                		// if(ins[a].holidayDate=="holiday"){
		                		// 	s += '<div class="calendar-holiday">休</div><span class="calendar-holiday-bg"></span>';
		                		// }else if(ins[a].holidayDate=="work"){
		                		//
		                		// 	s += '<div class="calendar-work">班</div><span class="calendar-work-bg"></span>';
		                		// }
		                	}
		                }
	                }
                }
                s += '<div class="calendar-content"><ul class="events">'
                if (showEvent && viewData[date] && renderDate.getMonth() == newDate.getMonth()) {
                    if (maxEvent && viewData[date].length > maxEvent) {
                        s += viewData[date].length + "个事件";
                    }
                    else {
                        viewData[date].forEach(function (item) {
                            s += '<li><span>' + item.name + '</span></li>'
                        })
                    }
                }
                s += '</ul ></div > ';
                s += '</div></td>';
                renderDate.setDate(renderDate.getDate() + 1);
            }
            s += '</tr>'
        }
        
        me.el.find(".calendar-tbody").html(s);
    },
    _cloneDate: function (date) {
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    },

}
$.fn.calendar = function (options) {

    var isSTR = typeof options == "string",
        args, ret;

    if (isSTR) {
        args = $.makeArray(arguments)
        args.splice(0, 1);
    }

    var name = "calendar",
        type = Calendar;

    var jq = this.each(function () {
        var ui = $.data(this, name);

        if (!ui) {
            ui = new type(this, options);
            $.data(this, name, ui);
        }
        if (isSTR) {
            ret = ui[options].apply(ui, args);
        }
    });

    return isSTR ? ret : jq;
};
(function(w) {
	/** 
	 * map遍历数组 
	 * @param fn [function] 回调函数； 
	 * @param context [object] 上下文； 
	 */
	Array.prototype.myMap = function(fn, context) {
		context = context || window;
		var ary = [];
		if(Array.prototype.map) {
			ary = this.map(fn, context);
		} else {
			for(var i = 0; i < this.length; i++) {
				ary[i] = fn.apply(context, [this[i], i, this]);
			}
		}
		return ary;
	};
})(window);
/**
 *  根据internId获得签到信息
 */
let getAttendances = function(internId, date) {
    $.ajax({
        url: '/attendance/searchPeriod?userId=' + internId + '&date=' + date,
        type: 'get',
        dataType: 'json',
        contentType: 'application/json;charset=UTF-8',
        async: false,
        success: function (data) {
            if (data.code === 1) {
                attendanceList = data.data;
            } else {
                attendanceList = [];
            }
        },
        error: function (XMLHttpRequest) {
        }
    })
};

/**
 *  初始化签到信息
 */
let initAttendance = function() {
    for (let i = 0; i< attendanceList.length; i++) {
        let date = new Date(attendanceList[i].workDay);
        let attendanceStatus = attendanceList[i].attendanceStatus;
        let id = '#' + (date.getMonth() + 1) + date.getDate();
        $(id).attr("attendanceId", attendanceList[i].attendanceId)
        if (attendanceStatus === "CheckedIn"){
            $(id).append(checkInStr);
        }else if (attendanceStatus === 'Approved') {
            $(id).append(approvedStr);
        } else if (attendanceStatus === 'Rejected') {
            $(id).append(rejectedStr);
        }
    }
};

/**
 *  移除签到信息
 */
let removeAttendance = function () {
    for (let i = 0; i< attendanceList.length; i++) {
        let date = new Date(attendanceList[i].workDay);
        let id = '#' + (date.getMonth() + 1) + date.getDate();
        $(id).attr("attendanceId", "");
        $(id).find("span").remove();
    }
};
