initSider("/admin/userMessage");

var workStatusDic;
$.get('/getDicByColumnName', {'columnName': 'work_status'}, function (data) {
    workStatusDic = data;
    $userTable.draw();
});

$('#dept').select2({
    placeholder: '部门',
    language: 'zh-CN',
    width: '100%',
    maximumSelectionLength: 1,  //最多选项数
    allowClear: true,  //显示清除按钮
    // multiple: true,   //配置可多选
    // tags: true,  //自建选项
    ajax: {
        url: "/dept/getDeptAll",
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

$('#position').select2({
    placeholder: '部门',
    language: 'zh-CN',
    width: '100%',
    maximumSelectionLength: 1,  //最多选项数
    allowClear: true,  //显示清除按钮
    // multiple: true,   //配置可多选
    // tags: true,  //自建选项
    ajax: {
        url: "/position/getPositionBySearch",
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

//修改 checkbox 和radio 样式代码
$('input[type=radio],input[type=checkbox]').iCheck({
    checkboxClass: 'icheckbox_square-green',   // 可以更改red换颜色
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' // optional
});


$.fn.dataTable.ext.errMode = 'throw';


var $userTable = $('#userTable').DataTable({
    "pagingType": "full_numbers",  //分页样式  除了数字还有上一页下一页，第一页和最后一页
    "sLoadingRecords": "正在加载数据...",  //数据较多时加载显示
    "sZeroRecords": "暂无数据",   //空数据显示
    serverSide: true,  //服务端分页显示
    stateSave: true,     //开启缓存
    "searching": false,   //datatables自带搜索，关闭，用服务端搜索
    "dom": '<"top">rt<"bottom"iflp<"clear">>',   //显示样式 ，信息，分页栏等均在底部  也可改到top
    "ordering": false, //禁用排序
    ajax: {  //请求数据
        url: "/user/select_userPage.do",
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

            var name = $('input[name="name"]').val();
            if (name !== null && name !== '') {
                str["userName"] = name;
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
            "data": "imgUrl", "title": "头像", render: function (data, type, full) {
                var imgUrl = full.imgUrl;
                imgUrl = imgUrl === null || imgUrl === '' ? '../asserts/img/null.jpg' : imgUrl;
                return '<a target="_blank" href="' + imgUrl + '" ><img src="' + imgUrl + '" style="width:50px;height:50px"></a>'
            }
        },
        {"data": "userName", "title": "姓名"},
        {"data": "deptName", "title": "部门名称"},
        {"data": "positionName", "title": "职称"},
        {"data": "telephone", "title": "联系方式"},
        {
            "data": "workStatus", "title": "工作状态", "orderable": false, render: function (data, type, full) {
                var result = data;
                $.each(workStatusDic, function (index, item) {
                    if (item.columnCode === data) {
                        result = item.columnValue;
                        return false;
                    }
                });
                return result;
            }
        },
        {
            "data": "enabled", "title": "账号状态", render: function (data) {
                return data ? '正常' : '禁用';
            }
        },
        {
            "data": null, "title": "操作", "width": "20%", render: function (data, type, full) {
                //按钮绑定事件  默认触发两次，所以解绑一次
                var rechargeBtn = "recharge" + full.id;
                var deductionBtn = "deduction" + full.id;
                var deleteDeptBtn = "delete" + full.id;
                var $userTable = $('#userTable');
                $userTable.undelegate('tbody #' + rechargeBtn, 'click');
                $userTable.on('click', 'tbody #' + rechargeBtn, function () {
                    editUser(full);
                });
                $userTable.undelegate('tbody #' + deductionBtn, 'click');
                $userTable.on('click', 'tbody #' + deductionBtn, function () {
                    updateUserStatus(full);
                });
                $userTable.undelegate('tbody #' + deleteDeptBtn, 'click');
                $userTable.on('click', 'tbody #' + deleteDeptBtn, function () {
                    updatePassWord(full);
                });
                var icss = full.enabled ? '<i class="fa fa-toggle-on" aria-hidden="true"></i>' : '<i class="fa fa-toggle-off " aria-hidden="true"></i> ';
                var msg = full.enabled ? '禁用' : '激活';
                return '<a class="btn btn-outline-secondary" type="button" id="' + rechargeBtn + '"  data-target=".bd-example-modal-lg" data-toggle="tooltip" data-placement="top" title="查看详细">' +
                    '<svg class="bi bi-clipboard-data" width="1em" height="1em" viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg">\n' +
                    '  <path fill-rule="evenodd" d="M4 1.5H3a2 2 0 0 0-2 2V14a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V3.5a2 2 0 0 0-2-2h-1v1h1a1 1 0 0 1 1 1V14a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1V3.5a1 1 0 0 1 1-1h1v-1z" clip-rule="evenodd"/>\n' +
                    '  <path fill-rule="evenodd" d="M9.5 1h-3a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5zm-3-1A1.5 1.5 0 0 0 5 1.5v1A1.5 1.5 0 0 0 6.5 4h3A1.5 1.5 0 0 0 11 2.5v-1A1.5 1.5 0 0 0 9.5 0h-3z" clip-rule="evenodd"/>\n' +
                    '  <path d="M4 11a1 1 0 1 1 2 0v1a1 1 0 1 1-2 0v-1zm6-4a1 1 0 1 1 2 0v5a1 1 0 1 1-2 0V7zM7 9a1 1 0 0 1 2 0v3a1 1 0 1 1-2 0V9z"/>\n' +
                    '</svg></a> '
                    +
                    '<a class="btn btn-outline-secondary" type="button"  id="' + deductionBtn + '" data-toggle="tooltip" data-placement="top" title="' + msg + '">' + icss + '</a>   ' +
                    '<a class="btn btn-outline-secondary" type="button"  id="' + deleteDeptBtn + '" data-toggle="tooltip" data-placement="top" title="修改个人密码" ><i class="fa fa-shield" aria-hidden="true"></i></a>';
            }
        }
    ],
    createdRow: function (row, data, index) {
        var $td5 = $('td', row).eq(5);
        //.css('font-weight', "bold")
        var $td6 = $('td', row).eq(6);
        //.css('font-weight', "bold")
        data.workStatus === 0 ? $td5.css("color", null) : $td5.css("color", "#d02e41");
        data.enabled ? $td6.css("color", null) : $td6.css("color", "#d02e41");
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
        // $("#checkAll").prop("checked",false);
    },
});


$('#userQueryBtn').click(function () {
    $userTable.draw();
});


function updateUserStatus(data) {
    $.post('/user/update_userEnabled.do', {"id": data.id, "status": !data.enabled}, function (res) {
        if (res.success) {
            $userTable.draw(false);
        } else {
            toastr.error(res.errorCode + " : " + res.errorMsg);
        }
    })
}

function updatePassWord(data) {
    $('input[name="id"]').val(data.id);
    $('#changePasswordModalLabel').text('修改密码');
    $('#changePasswordModal').modal('show');
}

$('#changeP').click(function () {
    var $changePassword = $('#changePassword');
    $changePassword.bootstrapValidator();
    var bootstrapValidator = $changePassword.data('bootstrapValidator');
    bootstrapValidator.validate();
    if (!bootstrapValidator.isValid()) {
        return;
    }

    $.ajax({
        type: 'put',
        url: '/user/update_passByAdmin.do',
        data: $changePassword.serialize(),
        success: function (data) {
            if (data.success) {
                toastr.success("修改成功");
                $('#changePasswordModal').modal('hide');
            } else {
                toastr.error(data.errorCode + " : " + data.errorMsg);
            }
            console.info(data);
            // layer.msg("修改成功", {shift: -1, time: 1000}, function(){
            //     deleteCurrentTab();
            // });
        }
    });
});

$('#changePasswordModal').on('hidden.bs.modal', function () {
    console.info("sss");
    $('#pass1').val('');
    $('#pass2').val('');
});


var showPass1 = true;
var $eys1 = $('#eyes1');
$eys1.on('click', function () {
    if (showPass1) {
        $eys1.removeClass('glyphicon glyphicon-eye-close');
        $eys1.addClass('glyphicon glyphicon-eye-open');
        $('#pass1').attr('type', "text");
        showPass1 = false;
    } else {
        $eys1.removeClass('glyphicon glyphicon-eye-open');
        $eys1.addClass('glyphicon glyphicon-eye-close');
        $('#pass1').attr('type', "password");
        showPass1 = true;
    }
});
var showPass2 = true;
var $eye2 = $('#eyes2');
$eye2.on('click', function () {
    if (showPass2) {
        $eye2.removeClass('glyphicon glyphicon-eye-close');
        $eye2.addClass('glyphicon glyphicon-eye-open');
        $('#pass2').attr('type', "text");
        showPass2 = false;
    } else {
        $eye2.removeClass('glyphicon glyphicon-eye-open');
        $eye2.addClass('glyphicon glyphicon-eye-close');
        $('#pass2').attr('type', "password");
        showPass2 = true;
    }
});


var $province = $("#province"), $city = $("#city"), $country = $("#country");

//datepicker 初始化
var $birthdayDatepicker = $('#birthday').datepicker({
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
var $workTimeDatepicker = $('#workTime').datepicker({
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

//distpicker
//地区赋值
$("#workAddress").distpicker(
    {
        autoSelect: false,
        // province: '福建省',
        // city: '厦门市',
        // country: '思明区'
    }
);

//重置  // $workAddress.distpicker('reset', true);

function editUser(data) {

    console.info(data);
    var img = data.imgUrl;
    if (img !== null && img !== '') {
        $('#image').attr('src', img);
    }else{
        $('#image').attr('src', '../asserts/img/null.jpg');
    }
    $('input[name="imgUrl"]').val(data.imgUrl);
    $('input[name="id"]').val(data.id);
    $('input[name="userName"]').val(data.userName);
    $('input[name="telephone"]').val(data.telephone);
    $('input[name="birthday"]').val(data.birthday);
    $('input[name="idCard"]').val(data.idCard);
    $('input[name="lastLoginTime"]').val(data.lastLoginTime);
    $('input[name="createTime"]').val(data.createTime);
    // $('input[name="deptId"]').val(data.deptId);
    // $('input[name="positionId"]').val(data.positionId);
    $('input[name="email"]').val(data.email);
    $('input[name="liveAddress"]').val(data.liveAddress);
    $('input[name="workTime"]').val(data.workTime);
    if(data.deptId !== null && data.deptId !== ''){
        var $dept = $("#dept");
        $dept.append(new Option(data.deptName, data.deptId, false, true));
        $dept.trigger("change");
    }
    if(data.positionId !== null && data.positionId !== ''){
        var $position = $("#position");
        $position.append(new Option(data.positionName, data.positionId, false, true));
        $position.trigger("change");
    }

    //是否已婚
    if (data.isMarry === 0) {
        $('#noMa').iCheck('check');
    } else {
        $('#isMa').iCheck('check');
    }
    if (data.sex === 0) {
        $('#isWomen').iCheck('check');
    } else {
        $('#isMan').iCheck('check');
    }
    var address = [], add = [];
    if(data.workAddress !== null && data.workAddress !== ''){
        address = data.workAddress.split(",");
        $.each(address, function (index, item) {
            add.push(item);
        });
        $province.val(add[0]);
        $province.trigger("change");
        $city.val(add[1]);
        $city.trigger("change");
        $country.val(add[2]);
        $country.trigger("change");
    }

    $birthdayDatepicker.value(data.birthday);
    $workTimeDatepicker.value(data.workTime);
    $('#editUserModalLabel').text('详细信息');
    $('#editUserModal').modal('show');
}


// 文件上传
var $file = $('#file');
var $img = $("#image");
$img.click(function () {
    $file.click();
    $file.on('change', function () {
        var file = this.files[0];
        var fileTyoe = file.type.split;
        var fileType = file.type.split("/")[0];
        if (fileType != "image") {
            toastr.error("上传的照片格式不支持！");
            return false;
        }
        var reader = new FileReader();
        reader.onload = function (ev) {
            $img.attr('src', reader.result);
        };
        reader.readAsDataURL(file);
    })
});

validate();

//校验数据
function validate() {
    $('#userForm').bootstrapValidator({
        message: 'This value is not valid',
        fields: {
            userName: {
                message: '昵称不建议不填写',
                validators: {
                    notEmpty: {
                        message: '昵称不建议不填写。'
                    },
                    stringLength: {
                        max: 15,
                        min: 2,
                        message: '建议您输入真实的姓名，方便联系。'
                    }
                }
            },
            telephone: {
                validators: {
                    notEmpty: {
                        message: '请填写联系方式'
                    },
                    regexp: {
                        regexp: /^1[34578]\d{9}$/,
                        message: '填入正确的手机号码'
                    }
                }
            },
            email: {
                validators: {
                    regexp: {
                        regexp: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
                        message: '请填写正确的邮箱'
                    },
                    stringLength: {
                        max: 30,
                        min: 2,
                        message: '请填写正确的邮箱地址。'
                    }
                }
            },
            positionId: {
                validators: {
                    notEmpty: {
                        message: '请填写该用户所有的职位'
                    },
                }
            },
            birthday: {
                validators: {
                    regexp: {
                        regexp: /^[1-9]\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])$/,
                        message: '请填写正确的邮箱'
                    },
                    stringLength: {
                        min: 1,
                        max: 11,
                        message: '时间格式为yyyy-MM-dd。'
                    }
                }
            },
            idCard: {
                validators: {
                    regexp: {
                        regexp: /^([1-6][1-9]|50)\d{4}(18|19|20)\d{2}((0[1-9])|10|11|12)(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/,
                        message: '请输入正确的身份证号码格式'
                    },
                    stringLength: {
                        min: 18,
                        max: 18,
                        message: '请填写正确的身份证号码。'
                    }
                }
            }
        }
    })
}

$('#userEdit').click(function () {
    // var userForm = $('#userForm');
    // userForm.bootstrapValidator();
    // var bootstrapValidator = userForm.data('bootstrapValidator');
    // bootstrapValidator.validate();
    // if (!bootstrapValidator.isValid()) {
    //     return;
    // }

    var form = $('#userForm').data("bootstrapValidator");
    form.validate();
    if (!form.isValid()) {
        return;
    }

    var data = $('#userForm').serializeArray();
    if(addOrUpdate){
        var v1 = $('#passwordAdmin').val();
        var v2 = $('#checkPasswordAdmin').val();
        if(v1 === null || v1 === '' || v2=== null ||v2===''){
            toastr.info("密码和确认密码均为必填项");
            return;
        }
        if( v1 !== v2){
            toastr.info("两次密码不一致");
            return;
        }
        var m1 = {"name":"password","value":v1};
        console.info(" m1 : " +m1 );
        var m2 = {"name":"checkPassword","value":v2};
        console.info(" m2 : " +m2 );

        data.push(m1);
        data.push(m2);
    }

    $.each(data, function (index, item) {
        if (item.name === 'birthday' || item.name === 'workTime') {
            var reg = new RegExp("-", "g");
            var value = item.value;
            item.value = value.replace(reg, "/");
        }
    });


    console.info("data : " + JSON.stringify(data));
    var url = addOrUpdate ? '/user/saveUserByAdmin':'/user/updateUserByAdmin';
    $.ajaxFileUpload({
        url: url+'?token=' + localStorage.getItem("token"),
        contentType: "application/json",
        method: 'post',
        fileElementId: 'file',
        dataType: 'json',
        data: data,
        success: function (res) {
            if (res.success) {
                toastr.success("信息保存成功");
                $('#editUserModal').modal('hide');
                $userTable.draw(false);
                initUserModal();
                addOrUpdate =false;
            } else {
                toastr.info(res.errorMsg);
            }
        }
    });
});

$('#editUserModal').on('hidden.bs.modal', function (e) {
    initUserModal();
});

function initUserModal(){
    $(':input', '#userForm').not(':button, :submit, :reset').val('').removeAttr('checked').removeAttr('selected');
    $('#image').attr('src', '../asserts/img/null.jpg');
    $("#userForm").data('bootstrapValidator').destroy();
    $("#userForm").data('bootstrapValidator', null);
    $('#dept').val(null).trigger('change');
    $('#position').val(null).trigger('change');
    $('.showPassword').hide();
    validate();
    addOrUpdate = false;
}

$('#addUserBtn').click(function () {
    $('.showPassword').show();
    addOrUpdate = true;
    $('#editUserModalLabel').text('添加用户');
      $('#editUserModal').modal('show');
});

var  addOrUpdate = false;