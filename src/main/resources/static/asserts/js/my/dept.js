initSider('/dept');

//获取职位信息
var  flag = false;
$.ajax({
    method: "get",
    async: true,
    url: "/position/getPositionBySelect2",
    dataType: 'json',
    success: function (result) {
        if (result.success) {
            var html = '';
            $.each(result.data, function (index, item) {
                html += '<option value="' + item.id + '">' + item.text + '</option>';
            });
            $('#right_menu').html(html);
            $('#right_menu').multiSelect('refresh');
        }
    }
});




//select2 测试文件
// $('#test').on('click', function () {
// $("#deptPosition").val([5, 3, 4]).trigger("change");
//     //获取已选择的值，无论是单选还是多选都可以用下边的代码来获取选择的option
//     var value = $('#userConnect').select2('val');
//     //获取已选择的对象
//     var $dd = $("#userConnect").select2("data");
// //    如果想要拿到已选择option的text值，可以通过如下方法，
// // 以下代码中的[0]用来获取第一个对象，
// // 对单选合适，如果是多选的话需要循环获取
//      value = $dd[0].text;
//     console.info(value);
//     //清空选中项
//     $("#userConnect").val(null).trigger('change');
//    // 禁用
//     $("#userConnect").prop('disabled',true);
//     //初始赋值没反应
//      $("#userConnect").val([5,3,4]).trigger("change");
//
//     //动态赋值
//     var _html = '<option value="9" selected>ops-coffee.cn</option>';
//     //change.select2： 新增select数据后触发select2更新
//     $('#userConnect').append(_html).trigger('change.select2');
//     //.select2("open")： 打开select，open改为close可动态关闭select，改为destroy可销毁select2，恢复select默认样式
//     $('#userConnect').select2("open");
// });



//选中职位 触发事件
 var deptPositionId = [];
$('#right_menu').multiSelect({
    selectableHeader: '<input class="form-control" autocomplete="off"  type="text" placeholder="现有职位">',
    selectionHeader: '<input class="form-control" autocomplete="off"  type="text" placeholder="已选职位">',
    keepOrder: true,
    afterInit: function(ms){
        var that = this,
            $selectableSearch = that.$selectableUl.prev(),
            $selectionSearch = that.$selectionUl.prev(),
            selectableSearchString = '#'+that.$container.attr('id')+' .ms-elem-selectable:not(.ms-selected)',
            selectionSearchString = '#'+that.$container.attr('id')+' .ms-elem-selection.ms-selected';

        that.qs1 = $selectableSearch.quicksearch(selectableSearchString)
            .on('keydown', function(e){
                if (e.which === 40){
                    that.$selectableUl.focus();
                    return false;
                }
            });

        that.qs2 = $selectionSearch.quicksearch(selectionSearchString)
            .on('keydown', function(e){
                if (e.which == 40){
                    that.$selectionUl.focus();
                    return false;
                }
            });
    },
    afterSelect: function (values) {
        //回显数据时会调用方法，传入数据如['2,1']导致问题
        // if(values.toString().indexOf(",") === -1){
        //     console.info("不含，");
        //     deptPositionId.push(values.toString());
        // }
        // console.info(deptPositionId);

    },
    afterDeselect: function (values) {
        //回显数据时会调用方法，传入数据如['2,1']导致问题
        // if(values.toString().indexOf(",") === -1){
        //     console.info("不含，");
        // }
        // console.info(deptPositionId);
        // $.each(deptPositionId, function (index, item) {
        //     console.info( deptPositionId[index] +"");
        //     if (item === values.toString()) {
        //         console.info(deptPositionId[index] + ":删除的");
        //         delete deptPositionId[index];
        //     }
        // });
        // deptPositionId = $.grep(deptPositionId, function (n) {
        //     return $.trim(n).length > 0;
        // });  //清空数组中的null
    }
});

//select2 远程搜索数据
$('#userConnect').select2({
    placeholder: '负责人',
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




//模态框打开  回调
$('#addDeptBtn').off().on('click', function () {
    $('#deptAddModal').modal('show');
    $('#deptAddModalLabel').text('部门添加');
});

//模态框关闭  回调
$('body').on('hidden.bs.modal', '.modal', function () {
    initAddDeptModal();
});

//清空 模态框
function initAddDeptModal() {
    flag = false;
    $("#addDeptForm :input[type='text']").each(function (i) {
        this.value = "";
    });
    $('input[name="deptId"]').val('');
    $('#addDeptRemark').val('');

    $("#userConnect").empty();
    $("#userConnect").val(null).trigger('change');
    deptPositionId = [];
    var $right_menu = $('#right_menu');
    $right_menu.multiSelect({});
    $right_menu.multiSelect('deselect_all');
    $right_menu.multiSelect('refresh');
    $('.time-updateDept').hide();
    $('.time-createDept').hide();
}




// 搜索栏触发事件
$('#deptQueryBtn').off().on('click', function () {
    $deptTable.draw();
});
$('#deptResetBtn').off().on('click', function () {
    $('input[name = "deptName"]').val('');
    $deptTable.draw();
});
$('input[name = "deptName"]').keypress(function (e) {
    if (e.which === 13) {
        $deptTable.draw();
    }
});

// 提交 信息
$('#deptSaveBtn').off().on('click', function () {
    var v_deptName = $('#add_deptName').val();

    if (v_deptName === "") {
        toastr.warning('部门名称为必填项', '错误提示');
        return false;
    }

    var deptFormData = $('#addDeptForm').serializeArray();
    var data = {};
    $.each(deptFormData, function (index, item) {
        if (item.value !== "") {
            data[item.name] = item.value;
        }
    });

    var ids=[];
    $("#right_menu :checked").each(function(i,item){
        ids.push($(item).attr("value"));
    });
    if(ids.length > 0 ){
        data['positionId'] = ids;
    }

  console.info('data:s' + JSON.stringify(data));
    var url = '';
    if(flag){
        url = "/dept/update_dept.do";
        data['oldPositionId'] = deptPositionId;
    }else{
        url = "/dept/save_dept.do";
    }

    $.ajax({
        method: "post",
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data),
        dataType: 'json',
        success: function (e) {
            if (e.success) {
                toastr.success('提交' + e.errorMsg);

                if(flag){
                    $deptTable.draw(false);
                }else{
                    $deptTable.draw();
                }

                flag = false;
                setTimeout(function () {
                    initAddDeptModal();
                    $('#deptAddModal').modal('hide');
                }, 1500);
            } else {
                toastr.error(e.errorCode + ":" + e.errorMsg, '错误提示');
            }
        }
    })
});




//datatable表格
var $deptTable = $('#deptTable').DataTable({
    "pagingType": "full_numbers",  //分页样式  除了数字还有上一页下一页，第一页和最后一页
    "sLoadingRecords": "正在加载数据...",  //数据较多时加载显示
    "sZeroRecords": "暂无数据",   //空数据显示
    serverSide: true,  //服务端分页显示
    stateSave: true,     //开启缓存
    "searching": false,   //datatables自带搜索，关闭，用服务端搜索
    "dom": '<"top">rt<"bottom"iflp<"clear">>',   //显示样式 ，信息，分页栏等均在底部  也可改到top
    "ordering": false, //禁用排序
    ajax: {  //请求数据
        url: "/dept/select_dept.do",
        contentType: "application/json",
        dataType: "JSON",
        type: "POST",
        dataSrc: 'data',  //数据名称
        data: function (d) {   //后台传回数据对应
            var str = {
                "draw": d.draw,
                "start": d.start,
                "length": d.length,
            };
            var deptName = $('input[name = "deptName"]').val();
            if (deptName !== '') {
                str['deptName'] = deptName;
            }
            //自定义需要传递的参数。
            console
                .info(JSON.stringify(str) + " :shuju");
            return JSON.stringify(str);
        }
    },
    columns: [
        {
            "class": 'details-control',
            "orderable": false,
            "data": null,
            "defaultContent": ''
        },
        {"title": "部门名称", "data": "deptName"},
        {
            "title": "简介", "data": "remark", render: function (data, type, row, meta) {
                //type 的值  dispaly sort filter
                //代表，是显示类型的时候判断值的长度是否超过8，如果是则截取
                //这里只处理了类型是显示的，过滤和排序返回原始数据
                if (type === 'display') {
                    if (data != null && data.length > 10) {
                        return '<span title="' + data + '">' + data.substr(0, 10) + '...</span>';
                    } else {
                        if(data === null ){
                            data = '';
                        }
                        return '<span title="' + data + '">' + data  + '</span>';
                    }
                }
                return data;
            }
        },
        {"title": "更新时间", "data": "updateTime"},
        {
            "title": "状态", "data": "deptStatus", render: function (data, type, full) {
                return full.deptStatus ? '正常' : '冻结';
            }
        },
        {
            "title": "操作", "data": "deptId", "width": "20%", render: function (data, type, full) {
                //按钮绑定事件  默认触发两次，所以解绑一次
                var rechargeBtn = "recharge" + full.deptId;
                var deductionBtn = "deduction" + full.deptId;
                var deleteDeptBtn = "delete" + full.deptId;
                var $dept = $('#deptTable');
                $dept.undelegate('tbody #' + rechargeBtn, 'click');
                $dept.on('click', 'tbody #' + rechargeBtn, function () {
                    editDeptRecord(full);
                });
                $dept.undelegate('tbody #' + deductionBtn, 'click');
                $dept.on('click', 'tbody #' + deductionBtn, function () {
                    updateDeptStatus(full);
                });
                $dept.undelegate('tbody #' + deleteDeptBtn, 'click');
                $dept.on('click', 'tbody #' + deleteDeptBtn, function () {
                    deleteDept(full);
                });
                var icss = full.deptStatus ? '<i class="fa fa-toggle-on " aria-hidden="true"></i>' : '<i class="fa fa-toggle-off " aria-hidden="true"></i> ';
                var msg = full.deptStatus ? '禁用' : '激活';
                return '<a class="btn btn-outline-secondary" type="button" id="' + rechargeBtn + '" data-toggle="tooltip" data-placement="top" title="修改">' +
                    '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                    '  <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/>\n' +
                    '  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/>\n' +
                    '</svg></a> ' +
                    '<a class="btn btn-outline-secondary" type="button"  id="' + deductionBtn + '" data-toggle="tooltip" data-placement="top" title="' + msg + '">' + icss + '</a>   '+
                    '<a class="btn btn-outline-secondary" type="button"  id="' + deleteDeptBtn + '" data-toggle="tooltip" data-placement="top" title="删除该部门" ><i class="fa fa-trash-o "  aria-hidden="true"></i></a>';
            }
        }
    ],
    createdRow: function (row, data, index) {

        var $td4 = $('td', row).eq(4).css('font-weight', "bold");
        data.deptStatus ? $td4.css("color", "blue") : $td4.css("color", "red");

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
    }
});

//展示负责人信息
$('#deptTable tbody').on('click', 'td.details-control', function () {
    var tr = $(this).closest('tr');
    var row = $deptTable.row(tr);
    if (row.child.isShown()) {
        // This row is already open - close it
        row.child.hide();
        tr.removeClass('shown');
    }
    else {
        // Open this row
        row.child(format(row.data())).show();
        tr.addClass('shown');
    }

});

//渲染 联系人信息
function format(d) {
    if(d.connectUser === null){
        return '<table class="display"  cellpadding="0" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr>' +
        '<td>暂未添加负责人</td>' +
            '</tr>' +
            '</table>';
    }else{
        console.info(d.connectUser);
        var name = d.connectUser.userName === null ?'':d.connectUser.userName;
        var phone =d.connectUser.telephone === null ?'':d.connectUser.telephone;
        var address = d.connectUser.liveAddress === null ? '':d.connectUser.liveAddress;
        return '<table class="display"  cellpadding="0" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr>' +
        '<td>联系人:</td>' +
        '<td>' + name  + ' ,</td>' +
        '<td> 联系方式 :</td>' +
        '<td>' + phone + ' ,</td>' +
        '<td>住址:</td>' +
        '<td>' + address+ '</td>' +
            '</tr>' +
            '</table>';
    }
}

//datatable抛出异常取代alert
$.fn.dataTable.ext.errMode = 'throw';


// table  按钮
//修改状态
function updateDeptStatus(data) {
    $.post('/dept/delete_dept.do', {"deptId": data.deptId, "deptStatus": !data.deptStatus}, function (res) {
        if (res.success) {
            $deptTable.draw(false);
        } else {
            toastr.warning(res.errorCode + ":" + res.errorMsg, "提示");
        }
    })
}

//更新操作
function editDeptRecord(e) {

    if(e.connectUser !== null){
        // console.info(e.connectUser);
        var $user =  $("#userConnect");
        $user.append(new Option(e.connectUser.userName, e.connectUser.id, false, true));
        $user.trigger("change");
    }

    $('input[name="deptId"]').val(e.deptId);
    $('#add_deptName').val(e.deptName);
    $('input[name="updateTime"]').val(e.updateTime);
    $('input[name="createTime"]').val(e.createTime);
    $('#addDeptRemark').val(e.remark);
    var s = [];
    if (e.positionId != null && e.positionId.length > 0) {
        $.each(e.positionId, function (index, item) {
            s.push(item.toString());
        });
    }

      deptPositionId = s;

    //告知 save 按钮  区别 添加和修改
    flag = true;

    var $menu = $('#right_menu');
    $menu.multiSelect({});
    $menu.multiSelect('select', s);
    $menu.multiSelect('refresh');

    $('.time-updateDept').show();
    $('.time-createDept').show();

    $('#deptAddModal').modal('show');
    $('#deptAddModalLabel').text('部门修改');
}

function deleteDept(e){
    $('#delModalLabel').text("提示");
    $('#confirmSpan').text('该操作不具有恢复性质，请确认在操作！！');
    $('#delConfirm').modal('show');
    $('#delete-submit').off().on('click', function () {
        $.post('/dept/delete_forever.do',{'id':e.deptId},function (res) {
            if(res.success){
                $('#delConfirm').modal('hide');
                $deptTable.draw(false);
            }else{
                toastr.error(res.errorCode+":" +res.errorMsg)
            }
        })

    });

}