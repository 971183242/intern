import {Alert} from "./alert.js";

$(document).on("click", ".calendar-cell-active", function (e) {
    console.log("点击了");
    e.stopPropagation();
    let tipStr = '';
    let status = $(this).children(":last").attr("status");
    let that = $(this);
    if (status === undefined) {
        tipStr = '<div class="calendar-replenish" status="1">待审批</div>';
        Alert.applySuccess();
    } else if (status === '1') {
        tipStr = '<div class="calendar-empty"><div> ';
        Alert.cancelSuccess();
        that.find(":last-child").css("display", "none");
    }
    that.append(tipStr);
});

SyntaxHighlighter.all();
new SYSUI({
    Module: '#pageModule',
    Method: [{'module': '#calendar',event: function(set, obj) {
            var m=set.$(set.par.Module);
            m.style.height=set.height()+'px';
            m.style.width=set.width()+'px';
            var scrollDiv=set.$("#pagescroll");
            set.scrollbar(set,m,scrollDiv);//滚动条调用方法
            $("#calendar").calendar({
                data: [], //记录数据
                holiday: [], //假日规划时间
                work:[],//上班时间
                mode: "month",
                width:'80%',
                monthClick: function(e, nextMonth, opts, me) {
                    //开始月份第一天
                    var start = me._cloneDate(opts.newDate);
                    start.setDate(1);
                    // 获取当前月的最后一天
                    me._refreshCalendar(opts.newDate, cycleData);
                }
            });
        }
    }]
});