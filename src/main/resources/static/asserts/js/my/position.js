initSider('/position');


$.get('/getDicByColumnName', {'columnName': 'position_sort'},function(res){
    var html_sort = '<option value="">--职称等级--</option>';
    $.each(res, function (index, item) {
        html_sort += '<option value=' + item.columnCode + '>' + item.columnValue + '</option>';
    });
    $('#positionSort').html(html_sort);
});

$("#positionSort").select2({
    width: '100%',
    minimumResultsForSearch: -1
});

//获取角色信息
var flag = false;
var $roleMenu = $('#role_menu') ;
$.ajax({
    method: "get",
    async: true,
    url: "/role/getRole",
    dataType: 'json',
    success: function (result) {
        if (result.success) {

            var html = '';
            $.each( result.data, function (index, item) {
                console.info(
                    item.id
                );
                html += '<option value="' + item.id + '">' + item.roleName + '->' + item.roleCode + '</option>';
            });

            $roleMenu.html(html);
            $roleMenu.multiSelect('refresh');
        }
    }
});



//选中角色 触发事件
var selectedRoleIds = [];
$roleMenu.multiSelect({
    selectableHeader: '<input class="form-control" autocomplete="off"  type="text" placeholder="现有角色">',
    selectionHeader: '<input class="form-control" autocomplete="off"  type="text" placeholder="已选角色">',
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



//模态框打开  回调
$('#addPositionBtn').off().on('click', function () {
    $('#positionAddModal').modal('show');
    $('#positionAddModalLabel').text('添加职位');
});

//模态框关闭  回调
$('body').on('hidden.bs.modal', '.modal', function () {
    initAddPositionModal();
});

//清空 模态框
function initAddPositionModal() {
    $("#positionSort").val(null).trigger('change');
    flag = false;
    $("#addPositionForm :input[type='text']").each(function (i) {
        this.value = "";
    });
    $('input[name="positionId"]').val('');
    $('#addPositionRemark').val('');
    selectedRoleIds = [];
    // var $right_menu = $('#right_menu');
    $roleMenu.multiSelect({});
    $roleMenu.multiSelect('deselect_all');
    $roleMenu.multiSelect('refresh');
    $('.time-updatePosition').hide();
    $('.time-createPosition').hide();
}



// 搜索栏触发事件
$('#positionQueryBtn').off().on('click', function () {
    $positionTable.draw();

    // if($positionTable){
    //     $positionTable.fnClearTable();    //清空数据
    //     $positionTable.fnDestroy();         //销毁datatable
    // }
    // initTable(str);
});

var $positionResetBtn = $('#positionResetBtn');

$positionResetBtn.off().on('click', function () {
    $('#searchPositionName').val('');
    $positionTable.draw();
});
$('#searchPositionName').keypress(function (e) {
    if (e.which === 13) {
        $positionTable.draw();
    }
});

// 提交 信息
$('#positionSaveBtn').off().on('click', function () {
    var v_positionName = $('#positionName').val();

    if (v_positionName === "") {
        toastr.warning('职位名称为必填项', '错误提示');
        return false;
    }

    var position_sort = $('#positionSort').select2('val');
    if('' === position_sort){
        toastr.info('职称等级必选');
        return false;
    }

    var positionFormData = $('#addPositionForm').serializeArray();
    var data = {};
    $.each(positionFormData, function (index, item) {
        if (item.value !== "") {
            data[item.name] = item.value;
        }
    });

    var ids=[];
    $("#role_menu :checked").each(function(i,item){
        ids.push($(item).attr("value"));
    });
    if(ids.length > 0 ){
        data['roleIds'] = ids;
    }


    var url = '';
    if(flag){
        url = "/position/update_position.do";
        data['oldRoleId'] = selectedRoleIds;
    }else{
        url = "/position/save_position.do";
    }
    console.info('data:s' + JSON.stringify(data));
    console.info(url);

    $.ajax({
        method: "post",
        url: url,
        contentType: "application/json",
        data: JSON.stringify(data),
        dataType: 'json',
        success: function (e) {
            if (e.success) {
                toastr.success('提交' + e.errorMsg);
                // flag ? orderColumn = 'update_time':orderColumn = 'create_time';
                if(flag){

                    $positionTable.draw(false);
                }else{
                    $positionTable.draw();
                }

                flag = false;
                setTimeout(function () {
                    initAddPositionModal();
                    $('#positionAddModal').modal('hide');
                }, 1500);
            } else {
                toastr.error(e.errorCode + ":" + e.errorMsg, '错误提示');
            }
        }
    })
});



//table 排序


var $positionTable = $('#positionTable').DataTable({
        "pagingType": "full_numbers",  //分页样式  除了数字还有上一页下一页，第一页和最后一页
        "sLoadingRecords": "正在加载数据...",  //数据较多时加载显示
        "sZeroRecords": "暂无数据",   //空数据显示
        serverSide: true,  //服务端分页显示
        stateSave: true,     //开启缓存
        "searching": false,   //datatables自带搜索，关闭，用服务端搜索
        "dom": '<"top">rt<"bottom"iflp<"clear">>',   //显示样式 ，信息，分页栏等均在底部  也可改到top
        "ordering": false, //禁用排序
        ajax: {  //请求数据
            url: "/position/select_position.do",
            contentType: "application/json",
            dataType: "JSON",
            type: "POST",
            dataSrc: 'data',  //数据名称
            data: function (d) {   //后台传回数据对应
                console.info(str);
                var str = {
                    "draw": d.draw,
                    "start": d.start,
                    "length": d.length
                };


                var searchPositionName = $('#searchPositionName').val();
                if (searchPositionName !== '') {
                    str['positionName'] = searchPositionName;
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
            {"title": "职称", "data": "positionName"},
            {
                "title": "简介", "data": "remark", render: function (data, type, row, meta) {
                    //type 的值  dispaly sort filter
                    //代表，是显示类型的时候判断值的长度是否超过8，如果是则截取
                    //这里只处理了类型是显示的，过滤和排序返回原始数据
                    if (type === 'display') {
                        if (data != null && data.length > 10) {
                            return '<span title="' + data + '">' + data.substr(0, 10) + '...</span>';
                        } else {
                            if (data === null) {
                                data = '';
                            }
                            return '<span title="' + data + '">' + data + '</span>';
                        }
                    }
                    return data;
                }
            },
            {"title": "添加时间", "data": "createTime"},
            {
                "title": "状态", "data": "positionStatus", render: function (data, type, full) {
                    return full.positionStatus ? '正常' : '冻结';
                }
            },
            {
                "title": "操作", "data": "positionId", "width": "20%", render: function (data, type, full) {
                    //按钮绑定事件  默认触发两次，所以解绑一次
                    var rechargeBtn = "recharge" + full.positionId;
                    var deductionBtn = "deduction" + full.positionId;
                    var deletePositionBtn = "delete" + full.positionId;
                    var $position = $('#positionTable');
                    $position.undelegate('tbody #' + rechargeBtn, 'click');
                    $position.on('click', 'tbody #' + rechargeBtn, function () {
                        editPositionRecord(full);
                    });
                    $position.undelegate('tbody #' + deductionBtn, 'click');
                    $position.on('click', 'tbody #' + deductionBtn, function () {
                        updatePositionStatus(full);
                    });
                    $position.undelegate('tbody #' + deletePositionBtn, 'click');
                    $position.on('click', 'tbody #' + deletePositionBtn, function () {
                        deletePosition(full);
                    });
                    var icss = full.positionStatus ? '<i class="fa fa-toggle-on " aria-hidden="true"></i>' : '<i class="fa fa-toggle-off " aria-hidden="true"></i> ';
                    var msg = full.positionStatus ? '禁用' : '激活';
                    return '<a class="btn btn-outline-secondary" type="button" id="' + rechargeBtn + '" data-toggle="tooltip" data-placement="top" title="修改">' +
                        '<svg class="bi bi-pencil-square" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                        '  <path d="M15.502 1.94a.5.5 0 010 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 01.707 0l1.293 1.293zm-1.75 2.456l-2-2L4.939 9.21a.5.5 0 00-.121.196l-.805 2.414a.25.25 0 00.316.316l2.414-.805a.5.5 0 00.196-.12l6.813-6.814z"/>\n' +
                        '  <path fill-rule="evenodd" d="M1 13.5A1.5 1.5 0 002.5 15h11a1.5 1.5 0 001.5-1.5v-6a.5.5 0 00-1 0v6a.5.5 0 01-.5.5h-11a.5.5 0 01-.5-.5v-11a.5.5 0 01.5-.5H9a.5.5 0 000-1H2.5A1.5 1.5 0 001 2.5v11z" clip-rule="evenodd"/>\n' +
                        '</svg></a> ' +
                        '<a class="btn btn-outline-secondary" type="button"  id="' + deductionBtn + '" data-toggle="tooltip" data-placement="top" title="' + msg + '">' + icss + '</a>   ' +
                        '<a class="btn btn-outline-secondary" type="button"  id="' + deletePositionBtn + '" data-toggle="tooltip" data-placement="top" title="删除该职称" ><i class="fa fa-trash-o "  aria-hidden="true"></i></a>';
                }
            }
        ],
        createdRow: function (row, data, index) {

            var $td4 = $('td', row).eq(4).css('font-weight', "bold");
            data.positionStatus ? $td4.css("color", "blue") : $td4.css("color", "red");

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



//展示联系人信息
$('#positionTable tbody').on('click', 'td.details-control', function () {
    var tr = $(this).closest('tr');
    var row = $positionTable.row(tr);
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
    return '<table class="display"  cellpadding="0" cellspacing="0" border="0" style="padding-left:50px;">' +
        '<tr>' +
        '<td>最近修改:</td>' +
        '<td>' + d.updateUserName + ',</td>' +'<td>修改时间 :</td>' +
        '<td>' + d.updateTime + '</td>' +
        '</tr>' +
        // '<tr>' +
        // '<td> 修改时间 :</td>' +
        // '<td>' + d.updateTime + '</td>' +
        // '</tr>' +
        '</table>';
}


//datatable抛出异常取代alert
$.fn.dataTable.ext.errMode = 'throw';


// table  按钮
//修改状态
function updatePositionStatus(data) {
    $.post('/position/delete_position.do', {"positionId": data.positionId, "positionStatus": !data.positionStatus}, function (res) {
        if (res.success) {
            $positionTable.draw(false)
        } else {
            toastr.warning(res.errorCode + ":" + res.errorMsg, "提示");
        }
    })
}

//更新操作
function editPositionRecord(e) {
    console.info(e);
    // if(e.connectUser !== null){
    //     var $user =  $("#userConnect");
    //     $user.append(new Option(e.connectUser.userName, e.connectUser.id, false, true));
    //     $user.trigger("change");
    // }

    $('input[name="positionId"]').val(e.positionId);
    $('#positionName').val(e.positionName);
    $('input[name="updateTime"]').val(e.updateTime);
    $('input[name="createTime"]').val(e.createTime);
    $('#addPositionRemark').val(e.remark);
    var s = [];
    if (e.roleIds != null && e.roleIds.length > 0) {
        $.each(e.roleIds, function (index, item) {
            s.push(item.toString());
        });
    }
    // var sort = [];
    // sort = e.positionSort;
    $("#positionSort").val( e.positionSort).trigger("change");


    selectedRoleIds = s;

    //告知 save 按钮  区别 添加和修改
    flag = true;

     // var $menu = $('#role_menu');
    $roleMenu.multiSelect({});
    $roleMenu.multiSelect('select', s);
    $roleMenu.multiSelect('refresh');

    $('.time-updatePosition').show();
    $('.time-createPosition').show();

    $('#positionAddModal').modal('show');
    $('#positionAddModalLabel').text('职称修改');
}

function deletePosition(e){
    $('#delModalLabel').text("提示");
    $('#confirmSpan').text('该操作不具有恢复性质，请确认在操作！！');
    $('#delConfirm').modal('show');
    $('#delete-submit').off().on('click', function () {
        $.post('/position/delete_forever.do',{'id':e.positionId},function (res) {
            if(res.success){
                $('#delConfirm').modal('hide');
                $positionTable.draw(false);
            }else{
                toastr.error(res.errorCode+":" +res.errorMsg)
            }
        })

    });

}