initSider("/caigo-admin");

var $caigoSpiuserId = $('#caigoSpiuserId'), $caigoAddBtn = $('#caigoAddBtn') , $addCaigo = $('#addCaigo'),$queryCaigoStatus = $('#query_caigo_status');

var caiGoStatusList ;
$.get('/getDicByColumnName', {'columnName': 'caigo_status'}, function (data) {
    caiGoStatusList = data;
    var html = '<option value="">-审批状态-</option>';
    $.each(data,function (index,item) {
        html += '<option value=' + item.columnCode + '>' + item.columnValue + '</option>';
    });
    $queryCaigoStatus.html(html);
    $caiGoTable.draw();
});





function editCaigoRecord(data) {
    console.info(data);
    // showTakeCaigo();
    $caigoSpiuserId.val(null).trigger('change');
    $caigoSpiuserId.append(new Option(data.caigoSpiUserName, data.caigoSpiuserId, false, true));
    $caigoSpiuserId.trigger("change");
    $caigoSpiuserId.prop('disabled', true);;


    $('input[name = "caigoId"]').val(data.caigoId);
    $('input[name = "caigoReason"]').val(data.caigoReason);
    $('input[name = "caigoName"]').val(data.caigoName);
    $('input[name = "caigoSize"]').val(data.caigoSize);
    $('input[name = "caigoNumber"]').val(data.caigoNumber);
    $('input[name = "caigoMoney"]').val(data.caigoMoney);
    $("#caigoRemark").val(data.caigoRemark);
    $('input[name = "caigoUserName"]').val(data.caigoUserName);
    $('input[name = "caigoCreateTime"]').val(data.caigoCreateTime);

    var result = data.caigoStatus;

    $.each(caiGoStatusList,function (index,item) {
        if(result === item.columnCode){
            result =  item.columnValue;
            return false;
        }
    });
    $('input[name = "caigoStatus"]').val(result);

    $('#caigoModalLabel').text("申请内容");
    $('#caigoModal').modal('show');
}

var $caiGoDeleteBtn = $('#caiGoDeleteBtn');
var $caigoOkBtn = $('#caigoOkBtn');
var $caigoNoBtn = $('#caigoNoBtn');

$caiGoDeleteBtn.click(function () {
    topBtnAction({path:'/delete_caigo.do',msg:'确定删除选中对象？'});
});
$caigoOkBtn.click(function () {
    topBtnAction({path:'/pass_caigo.do',msg:'确定通过审批？'});
});
$caigoNoBtn.click(function () {
    topBtnAction({path:'/unpass_caigo.do',msg:'确定拒绝该审批？'});
});

var checkByMe = false;
$('#checkByMe').click(function () {
    $caiGoDeleteBtn.hide();
    $caiGoDeleteBtn.prev('div').hide();
    $caigoOkBtn.show();
    $caigoNoBtn.show();
    $(':input', '#searchForm').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
    $('.wpp_header').text('待我审批');
    checkByMe = true;
    $caiGoTable.draw();
});

$('#allRecord').click(function () {
    $caiGoDeleteBtn.show();
    $caiGoDeleteBtn.prev('div').show();
    $caigoOkBtn.hide();
    $caigoNoBtn.hide();
    $(':input', '#searchForm').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
    $('.wpp_header').text('采购审批');
    checkByMe = false;
    $caiGoTable.draw();
});


$.fn.dataTable.ext.errMode = 'throw';


$('#caigoQueryBtn').click(function () {
    $caiGoTable.draw();
});


$('#resetBtn').click(function () {
    $(':input', '#searchForm').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
    $caiGoTable.draw();
});

var $caiGoTable = $('#caiGoTable').DataTable({
    "pagingType": "full_numbers",  //分页样式  除了数字还有上一页下一页，第一页和最后一页
    "sLoadingRecords": "正在加载数据...",  //数据较多时加载显示
    "sZeroRecords": "暂无数据",   //空数据显示
    serverSide: true,  //服务端分页显示
    stateSave: true,     //开启缓存
    "searching": false,   //datatables自带搜索，关闭，用服务端搜索
    "dom": '<"top">rt<"bottom"iflp<"clear">>',   //显示样式 ，信息，分页栏等均在底部  也可改到top
    "ordering": false, //禁用排序
    ajax: {  //请求数据
        url: "/caigo/caigo_select.do",
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
            var value1 = $queryCaigoStatus.val();
            if(value1 !== ''){
                str['caigoStatus'] = value1;
            }
            var value2 = $('#query_caigo_name').val();
            if(value2 !== ''){
                str['caigoName'] = value2;
            }
            if(checkByMe){
                str['checkUser'] = '*124.259zh';
            }
            //自定义需要传递的参数。
            console
                .info(JSON.stringify(str) + " :shuju");
            return JSON.stringify(str);
        }
    },
    // "columnDefs": [{"targets": 0, "orderable": false}],
    columns: [
        {
            "data": null, "width": "30px", "orderable": false,
            render: function (data, type, row, meta) {
                return '<input type="checkbox" name="ids"  value="' + data.caigoId + '">';
            }
        },
        { "data": "caigoName"},
        { "data": "caigoSize"},
        { "data": "caigoNumber"},
        {"data": "caigoMoney"},
        {
            "data": "caigoStatus",  " defaultContent" : "", render: function (data, type, full) {
                var result = full.caigoStatus;
                console.info(result + " : result");

                $.each(caiGoStatusList,function (index,item) {
                    if(result === item.columnCode){
                        result =  item.columnValue;
                        return false;
                    }
                });
                return result;
            }
        },
        {
            "data": null, "width": "20%", render: function (data, type, full) {
                //按钮绑定事件  默认触发两次，所以解绑一次
                var rechargeBtn = "recharge" + full.caigoId;
                // var deductionBtn = "deduction" + full.caigoId;
                // var deleteDeptBtn = "delete" + full.caigoId;
                var $caigo = $('#caiGoTable');
                $caigo.undelegate('tbody #' + rechargeBtn, 'click');
                $caigo.on('click', 'tbody #' + rechargeBtn, function () {
                    editCaigoRecord(full);
                });
                // $caigo.undelegate('tbody #' + deductionBtn, 'click');
                // $caigo.on('click', 'tbody #' + deductionBtn, function () {
                //     updateDeptStatus(full);
                // });
                // $caigo.undelegate('tbody #' + deleteDeptBtn, 'click');
                // $caigo.on('click', 'tbody #' + deleteDeptBtn, function () {
                //     deleteDept(full);
                // });
                // var icss = full.caigoStatus ===  1 ? '<i class="fa fa-toggle-off " aria-hidden="true"></i>' : '<i class="fa fa-toggle-on " aria-hidden="true"></i> ';
                // var msg = full.caigoStatus ? '禁用' : '激活';
                return '<a class="btn btn-outline-secondary" type="button" id="' + rechargeBtn + '" data-toggle="tooltip" data-placement="top" title="查看详细">' +
                    '<svg class="bi bi-clipboard-data" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                    '  <path fill-rule="evenodd" d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z" clip-rule="evenodd"/>\n' +
                    '  <path fill-rule="evenodd" d="M9.5 1h-3a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z" clip-rule="evenodd"/>\n' +
                    '  <path d="M4 11a1 1 0 1 1 2 0v1a1 1 0 1 1-2 0v-1zm6-4a1 1 0 1 1 2 0v5a1 1 0 1 1-2 0V7zM7 9a1 1 0 0 1 2 0v3a1 1 0 1 1-2 0V9z"/>\n' +
                    '</svg></a> ';
                // +
                // '<a class="btn btn-outline-secondary" type="button"  id="' + deductionBtn + '" data-toggle="tooltip" data-placement="top" title="' + msg + '">' + icss + '</a>   '+
                // '<a class="btn btn-outline-secondary" type="button"  id="' + deleteDeptBtn + '" data-toggle="tooltip" data-placement="top" title="删除该部门" ><i class="fa fa-trash-o "  aria-hidden="true"></i></a>';
            }
        }
    ],
    createdRow: function (row, data, index) {
        var $td5 = $('td', row).eq(5).css('font-weight', "bold");
        data.caigoStatus === 1 ? $td5.css("color", "#3bc7dc") : (data.caigoStatus === 2 ? $td5.css("color", "#39d493"):
            (data.caigoStatus === 3 ? $td5.css("color", "#d02e41"): $td5.css("color", "#ffc107")));

    },
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


$('#checkAll').on('click', function () {
    if ($('#checkAll').prop('checked') === true) {
        $('input[name="ids"]').prop('checked', 'checked');
    } else {
        $('input[name="ids"]').prop('checked', false);
    }
});

function topBtnAction(arg) {
    var ids = [];
    $(":checkbox").each(function (index, item) {
        if (item.checked && index != 0) {
            ids.push($(this).val())
        }
    });
    console.info(JSON.stringify(ids));

    $('#delModalLabel').text("提示");
    $('#confirmSpan').text(arg.msg);
    $('#delConfirm').modal('show'); //手动打开模态框
    //防止按钮绑定事件后，重复之前的操作
    $('#delete-submit').off().on('click', function () {
        var url = '/caigo'+arg.path;



        $.ajax({
            method: "post",
            url: url,
            contentType: "application/json",
            data: JSON.stringify(ids),
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    toastr.success("撤销" + result.errorMsg);
                    setTimeout(function () {
                        $caiGoTable.draw(false);//查询后不需要保持分页状态，回首页
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
}