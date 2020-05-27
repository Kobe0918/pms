initSider("/logs");


$.fn.dataTable.ext.errMode = 'throw';


var $logsTable = $('#logsTable').DataTable({
    "pagingType": "full_numbers",  //分页样式  除了数字还有上一页下一页，第一页和最后一页
    "sLoadingRecords": "正在加载数据...",  //数据较多时加载显示
    "sZeroRecords": "暂无数据",   //空数据显示
    serverSide: true,  //服务端分页显示
    stateSave: true,     //开启缓存
    "searching": false,   //datatables自带搜索，关闭，用服务端搜索
    "dom": '<"top">rt<"bottom"iflp<"clear">>',   //显示样式 ，信息，分页栏等均在底部  也可改到top
    "ordering": false, //禁用排序
    ajax: {  //请求数据
        url: "/logs/getLogsWithPage",
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
            var array = $('#logsForm').serializeArray();

            $.each(array,function (index,item) {
                if(item.value !== null && item.value !== ''){
                    str[item.name] = item.value;
                    if(item.name === 'beginTime' || item.name=== 'endTime'){
                        var time = item.value + " 00:00:00";
                        str[item.name] = time;
                    }
                }
            });

            //自定义需要传递的参数。
            console
                .info(JSON.stringify(str) + " :shuju");
            return JSON.stringify(str);
        }
    },
    // "columnDefs": [{"targets": 0, "orderable": false}],
    columns: [
        {
            "data": "null", render: function (data, type, full) {

                return '<input type="checkbox" name="ids"  value="' + full.id + '">';
            }
        },
        {"data": "userName"},
        {"data": "module" },
        {"data": "flag",render:function (data, type, full) {
                return  full.flag === 1 ? '成功':'异常';
            }},
        {"data": "createtime"},
        {
            "data": null,  "width": "20%", render: function (data, type, full) {
                //按钮绑定事件  默认触发两次，所以解绑一次
                var rechargeBtn = "recharge" + full.id;

                var $logsTable = $('#logsTable');
                $logsTable.undelegate('tbody #' + rechargeBtn, 'click');
                $logsTable.on('click', 'tbody #' + rechargeBtn, function () {
                    logsDetail(full);
                });

                return '<a class="btn btn-outline-secondary" type="button" id="' + rechargeBtn + '"  data-target=".bd-example-modal-lg" data-toggle="tooltip" data-placement="top" title="查看详细">' +
                    '<svg class="bi bi-clipboard-data" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                    '  <path fill-rule="evenodd" d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z" clip-rule="evenodd"/>\n' +
                    '  <path fill-rule="evenodd" d="M9.5 1h-3a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z" clip-rule="evenodd"/>\n' +
                    '  <path d="M4 11a1 1 0 1 1 2 0v1a1 1 0 1 1-2 0v-1zm6-4a1 1 0 1 1 2 0v5a1 1 0 1 1-2 0V7zM7 9a1 1 0 0 1 2 0v3a1 1 0 1 1-2 0V9z"/>\n' +
                    '</svg></a> ';
            }
        }
    ],
    createdRow: function (row, data, index) {
        var $td3 = $('td', row).eq(3);
        //.css('font-weight', "bold")

        //.css('font-weight', "bold")
        data.flag === 1 ? $td3.css("color", null) : $td3.css("color", "#d02e41");

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
    "fnDrawCallback": function () {
        $("#checkAll").prop("checked",false);
    },
});


$('#logsQueryBtn').click(function () {
    $logsTable.draw();
});

function logsDetail(data){
    console.info(JSON.stringify(data) + " :data");
    $('input[name="userName"]').val(data.userName);
    $('input[name="module"]').val(data.module);
    $('input[name="flag"]').val(data.flag === 1 ?'成功':'失败');
    $('input[name="createTime"]').val(data.createtime);
   $('#remark').val(data.remark);

   $('#logsModalLabel').text('日志详情');
   $('#logsModal').modal('show');

}

$('#logsDeleteBtn').click(function () {
    var ids = [];
    $(":checkbox").each(function (index, item) {
        if (item.checked && index !== 0) {
            ids.push($(this).val())
        }
    });
    console.info(JSON.stringify(ids));

    $('#delModalLabel').text("提示");
    $('#confirmSpan').text("确认删除该日志，该操作不具有恢复性");
    $('#delConfirm').modal('show'); //手动打开模态框
    //防止按钮绑定事件后，重复之前的操作
    $('#delete-submit').off().on('click', function () {
        $.ajax({
            method: "delete",
            url: '/logs/deleteLogs',
            contentType: "application/json",
            data: JSON.stringify(ids),
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    toastr.success("操作" + result.errorMsg);
                    setTimeout(function () {
                        $logsTable.draw(false);//查询后不需要保持分页状态，回首页
                    }, 800);
                } else {
                    toastr.error("错误信息", result.errorMsg);
                }
            },
            complete: function (response) {
                $('#delConfirm').modal('hide');
            }
        });
    })
});

$('#resetBtn').click(function () {
    $(':input', '#logsForm').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
    $logsTable.draw();
    return false;
});

$('#checkAll').on('click', function () {
    if ($('#checkAll').prop('checked') === true) {
        $('input[name="ids"]').prop('checked', 'checked');
    } else {
        $('input[name="ids"]').prop('checked', false);
    }
});




var $province = $("#province"), $city = $("#city"), $country = $("#country");

//datepicker 初始化
var $beginTime = $('#beginTime').datepicker({
    locale: 'zh-cn',
    format: 'yyyy-mm-dd ',
    uiLibrary: 'bootstrap4',
    maxDate: function () {
        var date = new Date();
        date.setDate(date.getDate());
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    },
    weekStartDay: 1
});
var $endTime = $('#endTime').datepicker({
    locale: 'zh-cn',
    format: 'yyyy-mm-dd ',
    uiLibrary: 'bootstrap4',
    maxDate: function () {
        var date = new Date();
        date.setDate(date.getDate());
        return new Date(date.getFullYear(), date.getMonth(), date.getDate());
    },
    weekStartDay: 1
});



