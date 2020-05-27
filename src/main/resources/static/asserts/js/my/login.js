$.ajaxSetup({
    cache : false,
    headers : {
        "Authorization" : "Bearer "+localStorage.getItem("token")
    },

    error : function(xhr, textStatus, errorThrown) {
        var msg = xhr.responseText;
        var response = JSON.parse(msg);
        var code = response.errorCode;
        var message = response.errorMsg;
        console.info(code + " : " +message);
        setTimeout(function () {
            console.info(code + " : " +message);
        },8000);
        if (code === 3001) {
            //无权
            toastr.warning(message);
        } else if ( code === 2001) {
            //登入过期或者未登入
            toastr.error(code + ": " + message);
            localStorage.removeItem("token");
            location.href = '/login';
        } else if (xhr.status === 400) {
            toastr.error(code + ": " + message);
            // localStorage.removeItem("token");
            // location.href = '/login';
        } else if (xhr.status === 500) {
            toastr.error('系统错误：' + message);
        }
    }

});


if (top != self) {
    parent.location.href = '/login';
}

var token = localStorage.getItem("token");

if (token != null && token.trim().length > 0) {
    $.get("/user/current", {"token": token}, function (res) {
        console.info(res.success);
        if (res.success) {
            location.href = '/dashboard';
        }
    })
}
;
//开启bootstrap 校验数据
validate();

//校验数据
function validate() {
    // $('#loginForm').bootstrapValidator({
    //     message: 'This value is not valid',
    //     fields: {
    //         telephone: {
    //             validators: {
    //                 notEmpty: {
    //                     message: '请输入手机号码'
    //                 },
    //                 regexp: {
    //                     regexp: /^1[34578]\d{9}$/,
    //                     message: '填入正确的手机号码'
    //                 }
    //             }
    //         },
    //         password: {
    //             validators: {
    //                 notEmpty: {
    //                     message: '用户密码为必填项'
    //                 },
    //                 stringLength: {
    //                     bootstrap-validator: 8,
    //                     max: 15,
    //                     message: '输入区间在8-15个字符'
    //                 },
    //                 // emailAddress: {
    //                 //     message: 'The input is not a valid email address'
    //                 // }
    //             }
    //         }
    //     }
    // });
    $('#registerForm').bootstrapValidator({
        message: 'This value is not valid',
        // feedbackIcons: {
        //     valid: 'glyphicon glyphicon-ok',
        //     invalid: 'glyphicon glyphicon-remove',
        //     validating: 'glyphicon glyphicon-refresh'
        // },
        fields: {
            name: {
                message: '真实姓名',
                validators: {
                    notEmpty: {
                        message: '姓名不能为空'
                    },
                    stringLength: {
                        min: 2,
                        max: 50,
                        message: '请输入真实姓名以方便后续联系'
                    },
                    /*remote: {  //跳转
                        url: 'remote.php',
                        message: 'The username is not available'
                    },*/
                    // regexp: {  正则
                    //     regexp: /^[a-zA-Z0-9_\.]+$/,
                    //     message: 'The username can only consist of alphabetical, number, dot and underscore'
                    // }
                    // callback: {   回调  输入以后判断开始号码和结束号码大小，以区别结束号码》开始号码
                    //     message: '结束号码需要大于等于开始号码',
                    //     callback: function (value, validate) {
                    //         var beginNum = $('#bill-begin-num').val();
                    //         console.info(Number(beginNum) + "ssss" + Number(value));
                    //
                    //         if (Number(beginNum) > Number(value)) {
                    //             console.info("true");
                    //             return false;
                    //         } else {
                    //             return true;
                    //         }
                    //
                    //     }
                    // }
                }
            },
            password: {
                validators: {
                    notEmpty: {
                        message: '用户密码为必填项'
                    },
                    stringLength: {
                        min: 8,
                        max: 15,
                        message: '输入区间在8-15个字符'
                    },
                }
            },
            telephone: {
                validators: {
                    notEmpty: {
                        message: '手机号码为必填项'
                    },
                    regexp: {
                        regexp: /^1[34578]\d{9}$/,
                        message: '填入正确的手机号码'
                    }
                }
            },
            verify: {
                validators: {
                    notEmpty: {
                        message: '必填项'
                    },
                    stringLength: {
                        min: 6,
                        max: 6,
                        message: '请输入6位验证码'
                    },
                    regexp: {
                        regexp: /^\d{6}$/,
                        message: '格式有误'
                    }
                }
            },
        }
    })
};


//修改 checkbox 和radio 样式代码
$('input[type=radio],input[type=checkbox]').iCheck({
    checkboxClass: 'icheckbox_square-green',   // 可以更改red换颜色
    radioClass: 'iradio_square-blue',
    increaseArea: '20%' // optional
});
$('input[type=checkbox]').on('ifChanged', function (obj) {
    $('#remember').val(obj.currentTarget.checked);
    console.log($(this).val());   // 获取该复选框的value值
    console.log(obj.currentTarget.checked)   // 获取该复选框当前状态是否选中
    console.log('您更改了复选框的状态');
});


//忘记密码界面初始化
$('#forgot').off().click(function () {
    $('#forgotModalLabel').text("忘记密码");
    $('#forgotModal').modal('show');
});


$('#forgot-submit').off().on('click', function (index, item) {
    var password = $('#password2').val();
    var checkPassword = $('#password3').val();
    if (password === checkPassword) {
        if (password.length > 7 && password.length < 16) {
            var pattern = /^1[34578]\d{9}$/;
            var phone = $('#for_phone').val();
            if (pattern.test(phone)) {
                var data = $.serializeForm($('#forgotForm').serializeArray());

                $.ajax({
                    url: '/user/forgot',
                    dataType: 'json',
                    data: data,
                    async: true,
                    contentType: "application/json",
                    method: 'POST',
                    success: function (response) {
                        if (response.success) {
                            localStorage.setItem("token", response.data);
                            location.href = '/dashboard';

                        } else {
                            log.info("sssss");
                            toastr.info(response.errorCode + " : " + response.errorMsg);

                        }
                    }
                });
            } else {
                toastr.info("输入的正确格式的手机号码");
            }
        } else {
            toastr.info("输入密码长度在8-16个字符");
        }
    } else {
        toastr.info('两次密码不同');
    }
});


//注册界面初始化
$('#register').off().on('click', function () {

    // $.get("/dept/g", null, function (response) {
    //     var data = [];
    //     $.each(response, function (index, item) {
    //         data.push({id: item.deptId, text: item.deptName});
    //     });
    //显示部门列表
    //select2 远程搜索数据

    $('#re_dept').select2({
        placeholder: '为你所在行业定制工作方式',
        language:'zh-CN',
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
                    search: params.term,
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
    // $("#re_dept").select2({
    //     placeholder: '为你所在行业定制工作方式',
    //     allowClear: true,
    //     data: data ,  //显示数据
    //     // tags: true, 允许输入项为自己输入的值 即option中没有
    //     minimumResultsForSearch: -1,   //隐藏搜索
    // });
    // $("#re_position").select2({
    //     placeholder: '获取个性化的体验和服务（请先选择部门选项）',
    //     minimumResultsForSearch: -1,
    //     allowClear: true,
    // });
    // });


    $('#registerModalLabel').text("注册/Register");
    $('#registerModal').modal('show');
});
//监听清除选项的回调
// $('#re_dept').on('select2:clear', function () {
//     $('#re_position').empty();
// });
// //根据 部门编号获取 职位列表 监听选择
// $('#re_dept').on("select2:select", function (e) {
//     var deptId = $(this).select2('val');
//     $("#re_position").empty();
//     $('#re_position').select2({
//         placeholder: '为你所在行业定制工作方式',
//         allowClear: true,
//         ajax: {
//             url: "/user/getPosition",
//             dataType: 'json',
//             data: function (params) {
//                 return {
//                     search: deptId,
//                 };
//             },
//             processResults: function (data) {
//                 var position = [];
//                 $.each(data, function (index, item) {
//                     position.push({id: item.positionId, text: item.positionName});
//                 });
//                 return {
//                     results: position
//                 };
//             },
//             cache: true
//         },
//         // minimumInputLength: 2
//     })
// });


// 密码显示隐藏
var isShow1 = true;
$('#registerLook').on('click', function () {
    if (isShow1) {
        $('#registerLook').removeClass('glyphicon glyphicon-eye-close');
        $('#registerLook').addClass('glyphicon glyphicon-eye-open');
        $('#password1').attr('type', "text");
        isShow1 = false;
    } else {
        $('#registerLook').removeClass('glyphicon glyphicon-eye-open');
        $('#registerLook').addClass('glyphicon glyphicon-eye-close');
        $('#password1').attr('type', "password");
        isShow1 = true;
    }
});
var isShow2 = true;
$('#registerLook2').on('click', function () {
    if (isShow2) {
        $('#registerLook2').removeClass('glyphicon glyphicon-eye-close');
        $('#registerLook2').addClass('glyphicon glyphicon-eye-open');
        $('#password2').attr('type', "text");
        isShow2 = false;
    } else {
        $('#registerLook2').removeClass('glyphicon glyphicon-eye-open');
        $('#registerLook2').addClass('glyphicon glyphicon-eye-close');
        $('#password2').attr('type', "password");
        isShow2 = true;
    }
});
var isShow3 = true;
$('#registerLook3').on('click', function () {
    if (isShow3) {
        $('#registerLook3').removeClass('glyphicon glyphicon-eye-close');
        $('#registerLook3').addClass('glyphicon glyphicon-eye-open');
        $('#password3').attr('type', "text");
        isShow3 = false;
    } else {
        $('#registerLook3').removeClass('glyphicon glyphicon-eye-open');
        $('#registerLook3').addClass('glyphicon glyphicon-eye-close');
        $('#password3').attr('type', "password");
        isShow3 = true;
    }
});


$(".input-passw").keypress(function (e) {
    if (e.which == 13) {
        login();
    }
});


$('#loginButton').off().on('click', function () {
    login();
});


function login() {
    var form = $('#loginForm').serializeArray();
    var isTrue = true;
    $.each(form, function (index, item) {
        if (item.name == "telephone") {
            var pattern = /^1[34578]\d{9}$/;
            if (!pattern.test(item.value)) {
                toastr.info("输入11位正确的手机号码");
                isTrue = false;
                return false;
            }
        } else if (item.name == "password") {
            var lengh = item.value.length;
            if (lengh < 8 || lengh > 16) {
                toastr.info("输入8~15位密码");
                isTrue = false;
                return false;
            }
        }
    });
    console.info("login :" + isTrue);
    if (isTrue) {
        $.ajax({
            url: '/login',
            // contentType: 'application/json',
            dataType: 'json',
            data: $("#loginForm").serialize(),
            method: 'POST',
            success: function (response) {
                if (response.success) {
                    console.info("success");
                    localStorage.setItem("token", response.data);
                    location.href = '/dashboard';
                } else {
                    toastr.error(response.errorCode + " : " + response.errorMsg);
                }
            }
        });
    }
}

$('#registerButton').off().on('click', function () {
    var form = $('#registerForm').data("bootstrapValidator");
    form.validate();
    if (form.isValid()) {
        form = $('#registerForm').serializeArray();
        form = $.serializeForm(form);
        console.info(form + ":form");
        $.ajax({
            url: '/user/register',
            dataType: 'json',
            data: form,
            async: true,
            contentType: "application/json",
            method: 'POST',
            success: function (response) {
                if (response.success) {
                    localStorage.setItem("token", response.data);
                    location.href = '/dashboard';
                } else {
                    toastr.warning(response.errorCode + ":" + response.errorMsg);
                }
            }
        });
    }
});


//模态框关闭  回调
$('body').on('hidden.bs.modal', '.modal', function () {
    $("#registerForm").data('bootstrapValidator').destroy();
    $('#registerForm').data('bootstrapValidator', null);
    $("#registerForm :input[type='text']").each(function (i) {
        this.value = "";
    });
    $("#re_dept").val(null).trigger('change');
    $("#re_position").val(null).trigger('change');
    // $("#forgotForm").data('bootstrapValidator').destroy();
    // $('#forgotForm').data('bootstrapValidator', null);

    validate();
});


//发送短信
$('#post1').off().on('click', function () {
    var pattern = /^1[34578]\d{9}$/;
    if (pattern.test($(this).parent('span').prev('input').val())) {
        $.sendMessage($(this).parent('span').prev('input').val(), 'post1');
    } else {
        toastr.error("手机号码格式不正确！");
    }
});
$('#post2').off().on('click', function () {
    var pattern = /^1[34578]\d{9}$/;
    if (pattern.test($(this).parent('span').prev('input').val())) {
        $.sendMessage($(this).parent('span').prev('input').val(), 'post2');
    } else {
        toastr.error("手机号码格式不正确！");
    }
});
$.sendMessage = function (phone, event) {
    $.get('/user/sendMessage', {'phone': phone}, function (response) {
        if (response.code == '4') {
            $.setTime($('#' + event));
        } else {
            toastr.warning(response.msg);
        }
    });
};

//60s倒计时实现逻辑
var countdown = 120;
$.setTime = function (obj) {
    if (countdown == 0) {
        obj.prop('disabled', false);
        obj.children('span').text('');
        countdown = 120;//60秒过后button上的文字初始化,计时器初始化;
        return;
    } else {
        obj.prop('disabled', true);
        obj.children('span').text("(" + countdown + "s)");
        countdown--;
    }
    setTimeout(function () {
        $.setTime(obj)
    }, 1000) //每1000毫秒执行一次
};


//ajax 请求   参数（路径，数据，是否异步false同步，true异步）
$.postMethod = function (url, data, async) {
    $.ajax({
        url: url,
        dataType: 'json',
        data: data,
        async: async,
        contentType: "application/json",
        method: 'POST',
        success: function (response) {
            data = response;
        },
        fail: function (response) {
            toastr.error("系统故障");
        }
    });
    return data;
};
//对数组进行封装返回json格式数据
$.serializeForm = function (data) {
    var returnData = {};
    $.each(data, function (index, item) {
        if (item.name == "deptId") {
            var s = $('#re_dept').select2('val');
            returnData[item.name] = item.value;
        } else if (item.name == "positionId") {
            var s = $('#re_position').select2('val');
            returnData[item.name] = item.value;
        } else {
            returnData[item.name] = item.value;
        }
    });
    return JSON.stringify(returnData);
};


