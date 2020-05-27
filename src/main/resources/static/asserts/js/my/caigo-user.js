initSider("/dashboard");

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


$caigoSpiuserId.select2({
    placeholder: '审批人',
    language: 'zh-CN',
    width: '100%',
    maximumSelectionLength: 1,  //最多选项数
    allowClear: true,  //显示清除按钮
    // multiple: true,   //配置可多选
    // tags: true,  //自建选项
    ajax: {
        url: "/user/getSelect2ByUserName",
        dataType: 'json',
        delay: 250,
        data: function (params) {
            return {
                search: params.term
            };
        },
        processResults: function (data) {
            return {
                results: data.data
            };
        },
        cache: true
    },
    minimumInputLength: 2
});
$caigoSpiuserId.on("select2-focus", function(e) {
    console.info ("focus");
}).on("select2-opening", function() { console.info("opening"); })  // select2 打开中事件
.on("select2-open", function() { console.info("open"); })    // select2 打开事件
.on("select2-loaded", function(e) { console.info ("loaded (data property omitted for brevity)");});


getCheckUserSelect();
function getCheckUserSelect(){
    $.get('/user/getCaiGoCheckUserSelect2',function (res) {
        $caigoSpiuserId.val(null).trigger('change');
        $caigoSpiuserId.append(new Option(res.data.text, res.data.id, false, true));
        $caigoSpiuserId.trigger("change");
    });
}




validate();

//校验数据
function validate() {
    $('#addCaigo').bootstrapValidator({
        message: 'This value is not valid',
        fields: {
            caigoReason: {
                message: '请输入采购事由',
                validators: {
                    notEmpty: {
                        message: '请填写申请事由。'
                    },
                    stringLength: {
                        max: 50,
                        message: '申请事由不超过50个字符。'
                    }
                }
            },
            caigoName: {
                validators: {
                    notEmpty: {
                        message: '请输入名称'
                    },
                    stringLength: {
                        max: 50,
                        message: '名称不超过50个字符。'
                    }
                }
            },
            caigoSize: {
                validators: {
                    notEmpty: {
                        message: '请输入规格'
                    },
                }
            },
            caigoNumber: {
                validators: {
                    notEmpty: {
                        message: '请输入数量'
                    },
                    stringLength: {
                        min: 1,
                        max: 11,
                        message: '数量超过限制。'
                    }
                }
            },
            caigoMoney: {
                validators: {
                    notEmpty: {
                        message: '请输入总金额'
                    },
                    stringLength: {
                        min: 1,
                        max: 11,
                        message: '数量超过限制。'
                    }
                }
            },
            caigoSpiuserId : {
                validators: {
                    notEmpty: {
                        message: '请选择审批人'
                    }
                }
            }
        }
    })
};


$caigoAddBtn.off().on('click',function(){
    console.info("ssss");
    var form = $addCaigo.data("bootstrapValidator");
    form.validate();
    if(form.isValid()){
        var data = {};
        $.each($addCaigo.serializeArray(),function (index,item) {
            data[item.name] = item.value;
        });
        console.info(JSON.stringify(data));
        var url;
        flag ?  url='/caigo/update_caigo.do':url='/caigo/caigo_save.do';
        $.ajax({
            method: "post",
            url: url,
            contentType: "application/json",
            data: JSON.stringify(data),
            dataType: 'json',
            success: function (e) {
                if (e.success) {
                    toastr.success('提交' + e.errorMsg);
                    showCaigoRecord();
                        $caiGoTable.draw();
                    flag = false;

                } else {
                    toastr.error(e.errorCode + ":" + e.errorMsg, '错误提示');
                }
            }
        })
    }
});


function initAddCaigoView(){
    //模态框关闭  回调
        flag = false;
        $addCaigo.data('bootstrapValidator').destroy();
        $addCaigo.data('bootstrapValidator', null);
        $(':input', '#addCaigo').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
        $caigoAddBtn.text('提交采购信息');
        // $("#addCaigo :input[type='text']").each(function (i) {
        //     this.value = "";
        // });
        // $caigoSpiuserId.val(null).trigger('change');
        validate();

}


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
                "length": d.length,
                "role":'user'
            };
            var value1 = $queryCaigoStatus.val();
            if(value1 !== ''){
                str['caigoStatus'] = value1;
            }
            var value2 = $('#query_caigo_name').val();
            if(value2 !== ''){
                str['caigoName'] = value2;
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
                var result='';
                    switch (data.caigoStatus) {
                    case 2:
                    case 3:
                        case 4:    result = '<input type="hidden">';break;
                    default:
                        result = '<input type="checkbox" name="ids"  value="' + data.caigoId + '">'
                    }
                return result
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
                return '<a class="btn btn-outline-secondary" type="button" id="' + rechargeBtn + '" data-toggle="tooltip" data-placement="top" title="修改">' +
                    '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                    '  <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/>\n' +
                    '  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/>\n' +
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

$('#caiGoDeleteBtn').click(function () {
    var ids = [];
    $(":checkbox").each(function (index,item) {
        if(item.checked && index != 0){
            console.info(JSON.stringify($(this).val()));
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
            url: "/caigo/revoke_caigo.do",
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
});

var flag = false;
$('#take_caigo').click(function () {
    getCheckUserSelect();
    showTakeCaigo();
});

$('#caigo_record').click(function () {
    showCaigoRecord();
});

function showTakeCaigo(){
    $('.wpp_header').text('采购申请');
    initAddCaigoView();
    $('.caigo_list').hide();
    $('.add_caigo').show();
}
function showCaigoRecord(){

    $('.wpp_header').text('采购记录');
    $('.caigo_list').show();
    $('.add_caigo').hide();
}

function editCaigoRecord(data) {
console.info(data);
    showTakeCaigo();
    $caigoSpiuserId.val(null).trigger('change');
    $caigoSpiuserId.append(new Option(data.caigoSpiUserName, data.caigoSpiuserId, false, true));
    $caigoSpiuserId.trigger("change");
    $caigoAddBtn.text('修改采购信息');
    $('input[name = "caigoId"]').val(data.caigoId);
    $('input[name = "caigoReason"]').val(data.caigoReason);
    $('input[name = "caigoName"]').val(data.caigoName);
    $('input[name = "caigoSize"]').val(data.caigoSize);
    $('input[name = "caigoNumber"]').val(data.caigoNumber);
    $('input[name = "caigoMoney"]').val(data.caigoMoney);
    $("#caigoRemark").val(data.caigoRemark);

    $('.wpp_header').text('查看详情');
    flag = true;

}