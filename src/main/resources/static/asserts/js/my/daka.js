initSider('/dashboard');

var dakaData = {},
    code = 0;

var timer = setInterval(function () {
    showTime();
}, 1000);


/**
 * 加载时间控件
 * @type {jQuery}
 */
var datepickerInit = $("#dakaTime").datepicker({  //  datetimepicker  datepicker
    locale: 'zh-cn',
    format: 'yyyy-mm-dd ',
    value: currentDate(),
    maxDate: function () {
        var date = new Date();
        date.setDate(date.getDate());
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    },
    footer: true,
    change: function (e) {
        //4-5 0.40改
        $('#downAttendance').css('background', '#3a7dee');
        $('#upAttendance').css('background', '#3a7dee');
        $('#upMsg').html('');
        $('#downMsg').html('');

        code = 0;
        $.getDakaMessage(e.target.value);
        if (code !== 200) {
            if(e.target.value === currentDate()){
                clearInterval(timer);
                timer = setInterval(function () {
                    showTime();
                }, 1000);
            }
            $.getAddress();
        }
    },
    weekStartDay: 1
});


/*模态框显示*/
$('#attendance').on('click', function () {

    $.getDakaMessage(currentDate());
    if (code !== 200) {
        $.getAddress();
    }
    $('#addAttendanceLabel').text("考勤打卡");
    $('#addAttendance').modal('show');

});


/*模态框关闭  触发事件*/
$('body').on('hidden.bs.modal', '.modal', function () {

    $('#downAttendance').css('background', '#3a7dee');
    $('#upAttendance').css('background', '#3a7dee');
    $('#upMsg').html('');
    $('#downMsg').html('');

});


//获取打卡地址
$.getAddress = function () {
    $.ajax({
        url: '/attendance/getDakaAddress',
        dataType: 'json',
        data: {"ip": "117.136.75.218"}, //218.192.3.42   183.253.76.197  202.101.96.65
        async: true,
        method: 'get',
        beforeSend: function (XMLHttpRequest) {
            //请求之前
            $('#downPlace').html('<font size="2" > 正在获取位置。。</font>');
            $('#upPlace').html('<font size="2" > 正在获取位置。。</font>');

            //获取地址期间禁用a标签 ********************************
            $('#downAttendance').css('pointer-events', 'none');
            $('#upAttendance').css('pointer-events', 'none');
        },
        success: function (response) {
            if (response.success) {
                if (response.data != null) {
                    dakaData['isException'] = response.data.isException;
                    dakaData['place'] = response.data.nowPlace;
                }
                $.htmlAddress();
            } else {
                toastr.error(response.errorCode + "  " + response.errorMsg);
            }

        },
        complete: function (XMLHttpRequest, textStatus) {
            //ajax请求结束
            $('#downAttendance').css('pointer-events', 'auto');
            $('#upAttendance').css('pointer-events', 'auto');
        }
    });
};


/**
 * 获取 time打卡信息
 * @param time
 */
$.getDakaMessage = function (time) {
    $.ajax({
        url: '/attendance/getDakaMessage',
        dataType: 'json',
        async: false,
        data: {"time": time},
        method: 'get',
        success: function (response) {
            console.info(response);
            $.hideAll();
            if (response.success) {
                if ((response.errorCode === 250)) {
                    $('#upAttendance').show();
                    $('.span1').html('<font size="4">上班打卡</font>');
                    $('#remark_textarea').show();
                    return false;
                }
                if (response.errorCode === 260) {
                    $('#upMsg').show();
                    $.htmlForgotMsg('upMsg');
                    $('.span1').html('<font size="4">下班打卡</font>');
                    $('#downAttendance').show();
                    $('#remark_textarea').show();
                    return false;
                }
                if (response.errorCode === 270) {
                    $('.span1').html('<font size="4">下班打卡</font>');
                    $('#downAttendance').show();
                    $('#remark_textarea').show();
                    $('#upMsg').show();
                    $.htmlDakaMsg(response.data);
                    return false;
                }
                if (response.errorCode === 280) {
                    code = 200;
                    $.showUpDownMsg();
                    $.htmlForgotMsg('upMsg');
                    $.htmlDakaMsg(response.data);
                    return false;
                }
                if (response.errorCode === 220) {
                    code = 200;
                    $.showUpDownMsg();
                    $.htmlForgotMsg('upMsg');
                    $.htmlForgotMsg('downMsg');
                    $.htmlDakaMsg(response.data);
                    return false;
                }
                if (response.errorCode === 200) {
                    code = 200;
                    $.showUpDownMsg();
                    $.htmlDakaMsg(response.data);
                    return false;
                }
                //4-5 0.40增
                if (response.errorCode === 290) {
                    code = 200;
                    $.showUpDownMsg();
                    $.htmlForgotMsg('upMsg');
                    $.htmlForgotMsg('downMsg');
                    return false;
                }
            } else {
                toastr.error(response.errorCode + " :" + response.errorMsg);
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            //之后
        }
    });
};


/*下班打卡按钮*/
$('#downAttendance').off().on('click', function () {
    dakaData['status'] = '2';
    var remark = $('#remark').val();
    if (remark !== '' && remark !== ' ') {
        dakaData['remark'] = remark;
    }
    $.saveDakaMassage(dakaData);
});

/*上班打卡按钮*/
$('#upAttendance').off().on('click', function () {
    dakaData['status'] = '1';
    var remark = $('#remark').val();
    if (remark !== '' && remark !== ' ') {
        dakaData['remark'] = remark;
    }
    $.saveDakaMassage(dakaData);
});

/**
 * 保存打卡信息
 * @param data  传入的数据
 */
$.saveDakaMassage = function (data) {
    $.ajax({
        url: '/attendance/saveDakaMessage',
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(data),
        async: true,
        method: 'post',
        beforeSend: function (XMLHttpRequest) {
        },
        success: function (response) {
            if (response.success) {
                $('#downAttendance').css('background', '#43c743');
                $('#upAttendance').css('background', '#43c743');
                $('.span1').html('<font size="4">打卡成功</font>');
                $('#downAttendance').css('pointer-events', 'none');
                $('#upAttendance').css('pointer-events', 'none');
                console.info("终止time");
                clearInterval(timer);
            } else {
                toastr.error(response.errorCode + " " + response.errorMsg);
            }
        },
        complete: function (XMLHttpRequest, textStatus) {
            //ajax请求结束
        }
    });
};


$.showUpDownMsg = function () {
    $('#upMsg').show();
    $('#downMsg').show();
};

$.hideAll = function () {
    $('#upMsg').hide();
    $('#downMsg').hide();
    $('#upAttendance').hide();
    $('#downAttendance').hide();
    $('#remark_textarea').hide();
};


/**
 * 将 4 ， 5 等 < 10的数转为 04, 05
 * @param s
 * @returns {string}
 */
function getNow(s) {
    return s < 10 ? '0' + s : s;
}

//绘制时间
function showTime() {
    var myDate = new Date();
    var now = myDate.getFullYear() + '-'
        + getNow(myDate.getMonth() + 1) + "-"
        + getNow(myDate.getDate()) + " "
        + getNow(myDate.getHours()) + ':'
        + getNow(myDate.getMinutes()) + ":"
        + getNow(myDate.getSeconds());
    $('#downTime').html('<font size="2" >' + now + '</font>');
    $('#upTime').html('<font size="2" >' + now + '</font>');
}

/**
 * 获取当前的时间
 * @returns {string}  格式为 yyyy-mm-dd
 */
function currentDate() {
    var myDate = new Date();
    var year = myDate.getFullYear();        //获取当前年
    var month = myDate.getMonth() + 1;   //获取当前月
    var date = myDate.getDate();            //获取当前日
    var h = myDate.getHours();              //获取当前小时数(0-23)
    var m = myDate.getMinutes();          //获取当前分钟数(0-59)
    var s = myDate.getSeconds();
    var now = year + '-' + getNow(month) + "-" + getNow(date);
    return now;
}


/**
 * 绘制地理位置
 */
$.htmlAddress = function () {
    $('#downPlace').html('<font size="2" >' + dakaData['place'] + '</font>');
    $('#upPlace').html('<font size="2" >' + dakaData['place'] + '</font>');
    if (!dakaData['isException']) {
        $('#upAttendance').css('background', '#ee901c');
        $('#downAttendance').css('background', '#ee901c');
    }
};

/**
 * 显示忘记打卡信息
 * @param attendanceId  a标签隐藏
 * @param msgId         span 的msg 绘制信息显示
 */
$.htmlForgotMsg = function (msgId) {
    $('#' + msgId).html('<span><font size="3">' + '打卡时间:' + ' </font></span><br>' +
        '<svg class="bi bi-geo-alt" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
        '  <path fill-rule="evenodd" d="M8 16s6-5.686 6-10A6 6 0 002 6c0 4.314 6 10 6 10zm0-7a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/>\n' +
        '</svg>' +
        '<svg class="bi bi-geo" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
        '  <path d="M11 4a3 3 0 11-6 0 3 3 0 016 0z"/>\n' +
        '  <path d="M7.5 4h1v9a.5.5 0 01-1 0V4z"/>\n' +
        '  <path fill-rule="evenodd" d="M6.489 12.095a.5.5 0 01-.383.594c-.565.123-1.003.292-1.286.472-.302.192-.32.321-.32.339 0 .013.005.085.146.21.14.124.372.26.701.382.655.246 1.593.408 2.653.408s1.998-.162 2.653-.408c.329-.123.56-.258.701-.382.14-.125.146-.197.146-.21 0-.018-.018-.147-.32-.339-.283-.18-.721-.35-1.286-.472a.5.5 0 11.212-.977c.63.137 1.193.34 1.61.606.4.253.784.645.784 1.182 0 .402-.219.724-.483.958-.264.235-.618.423-1.013.57-.793.298-1.855.472-3.004.472s-2.21-.174-3.004-.471c-.395-.148-.749-.336-1.013-.571-.264-.234-.483-.556-.483-.958 0-.537.384-.929.783-1.182.418-.266.98-.47 1.611-.606a.5.5 0 01.595.383z" clip-rule="evenodd"/>\n' +
        '</svg>' +
        '<span></span><br>' +
        '<span><font size="3" >备注:</font></span><br>'
        + '<span><font size="3" color="red">' + '异常：未打卡' + ' </font></span><br>');
};

/**
 * 绘制全部数据
 * @param data 显示的打卡信息（动态显示数据）
 */
$.htmlDakaMsg = function (data) {
    var html = '';
    $.each(data, function (index, item) {
        html += '<span><font size="3" >' + '打卡时间 ' + item.takeTime + ' </font></span><br>';
        html += '<svg class="bi bi-geo-alt" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '  <path fill-rule="evenodd" d="M8 16s6-5.686 6-10A6 6 0 002 6c0 4.314 6 10 6 10zm0-7a3 3 0 100-6 3 3 0 000 6z" clip-rule="evenodd"/>\n' +
            '</svg>' +
            '<svg class="bi bi-geo" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '  <path d="M11 4a3 3 0 11-6 0 3 3 0 016 0z"/>\n' +
            '  <path d="M7.5 4h1v9a.5.5 0 01-1 0V4z"/>\n' +
            '  <path fill-rule="evenodd" d="M6.489 12.095a.5.5 0 01-.383.594c-.565.123-1.003.292-1.286.472-.302.192-.32.321-.32.339 0 .013.005.085.146.21.14.124.372.26.701.382.655.246 1.593.408 2.653.408s1.998-.162 2.653-.408c.329-.123.56-.258.701-.382.14-.125.146-.197.146-.21 0-.018-.018-.147-.32-.339-.283-.18-.721-.35-1.286-.472a.5.5 0 11.212-.977c.63.137 1.193.34 1.61.606.4.253.784.645.784 1.182 0 .402-.219.724-.483.958-.264.235-.618.423-1.013.57-.793.298-1.855.472-3.004.472s-2.21-.174-3.004-.471c-.395-.148-.749-.336-1.013-.571-.264-.234-.483-.556-.483-.958 0-.537.384-.929.783-1.182.418-.266.98-.47 1.611-.606a.5.5 0 01.595.383z" clip-rule="evenodd"/>\n' +
            '</svg>' +
            '<span><font size="3" >' + item.place + ' </font></span><br>';
        if (item.remark != null) {
            html += '<span><font size="3">' + '备注:' + item.remark + ' </font></span><br>';
        }
        var isException = item.isException ? " " : "远程办公";
        if (isException !== ' ') {
            html += '<span><font size="3" color="red">' + '异常：' + isException + '</font></span>';
        }
        var id = item.status === 1 ? 'upMsg' : 'downMsg';
        $('#' + id).html(html);
        html = '';
    });
};