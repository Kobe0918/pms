initSider('/leave');

var imgUrl = [],currentUserId , inWitchView = false;   //存放图片


$.get('/permissions/getUserAuthority',function (data) {
    if(data.success){
      $('#f_user_name').show();
      $('.leave_record').show();
        $('.checkRecord').show();
      $('#div_leave_status').addClass('form-group mx-sm-3 mb-2');
        currentUserId = data.data;
    }else{
        currentUserId = data.data;
    }
});




/**
 * 加载 leave_status ，leave_style 字典数据
 */
var leaveStyle;
var leaveStatus;
$.get('/getDicByColumnName', {'columnName': 'leave_status'}, function (data) {
    leaveStatus = data;
    var html_status = '<option value="">--审批状态--</option>';
    $.each(data, function (index, item) {
        html_status += '<option value=' + item.columnCode + '>' + item.columnValue + '</option>';
    });
    $('#status').html(html_status);

});
$.get('/getDicByColumnName', {'columnName': 'leave_style'}, function (data) {
    leaveStyle = data;

    var html_style = '';
    $.each(data, function (index, item) {
        html_style += '<option value=' + item.columnCode + '>' + item.columnValue + '</option>';
    });
    $('#leave_style').html(html_style);
    //取消界面选中
    $(':input', '#form-add').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');

});





// $('#checkAll').on('ifChanged',function (obj) {
//     console
//         .info('sss');
// });
//
// $('input[type=checkbox]').on('ifChanged', function (obj) {
//     $('#checkAll').val(obj.currentTarget.checked);
//     console.info("dy");
//     console.log($(this).val());   // 获取该复选框的value值
//     console.log(obj.currentTarget.checked)   // 获取该复选框当前状态是否选中
//     console.log('您更改了复选框的状态');
// });
//
// function turn_on_icheck()
// {
//     $('input[type=checkbox]').iCheck({
//         checkboxClass: 'icheckbox_square-green',   // 可以更改red换颜色
//         radioClass: 'iradio_square-blue',
//         increaseArea: '20%' // optional
//     });
// }
//
// $('#leaveTable').on('draw.dt', function() {
//     turn_on_icheck();
// });


/**
 * 加载datatable
 * @type {*|jQuery}
 */
//4-13

$.fn.dataTable.ext.errMode = 'throw';

var leaveTable =$('#leaveTable').DataTable({
    "pagingType": "full_numbers",  //分页样式  除了数字还有上一页下一页，第一页和最后一页
    "sLoadingRecords": "正在加载数据...",  //数据较多时加载显示
    "sZeroRecords": "暂无数据",   //空数据显示
    serverSide: true,  //服务端分页显示
    stateSave: true,     //开启缓存
    "searching": false,   //datatables自带搜索，关闭，用服务端搜索
    "dom": '<"top">rt<"bottom"iflp<"clear">>',   //显示样式 ，信息，分页栏等均在底部  也可改到top
    ajax: {  //请求数据
        url: "/leave/select_leave.do",
        contentType: "application/json",
        dataType: "JSON",
        type: "POST",
        dataSrc: 'data',  //数据名称
        data: function (d) {   //后台传回数据对应
            var str = {
                "draw": d.draw,
                "start": d.start,
                "length": d.length
            };

            var data = $('#searchForm').serializeArray();
            $.each(data, function (index, item) {
                if (item.value !== '' && item.value !== null && item.value !== undefined) {
                    switch (item.name) {
                        case 'beginTime':
                            str['beginTime'] = item.value + ":00";
                            break;
                        case 'endTime':
                            str['endTime'] = item.value + ":00";
                            break;
                        default:
                            str[item.name] = item.value;
                    }
                }
            });
            if(!inWitchView){
                str['userId'] = currentUserId;
            }
            if(intCheckRecord){
                str['personnelId'] = currentUserId;
            }
console.info(inWitchView);

             console.info(JSON.stringify(str));
            //自定义需要传递的参数。
            return JSON.stringify(str);
        }
    },
    //4-13
    "ordering": false, //禁用排序
    "order": [ [ 3, 'asc' ]],
    createdRow: function ( row, data, index ) {

        var $td1 = $('td', row).eq(1).css('font-weight',"bold");
        var $td5 = $('td', row).eq(6).css('font-weight',"bold");
        var leaveStyle = data.leaveStyle;
        var leaveStatus =data.leaveStatus;
        leaveStyle === 1 ? $td1.css("color","green"): (leaveStyle === 2 ? $td1.css("color","red")
            : (leaveStyle === 3 ?$td1.css("color","blue"):$td1.css("color","#3bc7dc")));

        leaveStatus === 1 ? $td5.css("color","#3bc7dc"):(leaveStatus === 2 ? $td5.css("color","#3b60dc"):
            (leaveStatus === 5 ? $td5.css("color","#39d493"):(leaveStatus === 6?$td5.css("color","#ffc107"):$td5.css("color","#d02e41"))))

        },
    columns: [ //自定义列   重新渲染等
        {
            "data": null, "orderable": false,"width": "2%",
            render: function (data, type, row, meta) {
                var result = '<input type="hidden">';
                if(data.leaveStatus !== 6){
                    result = '<input type="checkbox" name="ids" class="ids" value="' + data.leaveId + '">';
                }
                return result ;
            }
        },
        {
            "data": "leaveStyle", render: function (data, type, row, meta) {
                var result;
                $.each(leaveStyle, function (index, item) {
                    if (item.columnCode === data) {
                        result = item.columnValue;
                        return false;
                    } else {
                        result = "withod this type";
                    }
                });
                return result;
            }
        },
        {"data": "userName"},
        {"data": "beginTime"},
        {"data": "endTime"},
        {"data": "updateTime"},
        {
            "data": "leaveStatus", render: function (data, type, row, meta) {
                var result;
                $.each(leaveStatus, function (index, item) {
                    if (item.columnCode === data) {
                        result = item.columnValue;
                        return false;
                    } else {
                        result = "withod this type";
                    }
                });
                return result;
            }
        },
        {"data": null, "width": "180px"}
    ],
    "columnDefs": [{"targets": 0, "orderable": false}, {
        "targets": -1,
        "data": null,
        "render": function (data, type, row) {
            return '<a class="btn btn-outline-secondary " id="btnEdit"  data-toggle="tooltip" data-placement="top" title="修改">' +
            '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
            '  <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/>\n' +
            '  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/>\n' +
            '</svg></a>';
        }
    }],
    //显示国际语言
    "language": {
        "processing": "玩命加载中...",
        "lengthMenu": "显示 _MENU_ 项结果",
        "zeroRecords": "没有匹配结果",
        "info": "第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
        "infoEmpty": "显示第 0 至 0 项结果，共 0 项",
        "infoFiltered": "(由 _MAX_ 项结果过滤)",
        "infoPostFix": "",
        "url": "",
        "paginate": {
            "first": "首页",
            "previous": "上一页",
            "next": "下一页",
            "last": "末页"
        }
    },
    "fnDrawCallback": function(){

        $("#checkAll").prop("checked",false);
    },
});


/**
 * 关键词查询
 */
$('#leaveQueryBtn').off().on('click', function () {
    leaveTable.draw();//查询后不需要保持分页状态，回首页
});

$('#refreshBtn').off().on('click',function () {
    $('#beginTime').val('');
    $('#endTime').val('');
    $('#status').val('');
    leaveTable.draw();
    return false;
});

/**
 * 撤回请假
 */

//全选
$('#checkAll').on('click', function () {
    console.info("quanxia");
    if ($('#checkAll').prop('checked') === true) {
        $('input[name="ids"]').prop('checked', 'checked');
    } else {
        $('input[name="ids"]').prop('checked', false);
    }
});


//撤回
$('#leaveDeleteBtn').on('click', function () {
    var ids = [];
    $(":checkbox").each(function (index,item) {
        if(item.checked && index != 0){
            ids.push($(this).val())
        }
    });
    console.info(JSON.stringify(ids));



    $('#delModalLabel').text("提示");
    $('#delConfirm').modal('show'); //手动打开模态框
    //防止按钮绑定事件后，重复之前的操作
    $('#delete-submit').off().on('click', function () {
        $.ajax({
            method: "post",
            url: "/leave/revoke_leave.do",
            contentType: "application/json",
            data: JSON.stringify(ids),
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    toastr.success("撤销" + result.errorMsg);
                    setTimeout(function () {

                        $('#checkAll').prop('checked', false);
                        leaveTable.draw(false);//查询后不需要保持分页状态，回首页
                    }, 800);
                } else {
                    toastr.error("错误信息", result.errorMsg);
                }
            },
            complete: function (response) {
                $('#delConfirm').modal('hide');
            }
        });
    });
});


//审批通过
$('#leaveOkBtn').on('click', function () {
    var ids = [];
    $(":checkbox").each(function (index,item) {
        if(item.checked && index != 0){
            ids.push($(this).val())
        }
    });
    if(ids.length === 0){
        toastr.info("请选中一条记录进行操作");
        return false;
    }
    console.info(JSON.stringify(ids));
        $.ajax({
            method: "post",
            url: "/leave/leave_pass.do",
            contentType: "application/json",
            data: JSON.stringify(ids),
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    toastr.success("审批" + result.errorMsg);
                    setTimeout(function () {
                        $('#checkAll').prop('checked', false);
                        leaveTable.draw(false);//查询后不需要保持分页状态，回首页
                    }, 800);
                } else {
                    toastr.error("错误信息", result.errorMsg);
                }
            },
            complete: function (response) {
                $('#delConfirm').modal('hide');
            }

    });
});

//拒绝通过审批
$('#leaveNoBtn').on('click', function () {
    var ids = [];
    $(":checkbox").each(function (index,item) {
        if(item.checked && index != 0){
            ids.push($(this).val())
        }
    });
    if(ids.length === 0){
        toastr.info("请选中一条记录进行操作");
        return false;
    }
    console.info(JSON.stringify(ids));
    $.ajax({
        method: "post",
        url: "/leave/leave_unPass.do",
        contentType: "application/json",
        data: JSON.stringify(ids),
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                toastr.success(  result.errorMsg +"拒绝了请假请求");
                setTimeout(function () {
                    $('#checkAll').prop('checked', false);
                    leaveTable.draw(false);//查询后不需要保持分页状态，回首页
                }, 800);
            } else {
                toastr.error("错误信息", result.errorMsg);
            }
        },
        complete: function (response) {
            $('#delConfirm').modal('hide');
        }

    });
});


var flag = false;
var deleteFile = [];
//点击回显数据
$('#leaveTable tbody').off().on('click', 'a#btnEdit', function () {

    var data = leaveTable.row($(this).parents('tr')).data();
    console.info(data);
    htmlUPCheck(data.userId);

    //显示图片
    var img_preview = data.imgUrl;
    var config = [];
    if(img_preview != null){
        $.each(img_preview, function (index, item) {
            var path = item;
            var img = {
                caption: 'Photo' + (index + 1) + '.png', size: 576237, width: "120px", url: "/leave/delete_file2.do",
                key: data.leaveId, path: item, extra: function () {
                    return {path: item}
                }
            };
            config.push(img);
        });
    }

    flag = true;
    deleteFile = config;

    imgUrl = [];
    $("#img_url").fileinput('destroy');

    //4-17

    var templates = {
         actionDelete:'', //去除上传预览的缩略图中的删除图标
         actionUpload: ''  //去除上传预览缩略图中的上传图片；
        // actionZoom: ''   //去除上传预览缩略图中的查看详情预览的缩略图标。
    };

    if( data.leaveStatus === 6){
        $('#submitBtn').hide();
        if(img_preview != null){
            init({preview:img_preview,config:config,template:templates});
        }else{
            init({config:config,template:templates});
        }
    }else{
        $('#submitBtn').show();
        if(img_preview != null){
            init({preview:img_preview,config:config,template:templa,browsClick:true});
        }else{
            init({config:config,template:templa,browsClick:true});
        }
    }

    //select2 赋值选中
    $("#leave_style").val(data.leaveStyle).select2({
        width: '100%',
        minimumResultsForSearch: -1
    });
    $("#monitor").val(data.monitorId).select2({
        width: '100%',
        minimumResultsForSearch: -1
    });
    $("#personnel").val(data.personnelId).select2({
        width: '100%',
        minimumResultsForSearch: -1
    });
    //时间是否可以标识 改为true
    addEditTimeOk = true;
    $('#beginTime1').val(data.beginTime);
    //修改的id
    $('input[name = "leaveId"]').val(data.leaveId);
    $('input[name = "userName"]').val(data.userName);
    $('input[name = "deptName"]').val(data.deptName);
    $('.form-userName').show();
    $('.form-deptName').show();
    $('#endTime1').val(data.endTime);
    $('#leaveReason').val(data.leaveReason);
    //展示更新时间
    $('#updateTime').val(data.updateTime);
    $('.update_time').show();
    //修改按钮样式
    var $btn = $('#submitBtn');
    $btn.attr('class', 'btn btn-outline-primary btn-lg btn-block');
    $btn.removeAttr('disabled');
    $btn.text('修改请假表');
    $('.fileinput-remove').hide();
    $('.add_leave').show();
    $('.leave_list').hide();
});




/**
 * 时间栏初始化
 */
$("#beginTime").datetimepicker({
    locale: 'zh-cn',
    uiLibrary: 'bootstrap4',
    format: 'yyyy-mm-dd HH:MM',
    footer: true
});
$('#endTime').datetimepicker({
    locale: 'zh-cn',
    uiLibrary: 'bootstrap4',
    format: 'yyyy-mm-dd HH:MM',
    footer: true
});
var addEditTimeOk = false;
$("#beginTime1").datetimepicker({
    locale: 'zh-cn',
    uiLibrary: 'bootstrap4',
    format: 'yyyy-mm-dd HH:MM',
    footer: true,
    change: function (e) {
        var beginTime1 = e.target.value;
        var endTime1 = $('#endTime1').val();
        endTime1 !== '' ? (addEditTimeOk = new Date(beginTime1) < new Date(endTime1)) : addEditTimeOk = false;
    }
});
$('#endTime1').datetimepicker({
    locale: 'zh-cn',
    uiLibrary: 'bootstrap4',
    format: 'yyyy-mm-dd HH:MM',
    footer: true,
    change: function (e) {
        var endTime1 = e.target.value;
        var beginTime1 = $('#beginTime1').val();
        beginTime1 !== '' ? (addEditTimeOk = new Date(e.target.value) > new Date(beginTime1)) : addEditTimeOk = false;
    }
});


//加载select2插件
// $("#monitor,#personnel").select2({
//     width: '100%',
//     minimumResultsForSearch: -1
// });



/**
 * 删除物理图片
 * @param url  路径
 */
$.deleteFile = function (url) {
    $.ajax({
        contentType: 'application/json',
        url: '/leave/delete_file.do',
        dataType: 'json',
        data: JSON.stringify(url),
        async: true,
        method: 'post',
        success: function (response) {
        }
    });
};


/**
 * 校验数据
 */
$('#submitBtn').on('click', function () {
    //校验数据一下
    //上传图片
    var style = $('#leave_style').val();
    if (style === '' || style === null) {
        toastr.error("请假类型为必填项", "错误提示：");
        return false;
    }
    var reason = $('#leaveReason').val();
    if (reason === '' || reason === null) {
        toastr.error("请假原因为必填项", "错误提示：");
        return false;
    }
    var monitor = $('#monitor').val();
    if (monitor === '' || monitor === null) {
        toastr.error("部门负责人为必填项", "错误提示：");
        return false;
    }
    var personnel = $('#personnel').val();
    if (personnel === '' || personnel === null) {
        toastr.error("人事抄送为必填项", "错误提示：");
        return false;
    }
    if (addEditTimeOk) {
        var img = [];
        var formData = {};
        var data = $('#form-add').serializeArray();
        $.each(data, function (index, item) {
            if (item.name === 'beginTime' || item.name === 'endTime') {
                formData[item.name] = item.value + ":00";
            } else {
                formData[item.name] = item.value;
            }
        });
        if (imgUrl !== null || imgUrl !== undefined || imgUrl.length !== 0) {
            $.each(imgUrl, function (index, item) {
                img.push(imgUrl[index].FileName);
            });
            formData['imgUrl'] = img;
        }
        console.info(JSON.stringify(formData));
        var path = $('#submitBtn').text() === '修改请假表' ? '/leave/update_leave.do' : '/leave/save_leave.do';
        $.saveLeaveMessage(formData, path);
    } else {
        toastr.error('开始时间需要在结束时间之前,且请假时间为必填');
    }
});


/**
 * 保存或修改 请假条
 * @param data  数据
 * @param path  保存路径
 */
$.saveLeaveMessage = function (data, path) {
    $.ajax({
        contentType: 'application/json',
        url: path,
        dataType: 'json',
        data: JSON.stringify(data),
        async: true,
        method: 'post',
        success: function (response) {
            if (response.success) {
                $('.wpp_header').text('请假管理');
                var $btn = $('#submitBtn');
                $btn.attr('class', 'btn btn-outline-success btn-lg btn-block');
                $btn.attr('disabled', "disabled");
                $btn.text('提交成功');
                setTimeout(function () {
                    $('.add_leave').hide();
                    $('.leave_list').show();
                    intCheckRecord = false;
                    inWitchView=false;
                    $('#leaveDeleteBtn').show();
                    $('#leaveOkBtn').hide();
                    $('#leaveNoBtn').hide();
                    leaveTable.draw();
                }, 1800);
            } else {
                toastr.error(response.errorCode + " ： " + response.errorMsg);
            }
        }
    });
};

var templa = {
      // actionDelete:'', //去除上传预览的缩略图中的删除图标
      actionUpload: '',  //去除上传预览缩略图中的上传图片；
    // actionZoom: ''   //去除上传预览缩略图中的查看详情预览的缩略图标。
};
init({template:templa,browsClick:true});

/**
 * fileinput  原理：描绘多个iframe 多张图片多次上传
 * @param preview  回显照片路径
 * @param config   回显配置
 */
function init(arg) {
    console.info(JSON.stringify(arg) + ": shuju");
    $("#img_url").fileinput({
        language: 'zh',
        initialPreviewAsData: true,
        initialPreviewFileType: 'image',
        initialPreview: arg.preview,
        initialPreviewConfig: arg.config,
        uploadAsync: true, //异步
        uploadUrl: '/leave/save_file.do', // you must set a valid URL here else you will get an error
        enctype: 'multipart/form-data',
        allowedFileExtensions: ['jpg', 'png', 'gif'],
        layoutTemplates: arg.template,
        // fileActionSettings:{removeClass:'hideBtn'},   去掉缩略图上的删除按钮  // actionDelete:'', 该操作也行
        //     {
        //     // actionDelete:'', //去除上传预览的缩略图中的删除图标
        //     actionUpload: ''  //去除上传预览缩略图中的上传图片；
        //     // actionZoom: ''   //去除上传预览缩略图中的查看详情预览的缩略图标。
        // },
        overwriteInitial: false, //不覆盖已上传的图片
        dropZoneEnabled: true, // 是否显示拖拽区域
        maxFileSize: 1024,      //kb
        maxFileCount: 2,
        showCancel: false,
        showUpload: false,
        showBrowse: false,
        browseOnZoneClick: arg.browsClick,
        msgFilesTooMany: "选择上传的文件数量({n}) 超过允许的最大数值{m}！",
        // msgInvalidFileType: '不正确的文件扩展名 "{name}". 只支持 "{extensions}" 的文件扩展名.',
        slugCallback: function (filename) {
            return filename.replace('(', '_').replace(']', '_');
        }

    })
}


/**
 * 图片上传回调
 */
$('#img_url').on("fileuploaded", function (event, data, previewId, index) {
    if (flag) {
        $.each(deleteFile, function (index, item) {
            if (item.path !== '') {
                console.info('item.path :' + item.path);
                $.post('/leave/delete_file2.do', {"key": item.key, "path": item.path})
            }
        });
        flag = false;
    }

    //文件上传成功的回调函数
    if (data.response.success) {
        imgUrl.push({KeyID: previewId, FileName: data.response.data});
    } else {
        toastr.error(data.response.errorCode + ": " + data.response.errorMsg);
    }
    console.info("上传成功回调 imgUrl :" + JSON.stringify(imgUrl));

}).on('fileerror', function (event, data, msg) {
    toastr.error(msg);  //上传失败
}).on('filebatchuploadsuccess', function (event, data, previewId, index) {
    //同步上传回调
}).on('filebatchselected', function (e, data) {
    $(this).fileinput('upload');    //选择触发上传图片
}).on("filesuccessremove", function (event, data, previewId, index) {

    // 缩略图中的删除按钮触发事件
    for (var i = 0; i < imgUrl.length; i++) {
        if (imgUrl[i].KeyID === data) {
            var img = [];
            img.push(imgUrl[i].FileName);
            $.deleteFile(img);
            delete imgUrl[i];
        }
    }
    imgUrl = $.grep(imgUrl, function (n) {
        return $.trim(n).length > 0;
    });  //清空数组中的null

}).on("filecleared", function (event, data, msg) {

    //触发右上角删除按钮
    if (imgUrl.length !== 0) {
        var img = [];
        $.each(imgUrl, function (index, item) {
            img.push(item.FileName);
        });
        $.deleteFile(img);
    }
    imgUrl = [];
});



// 申请请假
$('#take_leave').on('click', function () {
    $('.wpp_header').text('申请请假');
    htmlUPCheck(null);
    $('.form-userName').hide();
    $('.form-deptName').hide();
    imgUrl = [];
    var $imgUrl = $("#img_url");
    $imgUrl.fileinput('destroy');  //destory
    var templateConfig = {
    // actionDelete:'', //去除上传预览的缩略图中的删除图标
    actionUpload: '',  //去除上传预览缩略图中的上传图片；
    // actionZoom: ''   //去除上传预览缩略图中的查看详情预览的缩略图标。
};

    init({template:templateConfig,browsClick:true});
    var $btn = $('#submitBtn');
    $btn.attr('class', 'btn btn-outline-primary btn-lg btn-block');
    $btn.removeAttr('disabled');
    $btn.text('提交请假表');
    $btn.show();
    $(':input', '#form-add').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
    $("#monitor,#personnel,#leave_style").select2({
        width: '100%',
        minimumResultsForSearch: -1
    });
    $('.fileinput-remove').show();
    $('.add_leave').show();
    $('.leave_list').hide();
    $('.update_time').hide();
    $imgUrl.show();
});

// 请假记录
$('#leave_record').on('click', function () {
    $('#leaveDeleteBtn').show();
    $('#leaveOkBtn').hide();
    $('#leaveNoBtn').hide();
    $('.wpp_header').text('所有请假记录');
    $('#userName').val('');

    intCheckRecord = false;
    inWitchView=true;
    $('#f_user_name').show();
    leaveTable.draw();
    $('#checkAll').prop('checked', false);
    $('.add_leave').hide();
    $('.leave_list').show();
});

//我的记录
$('#myRecord').on('click',function () {
    $('#leaveDeleteBtn').show();
    $('#leaveOkBtn').hide();
    $('#leaveNoBtn').hide();

    $('.wpp_header').text('我的请假记录');
  $('#userName').val('');
    intCheckRecord = false;
    inWitchView=false;
    $('#f_user_name').hide();
    $('#div_leave_status').addClass('form-group  mb-2');
    leaveTable.draw();
    $('#checkAll').prop('checked', false);
    $('.add_leave').hide();
    $('.leave_list').show();
});

//待我审批
var intCheckRecord = false;
$('#checkRecord').on('click',function () {
    $('#leaveOkBtn').prev('div').hide();
    $('#leaveDeleteBtn').hide();
    $('#leaveOkBtn').show();
    $('#leaveNoBtn').show();

    $('.wpp_header').text('待我审批的假条');
    $('#userName').val('');

    intCheckRecord = true;
    inWitchView=true;
    $('#f_user_name').show();
    leaveTable.draw();
    $('#checkAll').prop('checked', false);
    $('.add_leave').hide();
    $('.leave_list').show();
});


htmlUPCheck(null);
function htmlUPCheck(data){
    $.get('/user/getUp',{"id":data},function (response) {
        if(response.success){
            var $monitor = $('#monitor');
            console
                .info(response.data);
            var idv = response.data === null?'':response.data.id;
            var idn = response.data === null?'':response.data.userName;
            $monitor.html(' <option value="'+ idv +'">'+ idn+'</option>');
        }
    });

    $.get('/user/getPersonnel',function (response) {
        if(response.success){
            var html = '';
            var $personnel = $('#personnel');
            $.each(response.data,function (index,item) {
                html +=' <option value="'+ item.id+'">'+ item.userName+'</option>';
            });
            $personnel.html(html);
        }
    });
}